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
import common.FormValidator.Type;

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
	private static final String PAGE_AGENT_COURSES = "agentCourses";
	private static final String PAGE_AGENT_COURSES_DETAIL ="courseDetail";
	private static final String PAGE_ALL_COURSES ="allCourse";
	private static final String REG_COURSE_AGENT = "regCourseAgent";
	private static final String PAGE_REG_AGENT  = "regAgent";
	private static final String PAGE_AGENT_DOMAIN  = "agentDomain";
	private static final String PAGE_DOMAIN_DETAIL  = "domainDetail";
	private static final String PAGE_USER_ENROLL_BY_AGENT = "userEnrollByAgent";
	private static final String PAGE_AGENT_ENROLL_INFO = "agentEnrollInfo";
	private static final String PAGE_AGENT_RECEIPT_INFO = "agentReceiptInfo";//学员报名信息
	private static final String PAGE_AGENT_REBATE_INFOS = "agentRebateInfos";
	private static final String PAGE_AGENT_REBATE_INFO = "agentRebateInfo";
	private static final String PAGE_AGENT_STATISTICE = "agentStatistics";
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
			return agentInfo();
		} else if (PAGE_AGENT_COURSES.equalsIgnoreCase(page)) {// 
			return pageAgentCourses();
		} else if (PAGE_ALL_COURSES.equalsIgnoreCase(page)) {// 
			return pageAllCourses();
		}  else if (PAGE_AGENT_COURSES_DETAIL.equalsIgnoreCase(page)) {// 
			return pageCourseDetail(null);
		} else if (REG_COURSE_AGENT.equalsIgnoreCase(page)) {// 
			return regCourseAgent();
		}else if (PAGE_REG_AGENT.equalsIgnoreCase(page)) {// 
			return pageRegAgent();
		}else if (PAGE_AGENT_DOMAIN.equalsIgnoreCase(page)) {// 
			return pageAgentDomain();
		} else if (PAGE_DOMAIN_DETAIL.equalsIgnoreCase(page)) {// 
			return pageDomainDetail(null);
		} else if (PAGE_USER_ENROLL_BY_AGENT.equalsIgnoreCase(page)) {// 
			return userEnrollByAgent(null);
		} else if (PAGE_AGENT_ENROLL_INFO.equalsIgnoreCase(page)) {// 
			return pageAgentEnrollInfo();
		} else if(PAGE_AGENT_RECEIPT_INFO.equalsIgnoreCase(page)){
			return agentReceiptInfo();
		} else if (PAGE_AGENT_REBATE_INFOS.equalsIgnoreCase(page)) {// 
			return pageRebateInfos();
		} else if (PAGE_AGENT_REBATE_INFO.equalsIgnoreCase(page)) {// 
			return pageRebateInfo();
		} else if (PAGE_AGENT_STATISTICE.equalsIgnoreCase(page)) {// 
			return agentStatistics();
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
		}else if (Domain.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人更新或添加
			return addOrUpdateDomain();
		}else if( "agent_enroll".equalsIgnoreCase(table) ){//代理人更新
			return addOrUpdateAgentEnroll();
		}else if("agent_receipt".equalsIgnoreCase(table)){//学员报名
			return addOrUpdateAgentReceipt();
		}else if("agent_rebate".equalsIgnoreCase(table)){//学员报名
			return addOrUpdateAgentRebate();
		}else if( "agentStatistics".equalsIgnoreCase(table)  ){ //收支统计
			return statisticsAgent();
		}
		else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result doApply() {
		String table = form().bindFromRequest().get("table");
		if (table.equalsIgnoreCase("applyCourse")) {// 申请代理课程
			return applyCourse();
		}else {
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
		CourseDistribution cd = CourseDistribution.findByAgentAndCourse(user.agent.id, course.id);
		Audit audit = new Audit();
		if(cd !=null && cd.audit !=null){
			return  badRequest(Constants.MSG_COURSE_REGED);
		}else{
			audit.status = Audit.STATUS_WAIT;
			audit.creator = user;
			audit.createTime = System.currentTimeMillis();
			audit.save();
		}
		
		CourseDistribution.createDistributon(course, user.agent, audit);
		List<CourseType> types = CourseType.findAll();
		List<EducationInstitution> edus = null;
		if( course!=null && course.edu!=null && course.edu.creator !=null){
			edus = course.edu.creator.edus;
		}

		return ok(views.html.module.agent.courseDetail.render(course, types,edus,false));
	
	}


	/**
	 * 教育机构课程
	 * 
	 * @return
	 */
	public static Result pageAgentCourses() {
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
		
		Page<CourseType> types = CourseType.findPage(form().bindFromRequest(), 0, Constants.MAX_DATA_SIZE);
		
		String[] courseTypes = null;
		if(types != null && types.getList() != null && types.getList().size() > 0){
			courseTypes = new String[types.getList().size()];
			for(int i = 0; i < types.getList().size(); i++){
				courseTypes[i] = types.getList().get(i).name;
			}
		}
		
		return ok(views.html.module.agent.agentCourses.render(courses, courseTypes));
	}

	/**
	 * 培训项目管理
	 * 
	 * @return
	 */
	public static Result pageAllCourses() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Course> courses = Course.findPage(form().bindFromRequest(), page,
				null);
				
		Page<CourseType> types = CourseType.findPage(form().bindFromRequest(), 0, Constants.MAX_DATA_SIZE);
		
		String[] courseTypes = null;
		if(types != null && types.getList() != null && types.getList().size() > 0){
			courseTypes = new String[types.getList().size()];
			for(int i = 0; i < types.getList().size(); i++){
				courseTypes[i] = types.getList().get(i).name;
			}
		}

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.agent.allCourse.render(courses, courseTypes));
	}


	/**
	 * 报名管理
	 * 
	 * @return
	 */
	public static Result pageAgentEnrollInfo() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Enroll> enrolls = Enroll.findPageByAgentId(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.agent.agentEnrollInfo.render(enrolls));
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
		List<EducationInstitution> edus= null;
		if(course.edu != null){
			edus = course.edu.creator.edus;
		}
		if(course!=null && course.agents!=null && user.agent !=null){
			if(course.agents.contains(user.agent)){
				return ok(views.html.module.agent.courseDetail.render(course, types,edus,false));
			}
			return ok(views.html.module.agent.courseDetail.render(course, types,edus,true));
		}
		return ok(views.html.module.agent.courseDetail.render(course, types,edus,false));
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "realname", validateType = Type.REQUIRED, msg = "代理人姓名不能为空"),
			@FormValidator(name = "sex", validateType = Type.REQUIRED, msg = "性别不能为空"),
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "机构名称不能为空"),
			@FormValidator(name = "idcard", validateType = Type.REQUIRED, msg = "身份证号称不能为空"),
			@FormValidator(name = "idcard", validateType = Type.NUMBER, msg = "身份证号只能是数字"),
			@FormValidator(name = "birthday", validateType = Type.REQUIRED, msg = "出生日期不能为空"),
			@FormValidator(name = "phone", validateType = Type.REQUIRED, msg = "座机号码不能为空"),
			@FormValidator(name = "mobile", validateType = Type.PHONE, msg = "请填写正确的手机号码"),
			@FormValidator(name = "qq", validateType = Type.REQUIRED, msg = "QQ号码不能为空"),
			@FormValidator(name = "qq", validateType = Type.NUMBER, msg = "QQ号码只能是数字"),
			@FormValidator(name = "email", validateType = Type.EMAIL, msg = "请填写正确的邮箱地址"),
			@FormValidator(name = "address", validateType = Type.REQUIRED, msg = "联系地址不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "机构简介不能为空"),
			@FormValidator(name = "contact", validateType = Type.REQUIRED, msg = "联系方式不能为空")
	})
	public static Result addOrUpdateAgent() {
		String msg = Validator.check(AgentController.class, "addOrUpdateAgent");
		if (msg != null) {
			return badRequest(msg);
		}
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
	 * add or update instructor
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "domain", validateType = Type.REQUIRED, msg = "domain不能为空")
	})
	public static Result addOrUpdateDomain() {
		String msg = Validator.check(AgentController.class, "addOrUpdateDomain");
		if (msg != null) {
			return badRequest(msg);
		}
		User user = LoginController.getSessionUser();
		if (user == null) {
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.agent == null){
			return badRequest(Constants.MSG_AGENT_NOT_EXIST);
		}

		Form<Domain> form = form(Domain.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			String str = FormHelper.getString(form().bindFromRequest(),"domain");

			Domain domain = Domain.findByDomain(str);
			if(domain != null  ){
				return badRequest(Constants.MSG_DOMAIN_EXIST); 
			}
			Domain newDomain = new Domain();
			newDomain.id = FormHelper.getLong(form().bindFromRequest(),"id");
			newDomain.domain = FormHelper.getString(form().bindFromRequest(),"domain");
			newDomain.agent = user.agent;
			Domain dom = Domain.addOrUpdate(newDomain);
			if(dom!=null){
				return ok(views.html.module.agent.domainDetail.render(dom));
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
	 * 域名
	 * 
	 * @return
	 */
	public static Result pageAgentDomain() {
		play.Logger.error(form().bindFromRequest().get("page"));
		int page = FormHelper.getPage(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Page<Domain> domain  = Domain.findPageByAgent(user.agent,form().bindFromRequest(),page,null);
		return ok(views.html.module.agent.agentDomain.render(domain));
		
	}

	/**
	 * 域名详情
	 * 
	 * @return
	 */
	public static Result pageDomainDetail(Domain domain) {
		if (domain == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				domain = Domain.find(id);
			}
		}
		return ok(views.html.module.agent.domainDetail.render(domain));
	}

	/**
	 * 代理人为用户报名课程 此时生成随机用户名和密码
	 * 
	 * @return
	 */
	public static Result userEnrollByAgent(Enroll enroll) {
		play.Logger.error(form().bindFromRequest().get("courseId"));
		User user =  LoginController.getSessionUser();
		if (user == null) {
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Agent agent = user.agent;
		if (enroll == null) {
			//新报名
			Long courseId =   FormHelper.getLong( form().bindFromRequest(), "courseId" );//FormHelper.getLong(form().bindFromRequest(), "courseId");
			Course course = Course.find( courseId );
			//return badRequest( String.valueOf (courseId));

			return ok(views.html.module.agent.userEnrollByAgent.render(agent, null, null, course));		
		}else{
			//旧报名
			Course course = enroll.course;
			Student student = enroll.student;

			return ok(views.html.module.agent.userEnrollByAgent.render(agent, enroll, student, course));
		}
	}

	/**
	 * 代理人
	 * 
	 * @return
	 */
	public static Result pageRegAgent() {
		play.Logger.error(form().bindFromRequest().get("page"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		long id  = (long)2;
		Contract contract = Contract.find(id);//2表示 代理人协议
		return ok(views.html.module.agent.regAgent.render(contract));
		
	}

	/**
	 * 代理人
	 * 
	 * @return
	 */
	public static Result agentInfo() {
		User user = LoginController.getSessionUser();
		Agent agent = user.agent;
		return ok(views.html.module.agent.agentInfo.render(agent, user));
	}

	/**
	 * 代理人
	 * 
	 * @return
	 */
	public static Result applyCourse() {
		User user = LoginController.getSessionUser();
		if(user ==null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Agent agent = user.agent;
		if(agent ==null){
			return badRequest(Constants.MSG_AGENT_NOT_EXIST);
		}
		Long courseId = FormHelper.getLong(form().bindFromRequest(), "courseId");
		CourseDistribution temp = CourseDistribution.findByAgentAndCourse(agent.id, courseId);
		if(temp != null){
			return badRequest(Constants.MSG_AGENT_APPLYED_COURSE);
		}
		else{
			Course course = Course.find(courseId);

			Audit au = new Audit(user, Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_COURSE_DISTRIBUTION);
			
			CourseDistribution cd = CourseDistribution.saveDistributon(course, agent, au);
			if(cd != null)
			return  ok(Constants.MSG_SUCCESS);
			
		}
		return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateAgentEnroll() {

		User user1 =  LoginController.getSessionUser();
		if(user1 ==null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Agent agent = user1.agent;
		if(agent ==null){
			return badRequest(Constants.MSG_AGENT_NOT_EXIST);
		}


		Long enrollId = FormHelper.getLong(form().bindFromRequest(), "enrollId");
		if( enrollId == null ){
			//添加新的报名
			Long courseId = FormHelper.getLong(form().bindFromRequest(), "courseId");
			Course course = Course.find( courseId );
			Student student = new Student();
			User user = User.getRandomUserForStudent(student, Role.ROLE_STUDENT, Audit.STATUS_SUCCESS );
			student.user = user;
			student.user.basicInfo = new UserInfo();

			student.user.basicInfo.realname =  FormHelper.getString( form().bindFromRequest(),"realname");
			student.user.basicInfo.sex =  FormHelper.getString( form().bindFromRequest(),"sex");
			student.companyName =  FormHelper.getString( form().bindFromRequest(),"companyName");
			student.position =  FormHelper.getString( form().bindFromRequest(),"position");
			student.user.basicInfo.idcard = FormHelper.getString( form().bindFromRequest(),"idcard");
			student.user.basicInfo.birthday = FormHelper.getLong( form().bindFromRequest(),"birthday");
			student.user.basicInfo.phone  = FormHelper.getString( form().bindFromRequest(),"phone");
			student.user.mobile  = FormHelper.getString( form().bindFromRequest(),"mobile");
			student.user.basicInfo.qq  = FormHelper.getString( form().bindFromRequest(),"qq");
			student.user.email = FormHelper.getString( form().bindFromRequest(),"email");
			student.user.basicInfo.address = FormHelper.getString( form().bindFromRequest(),"address");
			student.info = FormHelper.getString( form().bindFromRequest(),"info");
			student.save();


			Audit auditOfAgent = new Audit(student.user, Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_AGENT_ENROLL);  // 代理人审核
			Audit auditOfEdu = new Audit(student.user, Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_EDU_ENROLL);// 教育机构的审核信息
			ConfirmReceipt confirmOfStu = new ConfirmReceipt(); // 学生付款确认信息
			ConfirmReceipt confirmOfEdu = new ConfirmReceipt(); // 教育机构收款信息
			ConfirmReceipt confirmOfPlatform  = new ConfirmReceipt(); // 平台收款信息
			ConfirmReceipt confirmOfAgent  = new ConfirmReceipt(); // 代理人收款信息
			auditOfAgent.save();
			auditOfEdu.save();
			confirmOfStu.save();
			confirmOfEdu.save();
			confirmOfPlatform.save();
			confirmOfAgent.save();

			Enroll enroll = new Enroll();
			enroll.student = student;
			enroll.course = course;
			enroll.fromAgent = agent;
			enroll.edu = course.edu;
			enroll.enrollByAgent = 1;
			enroll.enrollTime = System.currentTimeMillis();

			enroll.auditOfAgent = auditOfAgent;
			enroll.auditOfEdu = auditOfEdu;
			enroll.confirmOfStu = confirmOfStu;
			enroll.confirmOfEdu = confirmOfEdu;
			enroll.confirmOfPlatform = confirmOfPlatform;
			enroll.confirmOfAgent = confirmOfAgent;

			enroll.save();
			return ok(views.html.module.agent.userEnrollByAgent.render(enroll.fromAgent, enroll ,enroll.student,enroll.course));


		}else{
			//更新报名
			Long courseId = FormHelper.getLong(form().bindFromRequest(), "courseId");
			Long studentId = FormHelper.getLong(form().bindFromRequest(), "studentId");
			Course course = Course.find(courseId);
			Student student = Student.find(studentId);
			student.user.basicInfo = new UserInfo();

			student.user.basicInfo.realname =  FormHelper.getString( form().bindFromRequest(),"realname");
			student.user.basicInfo.sex =  FormHelper.getString( form().bindFromRequest(),"sex");
			student.companyName =  FormHelper.getString( form().bindFromRequest(),"companyName");
			student.position =  FormHelper.getString( form().bindFromRequest(),"position");
			student.user.basicInfo.idcard = FormHelper.getString( form().bindFromRequest(),"idcard");
			student.user.basicInfo.birthday = FormHelper.getLong( form().bindFromRequest(),"birthday");
			student.user.basicInfo.phone  = FormHelper.getString( form().bindFromRequest(),"phone");
			student.user.mobile  = FormHelper.getString( form().bindFromRequest(),"mobile");
			student.user.basicInfo.qq  = FormHelper.getString( form().bindFromRequest(),"qq");
			student.user.email = FormHelper.getString( form().bindFromRequest(),"email");
			student.user.basicInfo.address = FormHelper.getString( form().bindFromRequest(),"address");
			student.info = FormHelper.getString( form().bindFromRequest(),"info");
			student.update();

			Enroll enroll = Enroll.find(enrollId);

			enroll.student = student;
			enroll.course = course;
			enroll.fromAgent = agent;
			enroll.edu = course.edu;
			enroll.enrollByAgent = 1;
			enroll.enrollTime = System.currentTimeMillis();
			enroll.update();
			return ok(views.html.module.agent.userEnrollByAgent.render(enroll.fromAgent, enroll ,enroll.student,enroll.course));


		}


		// play.Logger.error(form().bindFromRequest().get("courseId"));
		// User user =  LoginController.getSessionUser();
		// Student student = null;
		// if(user == null){
		// 	//return badRequest(Constants.MSG_NOT_LOGIN);
			
		// 	student = new Student();
		// 	user = User.getRandomUserForStudent(student, Role.ROLE_STUDENT, Audit.STATUS_SUCCESS );
		// }
		// student = user.student;
		// if(student!=null){
		// 	Long courseId =  FormHelper.getLong(form().bindFromRequest(),"courseId");
		// 	Course course = Course.find(courseId);
		// 	if(course!=null){
		// 		Enroll enroll = Enroll.findByStudentAndCourse(student, course);
		// 		if(enroll!=null){
		// 			return badRequest(Constants.MSG_USER_ENROLLED);
		// 		}
		// 	}else{
		// 		return badRequest(Constants.MSG_COURSE_NOT_EXIST);
		// 	}
		// }


		// UserInfo basicInfo = user.basicInfo;
		// if(basicInfo == null){
		// 	basicInfo = new UserInfo();

		// 	basicInfo.realname = form().bindFromRequest().get("realname");
		// 	basicInfo.sex = form().bindFromRequest().get("sex");
		// 	basicInfo.idcard = form().bindFromRequest().get("idcard");
		// 	basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));
			

		// 	basicInfo.phone = form().bindFromRequest().get("phone");
			
		// 	basicInfo.qq = form().bindFromRequest().get("qq");
		// 	basicInfo.address = form().bindFromRequest().get("address");
		// 	basicInfo.user = user;
		// 	basicInfo.save();
		// 	user.basicInfo = basicInfo;
		// 	user.mobile = form().bindFromRequest().get("mobile");
		// 	user.email = form().bindFromRequest().get("email");
		// 	if(user.id == null){
		// 		user.save();
		// 		user.basicInfo.user = user;
		// 		user.basicInfo.update();
		// 	}else{
		// 		user.update();
		// 	}
		// }
		// else{
		// 	basicInfo.realname = form().bindFromRequest().get("realname");
		// 	basicInfo.sex = form().bindFromRequest().get("sex");
		// 	basicInfo.idcard = form().bindFromRequest().get("idcard");
		// 	basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));


		// 	basicInfo.phone = form().bindFromRequest().get("phone");
		// 	basicInfo.qq = form().bindFromRequest().get("qq");
		// 	basicInfo.address = form().bindFromRequest().get("address");
		// 	basicInfo.user = user;
		// 	basicInfo.update();
		// 	user.basicInfo = basicInfo;
		// 	user.mobile = form().bindFromRequest().get("mobile");
		// 	user.email = form().bindFromRequest().get("email");
		// 	if(user.id == null){
		// 		user.save();
		// 		user.basicInfo.user = user;
		// 		user.basicInfo.update();
		// 	}
		// 	else{
		// 		user.update();
		// 	}
		// }
		// Form<Student> form = form(Student.class).bindFromRequest();
		// if (form != null && form.hasErrors() == false) {
		// 	student = Student.addOrUpdate(form.get());
		// 	if (student != null) {
		// 		user.student = student;
		// 		user.update();
		// 		Enroll enroll = new Enroll();
		// 		Agent agent = null;
		// 		if(form().bindFromRequest().get("agentId") != null){
		// 			Long agentId =  FormHelper.getLong(form().bindFromRequest(),"agentId");
		// 			agent = Agent.find(agentId);
		// 		}
		// 		if( form().bindFromRequest().get("courseId") != null){
		// 			Long courseId =  FormHelper.getLong(form().bindFromRequest(),"courseId");
		// 			Course course = Course.find(courseId);
		// 			enroll.course = course;
		// 			if(course!=null && course.edu!=null){
		// 				enroll.edu = course.edu;
		// 			}
		// 		}

		// 		if(agent!=null){
		// 			enroll.fromAgent = agent;
		// 		}
		// 		enroll.student = student;
		// 		enroll.enrollTime = System.currentTimeMillis();
		// 		//enroll.save();
		// 		Enroll.addOrUpdate(enroll);


		// 		return ok(views.html.module.platform.platformUserEnroll.render(enroll, enroll.course ,enroll.fromAgent,enroll.student.user,enroll.student));
		// 	}
		// } else if (form.hasErrors()) {
		// 	String error = FormHelper.getFirstError(form.errors());
		// 	play.Logger.debug("error:" + error);
		// 	if (error != null) {
		// 		return badRequest(error);
		// 	}
		// }
		// return internalServerError(Constants.MSG_INTERNAL_ERROR);
		//return internalServerError(Constants.MSG_INTERNAL_ERROR);
	}

	/**
	 * 收款确认管理
	 * 
	 * @return
	 */
	public static Result agentReceiptInfo() {
		// get page
		User user = LoginController.getSessionUser();
		if(user!=null){
			Agent agent = user.agent;
			if(agent == null){
				return badRequest(Constants.MSG_AGENT_NOT_EXIST);
			}
			Long enrollId =  FormHelper.getLong(form().bindFromRequest(),"enrollId");
			Enroll enroll = Enroll.find( enrollId );

			// reset flash
			FormHelper.resetFlash(form().bindFromRequest(), flash());
			if( enroll != null){
				return ok(views.html.module.agent.agentReceiptInfo.render(enroll));
			}else{
				return badRequest(Constants.MSG_ENROLL_NOT_EXIST);
			}
		}
		return badRequest(Constants.MSG_NOT_LOGIN);
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateAgentReceipt() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Agent agent = user.agent;
		if(agent!=null){
			Long enrollId =  FormHelper.getLong(form().bindFromRequest(),"enrollId");
			Enroll enroll = Enroll.find(enrollId);
			if(enroll!=null && enroll.fromAgent.id == agent.id ){
				if( enroll.confirmOfAgent.money!= null && enroll.confirmOfAgent.money > 0 ){
					return badRequest(Constants.MSG_RECEIPT_CONFIRMED);
				}
				enroll.confirmOfAgent.money = FormHelper.getDouble(form().bindFromRequest(),"money");
				enroll.confirmOfAgent.time = System.currentTimeMillis();
				enroll.confirmOfAgent.info =  FormHelper.getString(form().bindFromRequest(),"info");
				enroll.confirmOfAgent.confirmer =  user;
				enroll.confirmOfAgent.update();
				enroll.update();

				CourseDistribution cd = CourseDistribution.findByEnrollId( enroll.id );
				if( cd != null){
					
					cd.rebate.numAgentAdmit ++;
					cd.rebate.moneyAgentAdmit += enroll.confirmOfAgent.money;
					
					cd.rebate.update();
					cd.update();
				}

				return ok(views.html.module.agent.agentReceiptInfo.render(enroll));

			}else{
				return badRequest(Constants.MSG_ENROLL_NOT_EXIST);
			}
		}else{

			return badRequest(Constants.MSG_AGENT_NOT_EXIST);
		}
	}



	/**
	 * 代理人分账
	 * 
	 * @return
	 */
	public static Result pageRebateInfos() {
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
		
		Page<Rebate> rebate  = Rebate.findPageByAgent(user.agent,form().bindFromRequest(),page,null);
		return ok(views.html.module.agent.agentRebateInfos.render(rebate));
	}

	/**
	 * 分账管理
	 * 
	 * @return
	 */
	public static Result pageRebateInfo() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "rebateId");
		if (id != null) {
			Rebate rebate = Rebate.find(id);
			return ok(views.html.module.agent.agentRebateInfo.render(rebate));
		}

		return badRequest(Constants.MSG_FORBIDDEN);
	}

	/**
	 * 分账确认
	 * 
	 * @return
	 */
	public static Result addOrUpdateAgentRebate() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		
		Long rebateId =  FormHelper.getLong(form().bindFromRequest(),"rebateId");
		Rebate rebate = Rebate.find(rebateId);
		if( rebate == null ){
			return badRequest(Constants.MSG_REBATE_NOT_EXIST);
		}

		Agent agent = rebate.distribution.agent;
		if( agent == null || agent.user.id != user.id  ){
			return badRequest(Constants.MSG_FORBIDDEN);
		}

		if( rebate.lastReceiptOfAgent.money!= null && rebate.lastReceiptOfAgent.money > 0 ){
			return badRequest(Constants.MSG_RECEIPT_CONFIRMED);
		}
		rebate.lastReceiptOfAgent.money = FormHelper.getDouble(form().bindFromRequest(),"lastReceiptOfAgent.money");
		rebate.lastReceiptOfAgent.time = System.currentTimeMillis();
		rebate.lastReceiptOfAgent.info =  FormHelper.getString(form().bindFromRequest(),"lastReceiptOfAgent.info");
		rebate.lastReceiptOfAgent.confirmer =  user;
		
		rebate.lastReceiptOfAgent.update();
		rebate.update();

		return ok(views.html.module.agent.agentRebateInfo.render(rebate));

	}


	/**
	 * 统计
	 * 输入参数为起止时间   还可以输入一些过滤数据
	 * @return
	 */
	public static Result agentStatistics() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Long start = FormHelper.getLong(form().bindFromRequest(), "start");
		Long end = FormHelper.getLong(form().bindFromRequest(), "end");
		Statistics st = new Statistics();
		st.startTime = start;
		st.endTime = end;
		return ok(views.html.module.agent.agentStatistics.render(st));		
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result statisticsAgent() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		//return badRequest(form().bindFromRequest()+"");
		Agent agent = user.agent;


		Statistics statis = Statistics.getAgentStatistics(agent, form().bindFromRequest());

		return ok(views.html.module.agent.agentStatistics.render(statis));

	}
  
}
