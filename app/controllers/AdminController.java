package controllers;

import static play.data.Form.form;
import models.*;
import play.mvc.*;

import com.avaje.ebean.*;
import common.*;
import common.FormValidator.*;

import controllers.LoginController.Login;
import controllers.secure.*;

/**
 * contoller for admin
 * 
 * @author MonsterStorm
 * 
 */
@Security.Authenticated(Secured.class)
public class AdminController extends BaseController {

	private static final String PAGE_INDEX = "index";// 整个页面主体
	private static final String PAGE_HOME = "home";// 主页
	private static final String PAGE_LOGIN = "login";// 登录
	private static final String PAGE_AUDIT = "audit";// 审计
	private static final String PAGE_ADMIN_COURSES = "adminCourses";// 培训项目管理
	private static final String PAGE_ADMIN_EDUS = "adminEdus";// 教育机构管理
	private static final String PAGE_ADMIN_INSTRUCTORS = "adminInstructors";// 讲师管理
	private static final String PAGE_ADMIN_STUDENTS = "adminStudents";// 学员管理
	private static final String PAGE_ADMIN_AGENTS = "adminAgents";// 代理人管理
	private static final String PAGE_ADMIN_CONTRACTS = "adminContracts";// 合约管理
	private static final String PAGE_ADMIN_REBATES = "adminRebates";// 返点统计
	private static final String PAGE_ADMIN_TEMPLATE_TYPES = "adminTemplateTypes";// 模板类型管理
	private static final String PAGE_ADMIN_LOGIN_LOGS = "adminLoginLogs";// 登录日志
	private static final String PAGE_ADMIN_MESSAGES = "adminMessages";// 留言管理
	private static final String PAGE_ADMIN_OPERATINO_LOGS = "adminOperationLogs";// 操作日志
	private static final String PAGE_ADMIN_NEWS_TYPE = "adminNewsType";// 新闻类型
	private static final String PAGE_ADMIN_NEWS = "adminNews";// 新闻信息
	private static final String PAGE_ADMIN_ADVERTISMENTS = "adminAdvertisments";// 广告信息
	private static final String PAGE_ADMIN_COURSE_TYPE = "adminCourseType";// 课程类型管理
	private static final String PAGE_ADMIN_USERS = "adminUsers";//用户管理
	private static final String PAGE_ADMIN_STUDENT_WORDS = "adminStudentWords";//用户管理
	/**
	 * adming pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {// 主页
			return ok(views.html.module.admin.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) {
			return ok(views.html.module.admin.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.admin.login.render(form(Login.class)));
		} else if (PAGE_AUDIT.equals(page)) {// 审计
			return ok(views.html.module.admin.audit.render());
		} else if (PAGE_ADMIN_COURSES.equalsIgnoreCase(page)) {// 培训项目管理
			return pageAdminCourses();
		} else if (PAGE_ADMIN_EDUS.equalsIgnoreCase(page)) {
			return pageAdminEdus();
		} else if (PAGE_ADMIN_INSTRUCTORS.equalsIgnoreCase(page)) {
			return pageAdminInstructors();
		} else if (PAGE_ADMIN_STUDENTS.equalsIgnoreCase(page)) {
			return pageAdminStudents();
		} else if(PAGE_ADMIN_USERS.equalsIgnoreCase(page)) {
			return pageAdminUsers();
		} else if (PAGE_ADMIN_AGENTS.equalsIgnoreCase(page)) {
			return pageAdminAgents();
		} else if (PAGE_ADMIN_CONTRACTS.equalsIgnoreCase(page)) {
			return pageAdminContracts();
		} else if (PAGE_ADMIN_TEMPLATE_TYPES.equalsIgnoreCase(page)) {
			return pageAdminTemplateTypes();
		} else if (PAGE_ADMIN_REBATES.equalsIgnoreCase(page)) {
			return pageAdminRebates();
		} else if (PAGE_ADMIN_MESSAGES.equalsIgnoreCase(page)) {
			return pageAdminMessages();
		} else if (PAGE_ADMIN_LOGIN_LOGS.equalsIgnoreCase(page)) {
			return pageAdminLoginLogs();
		} else if (PAGE_ADMIN_OPERATINO_LOGS.equalsIgnoreCase(page)) {
			return pageAdminOperationLogs();
		} else if (PAGE_ADMIN_NEWS_TYPE.equalsIgnoreCase(page)) {// 新闻类型管理
			return pageAdminNewsType();
		} else if (PAGE_ADMIN_NEWS.equalsIgnoreCase(page)) {// 新闻信息管理
			return pageAdminNews();
		} else if (PAGE_ADMIN_ADVERTISMENTS.equalsIgnoreCase(page)) {// 广告管理
			return pageAdminAdvertisments();
		} else if(PAGE_ADMIN_COURSE_TYPE.equalsIgnoreCase(page)) {
			return pageAdminCourseType();
		}else if(PAGE_ADMIN_STUDENT_WORDS.equalsIgnoreCase(page)) {
			return pageAdminStudentWords();
		}  else {
			return badRequest("页面不存在");
		}
	}

	/**
	 * 培训项目管理
	 * 
	 * @return
	 */
	public static Result pageAdminCourses() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Course> courses = Course.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminCourses.render(courses));
	}

	/**
	 * 教育机构管理
	 * 
	 * @return
	 */
	public static Result pageAdminEdus() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<EducationInstitution> edus = EducationInstitution.findPage(form()
				.bindFromRequest(), page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminEdus.render(edus));
	}

	/**
	 * 教育讲师
	 * 
	 * @return
	 */
	public static Result pageAdminInstructors() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Instructor> instructors = Instructor.findPage(form()
				.bindFromRequest(), page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminInstructors.render(instructors));
	}

	/**
	 * 教育讲师
	 * 
	 * @return
	 */
	public static Result pageAdminStudents() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Student> students = Student.findPage(form().bindFromRequest(),
				page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminStudents.render(students));
	}
	
	/**
	 * 教育讲师
	 * 
	 * @return
	 */
	public static Result pageAdminUsers() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<User> users = User.findPage(form().bindFromRequest(), page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminUsers.render(users));
	}

	/**
	 * 代理人管理
	 * 
	 * @return
	 */
	public static Result pageAdminAgents() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Agent> agents = Agent.findPage(form().bindFromRequest(), page,
				null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminAgents.render(agents));
	}

	/**
	 * 合同管理
	 * 
	 * @return
	 */
	public static Result pageAdminContracts() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Contract> contracts = Contract.findPage(form().bindFromRequest(),
				page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminContracts.render(contracts));
	}

	/**
	 * 返点管理
	 * 
	 * @return
	 */
	public static Result pageAdminTemplateTypes() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<TemplateType> templateTypes = TemplateType.findPage(form()
				.bindFromRequest(), page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminTemplateTypes
				.render(templateTypes));
	}

	/**
	 * 返点管理
	 * 
	 * @return
	 */
	public static Result pageAdminRebates() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Rebate> rebates = Rebate.findPage(form().bindFromRequest(), page,
				null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminRebates.render(rebates));
	}

	/**
	 * 返点管理
	 * 
	 * @return
	 */
	public static Result pageAdminMessages() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Message> messages = Message.findPage(form().bindFromRequest(),
				page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminMessages.render(messages));
	}

	/**
	 * 返点管理
	 * 
	 * @return
	 */
	public static Result pageAdminLoginLogs() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<LogLogin> logs = LogLogin.findPage(form().bindFromRequest(), page,
				null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminLoginLogs.render(logs));
	}

	/**
	 * 返点管理
	 * 
	 * @return
	 */
	public static Result pageAdminOperationLogs() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<LogOperation> logs = LogOperation.findPage(form()
				.bindFromRequest(), page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminOperationLogs.render(logs));
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public static Result logout() {
		// 清空缓存
		LoginController.clearSession();
		return redirect(controllers.routes.AdminController.page(PAGE_LOGIN));
	}

	/**
	 * 新闻类型管理
	 * 
	 * @return
	 */
	public static Result pageAdminNewsType() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<NewsType> newstype = NewsType.findPage(form().bindFromRequest(),
				page, null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminNewsType.render(newstype));
	}

	/**
	 * 新闻类型管理
	 * 
	 * @return
	 */
	public static Result pageAdminNews() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<News> news = News.findPage(form().bindFromRequest(), page, null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminNews.render(news));
	}

	/**
	 * 广告管理
	 * 
	 * @return
	 */
	public static Result pageAdminAdvertisments() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Advertisment> advertisments = Advertisment.findPage(form()
				.bindFromRequest(), page, null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminAdvertisments
				.render(advertisments));
	}
	/**
	 * 课程类型详情
	 * 
	 * @return
	 */
	public static Result pageAdminCourseType() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<CourseType> courseType = CourseType.findPage(form().bindFromRequest(), page, null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminCourseType.render(courseType));
	}

	/**
	 * 学员感言详情
	 * 
	 * @return
	 */
	public static Result pageAdminStudentWords() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<StudentWords> studentWords = StudentWords.findPage(form().bindFromRequest(), page, null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.admin.adminStudentWords.render(studentWords));
	}

}
