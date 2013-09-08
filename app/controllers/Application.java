package controllers;

import static play.data.Form.form;
import play.mvc.*;
import views.html.*;
import controllers.LoginController.Login;

public class Application extends Controller {
  
	/***
	 * 跳转到首页
	 * @return
	 */
    public static Result index() {
    	return ok(views.html.module.platform.index.render());
    }
  
    /**
     * 跳转到管理员登录页面
     * @return
     */
    public static Result admin(){
    	return ok(views.html.module.admin.login.render(form(Login.class)));
    }
}
