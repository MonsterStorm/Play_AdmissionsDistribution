package controllers;

import static play.data.Form.form;

import java.util.*;

import models.*;
import play.api.templates.*;
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
	
	/**
	 * common pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_COURSE_DETAIL.equalsIgnoreCase(page)) {//课程
			return pageCourseDetail();
		} else if (PAGE_EDU_DETAIL.equalsIgnoreCase(page)) {//教育机构
			return pageEduDetail(null);
		} else if (PAGE_MESSAGE_DETAIL.equalsIgnoreCase(page)) {//留言
			return pageMessageDetail();
		} else if (PAGE_TEMPLATE_TYPE_DETAIL.equalsIgnoreCase(page)) {//模板类型
			return pageTemplateTypeDetail();
		} else if (PAGE_INSTRUCTOR_TYPE_DETAIL.equalsIgnoreCase(page)) {//讲师详情
			return pageInstructorDetail();
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
		if(EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)){//教育机构新增或删除
			return addOrUpdateEdu(); 
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
	
	/**
	 * 课程详情
	 * @return
	 */
	public static Result pageCourseDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("courseId"));
		Course course = Course.find(id);
		List<CourseType> types = CourseType.findAll(); 
		return ok(views.html.module.common.courseDetail.render(course, types));
	}
	
	/**
	 * 教育机构详情
	 * @return
	 */
	public static Result pageEduDetail(EducationInstitution edu){
		if(edu == null){
			Long id = Long.valueOf(form().bindFromRequest().get("eduId"));
			edu = EducationInstitution.find(id);
		}
		return ok(views.html.module.common.eduDetail.render(edu));
	}
	
	/**
	 * 留言详情
	 * @return
	 */
	public static Result pageMessageDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("messageId"));
		Message message = Message.find(id);
		return ok(views.html.module.common.messageDetail.render(message));
	}
	
	/**
	 * 模板类型详情
	 * @return
	 */
	public static Result pageTemplateTypeDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("templateTypeId"));
		TemplateType templateType = TemplateType.find(id);
		return ok(views.html.module.common.templateTypeDetail.render(templateType));
	}
	
	/**
	 * 讲师详情
	 * @return
	 */
	public static Result pageInstructorDetail(){
		Long id = Long.valueOf(form().bindFromRequest().get("instructorId"));
		Instructor instructor = Instructor.find(id);
		return ok(views.html.module.common.instructorDetail.render(instructor));
	}
}
