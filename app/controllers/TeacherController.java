package controllers;

import static play.data.Form.form;
import models.*;
import play.mvc.*;

import com.avaje.ebean.*;
import common.*;

import controllers.LoginController.Login;
import controllers.secure.*;

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
		// 清空缓存
		LoginController.clearSession();
		return redirect(controllers.routes.TeacherController.page(PAGE_LOGIN));
	}

}
