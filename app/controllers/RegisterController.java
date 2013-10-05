package controllers;
import common.*;
import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;


/**
 * 处理用户注册 后期要加入邮箱验证与手机号码验证
 * 
 * @author khx
 * 
 */
public class RegisterController extends BaseController {
	public static final String KEY_USER_ACCOUNT = "account";
	public static final String KEY_USER_ID = "user_id";
	// -- Authentication

	public static class Register {
		public String username;
		public String password;
		public String passwordConfirm;
		public String email;
		public User user;

		public String validate() {
			if(!password.equals(passwordConfirm))
				return Constants.MSG_PASSWORD_NOT_SAME;
			int state = User.canRegister(username, email);
			if (state == Constants.INT_USERNAME_EXIST) {
				return Constants.MSG_USERNAME_EXIST;
			}
			if (state == Constants.INT_EMAIL_EXIST) {
				return Constants.MSG_EMAIL_EXIST;
			}
			return null;
		}
	}

	public static Result addUser(){
		Form<Register> registerForm = form(Register.class).bindFromRequest();
		if (registerForm.hasErrors()) {
			return badRequest(views.html.module.platform.register.render(registerForm));
		}
		// if(!form().bindFromRequest().get("password").equals(form().bindFromRequest().get("passwordConfirm"))){
		// 	return badRequest(Constants.MSG_PASSWORD_NOT_SAME);
		// }
		// int canAdd = User.canRegister(form().bindFromRequest().get("username"), form().bindFromRequest().get("email"));
		// if(canAdd == Constants.INT_USERNAME_EXIST){
		// 	return badRequest(Constants.MSG_USERNAME_EXIST);
		// }else if(canAdd == Constants.INT_EMAIL_EXIST){
		// 	return badRequest(Constants.MSG_EMAIL_EXIST);
		// }
		else{
			User user = User.addOrUpdate(form().bindFromRequest());
			if(user != null){
				session(KEY_USER_ACCOUNT, user.username);
				session(KEY_USER_ID, user.id.toString());//存用户id
				return redirect(controllers.routes.PlatformController.page("index"));
			}
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		} 
	}
//	/**
//	 * 处理实际的登录操作
//	 * 
//	 * @return
//	 */
//	public static Result registerForm() {
//		Form<Register> registerForm = form(Register.class).bindFromRequest();
//		if (registerForm.hasErrors()) {
//			return badRequest(views.html.module.platform.register.render(registerForm));
//			// return
//			// badRequest(views.html.module.admin.login.render(form(Login.class)));
//		} else {
//			//将数据加入数据库
//			//saveSession(loginForm.get());
//			User.addOrUpdate(registerForm.get());
//			return redirect(controllers.routes.PlatformController.page("index"));
//		}
//	}
	
//	/**
//	 * save information to session
//	 * @param login
//	 */
//	public static void saveSession(Login login){
//		session(KEY_USER_ACCOUNT, login.account);
//		session(KEY_USER_ID, login.user.id.toString());//存用户id
//	}
	
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
}
