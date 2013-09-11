package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 讲师
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "instructor")
public class Instructor extends Model {

	@Id
	public Long id;

	@OneToOne
	public User user;// 讲师对应的用户，一个用户只能对应一个讲师，一个讲师只能对应一个用户

	@OneToMany
	public List<Course> courses;// 讲师对应的课程，一个讲师可以创建多个课程，一个课程只能被一个讲师创建

	// 其他属性字段

	// -- 查询
	public static Model.Finder<String, Instructor> finder = new Model.Finder(
			Long.class, Instructor.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Instructor> findAll() {
		return finder.findList();
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
}
