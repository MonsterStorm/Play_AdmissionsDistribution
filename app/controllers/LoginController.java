package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;

/**
 * 处理认证，登录，授权等内容
 * 
 * @author MonsterStorm
 * 
 */
public class LoginController extends BaseController {
	// -- Authentication

	public static class Login {
		public String account;
		public String password;

		public String validate() {
			if (User.authenticate(account, password) == null) {
				return "用户名或密码无效";
			}
			return null;
		}
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	public static Result login() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		return ok("xxx");
	}

	/**
	 * 处理实际的登录操作
	 * 
	 * @return
	 */
	public static Result loginAdmin() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(views.html.module.admin.login.render(loginForm));
			// return
			// badRequest(views.html.module.admin.login.render(form(Login.class)));
		} else {
			//登录成功，将accout添加到session，这样就可以访问AdminController的函数了
			session("account", loginForm.get().account);
			return redirect(controllers.routes.AdminController.page("index"));
		}
	}
}
