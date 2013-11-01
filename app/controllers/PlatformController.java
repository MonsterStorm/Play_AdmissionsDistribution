package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;
import controllers.RegisterController.Register;
import controllers.LoginController.Login;
import controllers.secure.*;

import com.avaje.ebean.*;
import common.*;
import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

import play.data.*;
import play.data.validation.Constraints.ValidateWith;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import common.FileHelper.ErrorType;
import common.FormValidator.Type;


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
	private static final String PAGE_NEWS_DETAIL = "newsDetail";
	private static final String PAGE_TEACHER = "teacher";
	private static final String PAGE_STUDENT_WORDS = "studentWords";
	private static final String PAGE_TEACHER_DETAIL = "teacherDetail";
	private static final String PAGE_NEWS_COMP = "newsComp";
	private static final String PAGE_COURSE_COMP = "courseComp";
	private static final String PAGE_TEACHER_COMP = "teacherComp";
	private static final String PAGE_STUDENTWORDS_COMP = "studentWordsComp";
	private static final String PAGE_COURSE = "course";
	private static final String PAGE_COURSE_DETAIL = "courseDetail";
	private static final String PAGE_PLATFORM_ADV = "platformAdv";//图片广告
	private static final String PAGE_PLATFORM_ADV2= "platformAdv2";//侧栏连接广告
	private static final String PAGE_USER_ENROLL = "pageUserEnroll";//用户报名课程
	private static final String PAGE_EDUCATION2= "platformEducation2";//教育机构广告
	private static final String PAGE_REG_AGENT  = "regAgent";//代理人注册
 	private static final String PAGE_REG_EDUCATION  = "regEducation";//教育机构注册
 	private static final String PAGE_REG_TEACHER  = "regTeacher";//教育机构注册
 	private static final String PAGE_EDUINST_LIST= "eduInstList";//
 	private static final String PAGE_EDUINST_INTRODUCTION= "eduInstIntroduction";//
 	private static final String PAGE_ADD_EDUCATION= "addEducation";//
 	private static final String PAGE_ADD_AGENT= "addAgent";//
 	private static final String PAGE_ADD_TEACHER= "addTeacher";//


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
			return pagePlatformNews();
			//return ok(views.html.module.platform.news.render());
		}  else if (PAGE_STUDENT_WORDS.equalsIgnoreCase(page)){
			return pagePlatformStudentWords();
		}  else if (PAGE_TEACHER.equalsIgnoreCase(page)){
			return pagePlatformTeacher();
			//return ok(views.html.module.platform.news.render());
		} else if (PAGE_NEWS_COMP.equalsIgnoreCase(page)){
			return pagePlatformNewsComp();
			//return ok(views.html.module.platform.news.render());
		} else if (PAGE_STUDENTWORDS_COMP.equalsIgnoreCase(page)){
			return pageStudentWordsComp();
			//return ok(views.html.module.platform.news.render());
		} else if (PAGE_COURSE_COMP.equalsIgnoreCase(page)){
			return pagePlatformCourseComp();
			//return ok(views.html.module.platform.news.render());
		} else if (PAGE_TEACHER_COMP.equalsIgnoreCase(page)){
			return pagePlatformTeacherComp();
			//return ok(views.html.module.platform.news.render());
		} else if (PAGE_REGISTER_AND_LOGIN.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.register_and_login.render());
		} else if(PAGE_CONTACT_US.equalsIgnoreCase(page)){
			//return ok(views.html.module.platform.contact_us.render());
			return pageContactUs();
		}else if(PAGE_REGISTER.equalsIgnoreCase(page)){
			//普通注册
			return ok(views.html.module.platform.register.render(form(Register.class)));
		} else if(PAGE_REGISTER_STUDENT.equalsIgnoreCase(page)){
			//学生注册
			return ok(views.html.module.platform.contact_us.render(null));
		}else if(PAGE_REGISTER_AGENT.equalsIgnoreCase(page)){
			//代理人注册
			return ok(views.html.module.platform.contact_us.render(null));
		}else if(PAGE_REGISTER_EDUCATION.equalsIgnoreCase(page)){
			//教育机构注册
			return ok(views.html.module.platform.contact_us.render(null));
		}else if(PAGE_LOGIN.equalsIgnoreCase(page)){
			//教育机构注册
			return ok(views.html.module.platform.login.render(form(Login.class)));
		}else if (PAGE_NEWS_DETAIL.equalsIgnoreCase(page)){
			return pageNewsDetail();
		}else if(PAGE_COURSE.equalsIgnoreCase(page)){
			return pagePlatformCourse();
		}else if(PAGE_COURSE_DETAIL.equalsIgnoreCase(page)){
			return pageCourseDetail();
		}else if(PAGE_TEACHER_DETAIL.equalsIgnoreCase(page)){
			return pageTeacherDetail();
		}else if(PAGE_PLATFORM_ADV.equalsIgnoreCase(page)){
			return pagePlatformAdv();
		}else if(PAGE_PLATFORM_ADV2.equalsIgnoreCase(page)){
			return pagePlatformAdv2();
		}else if(PAGE_EDUCATION2.equalsIgnoreCase(page)){
			return pageEducation2();
		}else if(PAGE_USER_ENROLL.equalsIgnoreCase(page)){
			return pageUserEnroll();
		}else if (PAGE_REG_AGENT.equalsIgnoreCase(page)) {// 
			return pageRegAgent();
		}else if (PAGE_REG_TEACHER.equalsIgnoreCase(page)) {// 
			return pageRegTeacher();
		}else if (PAGE_REG_EDUCATION.equalsIgnoreCase(page)) {// 
			return pageRegEducations();
		}else if(PAGE_EDUINST_LIST.equalsIgnoreCase(page)){
			return pageEduInstList();
		}else if(PAGE_EDUINST_INTRODUCTION.equalsIgnoreCase(page)){
			return pageEduInstIntroduction();
  		}else if(PAGE_ADD_EDUCATION.equalsIgnoreCase(page)){
			return pageAddEducation();
  		}else if(PAGE_ADD_AGENT.equalsIgnoreCase(page)){
			return pageAddAgent();
  		}else if(PAGE_ADD_TEACHER.equalsIgnoreCase(page)){
			return pageAddTeacher();
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

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result addOrUpdateEntity() {
		String table = form().bindFromRequest().get("table");
		if("student_enroll".equalsIgnoreCase(table)){//学员报名
			return addOrUpdateStudentEnroll();
		}if (EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构更新或添加
			return addOrUpdateEducation();
		}else if (Agent.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人更新或添加
			return addOrUpdateAgent();
		}else if (Instructor.TABLE_NAME.equalsIgnoreCase(table)) {// 讲师信息更新或添加
			return addOrUpdateTeacher();
		}
		 else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * 返回教育机构信息
	 * 
	 * @return
	 */
	public static Result pageAddEducation() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		return ok(views.html.module.platform.addEducation.render(null, user));
	}

	/**
	 * 返回代理人信息
	 * 
	 * @return
	 */
	public static Result pageAddAgent() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}

		return ok(views.html.module.platform.addAgent.render(null, user));
	}
	/**
	 * 返回讲师信息
	 * 
	 * @return
	 */
	public static Result pageAddTeacher() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		return ok(views.html.module.platform.addTeacher.render(null, user));
	}


	/**
	 * 返回新闻列表
	 * 
	 * @return
	 */
	public static Result pagePlatformNews() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<News> news = News.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.news.render(news));
	}
	/**
	 * 返回新闻列表
	 * 
	 * @return
	 */
	public static Result pagePlatformStudentWords() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<StudentWords> words = StudentWords.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.studentWords.render(words));
	}
	/**
	 * 返回讲师列表
	 * 
	 * @return
	 */
	public static Result pagePlatformTeacher() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Instructor> teachers = Instructor.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.teacher.render(teachers));
	}
	/**
	 * 返回新闻列表
	 * 
	 * @return
	 */
	public static Result pagePlatformNewsComp() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<News> news = News.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.newsComp.render(news));
	}
	/**
	 * 返回学员感言列表
	 * 
	 * @return
	 */
	public static Result pageStudentWordsComp() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<StudentWords> words = StudentWords.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.studentWordsComp.render(words));
	}
	/**
	 * 返回课程列表
	 * 
	 * @return
	 */
	public static Result pagePlatformCourseComp() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Course> course = Course.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.courseComp.render(course));
	}
	/**
	 * 返回讲师列表
	 * 
	 * @return
	 */
	public static Result pagePlatformTeacherComp() {
		play.Logger.error(form().bindFromRequest().get("page"));
		
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Instructor> instructor = Instructor.findPage(form().bindFromRequest(), page,
				12);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.teacherComp.render(instructor));
	}

	/**
	 * 新闻详情
	 * 
	 * @return
	 */
	public static Result pageNewsDetail() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		News news = null;
		if (id != null) {
			news = News.find(id);
		}
		return ok(views.html.module.platform.newsDetail.render(news));
	}

	/**
	 * 返回课程列表
	 * 
	 * @return
	 */
	public static Result pagePlatformCourse() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Course> course = Course.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.course.render(course));
	}
	/**
	 * 课程详情
	 * 
	 * @return
	 */
	public static Result pageCourseDetail() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		Course course = null;
		if (id != null) {
			course = Course.find(id);
		}
		return ok(views.html.module.platform.courseDetail.render(course));
	}
	/**
	 * 讲师详情
	 * 
	 * @return
	 */
	public static Result pageTeacherDetail() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		Instructor teacher = null;
		if (id != null) {
			teacher = Instructor.find(id);
		}
		return ok(views.html.module.platform.teacherDetail.render(teacher));
	}
	/**
	 * 广告图片详情
	 * 
	 * @return
	 */
	public static Result pagePlatformAdv() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Advertisment> advertisment = Advertisment.findPage(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.platformAdv.render(advertisment));
	}
	/**
	 * 侧栏链接广告详情
	 * 
	 * @return
	 */
	public static Result pagePlatformAdv2() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Advertisment> advertisment = Advertisment.findPage(form().bindFromRequest(), page,
				5);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.platformAdv2.render(advertisment));
	}
	/**
	 * 侧栏链接教育机构
	 * 
	 * @return
	 */
	public static Result pageEducation2() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<EducationInstitution> education = EducationInstitution.findPage(form().bindFromRequest(), page,
				5);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.platformEducation2.render(education));
	}
	/**
	* 前台用户报名课程
	* 
	**/
	public static Result pageUserEnroll(){
		play.Logger.error(form().bindFromRequest().get("courseId"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Student student = user.student;
		Long courseId =FormHelper.getLong( form().bindFromRequest(),"courseId");
		Course course = Course.find(courseId);
		if(student!=null){
			
			if(course!=null){
				Enroll enroll = Enroll.findByStudentAndCourse(student, course);
				if(enroll!=null){
					return badRequest(Constants.MSG_USER_ENROLLED);
				}
				//return badRequest(form().bindFromRequest().get("courseId"));
				return ok(views.html.module.platform.platformUserEnroll.render(null, course ,null ,user ,student));

			}else{
				return badRequest(Constants.MSG_COURSE_NOT_EXIST);
			}
		}
		//return badRequest("name" + course.name);
		return ok(views.html.module.platform.platformUserEnroll.render(null, course , null , user ,null));

	}
	
	/**
	* 联系我们页面
	* 留言列表
	**/
	public static Result pageContactUs(){
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Message> message = Message.findPage(form().bindFromRequest(), page, 5);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.contact_us.render(message));
	}
	




	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateStudentEnroll() {
		play.Logger.error(form().bindFromRequest().get("courseId"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Student student = user.student;
		if(student!=null){
			Long courseId =  FormHelper.getLong(form().bindFromRequest(),"courseId");
			Course course = Course.find(courseId);
			if(course!=null){
				Enroll enroll = Enroll.findByStudentAndCourse(student, course);
				if(enroll!=null){
					return badRequest(Constants.MSG_USER_ENROLLED);
				}
			}else{
				return badRequest(Constants.MSG_COURSE_NOT_EXIST);
			}
		}


		UserInfo basicInfo = user.basicInfo;
		if(basicInfo == null){
			basicInfo = new UserInfo();

			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));
			

			basicInfo.phone = form().bindFromRequest().get("phone");
			
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.save();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			user.update();
		}
		else{
			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));


			basicInfo.phone = form().bindFromRequest().get("phone");
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.update();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			user.update();

		}
		Form<Student> form = form(Student.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			student = Student.addOrUpdate(form.get());
			if (student != null) {
				user.student = student;
				user.update();
				Enroll enroll = new Enroll();
				Agent agent = null;
				if(form().bindFromRequest().get("agentId") != null){
					Long agentId =  FormHelper.getLong(form().bindFromRequest(),"agentId");
					agent = Agent.find(agentId);
				}
				if( form().bindFromRequest().get("courseId") != null){
					Long courseId =  FormHelper.getLong(form().bindFromRequest(),"courseId");
					Course course = Course.find(courseId);
					enroll.course = course;
					if(course!=null && course.edu!=null){
						enroll.edu = course.edu;
					}
				}

				if(agent!=null){
					enroll.fromAgent = agent;
				}
				enroll.student = student;
				enroll.enrollTime = System.currentTimeMillis();
				//enroll.save();
				Enroll.addOrUpdate(enroll);


				return ok(views.html.module.platform.platformUserEnroll.render(enroll, enroll.course ,enroll.fromAgent,enroll.student.user,enroll.student));
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
	* 教育机构列表
	*
	**/
	public static Result pageEduInstList(){
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<EducationInstitution> eduInst = EducationInstitution.findPage(form().bindFromRequest(), page,
				null);
 
		FormHelper.resetFlash(form().bindFromRequest(), flash());
		
		return ok(views.html.module.platform.eduInstList.render(eduInst));
	}
	
	/**
	* 教育机构介绍
	*
	**/
	public static Result pageEduInstIntroduction(){
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		EducationInstitution eduInst = null;
		if (id != null) {
			eduInst = EducationInstitution.find(id);
		}
		
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		Page<Course> course = Course.findPageByEducation(eduInst,form(),page,10);
		
		FormHelper.resetFlash(form().bindFromRequest(), flash());
		
		return ok(views.html.module.platform.eduInstIntroduction.render(eduInst, course));
	}
	/**
	 * 代理人注册
	 * 
	 * @return
	 */
	public static Result pageRegAgent() {
		play.Logger.error(form().bindFromRequest().get("page"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.agent != null && user.agent.audit != null && user.agent.audit.status == Audit.STATUS_WAIT){
			return badRequest(Constants.MSG_AGENT_APPLYED_COURSE);
		}
		if(user.agent !=null){
			return ok(views.html.module.teacher.index.render());
		}
		long id  = (long)2;
		Contract contract = Contract.find(id);
		return ok(views.html.module.platform.regAgent.render(contract));
	}
	/**
	 * 教育机构注册
	 * 
	 * @return
	 */
	public static Result pageRegEducations() {
		play.Logger.error(form().bindFromRequest().get("page"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		long id  = (long)1;
		Contract contract = Contract.find(id);
		return ok(views.html.module.platform.regEducation.render(contract));		
	}

	/**
	 * 讲师注册
	 * 
	 * @return
	 */
	public static Result pageRegTeacher() {
		play.Logger.error(form().bindFromRequest().get("page"));
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.instructor != null && user.instructor.audit != null && user.instructor.audit.status == Audit.STATUS_WAIT){
			return badRequest(Constants.MSG_AGENT_APPLYED_COURSE);
		}
		if(user.instructor !=null){
			return ok(views.html.module.teacher.index.render());
		}
		long id  = (long)3;
		Contract contract = Contract.find(id);
		return ok(views.html.module.platform.regTeacher.render(contract));
	}
	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "realname", validateType = Type.REQUIRED, msg = "真实姓名不能为空"),
			@FormValidator(name = "sex", validateType = Type.REQUIRED, msg = "性别不能为空"),
			@FormValidator(name = "idcard", validateType = Type.REQUIRED, msg = "身份证号不能为空"),
			@FormValidator(name = "idcard", validateType = Type.NUMBER, msg = "身份证号只能为数字"),
			@FormValidator(name = "birthday", validateType = Type.REQUIRED, msg = "出生日期不能为空"),
			@FormValidator(name = "phone", validateType = Type.PHONE, msg = "请填写正确的座机号码"),
			@FormValidator(name = "mobile", validateType = Type.PHONE, msg = "请填写正确的手机号码"),
			@FormValidator(name = "qq", validateType = Type.REQUIRED, msg = "qq号码不能为空"),
			@FormValidator(name = "qq", validateType = Type.NUMBER, msg = "qq号码只能为数字"),
			@FormValidator(name = "email", validateType = Type.EMAIL, msg = "请填写正确的邮箱"),
			@FormValidator(name = "address", validateType = Type.REQUIRED, msg = "联系地址不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "机构简介不能为空"),
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "教育机构名称不能为空")
	})
	public static Result addOrUpdateEducation() {
		String msg = Validator.check(PlatformController.class, "addOrUpdateEducation");
		if (msg != null) {
			return badRequest(msg);
		}
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		UserInfo basicInfo = user.basicInfo;
		if(basicInfo == null){
			basicInfo = new UserInfo();

			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));
			

			basicInfo.phone = form().bindFromRequest().get("phone");
			
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.save();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			
		}
		else{
			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));


			basicInfo.phone = form().bindFromRequest().get("phone");
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.update();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			

		}
		Form<EducationInstitution> form = form(EducationInstitution.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			List<EducationInstitution>  edus = user.edus;
			if(edus == null){
				EducationInstitution education2 = EducationInstitution.addOrUpdate(form.get());
				if (education2 != null) {
					List<EducationInstitution> list = new ArrayList<EducationInstitution>();
					list.add(education2);
					user.edus = list;
					user.update();
					return ok(views.html.module.platform.addEducation.render(education2, user));
				}
			}else{
				for(EducationInstitution edu : edus){
					if(edu.name!=null && edu.name.equalsIgnoreCase(FormHelper.getString(form().bindFromRequest(),"name"))){
						return badRequest(Constants.MSG_EDUCATION_EXIST);

					}
				}
				EducationInstitution education2 = EducationInstitution.addOrUpdate(form.get());
				if (education2 != null) {
					edus.add(education2);
					user.edus = edus;
					user.update();
					return ok(views.html.module.platform.addEducation.render(education2, user));
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
			@FormValidator(name = "realname", validateType = Type.REQUIRED, msg = "代理人姓名不能为空"),
			@FormValidator(name = "sex", validateType = Type.REQUIRED, msg = "性别不能为空"),
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "机构名称不能为空"),
			@FormValidator(name = "idcard", validateType = Type.REQUIRED, msg = "身份证号称不能为空"),
			@FormValidator(name = "idcard", validateType = Type.NUMBER, msg = "身份证号只能是数字"),
			@FormValidator(name = "birthday", validateType = Type.REQUIRED, msg = "出生日期不能为空"),
			@FormValidator(name = "phone", validateType = Type.PHONE, msg = "请填写正确的座机号码"),
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
					return ok(views.html.module.platform.addAgent.render(agent2,
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
					return ok(views.html.module.platform.addAgent.render(agent2,
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
			@FormValidator(name = "realname", validateType = Type.REQUIRED, msg = "真实姓名不能为空"),
			@FormValidator(name = "sex", validateType = Type.REQUIRED, msg = "性别不能为空"),
			@FormValidator(name = "idcard", validateType = Type.REQUIRED, msg = "身份证号不能为空"),
			@FormValidator(name = "idcard", validateType = Type.NUMBER, msg = "身份证号只能是数字"),
			@FormValidator(name = "birthday", validateType = Type.REQUIRED, msg = "出生日期不能为空"),
			@FormValidator(name = "phone", validateType = Type.PHONE, msg = "请填写正确的座机号码"),
			@FormValidator(name = "qq", validateType = Type.REQUIRED, msg = "QQ号码不能为空"),
			@FormValidator(name = "qq", validateType = Type.NUMBER, msg = "QQ号码只能是数字"),
			@FormValidator(name = "address", validateType = Type.REQUIRED, msg = "联系地址不能为空"),
			@FormValidator(name = "mobile", validateType = Type.PHONE, msg = "请填写正确的手机号码"),
			@FormValidator(name = "email", validateType = Type.EMAIL, msg = "请填写正确的邮箱地址"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "个人简介不能为空"),
			@FormValidator(name = "jobTitle", validateType = Type.REQUIRED, msg = "职称不能为空"),
			@FormValidator(name = "field", validateType = Type.REQUIRED, msg = "擅长领域不能为空")
	})
	public static Result addOrUpdateTeacher() {
		String msg = Validator.check(TeacherController.class, "addOrUpdateTeacher");
		if (msg != null) {
			return badRequest(msg);
		}

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		UserInfo basicInfo = user.basicInfo;
		if(basicInfo == null){
			basicInfo = new UserInfo();

			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));
			

			basicInfo.phone = form().bindFromRequest().get("phone");
			
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.save();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			
		}
		else{
			basicInfo.realname = form().bindFromRequest().get("realname");
			basicInfo.sex = form().bindFromRequest().get("sex");
			basicInfo.idcard = form().bindFromRequest().get("idcard");
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));


			basicInfo.phone = form().bindFromRequest().get("phone");
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.update();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");
			user.email = form().bindFromRequest().get("email");
			

		}
		Form<Instructor> form = form(Instructor.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Instructor instructor = user.instructor;
			if(instructor == null){
				Instructor instructor2 = Instructor.addOrUpdate(form.get());
				if (instructor2 != null) {
					user.instructor = instructor2;
					user.update();
					return ok(views.html.module.platform.addTeacher.render(instructor2, user));
				}
			}else{
				instructor.info = FormHelper.getString(form().bindFromRequest(),"info");
				instructor.jobTitle = FormHelper.getString(form().bindFromRequest(),"jobTitle");
				instructor.field = FormHelper.getString(form().bindFromRequest(),"field");

				Instructor instructor2 = Instructor.addOrUpdate(instructor);
				if (instructor2 != null) {
					user.instructor = instructor2;
					user.update();
					return ok(views.html.module.platform.addTeacher.render(instructor2, user));
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

}