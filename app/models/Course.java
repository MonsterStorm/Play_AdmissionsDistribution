package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;

import controllers.*;

/**
 * 课程类，由讲师创建
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Course.TABLE_NAME)
public class Course extends Model {
	private static final String TAG = Course.class.getSimpleName();
	public static final String TABLE_NAME = "course";
	@Id
	public Long id;

	@Required(message = Constants.MSG_FORM_COURSE_REQUIRED_NAME)
	public String name;// 课程名称

	@Required(message = Constants.MSG_FORM_COURSE_REQUIRED_MONEY)
	public Double money;// 课程费用

	public Long startTime; // 开课时间

	public String contact;// 联系方式，包括联系人，电话等

	@OneToOne(cascade = CascadeType.ALL)
	public Audit audit;// 审核状态，课程需要被审核，当前审核状态

	@ManyToOne
	public CourseType courseType;// 课程类别

	@ManyToOne
	public CourseClass courseClass;// 新课程类别

	@Lob
	public String info;// 课程简介

	@Lob
	public String detail;// 课程详细信息

	@ManyToOne
	public EducationInstitution edu;// 一个课程只能被一个教育机构拥有，一个教育机构可以有多个课程

	@OneToOne(cascade = CascadeType.ALL)
	public RebateType eduRebateType;// 教育机构分成

	@OneToOne(cascade = CascadeType.ALL)
	public RebateType agentRebateType;// 代理人分成

	@ManyToOne
	public Instructor instructor;// 一个课程只能被一个讲师拥有，一个讲师可以有多个课程

	@ManyToMany(mappedBy = "courses")
	public List<Agent> agents = new ArrayList<Agent>();// 代理该课程的代理人列表，一个代理人可以代理多个课程，一个课程可以被多个代理人代理

	@OneToMany(mappedBy="course", cascade=CascadeType.ALL)
	public List<QAndS> qands = new ArrayList<QAndS>();
	
	// @OneToMany(mappedBy="course")
	// public List<Audit> agentRegAudit = new
	// ArrayList<Audit>();//申请代理该课程的审核列表，一个课程对应多个审核 一个审核对应一个课程

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = CourseDistribution.class)
	public List<CourseDistribution> distributions = new ArrayList<CourseDistribution>();// 课程的代理信息，一个课程可以被很多代理人代理。一个代理行为对应一个记录。

	static {
		FormFormatter.registerCourseClass();
		FormFormatter.registerCourseType();
		FormFormatter.registerEducationType();
	}

	/**
	 * 根据类的属性名称，获取属性值
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String property) {
		try {
			Field field = User.class.getField(property);
			if (field != null) {
				field.setAccessible(true);// 设置可以访问，对于私有变量有用
				return field.get(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// -- 查询
	public static Model.Finder<Long, Course> finder = new Model.Finder(
			Long.class, Course.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Course> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Course find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	public static Course addOrUpdate(Course course){
		return addOrUpdate(course, null);
	}
	
	/**
	 * 新增或更新一个课程
	 * 
	 * @param form
	 * @return
	 */
	public static Course addOrUpdate(Course course, String[] updateFiledNames) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + course.id + ", name="
				+ course.name);
		if (course != null) {
			if (course.id == null) {// 新增
				course.id = finder.nextId();

				User user = LoginController.getSessionUser();

				// 教育机构
				String eduId = LoginController
						.getFromSession(LoginController.KEY_EDU_ID);
				if (StringHelper.isValidate(eduId)) {
					course.edu = EducationInstitution.find(Long.valueOf(eduId));
				} else {
					if (user != null && user.instructor != null) {// 讲师
						course.instructor = user.instructor;
					}
				}

				// 创建Audit
				Audit audit = new Audit(user, Audit.STATUS_WAIT,
						AuditType.TYPE_AUDITTYPE_COURSE);
				course.audit = audit;

				// 创建返利信息

				course.save();
			} else {// 更新
				ModelHelper.update(Course.find(course.id), course, updateFiledNames);
			}
			return course;
		}
		return null;
	}

	/**
	 * course
	 * 
	 * @param courseId
	 * @param auditStatus
	 * @return
	 */
	public static Course updateAudit(Long courseId, Integer auditStatus) {
		Course course = Course.find(courseId);
		if (course != null) {
			if (course.audit != null) {
				play.Logger.debug("!!!");
				course.audit.status = auditStatus;
				course.audit.auditor = LoginController.getSessionUser();
				course.audit.auditTime = System.currentTimeMillis();
				course.update();
				return course;
			}
		}
		return null;
	}

	/**
	 * delete an edu
	 * 
	 * @param form
	 * @return
	 */
	public static Course delete(Long id) {
		Course course = find(id);
		if (course != null) {
			course.delete();
			return course;
		}
		return null;
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = {
			@QueryFilter(dataName="name", paramName="courseName", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="courseType.name", paramName="courseTypeName", queryType=QueryFilter.Type.EQ, dataType=String.class),
			@QueryFilter(dataName="edu.name", paramName="eduName", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="startTime", paramName="startEndTime", queryType=QueryFilter.Type.BETWEEN, dataType=Long.class),
			@QueryFilter(dataName="agentRebateType.ratioOfTotal * money + agentRebateType.ratioOfPerStudent * ", paramName="myorder", queryType=QueryFilter.Type.NEWORDERBY, dataType=Long.class)
	})
	public static Page<Course> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<Course> queryFilter = new QueryFilterHelper<Course>(finder, form).filter(Course.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);

		//Model.Finder<Long, Course> finder2 = new Model.Finder(Long.class, Course.class);
		//return finder.where().in( "id" , finder2.select("id").where().eq("id", 1).query() ).query().findPagingList(10).getPage(0);
		//return finder.query().order().asc("agentRebateType.ratioOfTotal * money + agentRebateType.ratioOfPerStudent * 10").findPagingList(50).getPage(0);

