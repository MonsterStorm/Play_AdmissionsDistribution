package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 
 * 课程类别，总裁管理，工商管理，金融私募，房地产等
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "course_type")
public class CourseType extends Model {

	@Id
	public Long id;

	public String name;// 列表名称

	// -- 查询
	public static Model.Finder<String, CourseType> finder = new Model.Finder(
			Long.class, CourseType.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<CourseType> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static CourseType find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
}
