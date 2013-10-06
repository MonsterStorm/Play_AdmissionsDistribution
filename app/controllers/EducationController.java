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
			User user =  LoginController.getSessionUser();
			return ok(views.html.module.education.educationInfo.render(null, user));
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

}
