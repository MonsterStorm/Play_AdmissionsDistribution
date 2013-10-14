package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
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

	@OneToMany(cascade=CascadeType.ALL)
	public List<Domain> domain;//  教育机构对应的域名信息，一个 教育机构对应多个域名，一个域名隶属于一个教育机构（也可以没有讲师）

	@ManyToOne(fetch=FetchType.EAGER)
	public User creator;// 教育机构的创建者，一个创建者可以创建多个教育机构，一个教育机构只能被一个用户创建

	public Long createTime;// 创建日期

	@OneToMany(mappedBy="edu", cascade=CascadeType.ALL)
	public List<Course> courses;// 教育机构对应的课程列表，一个教育机构可以有多个课程

	@OneToOne(cascade=CascadeType.ALL)
	public Audit audit;//教育机构状态，状态在Audit类中
	
	@OneToOne(cascade=CascadeType.ALL)
	public Template template;//一个教育机构有一个专属推广页面
	
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
	 * find one by userName
	 * 
	 * @param userName
	 * @return
	 */
	public static EducationInstitution findByName(String name) {
		return finder.where().eq("name", name).findUnique();
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
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<EducationInstitution> findPageByUser(User  user, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("creatorId", user.id.toString());
		form = form.bind(datas);
		return new QueryHelper<EducationInstitution>(finder, form).addEq("creator.id", "creatorId", Long.class).addOrderBy("orderby").findPage(page, pageSize);
//		return new QueryHelper<EducationInstitution>(finder, form).addEqual("creator.id", user.id.toString(), Long.class).addOrderBy("orderby").findPage(finder, form, page, pageSize);
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
	/*public static EducationInstitution addOrUpdate(DynamicForm form){
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
	}*/

	/**
	 * course
	 * @param courseId
	 * @param auditStatus
	 * @return
	 */
	public static EducationInstitution updateAudit(Long eduId, Integer auditStatus){
		EducationInstitution edu = EducationInstitution.find(eduId);
		if(edu != null ){
			if(edu.audit != null){
				edu.audit.status = auditStatus;
				edu.audit.auditor = LoginController.getSessionUser();
				edu.audit.auditTime = System.currentTimeMillis();
				edu.update();
				return edu;
			}
		}
		return null;
	}
	
	/**
	 * add or update an education institution
	 * @return
	 */
	public static EducationInstitution addOrUpdate(EducationInstitution edu){
		if (edu != null) {
			if (edu.id == null) {// 新增
				User user = LoginController.getSessionUser();//创建用户必须是当前用户
				edu.creator = user;//绑定到当前用户
				edu.id = finder.nextId();
				edu.createTime = System.currentTimeMillis();
				
				Template template = new Template(edu, TemplateType.TYPE_DEFAULT);
				edu.template = template;
				
				edu.audit = new Audit(edu.creator, Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_EDU);
				
				edu.save();
			} else {// 更新
				edu.update();
			}
			return edu;
		}
		return null;
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
