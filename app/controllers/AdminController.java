package controllers;

import static play.data.Form.form;

import java.util.*;

import models.*;
import play.db.ebean.*;
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
		List<String> header = new ArrayList<String>();
		header.add("title");
		
		List<Model> users = new ArrayList<Model>();
		users.add(User.find(1L));
		
//		return ok(views.html.basic.admin.tool.tableBasic.render());
		
		if (PAGE_INDEX.equalsIgnoreCase(page)) {
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.admin.login.render(form(Login.class)));
		} else if (PAGE_AUDIT.equals(page)) {
			return ok(views.html.module.admin.audit.render());
		} else if (PAGE_COURSES_LIST.equals(page)){
			return ok(views.html.module.admin.coursesList.render());
		} else {
			return badRequest("页面不存在");
		}
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
