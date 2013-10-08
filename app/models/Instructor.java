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

	@OneToOne
	public User user;// 讲师对应的用户，一个用户只能对应一个讲师，一个讲师只能对应一个用户

	@OneToMany(mappedBy = "instructor")
	public List<Course> courses;// 讲师对应的课程，一个讲师可以创建多个课程，一个课程只能被一个讲师创建

	@OneToOne(cascade=CascadeType.ALL)
	public Template template;//一个讲师有一个专属推广页面
	
	// 其他属性字段
	public String jobTitle;// 职称

	@OneToOne
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
	public static Page<Instructor> findPage(DynamicForm form, int page, Integer pageSize){
		return new QueryHelper<Instructor>().findPage(finder, form, page, pageSize);
	}

	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static Instructor addOrUpdate(Instructor instructor) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + instructor.id + ", name="	+ instructor.jobTitle);
		if (instructor != null) {
			if (instructor.id == null) {// 新增
				User user = LoginController.getSessionUser();//创建用户必须是当前用户
				instructor.user = user;
				instructor.id = finder.nextId();
				instructor.save();
			} else {// 更新
				instructor.update();
			}
			return instructor;
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
