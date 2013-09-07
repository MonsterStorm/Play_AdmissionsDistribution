package controllers;

import play.mvc.*;

/**
 * contoller for admin
 * @author MonsterStorm
 *
 */
public class AdminController extends BaseController{
	private static final String PAGE_INDEX = "index";
	private static final String PAGE_LOGIN = "login";
	
	/**
	 * adming pages
	 * @param page
	 * @return
	 */
	public static Result page(String page){
		if(PAGE_INDEX.equalsIgnoreCase(page)){
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)){
			return ok(views.html.module.admin.login.render());
		} else {
			return ok(views.html.module.admin.index.render());
		}
	}
	
	
	public static Result login(){
		return ok();
	}
}
