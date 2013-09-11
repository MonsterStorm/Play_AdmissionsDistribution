package controllers;

import static play.data.Form.form;

import java.util.*;

import models.*;
import play.mvc.*;
import controllers.LoginController.Login;
import controllers.secure.*;
import static play.data.Form.*;
/**
 * contoller for admin
 * 
 * @author MonsterStorm
 * 
 */
@Security.Authenticated(Secured.class)
public class AdminController extends BaseController {
	
	private static final String PAGE_INDEX = "index";//整个页面主体
	private static final String PAGE_HOME = "home";//主页
	private static final String PAGE_LOGIN = "login";//登录
	private static final String PAGE_AUDIT = "audit";//审计
	private static final String PAGE_ADMIN_COURSES = "adminCourses";//培训项目管理
	private static final String PAGE_ADMIN_EDUS = "adminEdus";//教育机构管理
	private static final String PAGE_ADMIN_INSTRUCTORS = "adminInstructors";//讲师管理
	private static final String PAGE_ADMIN_AGENTS = "adminAgents";//代理人管理
	private static final String PAGE_ADMIN_CONTRACTS = "adminContracts";//合约管理
	
	/**
	 * adming pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {//主页
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) { 
			return  ok(views.html.module.admin.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.admin.login.render(form(Login.class)));
		} else if (PAGE_AUDIT.equals(page)) {//审计
			return ok(views.html.module.admin.audit.render());
		} else if (PAGE_ADMIN_COURSES.equalsIgnoreCase(page)){//培训项目管理
			return pageAdminCourses();
		} else if (PAGE_ADMIN_EDUS.equalsIgnoreCase(page)){
			return pageAdminEdus();
		} else if (PAGE_ADMIN_INSTRUCTORS.equalsIgnoreCase(page)){
			return pageAdminInstructors();
		} else if (PAGE_ADMIN_AGENTS.equalsIgnoreCase(page)){
			return pageAdminAgents();
		} else if (PAGE_ADMIN_CONTRACTS.equalsIgnoreCase(page)){
			return pageAdminContracts();
		} else {
			return badRequest("页面不存在");
		}
	}
	
	/**
	 * 培训项目管理
	 * @return
	 */
	public static Result pageAdminCourses(){
		List<Course> courses = new ArrayList<Course>();
		courses.addAll(Course.findAll());
		return ok(views.html.module.admin.adminCourses.render(courses));
	}
	
	/**
	 * 教育机构管理
	 * @return
	 */
	public static Result pageAdminEdus(){
		List<EducationInstitution> edus = new ArrayList<EducationInstitution>();
		edus.addAll(EducationInstitution.findAll());
		return ok(views.html.module.admin.adminEdus.render(edus));
	}
	
	/**
	 * 教育讲师
	 * @return
	 */
	public static Result pageAdminInstructors(){
		List<Instructor> instructors = new ArrayList<Instructor>();
		instructors.addAll(Instructor.findAll());
		return ok(views.html.module.admin.adminInstructors.render(instructors));
	}
	
	/**
	 * 代理人讲师
	 * @return
	 */
	public static Result pageAdminAgents(){
		List<Agent> agents = new ArrayList<Agent>();
		agents.addAll(Agent.findAll());
		return ok(views.html.module.admin.adminAgents.render(agents));
	}
	
	/**
	 * 代理人讲师
	 * @return
	 */
	public static Result pageAdminContracts(){
		Contract contract = Contract.findUnique();
		return ok(views.html.module.admin.adminContracts.render(contract));
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public static Result logout() {
		//清空缓存
		session().clear();
		return redirect(controllers.routes.AdminController.page(PAGE_LOGIN));
	}
}
