package controllers;

import common.*;

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
	public static final String KEY_USER_ACCOUNT = "account";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_USER_ROLES = "user_roles";//:role1_id:role2_id:...:rolen_id:
	public static final String KEY_USER_MODULES = "user_modules";//:module1_id:module2_id:...:modulen_id:
	public static final String KEY_USER_FUNCTINS = "user_functions";//:function1_id:function2_id:...:functionn_id:
	
	// -- Authentication

	public static class Login {
		public String account;
		public String password;
		public User user;

		public String validate() {
			user = User.authenticate(account, password);
			if (user == null) {
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
		} else {//登录成功，将accout添加到session，这样就可以访问AdminController的函数了
			saveSession(loginForm.get());
			return redirect(controllers.routes.AdminController.page("index"));
		}
	}
	
	/**
	 * save information to session
	 * @param login
	 */
	public static void saveSession(Login login){
		if(StringHelper.isValidate(login.user.nickname))//存用户昵称
			session(KEY_USER_ACCOUNT, login.user.nickname);
		else
			session(KEY_USER_ACCOUNT, login.account);
		session(KEY_USER_ID, login.user.id.toString());//存用户id
		if(StringHelper.isValidate(login.user.getRoles()))
			session(KEY_USER_ROLES, login.user.getRoles());//存用户角色
		if(StringHelper.isValidate(login.user.getModles()))
			session(KEY_USER_MODULES, login.user.getModles());//存用户模块权限
		if(StringHelper.isValidate(login.user.getFunctions()))
			session(KEY_USER_FUNCTINS, login.user.getFunctions());//存用户功能权限
	}

	/**
	 * update session
	 * @param user
	 */
	public static void updateSession(User user){
		if(StringHelper.isValidate(user.nickname)){
			session(KEY_USER_ACCOUNT, user.nickname);
		} else {
			session(KEY_USER_ACCOUNT, user.username);
		}
	}
	
	/**
	 * clear session
	 */
	public static void clearSession(){
		session().clear();
	}
	
	/**
	 * get string from session
	 * @param key
	 * @return
	 */
	public static String getFromSession(Object key){
		return session().get(key);
	}
	
	/**
	 * get current user from session
	 * @return
	 */
	public static User getSessionUser(){
		Long userId = Long.valueOf(getFromSession(KEY_USER_ID));
		User user = User.find(userId);
		return user;
	}

	/**
	 * add by khx
	 * 平台登录操作
	 * @return
	 */
	public static Result loginPlatform() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(views.html.module.platform.login.render(loginForm));
			// return
			// badRequest(views.html.module.admin.login.render(form(Login.class)));
		} else {
			//登录成功，将accout添加到session，这样就可以访问AdminController的函数了
			saveSession(loginForm.get());
			return redirect(controllers.routes.PlatformController.page("index"));
		}
	}

}
