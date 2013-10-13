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
import java.lang.*;

import javax.validation.*;

import play.data.*;
import play.data.validation.Constraints.ValidateWith;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import common.FileHelper.ErrorType;

/**
 * contoller for education
 * 
 * @author khx
 * 
 */
@Security.Authenticated(Secured.class)
public class EducationController extends BaseController {

	private static final String PAGE_INDEX = "index";// 整个页面主体
	private static final String PAGE_HOME = "home";// 主页
	private static final String PAGE_LOGIN = "login";// 登录
	private static final String PAGE_EDUCATION_INFO = "educationInfo";
	private static final String PAGE_EDUCATION_COURSES = "educationCourses";
	private static final String PAGE_EDUCATION_COURSES_DETAIL ="courseDetail";
	private static final String PAGE_EDUCATION_INSTITUTION ="educationInstitution";
	private static final String PAGE_REG_EDUCATION  = "regEducation";
	private static final String PAGE_EDUCATION_DOMAIN  = "eduDomain";
	private static final String PAGE_DOMAIN_DETAIL  = "domainDetail";

	/**
	 * education pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {// 主页
			return ok(views.html.module.education.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) {
			return ok(views.html.module.education.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.education.login.render(form(Login.class)));
		}else if (PAGE_EDUCATION_INFO.equalsIgnoreCase(page)) {// 
			return pageEducationInfo(null);
		}else if (PAGE_EDUCATION_COURSES.equalsIgnoreCase(page)) {// 
			return pageEducationCourses();
		} else if (PAGE_EDUCATION_COURSES_DETAIL.equalsIgnoreCase(page)) {// 
			return pageCourseDetail(null);
		} else if (PAGE_EDUCATION_INSTITUTION.equalsIgnoreCase(page)) {// 
			return pageEducations();
		} else if (PAGE_REG_EDUCATION.equalsIgnoreCase(page)) {// 
			return pageRegEducations();
		}else if (PAGE_EDUCATION_DOMAIN.equalsIgnoreCase(page)) {// 
			return pageEducationDomain();
		} else if (PAGE_DOMAIN_DETAIL.equalsIgnoreCase(page)) {// 
			return pageDomainDetail(null);
		}  else {
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
		return redirect(controllers.routes.EducationController.page(PAGE_LOGIN));
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result addOrUpdateEntity() {
		String table = form().bindFromRequest().get("table");
		if (EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构更新或添加
			return addOrUpdateEducation();
		}if (Course.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构更新或添加
			return addOrUpdateCourse();
		}if (Domain.TABLE_NAME.equalsIgnoreCase(table)) {// 域名更新或添加
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
	public static Result addOrUpdateEducation() {
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
		Form<EducationInstitution> form = form(EducationInstitution.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			List<EducationInstitution>  edus = user.edus;
			if(edus == null){
				EducationInstitution education2 = EducationInstitution.addOrUpdate(form.get());
				if (education2 != null) {
					List<EducationInstitution> list = new ArrayList<EducationInstitution>();
					list.add(education2);
					user.edus = list;
					user.update();
					return ok(views.html.module.education.educationInfo.render(education2, user));
				}
			}else{
				for(EducationInstitution edu : edus){
					if(edu.name!=null && edu.name.equalsIgnoreCase(FormHelper.getString(form().bindFromRequest(),"name"))){
						return badRequest(Constants.MSG_EDUCATION_EXIST);

					}
				}
				EducationInstitution education2 = EducationInstitution.addOrUpdate(form.get());
				if (education2 != null) {
					edus.add(education2);
					user.edus = edus;
					user.update();
					return ok(views.html.module.education.educationInfo.render(education2, user));
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


	// /**
	//  * add or update instructor
	//  * 
	//  * @return
	//  */
	// public static Result addOrUpdateCourse() {
	// 	User user =  LoginController.getSessionUser();
	// 	if(user == null){
	// 		return badRequest(Constants.MSG_NOT_LOGIN);
	// 	}
		
