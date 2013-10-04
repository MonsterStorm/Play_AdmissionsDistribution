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
     /**
     * 跳转到代理人登录页面
     * @return
     */
    public static Result agent(){
        return ok(views.html.module.agent.login.render(form(Login.class)));
    }
     /**
     * 跳转到学生登录页面
     * @return
     */
    public static Result student(){
        return ok(views.html.module.student.login.render(form(Login.class)));
    }
     /**
     * 跳转到讲师登录页面
     * @return
     */
    public static Result teacher(){
        return ok(views.html.module.teacher.login.render(form(Login.class)));
    }
     /**
     * 跳转到教育机构登录页面
     * @return
     */
    public static Result education(){
        return ok(views.html.module.education.login.render(form(Login.class)));
    }
}
