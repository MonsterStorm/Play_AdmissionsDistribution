package controllers;

import static play.data.Form.form;
import play.api.mvc.*;
import play.mvc.Result;
import play.mvc.Security;
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

	/**
	 * adming pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.admin.login.render(form(Login.class)));
		} else if (PAGE_AUDIT.equals(page)) {
			return ok(views.html.module.admin.audit.render());
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
