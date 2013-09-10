package controllers;

import static play.data.Form.form;

import java.util.*;

import models.*;
import play.mvc.*;
import controllers.LoginController.Login;
import controllers.secure.*;
/**
 * contoller for admin
 * 
 * @author MonsterStorm
 * 
 */
@Security.Authenticated(Secured.class)
public class AdminController extends BaseController {
	private static final String PAGE_INDEX = "index";
	private static final String PAGE_LOGIN = "login";
	private static final String PAGE_AUDIT = "audit";
	private static final String PAGE_COURSES_LIST = "coursesList";
	/**
	 * adming pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {//主页
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.admin.login.render(form(Login.class)));
		} else if (PAGE_AUDIT.equals(page)) {//审计
			return ok(views.html.module.admin.audit.render());
		} else if (PAGE_COURSES_LIST.equalsIgnoreCase(page)){//培训项目管理
			return pageCoursesList();
		} else {
			return badRequest("页面不存在");
		}
	}
	
	/**
	 * 培训项目管理
	 * @return
	 */
	public static Result pageCoursesList(){
		List<Course> courses = new ArrayList<Course>();
		courses.addAll(Course.findAll());
		return ok(views.html.module.admin.coursesList.render(courses));
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public static Result logout() {
		//清空缓存
		session().clear();
		return redirect(controllers.routes.AdminController.page(PAGE_LOGIN));
	}
}
