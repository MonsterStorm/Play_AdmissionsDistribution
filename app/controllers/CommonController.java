package controllers;

import static play.data.Form.form;

import java.util.*;

import models.*;
import play.data.*;
import play.mvc.*;

import common.*;
/**
 * 通用controller，存放公用页面
 * @author MonsterStorm
 *
 */
public class CommonController extends Controller{
	private static final String PAGE_COURSE_DETAIL = "courseDetail";//课程详情
	private static final String PAGE_EDU_DETAIL = "eduDetail";//教育机构详情
	private static final String PAGE_MESSAGE_DETAIL = "messageDetail";//留言详情
	private static final String PAGE_TEMPLATE_TYPE_DETAIL = "templateTypeDetail";//模板类型详情
	private static final String PAGE_INSTRUCTOR_TYPE_DETAIL = "instructorDetail";//讲师详情
	private static final String PAGE_USER_DETAIL = "userDetail";//用户详情
	
	/**
	 * common pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_COURSE_DETAIL.equalsIgnoreCase(page)) {//课程
			return pageCourseDetail(null);
		} else if (PAGE_EDU_DETAIL.equalsIgnoreCase(page)) {//教育机构
			return pageEduDetail(null);
		} else if (PAGE_MESSAGE_DETAIL.equalsIgnoreCase(page)) {//留言
			return pageMessageDetail();
		} else if (PAGE_TEMPLATE_TYPE_DETAIL.equalsIgnoreCase(page)) {//模板类型
			return pageTemplateTypeDetail();
		} else if (PAGE_INSTRUCTOR_TYPE_DETAIL.equalsIgnoreCase(page)) {//讲师详情
			return pageInstructorDetail();
		} else if (PAGE_USER_DETAIL.equalsIgnoreCase(page)) {//用户详情
			return pageUserDetail(null);
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}
	
	/**
	 * add or update entity
	 * @return
	 */
	public static Result addOrUpdateEntity(){
		String table = form().bindFromRequest().get("table");
		if(EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)){//教育机构新增或更新
			return addOrUpdateEdu(); 
		} else if (User.TABLE_NAME.equalsIgnoreCase(table)) {//用户信息的新增或者更新
			return addOrUpdateUser();
		} else if (Course.TABLE_NAME.equalsIgnoreCase(table)) {//用户信息的新增或者更新
			return addOrUpdateCourse();
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}
	
	/**
	 * delete an entity
	 */
	public static Result deleteEntity(){
		String table = form().bindFromRequest().get("table");
		if(EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)){//教育机构删除
			return deleteEdu();
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}
	
	/**
	 * add or update education institution
	 * @return
	 */
	public static Result addOrUpdateEdu(){
		EducationInstitution edu = EducationInstitution.addOrUpdate(form().bindFromRequest());
		if(edu != null){
			return pageEduDetail(edu);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}
	
	public static Result addOrUpdateUser(){
		User user = User.addOrUpdate(form().bindFromRequest());
		if(user != null){
			return pageUserDetail(user);
		} else {
			final String username = FormHelper.getString(form().bindFromRequest(), "username");
			if (User.findByUsername(username) == null){
				return internalServerError(Constants.MSG_INTERNAL_ERROR);
			} else {
				return badRequest(Constants.MSG_USER_USERNAME_EXIST);
			}
		}
	}
	
	/**
	 * add or update course
	 */
	public static Result addOrUpdateCourse(){
		Form<Course> form = form(Course.class).bindFromRequest();
		Course course = Course.addOrUpdate(form.get());
		if(course != null){
			return pageCourseDetail(course);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}
	
	/**
	 * delete edu
	 * @return
	 */
	public static Result deleteEdu(){
		EducationInstitution edu = EducationInstitution.delete(form().bindFromRequest());
		if(edu != null){
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}
	
	/**
	 * 课程详情
	 * @return
	 */
	public static Result pageCourseDetail(Course course){
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		
		List<CourseType> types = CourseType.findAll(); 
		if(isAddNew){
			return ok(views.html.module.common.courseDetail.render(null, types));
		}
		
		if(course == null){
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if(id != null){
				course = Course.find(id);
			}
		}
		Long id = Long.valueOf(form().bindFromRequest().get("id"));
		course = Course.find(id);
		return ok(views.html.module.common.courseDetail.render(course, types));
	}
	
	/**
	 * 教育机构详情
	 * @return
	 */
	public static Result pageEduDetail(EducationInstitution edu){
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		
		if(isAddNew){
			return ok(views.html.module.common.eduDetail.render(null));
		}
		
		if(edu == null){
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if(id != null){
				edu = EducationInstitution.find(id);
			}
		}
		return ok(views.html.module.common.eduDetail.render(edu));
	}
	
	/**
	 * 留言详情
	 * @return
	 */
	public static Result pageMessageDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("id"));
		Message message = Message.find(id);
		return ok(views.html.module.common.messageDetail.render(message));
	}
	
	/**
	 * 模板类型详情
	 * @return
	 */
	public static Result pageTemplateTypeDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("id"));
		TemplateType templateType = TemplateType.find(id);
		return ok(views.html.module.common.templateTypeDetail.render(templateType));
	}
	
	/**
	 * 讲师详情
	 * @return
	 */
	public static Result pageInstructorDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("id"));
		Instructor instructor = Instructor.find(id);
		return ok(views.html.module.common.instructorDetail.render(instructor));
	}
	
	/**
	 * 讲师详情
	 * @return
	 */
	public static Result pageUserDetail(User user){
		play.Logger.debug("flash:" + flash().get("xxx")+"," + flash().get("yyy"));
		if(user == null){
			String strId = form().bindFromRequest().get("id");
			if(StringHelper.isValidate(strId)){
				user = User.find(Long.parseLong(strId));
			}
		}
		return ok(views.html.module.common.userDetail.render(user));
	}
}
