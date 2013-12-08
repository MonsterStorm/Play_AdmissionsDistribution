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
import common.FormValidator.Type;

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
	private static final String PAGE_TEACHER_DOMAIN  = "teacherDomain";
	private static final String PAGE_DOMAIN_DETAIL  = "domainDetail";
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
		}else if (PAGE_TEACHER_DOMAIN.equalsIgnoreCase(page)) {// 
			return pageTeacherDomain();
		} else if (PAGE_DOMAIN_DETAIL.equalsIgnoreCase(page)) {// 
			return pageDomainDetail(null);
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
		}if (Domain.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人更新或添加
			return addOrUpdateDomain();
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
	 @FormValidators(values = {
			@FormValidator(name = "realname", validateType = Type.REQUIRED, msg = "真实姓名不能为空"),
			@FormValidator(name = "sex", validateType = Type.REQUIRED, msg = "性别不能为空"),
			@FormValidator(name = "idcard", validateType = Type.REQUIRED, msg = "身份证号不能为空"),
			@FormValidator(name = "idcard", validateType = Type.NUMBER, msg = "身份证号只能是数字"),
			@FormValidator(name = "birthday", validateType = Type.REQUIRED, msg = "出生日期不能为空"),
			@FormValidator(name = "phone", validateType = Type.PHONE, msg = "请填写正确的座机号码"),
			@FormValidator(name = "qq", validateType = Type.REQUIRED, msg = "QQ号码不能为空"),
			@FormValidator(name = "qq", validateType = Type.NUMBER, msg = "QQ号码只能是数字"),
			@FormValidator(name = "address", validateType = Type.REQUIRED, msg = "联系地址不能为空"),
			@FormValidator(name = "mobile", validateType = Type.PHONE, msg = "请填写正确的手机号码"),
			@FormValidator(name = "email", validateType = Type.EMAIL, msg = "请填写正确的邮箱地址"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "个人简介不能为空")/*,
			@FormValidator(name = "jobTitle", validateType = Type.REQUIRED, msg = "职称不能为空"),
			@FormValidator(name = "field", validateType = Type.REQUIRED, msg = "擅长领域不能为空")*/
	})
	public static Result addOrUpdateTeacher() {
		String msg = Validator.check(CommonController.class, "addOrUpdateTeacher");
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

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateDomain() {
		User user = LoginController.getSessionUser();
		if (user == null) {
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.instructor == null){
			return badRequest(Constants.MSG_TEACHER_NOT_EXIST);
		}

		Form<Domain> form = form(Domain.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			String str = FormHelper.getString(form().bindFromRequest(),"domain");

			Domain domain = Domain.findByDomain(str);
			if(domain != null  ){
				return badRequest(Constants.MSG_DOMAIN_EXIST); 
			}
			Domain newDomain = new Domain();
			newDomain.id = FormHelper.getLong(form().bindFromRequest(),"id");
			newDomain.domain = FormHelper.getString(form().bindFromRequest(),"domain");
			newDomain.instructor = user.instructor;
			Domain dom = Domain.addOrUpdate(newDomain);
			if(dom!=null){
				return ok(views.html.module.teacher.domainDetail.render(dom));
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
	 * 域名
	 * 
	 * @return
	 */
	public static Result pageTeacherDomain() {
		play.Logger.error(form().bindFromRequest().get("page"));
		int page = FormHelper.getPage(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Page<Domain> domain  = Domain.findPageByTeacher(user.instructor,form().bindFromRequest(),page,null);
		return ok(views.html.module.teacher.teacherDomain.render(domain));
		
	}

	/**
	 * 域名详情
	 * 
	 * @return
	 */
	public static Result pageDomainDetail(Domain domain) {
		if (domain == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				domain = Domain.find(id);
			}
		}
		return ok(views.html.module.teacher.domainDetail.render(domain));
	}




}
