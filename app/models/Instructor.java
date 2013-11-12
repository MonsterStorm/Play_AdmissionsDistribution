package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.*;
import common.*;
import controllers.*;

import play.data.*;
import play.db.ebean.*;

/**
 * 讲师
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Instructor.TABLE_NAME)
public class Instructor extends Model {
	private static final String TAG = Instructor.class.getSimpleName();
	public static final String TABLE_NAME = "instructor";

	@Id
	public Long id;

	@OneToMany(mappedBy="instructor", cascade=CascadeType.ALL)
	public List<Domain> domain = new ArrayList<Domain>();//  讲师对应的域名信息，一个讲师对应多个域名，一个域名隶属于一个讲师（也可以没有讲师）

	@OneToOne(cascade=CascadeType.ALL)
	public User user;// 讲师对应的用户，一个用户只能对应一个讲师，一个讲师只能对应一个用户

	@OneToMany(mappedBy = "instructor")
	public List<Course> courses;// 讲师对应的课程，一个讲师可以创建多个课程，一个课程只能被一个讲师创建

	@OneToOne(cascade=CascadeType.ALL)
	public Template template;//一个讲师有一个专属推广页面
	
	// 其他属性字段
	public String name;//讲师名称
	
	public String jobTitle;// 职称

	@OneToOne(cascade=CascadeType.ALL)
	public Audit audit;// 审核状态

	public Long createTime;// 创建时间

	@Lob
	public String info;// 简介

	public String field;//擅长领域

	// -- 查询
	public static Model.Finder<Long, Instructor> finder = new Model.Finder(Long.class, Instructor.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Instructor> findAll() {
		return finder.findList();
	}

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Instructor> findLimit(int limit) {
		return finder.setMaxRows(limit).findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Instructor find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * find page with filter
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = {
			@QueryFilter(dataName="name", paramName="instructorName", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="jobTitle", paramName="instructorJobTitle", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="audit.status", paramName="auditStatus", queryType=QueryFilter.Type.EQ, dataType=Integer.class)
	})
	public static Page<Instructor> findPage(DynamicForm form, Integer page, Integer pageSize){
		QueryHelper<Instructor> queryFilter = new QueryFilterHelper<Instructor>(finder, form).filter(Instructor.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<Instructor>().findPage(finder, form, page, pageSize);
	}

	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static Instructor addOrUpdate(Instructor instructor) {
		if (instructor != null) {
			if (instructor.id == null) {// 新增
				User user = LoginController.getSessionUser();//创建用户必须是当前用户
				instructor.user = user;
				instructor.id = finder.nextId();
				instructor.createTime = System.currentTimeMillis();
				
				//创建Audit
				instructor.audit = new Audit(instructor.user, Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_INSTRUCTOR);
				
				//创建模板
				instructor.template = new Template(instructor, TemplateType.TYPE_DEFAULT);
				
				//创建默认域名
				instructor.domain.add(new Domain(instructor));
				
				instructor.save();
			} else {// 更新
				instructor.update();
			}
			return instructor;
		}
		return null;
	}
	
	/**
	 * course
	 * @param courseId
	 * @param auditStatus
	 * @return
	 */
	public static Instructor updateAudit(Long instructorId, Integer auditStatus){
		Instructor instructor = Instructor.find(instructorId);
		if(instructor != null ){
			if(instructor.audit != null){
				instructor.audit.status = auditStatus;
				instructor.audit.auditor = LoginController.getSessionUser();
				instructor.audit.auditTime = System.currentTimeMillis();
				instructor.update();
				return instructor;
			}
		}
		return null;
	}
	
	/**
	 * delete an edu
	 * @param form
	 * @return
	 */
	public static Instructor delete(Long id){
		Instructor instructor = find(id);
		if(instructor != null){
			instructor.delete();
			return instructor;
		}
		return null;
	}
}
