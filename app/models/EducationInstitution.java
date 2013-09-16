package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;

import common.*;

import controllers.*;
/**
 * 教育机构
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = EducationInstitution.TABLE_NAME)
public class EducationInstitution extends Model {
	
	public static final String TABLE_NAME = "education_institution";

	@Id
	public Long id;

	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	public User creator;// 教育机构的创建者，一个创建者可以创建多个教育机构，一个教育机构只能被一个用户创建

	public Long createTime;// 创建日期

	@OneToMany(mappedBy="edu")
	public List<Course> courses;// 教育机构对应的课程列表，一个教育机构可以有多个课程

	// 其他属性字段
	public String name;// 教育机构名称

	// -- 查询
	public static Model.Finder<String, EducationInstitution> finder = new Model.Finder(Long.class, EducationInstitution.class);
	
	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<EducationInstitution> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static EducationInstitution find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * add or update a education institution
	 * @return
	 */
	public static EducationInstitution addOrUpdate(DynamicForm form){
		final String id = form.get("id");
		final String name = form.get("name");
		
		if(StringHelper.isValidate(id)){//更新
			EducationInstitution edu = find(Long.valueOf(id));
			if(edu != null){
				edu.name = name;
				edu.update();
				return edu;
			}
		} 
		//插入
		EducationInstitution edu = new EducationInstitution();
		edu.name = name;
		edu.creator = LoginController.getSessionUser();
		edu.createTime = System.currentTimeMillis();
		edu.save();
		return edu;
	}
}
