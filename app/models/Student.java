package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;
import controllers.*;

/**
 * 学员，该类包含学员的基本信息，填写报名信息的时候为该表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Student.TABLE_NAME)
public class Student extends Model {
	public static final String TABLE_NAME = "student";
	
	@Id
	public Long id;

	@OneToOne(cascade=CascadeType.ALL)
	public User user;
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Enroll> enrolls;//学员报名信息

	// --其他属性，只在报名第一次用到，其他时候公用该信息--
	public String name;//学员名称
	
	public String companyName;// 公司名称

	public String position; // 职务

	public String info;// 额外信息

	static {
		FormFormatter.registerUser();
	}

	// -- 查询
	public static Model.Finder<Long, Student> finder = new Model.Finder(Long.class, Student.class);

	
	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Student> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Student find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Student find(User user) {
		return finder.where().eq("user", user).findUnique();
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = {
			@QueryFilter(dataName="name", paramName="studentName", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="companyName", paramName="studentCompanyName", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="position", paramName="studentPosition", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="user.audit.status", paramName="auditStatus", queryType=QueryFilter.Type.EQ, dataType=Integer.class)
	})
	public static Page<Student> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<Student> queryFilter = new QueryFilterHelper<Student>(finder, form).filter(Student.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<Student>(finder, form).addEq("user.audit.status", "auditStatus", Integer.class).addOrderBy("orderby").findPage(page, pageSize);
	}
	
	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static Student addOrUpdate(Student student) {
		if (student != null) {
			if (student.id == null) {// 新增，每次都新增一个账户
				if(student.user != null){
					User.updateRandomUserForStudent(student, Role.ROLE_STUDENT, Audit.STATUS_SUCCESS);
				}
				student.id = finder.nextId();
				student.save();
			} else {// 更新
				student.update();
			}
			return student;
		}
		return null;
	}
	
	/**
	 * course
	 * @param courseId
	 * @param auditStatus
	 * @return
	 */
	/*public static Student updateAudit(Long stuId, Integer auditStatus){
		Student stu = Student.find(stuId);
		if(stu != null ){
			if(stu.audit != null){
				stu.audit.status = auditStatus;
				stu.audit.auditor = LoginController.getSessionUser();
				stu.audit.auditTime = System.currentTimeMillis();
				stu.update();
				return instructor;
			}
		}
		return null;
	}*/
	
	
	/**
	 * delete an edu
	 * @param form
	 * @return
	 */
	public static Student delete(Long id){
		Student student = find(id);
		if(student != null){
			student.delete();
			return student;
		}
		return null;
	}
}