//		return new QueryHelper<Course>().findPage(finder, form, page, pageSize);
	}

	/**
	 * find page by agent
	 * 
	 * @param form
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public static Page<Course> findPageByAgent(DynamicForm form, int page,
			Integer pageSize) {
		QueryHelper<Course> queryFilter = new QueryFilterHelper<Course>(finder, form).filter(Course.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
	//	return new QueryHelper<Course>(finder, form).addEq("agents.id",
	//			"agentId", Long.class).findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Course> findPageByTeacher(Instructor teacher,
			DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("teacherId", teacher.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Course>(finder, form)
				.addEq("instructor.id", "teacherId", Long.class)
				.addOrderBy("orderby").findPage(page, pageSize);
		// return new QueryHelper<Course>(finder,
		// form).addEqual("instructor.id", teacher.id.toString(),
		// Long.class).addOrderBy("orderby").findPage(finder, form, page,
		// pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Course> findPageByEducation(EducationInstitution edu,
			DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("eduId", edu.id.toString());
		form = form.bind(datas);
		
		QueryHelper<Course> queryFilter = new QueryFilterHelper<Course>(finder, form).filter(Course.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
		
		// return new QueryHelper<Course>(finder, form)
		//		.addEq("edu.id", "eduId", Long.class).addOrderBy("orderby")
		//		.findPage(page, pageSize);
		// return new QueryHelper<Course>(finder, form).addEqual("edu.id",
		// edu.id.toString(), Long.class).addOrderBy("orderby").findPage(finder,
		// form, page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Course> findPageByEducationUser(User user,
			DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("creatorId", user.id.toString());
		form = form.bind(datas);
		
		QueryHelper<Course> queryFilter = new QueryFilterHelper<Course>(finder, form).filter(Course.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
		
		// return new QueryHelper<Course>(finder, form)
		//		.addEq("edu.creator.id", "creatorId", Long.class)
		//		.addOrderBy("orderby").findPage(page, pageSize);
		// return new QueryHelper<Course>(finder,
		// form).addEqual("edu.creator.id", user.id.toString(),
		// Long.class).addOrderBy("orderby").findPage(finder, form, page,
		// pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Course> findPageByAgent(Agent agent, DynamicForm form,
			int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("agentId", agent.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Course>(finder, form).addEq("agents.id",
				"agentId", Long.class).findPage(page, pageSize);
	}
}
