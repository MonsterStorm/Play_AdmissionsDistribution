package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 课程类，由讲师创建
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "course")
public class Course extends Model {

	@Id
	public Long id;

	public String name;// 课程名称

	public Double money;// 课程费用

	public Long startTime; // 开课时间

	public String contact;// 联系方式，包括联系人，电话等

	public CourseType type;// 课程类别

	@Lob
	public String info;// 课程简介

	@Lob
	public String detail;// 课程详细信息

	@ManyToOne
	public EducationInstitution edu;// 一个课程只能被一个教育机构拥有，一个教育机构可以有多个课程

	@ManyToOne
	public Instructor instructor;// 一个课程只能被一个讲师拥有，一个讲师可以有多个课程

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
	public static Model.Finder<String, Course> finder = new Model.Finder(Long.class, Course.class);

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
}
