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
		public String username;
		public String password;

		public String validate() {
			if (User.authenticate(username, password) == null) {
				return "用户名或密码无效";
			}
			return null;
		}
	}

	/**
	 * 返回一个表单
	 * @return
	 */
	public static Result pageForm(){
		return ok(views.html.basic.login.render(form(Login.class)));
	}
	
	/**
	 * 登录
	 * @return
	 */
	public static Result login(){
		Form<Login> loginForm = form(Login.class).bindFromRequest();
	    return ok("xxx");
	}
}
