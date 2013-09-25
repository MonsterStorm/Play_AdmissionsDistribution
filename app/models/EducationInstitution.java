package models;

import java.text.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.format.*;
import play.data.format.Formatters.SimpleFormatter;
import play.db.ebean.*;

import com.avaje.ebean.*;
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

	@ManyToOne(fetch=FetchType.EAGER)
	public User creator;// 教育机构的创建者，一个创建者可以创建多个教育机构，一个教育机构只能被一个用户创建

	public Long createTime;// 创建日期

	@OneToMany(mappedBy="edu", cascade=CascadeType.ALL)
	public List<Course> courses;// 教育机构对应的课程列表，一个教育机构可以有多个课程

	@OneToOne
	public Audit audit;//教育机构状态，状态在Audit类中
	
	// 其他属性字段
	public String name;// 教育机构名称
	
	@Lob
	public String info;// 教育机构简介

	// -- 查询
	public static Model.Finder<Long, EducationInstitution> finder = new Model.Finder(Long.class, EducationInstitution.class);
	
	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<EducationInstitution> findAll() {
		return finder.findList();
	}
	
	/**
	 * find page
	 */
	public static Page<EducationInstitution> findPage(int page, Integer pageSize){
		if(pageSize == null){
			pageSize = Constants.PAGE_SIZE;
		}
		return finder.findPagingList(pageSize).getPage(page);
	}
	
	/**
	 * find page with filter
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<EducationInstitution> findPage(DynamicForm form, int page, Integer pageSize){
		return new QueryHelper<EducationInstitution>().findPage(finder, form, page, pageSize);
	}
	
	/**
	 * get total pages
	 */
	public static int pagesCount(Integer pageSize){
		if(pageSize == null){
			pageSize = Constants.PAGE_SIZE;
		}
		int rowCount = finder.findRowCount();
		if(rowCount % pageSize == 0){
			return rowCount / pageSize;
		} else {
			return rowCount / pageSize + 1;
		}
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
	 * add or update an education institution
	 * @return
	 */
	public static EducationInstitution addOrUpdate(DynamicForm form){
		final String id = form.get("id");
		final String name = form.get("name");
		final String info = form.get("info");
		
		if(StringHelper.isValidate(id)){//更新
			EducationInstitution edu = find(Long.valueOf(id));
			if(edu != null){
				edu.name = name;
				edu.info = info;
				edu.update();
				return edu;
			}
		} 
		//插入
		EducationInstitution edu = new EducationInstitution();
		edu.name = name;
		edu.info = info;
		edu.creator = LoginController.getSessionUser();
		edu.createTime = System.currentTimeMillis();
		edu.save();
		return edu;
	}
	
	/**
	 * delete an edu
	 * @param form
	 * @return
	 */
	public static EducationInstitution delete(Long id){
		EducationInstitution edu = find(id);
		if(edu != null){
			edu.delete();
			return edu;
		}
		return null;
	}
}
