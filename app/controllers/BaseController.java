package controllers;

import play.mvc.*;
/**
 * 控制类基类，封装一些常用方法
 * @author MonsterStorm
 *
 */
public class BaseController extends Controller{

	/**
	 * 返回普通页面
	 * @param page
	 * @return
	 */
	public static Result _page(String page){
		return ok(page);
	}
	
	/**
	 * 返回表单页面
	 * @param v
	 * @param clazz
	 * @return
	 */
	public static Result _form(Class clazz){
		return ok();
	}
	
	
	
}