	// 	Form<Course> form = form(Course.class).bindFromRequest();
	// 	if (form != null && form.hasErrors() == false) {
	// 		Course course = Course.addOrUpdate(form.get());
	// 		if (course != null) {
	// 			return pageCourseDetail(course);
	// 		}

	// 	} else if (form.hasErrors()) {
	// 		String error = FormHelper.getFirstError(form.errors());
	// 		play.Logger.debug("error:" + error);
	// 		if (error != null) {
	// 			return badRequest(error);
	// 		}
	// 	}
	// 	return internalServerError(Constants.MSG_INTERNAL_ERROR);
	// }

	/**
	 * add or update course
	 */
	/**
	 * add or update course
	 */
	public static Result addOrUpdateCourse() {
		Form<Course> form = form(Course.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Course course = Course.addOrUpdate(form.get());
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
	 * 教育机构课程
	 * 
	 * @return
	 */
	public static Result pageEducationCourses() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.edus == null){
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
		}
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		if(form().bindFromRequest().get("eduId") == null){
			Page<Course> courses  = Course.findPageByEducationUser(user,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.educationCourses.render(courses));
		}else{
			Long eduId = FormHelper.getLong(form().bindFromRequest(), "eduId");
			EducationInstitution edu = EducationInstitution.find(eduId);

			Page<Course> courses  = Course.findPageByEducation(edu,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.educationCourses.render(courses));
		}
	}

	/**
	 * 教育机构
	 * 
	 * @return
	 */
	public static Result pageEducations() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		int page = FormHelper.getPage(form().bindFromRequest());
		
		Page<EducationInstitution> edus  = EducationInstitution.findPageByUser(user,form().bindFromRequest(),page,null);
		return ok(views.html.module.education.educationInstitution.render(edus));
		
	}
	/**
	 * 教育机构
	 * 
	 * @return
	 */
	public static Result pageRegEducations() {
		play.Logger.error(form().bindFromRequest().get("page"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		long id  = (long)1;
		Contract contract = Contract.find(id);//1表示 教育机构协议
		return ok(views.html.module.education.regEducation.render(contract));
		
	}
	/**
	 * 教育机构课程详情
	 * 
	 * @return
	 */
	public static Result pageCourseDetail(Course course) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		List<CourseType> types = CourseType.findAll();
		if (isAddNew) {
			return ok(views.html.module.education.courseDetail.render(null, types, user.edus));
		}

		if (course == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				course = Course.find(id);
			}
		}
		return ok(views.html.module.education.courseDetail.render(course, types,user.edus));
	}

	/**
	 * 教育机构详情
	 * 
	 * @return
	 */
	public static Result pageEducationInfo(EducationInstitution edu) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if (isAddNew) {

			return ok(views.html.module.education.educationInfo.render(null, user));
		}

		if (edu == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				edu = EducationInstitution.find(id);
			}
		}
		return ok(views.html.module.education.educationInfo.render(edu, user));
	}


	/**
	 * 域名
	 * 
	 * @return
	 */
	public static Result pageEducationDomain() {
		play.Logger.error(form().bindFromRequest().get("page"));
		int page = FormHelper.getPage(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Page<Domain> domain  = Domain.findPageByEducationUser(user,form().bindFromRequest(),page,null);
		return ok(views.html.module.education.eduDomain.render(domain));
		
	}

	/**
	 * 域名详情
	 * 
	 * @return
	 */
	public static Result pageDomainDetail(Domain domain) {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if (domain == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				domain = Domain.find(id);
			}
		}
		return ok(views.html.module.education.domainDetail.render(domain,user.edus));
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
		if(user.edus == null){
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
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
			Long eduId = FormHelper.getLong(form().bindFromRequest(), "edu");
			EducationInstitution edu = EducationInstitution.find(eduId);
			if(edu == null){
				return badRequest(form().bindFromRequest().get().toString());
			}
			if(edu.creator.id != user.id){
				return badRequest(Constants.MSG_NOT_USER_EDUCATION); 
			}
			newDomain.edu = edu;
			Domain dom = Domain.addOrUpdate(newDomain);
			if(dom!=null){
				return ok(views.html.module.education.domainDetail.render(dom,user.edus));
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
