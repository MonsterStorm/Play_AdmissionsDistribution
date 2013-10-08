package controllers;

import static play.data.Form.form;
import models.*;
import play.mvc.*;

import com.avaje.ebean.*;
import common.*;

import controllers.LoginController.Login;
import controllers.secure.*;

import java.util.*;
import java.text.*;

import play.data.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import common.FileHelper.ErrorType;
/**
 * contoller for teacher
 * 
 * @author khx
 * 
 */
@Security.Authenticated(Secured.class)
public class TeacherController extends BaseController {

	private static final String PAGE_INDEX = "index";// 整个页面主体
	private static final String PAGE_HOME = "home";// 主页
	private static final String PAGE_LOGIN = "login";// 登录
	private static final String PAGE_TEACHER_INFO = "teacherInfo";// 信息页面
	private static final String PAGE_TEACHER_ENROLL_INFO = "teacherEnrollInfo";//学员报名信息
	private static final String PAGE_COURSE_DETAIL = "courseDetail";//返回课程信息
	private static final String PAGE_TEACHER_COURSE_INFO ="teacherCourseInfo";//讲师课程信息
	/**
	 * teacher pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {// 主页
			return ok(views.html.module.teacher.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) {
			return ok(views.html.module.teacher.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.teacher.login.render(form(Login.class)));
		} else if (PAGE_TEACHER_INFO.equalsIgnoreCase(page)) {// 
			User user =  LoginController.getSessionUser();
			Instructor ins = user.instructor;
			return ok(views.html.module.teacher.teacherInfo.render(ins,user));
		} else if(PAGE_TEACHER_ENROLL_INFO.equalsIgnoreCase(page)){
			return pageTeacherEnrollInfo();
		} else if(PAGE_TEACHER_COURSE_INFO.equalsIgnoreCase(page)){
			return pageTeacherCourses();
		}  else if(PAGE_COURSE_DETAIL.equalsIgnoreCase(page)){
			return pageCourseDetail(null);
		}else {
			return badRequest("页面不存在");
		}
	}
	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public static Result logout() {
		// 清空缓存
		LoginController.clearSession();
		return redirect(controllers.routes.TeacherController.page(PAGE_LOGIN));
	}
	/**
	 * 讲师报名管理
	 * 
	 * @return
	 */
	public static Result pageTeacherEnrollInfo() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		User user = LoginController.getSessionUser();
		if(user!=null){
			Instructor instructor = user.instructor;
			if(instructor == null){
				return badRequest(Constants.MSG_TEACHER_NOT_EXIST);
			}

			Page<Enroll> enroll = Enroll.findPageByTeacher(instructor,form()
				.bindFromRequest(), page, null);

			// reset flash
			FormHelper.resetFlash(form().bindFromRequest(), flash());

			return ok(views.html.module.teacher.teacherEnrollInfo.render(enroll));
		}

		return badRequest(Constants.MSG_NOT_LOGIN);
	}
	/**
	 * 培训项目管理
	 * 
	 * @return
	 */
	public static Result pageTeacherCourses() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.instructor == null){
			return badRequest(Constants.MSG_TEACHER_NOT_EXIST);
		}
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Course> courses = Course.findPageByTeacher(user.instructor,form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.teacher.teacherCourseInfo.render(courses));
	}

	/**
	 * 课程详情
	 * 
	 * @return
	 */
	public static Result pageCourseDetail(Course course) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		List<CourseType> types = CourseType.findAll();
		if (isAddNew) {
			return ok(views.html.module.teacher.courseDetail.render(null, types));
		}

		if (course == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				course = Course.find(id);
			}
		}
		return ok(views.html.module.teacher.courseDetail.render(course, types));
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result addOrUpdateEntity() {
		String table = form().bindFromRequest().get("table");
		if (Instructor.TABLE_NAME.equalsIgnoreCase(table)) {// 讲师信息更新或添加
			return addOrUpdateTeacher();
		}if (Course.TABLE_NAME.equalsIgnoreCase(table)) {// 课程信息更新或添加
			return addOrUpdateCourse();
		}
		 else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateTeacher() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		UserInfo basicInfo = user.basicInfo;
		if(basicInfo == null){
			basicInfo = new UserInfo();

			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));
			

			basicInfo.phone = form().bindFromRequest().get("phone");
			
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.save();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			
		}
		else{
			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));


			basicInfo.phone = form().bindFromRequest().get("phone");
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.update();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			

		}
		Form<Instructor> form = form(Instructor.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Instructor instructor = user.instructor;
			if(instructor == null){
				Instructor instructor2 = Instructor.addOrUpdate(form.get());
				if (instructor2 != null) {
					user.instructor = instructor2;
					user.update();
					return ok(views.html.module.teacher.teacherInfo.render(instructor2, user));
				}
			}else{
				instructor.info = FormHelper.getString(form().bindFromRequest(),"info");
				instructor.jobTitle = FormHelper.getString(form().bindFromRequest(),"jobTitle");

				Instructor instructor2 = Instructor.addOrUpdate(instructor);
				if (instructor2 != null) {
					user.instructor = instructor2;
					user.update();
					return ok(views.html.module.teacher.teacherInfo.render(instructor2, user));
				}

			}
		} else if (form.hasErrors()) {
			String error = FormHelper.getFirstError(form.errors());
			play.Logger.debug("error:" + error);
			if (error != null) {
				return badRequest(error);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}

	/**
	 * add or update course
	 */
	public static Result addOrUpdateCourse() {
		User user = LoginController.getSessionUser();
		if(user == null)
			return badRequest(Constants.MSG_NOT_LOGIN);
		if(user.instructor == null){
			return badRequest(Constants.MSG_TEACHER_NOT_EXIST);
		}
		Form<Course> form = form(Course.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Course course = Course.addOrUpdate(form.get());
			course.instructor = user.instructor;
			course.update();
			if (course != null) {
				return pageCourseDetail(course);
			}
		} else if (form.hasErrors()) {
			String error = FormHelper.getFirstError(form.errors());
			play.Logger.debug("error:" + error);
			if (error != null) {
				return badRequest(error);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}

}
