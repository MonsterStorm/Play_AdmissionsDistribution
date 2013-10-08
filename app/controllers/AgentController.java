package controllers;

import static play.data.Form.form;
import models.*;
import play.mvc.*;

import com.avaje.ebean.*;
import common.*;

import controllers.LoginController.Login;
import controllers.secure.*;

import java.util.*;
import java.text.*;

import play.data.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import common.FileHelper.ErrorType;
/**
 * contoller for agent
 * 
 * @author khx
 * 
 */
@Security.Authenticated(Secured.class)
public class AgentController extends BaseController {

	private static final String PAGE_INDEX = "index";// 整个页面主体
	private static final String PAGE_HOME = "home";// 主页
	private static final String PAGE_LOGIN = "login";// 登录
	private static final String PAGE_AGENT_INFO = "agentInfo";// 代理人信息
	private static final String PAGE_TEMPLATE_AGENT_COURSES = "templateAgentCourses";// 代理人的课程信息模板页面
	private static final String PAGE_AGENT_COURSES = "agentCourses";
	private static final String PAGE_AGENT_COURSES_DETAIL ="courseDetail";
	private static final String PAGE_ALL_COURSES ="allCourse";
	private static final String REG_COURSE_AGENT = "regCourseAgent";
	/**
	 * agent pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {// 主页
			return ok(views.html.module.agent.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) {
			return ok(views.html.module.agent.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.agent.login.render(form(Login.class)));
		} else if (PAGE_AGENT_INFO.equalsIgnoreCase(page)) {// 只做登录页面跳转
			User user = LoginController.getSessionUser();
			Agent agent = user.agent;

			return ok(views.html.module.agent.agentInfo.render(agent, user));
		} else if (PAGE_TEMPLATE_AGENT_COURSES.equalsIgnoreCase(page)) {
			return getTemplateAgentCourses();
		} else if (PAGE_AGENT_COURSES.equalsIgnoreCase(page)) {// 
			return pageAgentCourses();
		} else if (PAGE_ALL_COURSES.equalsIgnoreCase(page)) {// 
			return pageAllCourses();
		}  else if (PAGE_AGENT_COURSES_DETAIL.equalsIgnoreCase(page)) {// 
			return pageCourseDetail(null);
		} else if (REG_COURSE_AGENT.equalsIgnoreCase(page)) {// 
			return regCourseAgent();
		} else {
			return badRequest("页面不存在");
		}
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public static Result logout() {
		// 清空缓存
		LoginController.clearSession();
		return redirect(controllers.routes.AgentController.page(PAGE_LOGIN));
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result addOrUpdateEntity() {
		String table = form().bindFromRequest().get("table");
		if (Agent.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人更新或添加
			return addOrUpdateAgent();
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result regCourseAgent() {
		if(form().bindFromRequest().get("id")==null){
			return badRequest(Constants.MSG_COURSE_NOT_SELECT);
		}
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.agent == null){
			return badRequest(Constants.MSG_AGENT_NOT_EXIST);
		}
		Course course = Course.find( FormHelper.getLong(form().bindFromRequest(), "id"));
		if(course == null){
			return badRequest(Constants.MSG_COURSE_NOT_EXIST);
		}
		if(course.agents != null  && course.agents.contains(user.agent)){
			return  badRequest(Constants.MSG_COURSE_AGENTED);

		}
		Audit audit = Audit.findByCourseIdAndAgentId(course.id, user.agent.id, Audit.STATUS_WAIT);
		if(audit !=null){
			return  badRequest(Constants.MSG_COURSE_REGED);
		}
		Audit a = new Audit();
		a.course = course;
		a.status = Audit.STATUS_WAIT;
		a.creator = user;
		a.createTime = System.currentTimeMillis();
		a.save();
		List<CourseType> types = CourseType.findAll();
		return ok(views.html.module.agent.courseDetail.render(course, types,user.edus,false));
	
	}


	/**
	 * 教育机构课程
	 * 
	 * @return
	 */
	public static Result pageAgentCourses() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.agent == null){
			return badRequest(Constants.MSG_AGENT_NOT_EXIST);
		}
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		
		Page<Course> courses  = Course.findPageByAgent(user.agent,form().bindFromRequest(),page,null);
		return ok(views.html.module.agent.agentCourses.render(courses));
	}

	/**
	 * 培训项目管理
	 * 
	 * @return
	 */
	public static Result pageAllCourses() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Course> courses = Course.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.agent.allCourse.render(courses));
	}

	/**
	 * 教育机构课程详情
	 * 
	 * @return
	 */
	public static Result pageCourseDetail(Course course) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		List<CourseType> types = CourseType.findAll();
		if (course == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				course = Course.find(id);
			}
		}
		if(course!=null && course.agents!=null && user.agent !=null){
			if(course.agents.contains(user.agent)){
				return ok(views.html.module.agent.courseDetail.render(course, types,user.edus,false));
			}
			return ok(views.html.module.agent.courseDetail.render(course, types,user.edus,true));
		}
		return ok(views.html.module.agent.courseDetail.render(course, types,user.edus,false));
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateAgent() {
		User user = LoginController.getSessionUser();
		if (user == null) {
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		UserInfo basicInfo = user.basicInfo;
		if (basicInfo == null) {
			basicInfo = new UserInfo();

			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get(
					"birthday"));

			basicInfo.phone = form().bindFromRequest().get("phone");

			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.save();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");

		} else {
			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get(
					"birthday"));

			basicInfo.phone = form().bindFromRequest().get("phone");
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.update();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");

		}
		Form<Agent> form = form(Agent.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Agent agent = user.agent;
			if (agent == null) {
				Agent agent2 = Agent.addOrUpdate(form.get());
				if (agent2 != null) {
					user.agent = agent2;
					user.update();
					return ok(views.html.module.agent.agentInfo.render(agent2,
							user));
				}
			} else {
				agent.info = FormHelper.getString(form().bindFromRequest(),
						"info");
				agent.name = FormHelper.getString(form().bindFromRequest(),
						"name");
				agent.contact = FormHelper.getString(form().bindFromRequest(),
						"contact");

				Agent agent2 = Agent.addOrUpdate(agent);
				if (agent2 != null) {
					user.agent = agent2;
					user.update();
					return ok(views.html.module.agent.agentInfo.render(agent2,
							user));
				}

			}
		} else if (form.hasErrors()) {
			String error = FormHelper.getFirstError(form.errors());
			play.Logger.debug("error:" + error);
			if (error != null) {
				return badRequest(error);
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}

	/**
	 * get template agent courses
	 * @return
	 */
	public static Result getTemplateAgentCourses() {
		Long agentId = FormHelper.getLong(form().bindFromRequest(), "id");
		if(agentId != null){
			Agent agent = Agent.find(agentId);
			play.Logger.debug("agent:" + agent);
			if(agent != null){
				return ok(views.html.basic.template.agentCourses.render(agent.courses));
			}
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}
}
