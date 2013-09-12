package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 学员，该类包含学员的基本信息，填写报名信息的时候为该表
 * @author MonsterStorm
 *
 */
@Entity
@Table(name = "student")
public class Student extends Model {

	@Id
	public Long id;

	@OneToOne
	public User user;

	// -- 查询
	public static Model.Finder<String, Student> finder = new Model.Finder(
			Long.class, Student.class);

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

}
