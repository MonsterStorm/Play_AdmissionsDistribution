package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;
import views.html.helper.*;

import com.avaje.ebean.*;
import common.*;

import controllers.*;

/**
 * 报名信息
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "enroll")
public class Enroll extends Model {
	public static final String TABLE_NAME = "enroll";
	@Id
	public Long id;

	@ManyToOne
	public Student student;// 一个学员有多个报名信息，一个报名信息只归属于一个学员

	@ManyToOne
	public Course course;//一个课程有多个报名信息，一个报名信息只归属于一个课程
	// -- 报名所属
	@ManyToOne
	public Agent fromAgent;// 来源代理人，一个代理人可以有多个报名信息，一个报名信息只能隶属于一个代理人，如果是直接通过平台过来，则该字段为空

	@ManyToOne
	public EducationInstitution edu;// 所属教育机构，一个教育机构可以有多个报名信息，一个报名信息隶属于一个教育机构。
	// @ManyToOne
	// public Instructor instructor;//所属讲师，一个讲师可以有多个报名信息，一个报名信息隶属于一个讲师。

	// -- 报名确认信息
	@OneToOne
	public Audit auditOfAgent;// 代理人审核

	@OneToOne
	public Audit auditOfEdu;// 教育机构的审核信息
	//付款确认信息
	@OneToOne
	public ConfirmReceipt confirmOfStu;// 学生付款确认信息

	// -- 收款确认信息
	@OneToOne
	public ConfirmReceipt confirmOfEdu;// 教育机构收款信息

	@OneToOne
	public ConfirmReceipt confirmOfPlatform;// 平台收款信息

	@OneToOne
	public ConfirmReceipt confirmOfAgent;// 代理人收款信息

	public Long enrollTime;// 报名时间

	public String enrollIp;// 登记时的ip

	public String enrollDomain;// 来源域名，如www.google.com，用于分销的统计
	//其他基本信息
	public int enrollByAgent;//1 表示由代理人报名 代理人可以查看  用户名  密码等信息

	// -- 查询
	public static Model.Finder<Long, Enroll> finder = new Model.Finder(Long.class, Enroll.class);

	/**
	 * find all enroll
	 * 
	 * @return
	 */
	public static List<Enroll> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Enroll find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPage(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Enroll>().findPage(finder, form, page, pageSize);
	}

	/**
	 * 删除一个报名信息
	 * @param form
	 * @return
	 */
	public static Enroll delete(Long id){
		return QueryHelper.deleteEntity(finder, id);
	}

	/**
	 * 新增或更新一个报名信息
	 * 
	 * @param form
	 * @return
	 */
	public static Enroll addOrUpdate(Enroll enroll) {
		if (enroll != null) {
			if (enroll.id == null) {// 新增
				enroll.id = finder.nextId();
				enroll.save();
			} else {// 更新
				enroll.update();
			}
			return enroll;
		}
		return null;
	}

	/**
	 * find user by account
	 * 
	 * @param account
	 * @return
	 */
	public static Enroll findByStudentAndCourse(Student student, Course course) {
		return finder.where().eq("student", student).eq("course",course).findUnique();
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByStudentId(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Enroll>(finder, form).addEq("student.id", "id", Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByStudent(Student student, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("stuId", student.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Enroll>(finder, form).addEq("student.id", "stuId", Long.class).addOrderBy("orderby").findPage(page, pageSize);
//		return new QueryHelper<Enroll>(finder, form).addEqual("student.id", student.id.toString(), Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByTeacher(Instructor instructor, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("instructorId", instructor.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Enroll>(finder, form).addEq("course.instructor.id", "instructorId", Long.class).addOrderBy("orderby").findPage(page, pageSize);
//		return new QueryHelper<Enroll>(finder, form).addEqual("course.instructor.id", instructor.id.toString(), Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByAgent(Agent agent, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("agentId", agent.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Enroll>(finder, form).addEq("fromAgent.id", "agentId", Long.class).addOrderBy("orderby").findPage(page, pageSize);
//		return new QueryHelper<Enroll>(finder, form).addEqual("course.instructor.id", instructor.id.toString(), Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByCourseId(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Enroll>(finder, form).addEq("course.id", "id", Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByAgentId(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Enroll>(finder, form).addEq("fromAgent.id", "id", Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Enroll> findPageByEduId(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Enroll>(finder, form).addEq("edu.id", "id", Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static List<Enroll> findByEdu(EducationInstitution edu, Long startTime, Long endTime) {
		return finder.where().eq("edu.id", edu.id).ge("confirmOfEdu.time", startTime).le("confirmOfEdu.time", endTime ).findList();

		//return new QueryHelper<Enroll>(finder, form).addEq("edu.creator.id", "userId", Long.class).addGe("confirmOfEdu.time", "startTime", Long.class).addLe("confirmOfEdu.time", "endTime", Long.class).findPage(1, 100000);
	}


	/**
	 * enroll
	 * 
	 * @param enrollId
	 * @param auditStatus
	 * @return
	 */
	public static Enroll updateAgentAudit(Long enrollId, Integer auditStatus) {
		Enroll enroll = Enroll.find(enrollId);
		if (enroll != null) {
			if( enroll.auditOfAgent == null ){
				return null;
			}
			enroll.auditOfAgent.status = auditStatus;
			enroll.auditOfAgent.auditTime = System.currentTimeMillis();
			enroll.auditOfAgent.auditor = LoginController.getSessionUser();
			enroll.auditOfAgent.update();
			enroll.update();
			return enroll;
		}
		return null;
	}

	/**
	 * enroll
	 * 
	 * @param enrollId
	 * @param auditStatus
	 * @return
	 */
	public static Enroll updateEduAudit(Long enrollId, Integer auditStatus) {
		Enroll enroll = Enroll.find(enrollId);
		if (enroll != null) {
			if( enroll.auditOfEdu == null ){
				return null;
			}
			enroll.auditOfEdu.status = auditStatus;
			enroll.auditOfEdu.auditTime = System.currentTimeMillis();
			enroll.auditOfEdu.auditor = LoginController.getSessionUser();
			enroll.auditOfEdu.update();
			enroll.update();
			return enroll;
		}
		return null;
	}




}
