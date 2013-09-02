package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;

public class PlatformController extends BaseController {
	//pages
	private static final String PAGE_INDEX = "index";
	private static final String PAGE_SUCCESSFUL_CASE = "successful_case";
	private static final String PAGE_NEWS = "news";
	private static final String PAGE_REGISTER_AND_LOGIN = "register_and_login";
	private static final String PAGE_CONTACT_US = "contact_us";
	
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
		} else {
			return ok(views.html.module.platform.index.render());
		}
	}
}
