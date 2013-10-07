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
 * contoller for student
 * 
 * @author khx
 * 
 */
@Security.Authenticated(Secured.class)
public class StudentController extends BaseController {

	private static final String PAGE_INDEX = "index";// 整个页面主体
	private static final String PAGE_HOME = "home";// 主页
	private static final String PAGE_LOGIN = "login";// 登录
	private static final String PAGE_STUDENT_INFO = "studentInfo";//学员信息
	private static final String PAGE_STUDENT_ENROLL_INFO = "studentEnrollInfo";//学员报名信息
	/**
	 * student pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {// 主页
			return ok(views.html.module.student.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) {
			return ok(views.html.module.student.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.student.login.render(form(Login.class)));
		} else if(PAGE_STUDENT_INFO.equalsIgnoreCase(page)){
			return pageStudentInfo();
		}else if(PAGE_STUDENT_ENROLL_INFO.equalsIgnoreCase(page)){
			return pageStudentEnrollInfo();
		}
		else {
			return badRequest("页面不存在");
		}
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result addOrUpdateEntity() {
		String table = form().bindFromRequest().get("table");
		if (Student.TABLE_NAME.equalsIgnoreCase(table)) {// 学员更新或添加
			return addOrUpdateStudent();
		} if("student_enroll".equalsIgnoreCase(table)){//学员报名
			return addOrUpdateStudentEnroll();
		}
		 else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
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
		return redirect(controllers.routes.StudentController.page(PAGE_LOGIN));
	}

	/**
	 * 学员信息页面
	 * @return
	 */
	public static Result pageStudentInfo(){
		User user = LoginController.getSessionUser();
		if(user != null){
			Student student = Student.find(user);
			return ok(views.html.module.student.studentInfo.render(student,user));
		}
		return badRequest(Constants.MSG_NOT_LOGIN);
	}
	/**
	 * 学生报名管理
	 * 
	 * @return
	 */
	public static Result pageStudentEnrollInfo() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		User user = LoginController.getSessionUser();
		if(user!=null){
			Student student = user.student;
			if(student == null){
				 badRequest(Constants.MSG_STUDENT_NOT_EXIST);
			}

			Page<Enroll> enroll = Enroll.findPageByStudent(student,form()
				.bindFromRequest(), page, null);

			// reset flash
			FormHelper.resetFlash(form().bindFromRequest(), flash());

			return ok(views.html.module.student.studentEnrollInfo.render(enroll));
		}

		return badRequest(Constants.MSG_NOT_LOGIN);
	}
	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateStudent() {
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
		Form<Student> form = form(Student.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Student student = user.student;
			if(student == null){
				Student student2 =  Student.addOrUpdate(form.get());
				user.student = student2;
				user.update();
				if (student2 != null) {
					return ok(views.html.module.student.studentInfo.render(student2, user));
				}
			}else{
				student.info = FormHelper.getString(form().bindFromRequest(),"info");
				student.position = FormHelper.getString(form().bindFromRequest(),"position");
				student.companyName = FormHelper.getString(form().bindFromRequest(),"companyName");
				Student student2 =  Student.addOrUpdate(student);
				user.student = student2;
				user.update();
				if (student2 != null) {
					return ok(views.html.module.student.studentInfo.render(student2, user));
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
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateStudentEnroll() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Student student = user.student;
		if(student!=null){
			Long courseId =  FormHelper.getLong(form().bindFromRequest(),"courseId");
			Course course = Course.find(courseId);
			if(course!=null){
				Enroll enroll = Enroll.findByStudentAndCourse(student, course);
				if(enroll!=null){
					return badRequest(Constants.MSG_USER_ENROLLED);
				}
			}else{
				return badRequest(Constants.MSG_COURSE_NOT_EXIST);
			}
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
			user.update();
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
			user.update();

		}
		Form<Student> form = form(Student.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			student = Student.addOrUpdate(form.get());
			if (student != null) {

				Agent agent = null;
				if(form().bindFromRequest().get("agentId") != null){
					Long agentId =  FormHelper.getLong(form().bindFromRequest(),"agentId");
					agent = Agent.find(agentId);
				}
				Course course = null;
				if( form().bindFromRequest().get("courseId") != null){
					Long courseId =  FormHelper.getLong(form().bindFromRequest(),"courseId");
					course = Course.find(courseId);
				}

				Enroll enroll = new Enroll();
				enroll.course = course;
				if(agent!=null){
					enroll.fromAgent = agent;
				}
				enroll.edu = course.edu;
				enroll.student = student;
				//enroll.save();
				Enroll.addOrUpdate(enroll);


				return ok(views.html.module.student.enrollInfo.render(enroll, enroll.course ,enroll.fromAgent,enroll.student.user,enroll.student));
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