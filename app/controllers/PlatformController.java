package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;
import controllers.RegisterController.Register;
import controllers.LoginController.Login;
public class PlatformController extends BaseController {
	//pages
	private static final String PAGE_INDEX = "index";
	private static final String PAGE_SUCCESSFUL_CASE = "successful_case";
	private static final String PAGE_NEWS = "news";
	private static final String PAGE_REGISTER_AND_LOGIN = "register_and_login";
	private static final String PAGE_CONTACT_US = "contact_us";
	private static final String PAGE_REGISTER_STUDENT = "student_register";
	private static final String PAGE_REGISTER_AGENT = "agent_register";
	private static final String PAGE_REGISTER_EDUCATION = "education_register";
	private static final String PAGE_REGISTER = "register";
	private static final String PAGE_LOGIN = "login";
	/**
	 * get a page
	 * @param page
	 * @return
	 */
	public static Result page(String page){
		//set response charset
		response().setContentType("text/html; charset=utf-8");
		
		if (PAGE_INDEX.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.index.render());
		} else if (PAGE_SUCCESSFUL_CASE.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.successfual_case.render());
		} else if (PAGE_NEWS.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.news.render());
		} else if (PAGE_REGISTER_AND_LOGIN.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.register_and_login.render());
		} else if(PAGE_CONTACT_US.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_REGISTER.equalsIgnoreCase(page)){
			//普通注册
			return ok(views.html.module.platform.register.render(form(Register.class)));
		} else if(PAGE_REGISTER_STUDENT.equalsIgnoreCase(page)){
			//学生注册
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_REGISTER_AGENT.equalsIgnoreCase(page)){
			//代理人注册
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_REGISTER_EDUCATION.equalsIgnoreCase(page)){
			//教育机构注册
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_LOGIN.equalsIgnoreCase(page)){
			//教育机构注册
			return ok(views.html.module.platform.login.render(form(Login.class)));
		}
		else {
			return ok(views.html.module.platform.index.render());
		}
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public static Result logout() {
		//清空缓存
		LoginController.clearSession();
		return redirect(controllers.routes.PlatformController.page(PAGE_INDEX));
	}
}