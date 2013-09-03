package controllers;

import play.mvc.*;

public class AdminController extends BaseController{
	private static final String PAGE_INDEX = "index";
	private static final String PAGE_LOGIN = "login";
	
	public static Result page(String page){
		if(PAGE_INDEX.equalsIgnoreCase(page)){
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)){
			return ok(views.html.module.admin.login.render());
		} else {
			return ok(views.html.module.admin.index.render());
		}
	}
}
