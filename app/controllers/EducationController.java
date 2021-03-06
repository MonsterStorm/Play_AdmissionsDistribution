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
import java.lang.*;

import play.data.*;
import play.data.validation.Constraints.ValidateWith;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import common.FileHelper.ErrorType;
import common.FormValidator.Type;


/**
 * contoller for education
 * 
 * @author khx
 * 
 */
@Security.Authenticated(Secured.class)
public class EducationController extends BaseController {

	private static final String PAGE_INDEX = "index";// 整个页面主体
	private static final String PAGE_HOME = "home";// 主页
	private static final String PAGE_LOGIN = "login";// 登录
	private static final String PAGE_EDUCATION_INFO = "educationInfo";
	private static final String PAGE_EDUCATION_COURSES = "educationCourses";
	private static final String PAGE_EDUCATION_COURSES_DETAIL ="courseDetail";
	private static final String PAGE_EDUCATION_INSTITUTION ="educationInstitution";
	private static final String PAGE_REG_EDUCATION  = "regEducation";
	private static final String PAGE_EDUCATION_DOMAIN  = "eduDomain";
	private static final String PAGE_DOMAIN_DETAIL  = "domainDetail";
	private static final String PAGE_ALL_COURSE_DISTRIBUTION = "allCourseDistribution";
	private static final String PAGE_EDU_ENROLL_INFO = "eduEnrollInfo";
	private static final String PAGE_EDU_RECEIPT_INFO = "eduReceiptInfo";
	private static final String PAGE_EDU_REBATE_INFOS = "eduRebateInfos";
	private static final String PAGE_EDU_REBATE_INFO = "eduRebateInfo";
	private static final String PAGE_EDU_STATISTICE = "eduStatistics";
	/**
	 * education pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_INDEX.equalsIgnoreCase(page)) {// 主页
			return ok(views.html.module.education.index.render());
		} else if (PAGE_HOME.equalsIgnoreCase(page)) {
			return ok(views.html.module.education.home.render());
		} else if (PAGE_LOGIN.equalsIgnoreCase(page)) {// 只做登录页面跳转
			return ok(views.html.module.education.login.render(form(Login.class)));
		}else if (PAGE_EDUCATION_INFO.equalsIgnoreCase(page)) {// 
			return pageEducationInfo(null);
		}else if (PAGE_EDUCATION_COURSES.equalsIgnoreCase(page)) {// 
			return pageEducationCourses();
		} else if (PAGE_EDUCATION_COURSES_DETAIL.equalsIgnoreCase(page)) {// 
			return pageCourseDetail(null);
		} else if (PAGE_EDUCATION_INSTITUTION.equalsIgnoreCase(page)) {// 
			return pageEducations();
		} else if (PAGE_REG_EDUCATION.equalsIgnoreCase(page)) {// 
			return pageRegEducations();
		}else if (PAGE_EDUCATION_DOMAIN.equalsIgnoreCase(page)) {// 
			return pageEducationDomain();
		} else if (PAGE_DOMAIN_DETAIL.equalsIgnoreCase(page)) {// 
			return pageDomainDetail(null);
		}  else if (PAGE_ALL_COURSE_DISTRIBUTION.equalsIgnoreCase(page)) {// 
			return pageAllCourseDistribution();
		} else if (PAGE_EDU_ENROLL_INFO.equalsIgnoreCase(page)) {// 
			return pageEduEnrollInfo();
		}else if (PAGE_EDU_RECEIPT_INFO.equalsIgnoreCase(page)) {// 
			return eduReceiptInfo();
		} else if (PAGE_EDU_REBATE_INFOS.equalsIgnoreCase(page)) {// 
			return pageRebateInfos();
		} else if (PAGE_EDU_REBATE_INFO.equalsIgnoreCase(page)) {// 
			return pageRebateInfo();
		}else if (PAGE_EDU_STATISTICE.equalsIgnoreCase(page)) {// 
			return eduStatistics();
		}  else {
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
		return redirect(controllers.routes.EducationController.page(PAGE_LOGIN));
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result doApply() {
		String table = form().bindFromRequest().get("table");
		if (table.equalsIgnoreCase("applyCourse")) {// 申请代理课程
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
			//return applyCourse();
		}else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * add or update entity
	 * 
	 * @return
	 */
	public static Result addOrUpdateEntity() {
		String table = form().bindFromRequest().get("table");
		if (EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构更新或添加
			return addOrUpdateEducation();
		}if (Course.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构更新或添加
			return addOrUpdateCourse();
		}if (Domain.TABLE_NAME.equalsIgnoreCase(table)) {// 域名更新或添加
			return addOrUpdateDomain();
		} if ("edu_receipt".equalsIgnoreCase(table)) {// 域名更新或添加
			return addOrUpdateEduReceipt();
		} if( "edu_rebate".equalsIgnoreCase(table) ) {//最终确认收款更新
			return addOrUpdateEduRebate();
		} if( "eduStatistics".equalsIgnoreCase(table)  ){ //收支统计
			return statisticsEdu();
		}
		 else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
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
			@FormValidator(name = "phone", validateType = Type.REQUIRED, msg = "座机号码不能为空"),
//			@FormValidator(name = "phone", validateType = Type.PHONE, msg = "请填写正确的座机号码"),//使用PHONE有问题，需要座机的regex，暂时先去掉,bycst
			@FormValidator(name = "mobile", validateType = Type.PHONE, msg = "请填写正确的手机号码"),
			@FormValidator(name = "qq", validateType = Type.REQUIRED, msg = "qq号码不能为空"),
			@FormValidator(name = "qq", validateType = Type.NUMBER, msg = "qq号码只能为数字"),
			@FormValidator(name = "email", validateType = Type.EMAIL, msg = "请填写正确的邮箱"),
			@FormValidator(name = "address", validateType = Type.REQUIRED, msg = "联系地址不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "机构简介不能为空"),
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "教育机构名称不能为空")
	})
	public static Result addOrUpdateEducation() {
		String msg = Validator.check(EducationController.class, "addOrUpdateEducation");
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

			basicInfo.realname = form().bindFromRequest().get("realname");//不可修改
			basicInfo.sex = form().bindFromRequest().get("sex");//不可修改
			basicInfo.idcard = form().bindFromRequest().get("idcard");//不可修改
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));//不可修改
			

			basicInfo.phone = form().bindFromRequest().get("phone");
			
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.save();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");//不可修改
			user.email = form().bindFromRequest().get("email");//不可修改
			
		}
		else{
			basicInfo.realname = form().bindFromRequest().get("realname");//不可修改
			basicInfo.sex = form().bindFromRequest().get("sex");//不可修改
			basicInfo.idcard = form().bindFromRequest().get("idcard");//不可修改
			basicInfo.birthday = Long.parseLong(form().bindFromRequest().get("birthday"));//不可修改


			basicInfo.phone = form().bindFromRequest().get("phone");//不可修改
			basicInfo.qq = form().bindFromRequest().get("qq");
			basicInfo.address = form().bindFromRequest().get("address");
			basicInfo.user = user;
			basicInfo.update();
			user.basicInfo = basicInfo;
			user.mobile = form().bindFromRequest().get("mobile");//不可修改
			user.email = form().bindFromRequest().get("email");//不可修改
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
					return ok(views.html.module.education.educationInfo.render(education2, user));
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
					return ok(views.html.module.education.educationInfo.render(education2, user));
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


	// /**
	//  * add or update instructor
	//  * 
	//  * @return
	//  */
	// public static Result addOrUpdateCourse() {
	// 	User user =  LoginController.getSessionUser();
	// 	if(user == null){
	// 		return badRequest(Constants.MSG_NOT_LOGIN);
	// 	}
		
	// 	Form<Course> form = form(Course.class).bindFromRequest();
	// 	if (form != null && form.hasErrors() == false) {
	// 		Course course = Course.addOrUpdate(form.get());
	// 		if (course != null) {
	// 			return pageCourseDetail(course);
	// 		}

	// 	} else if (form.hasErrors()) {
	// 		String error = FormHelper.getFirstError(form.errors());
	// 		play.Logger.debug("error:" + error);
	// 		if (error != null) {
	// 			return badRequest(error);
	// 		}
	// 	}
	// 	return internalServerError(Constants.MSG_INTERNAL_ERROR);
	// }

	/**
	 * add or update course
	 */
	/**
	 * add or update course
	 */
	@FormValidators(values = {
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "课程名称不能为空"),
			@FormValidator(name = "courseType", validateType = Type.REQUIRED, msg = "课程类别不能为空"),
			@FormValidator(name = "edu", validateType = Type.REQUIRED, msg = "所属教育机构不能为空"),
			@FormValidator(name = "money", validateType = Type.REQUIRED, msg = "学费不能为空"),
			@FormValidator(name = "money", validateType = Type.NUMBER, msg = "学费只能是数字"),
			@FormValidator(name = "startTime", validateType = Type.REQUIRED, msg = "开课时间不能为空"),
			@FormValidator(name = "contact", validateType = Type.REQUIRED, msg = "联系方式不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "课程简介不能为空"),
			@FormValidator(name = "detail", validateType = Type.REQUIRED, msg = "课程详情不能为空"),
			@FormValidator(name = "eduRebateType.ratioOfTotal", validateType = Type.NUMBER, msg = "总金额返点比例只能是数字"),
			@FormValidator(name = "eduRebateType.ratioOfPerStudent", validateType = Type.NUMBER, msg = " 每个学生返点金额只能是数字")
	})
	public static Result addOrUpdateCourse() {
		String msg = Validator.check(EducationController.class, "addOrUpdateCourse");
		if (msg != null) {
			return badRequest(msg);
		}
		Form<Course> form = form(Course.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Course course = Course.addOrUpdate(form.get());
			if (course != null) {
				return pageCourseDetail(course);
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
	 * 报名管理
	 * 
	 * @return
	 */
	public static Result pageEduEnrollInfo() {
		play.Logger.error(form().bindFromRequest().get("page"));
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<Enroll> enrolls = Enroll.findPageByEduId(form().bindFromRequest(), page,
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.education.eduEnrollInfo.render(enrolls));
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
			return ok(views.html.module.education.eduRebateInfo.render(rebate));
		}

		return badRequest(Constants.MSG_FORBIDDEN);
	}

	/**
	 * 教育机构课程
	 * 
	 * @return
	 */
	public static Result pageEducationCourses() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.edus == null){
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
		}
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		
		Page<CourseType> types = CourseType.findPage(form().bindFromRequest(), 0, Constants.MAX_DATA_SIZE);
		
		String[] courseTypes = null;
		if(types != null && types.getList() != null && types.getList().size() > 0){
			courseTypes = new String[types.getList().size()];
			for(int i = 0; i < types.getList().size(); i++){
				courseTypes[i] = types.getList().get(i).name;
			}
		}
		
		if(form().bindFromRequest().get("eduId") == null){
			Page<Course> courses  = Course.findPageByEducationUser(user,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.educationCourses.render(courses, courseTypes));
		}else{
			Long eduId = FormHelper.getLong(form().bindFromRequest(), "eduId");
			EducationInstitution edu = EducationInstitution.find(eduId);

			Page<Course> courses  = Course.findPageByEducation(edu,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.educationCourses.render(courses, courseTypes));
		}
	}


	/**
	 * 教育机构分账
	 * 
	 * @return
	 */
	public static Result pageRebateInfos() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.edus == null){
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
		}
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		if(form().bindFromRequest().get("eduId") == null){
			Page<Rebate> rebate  = Rebate.findPageByEduUser(user,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.eduRebateInfos.render(rebate));
		}else{
			Long eduId = FormHelper.getLong(form().bindFromRequest(), "eduId");
			EducationInstitution edu = EducationInstitution.find(eduId);

			Page<Rebate> rebate  = Rebate.findPageByEdu(edu,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.eduRebateInfos.render(rebate));
		}
	}


	/**
	 * 教育机构课程代理管理
	 * 
	 * @return
	 */
	public static Result pageAllCourseDistribution() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.edus == null){
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
		}
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());
		if(form().bindFromRequest().get("eduId") == null){
			Page<CourseDistribution> cd  = CourseDistribution.findPageByEduUser(user,form().bindFromRequest(),page,null);
			return ok(views.html.module.education.allCourseDistribution.render(cd));
		}else{
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
			// Long eduId = FormHelper.getLong(form().bindFromRequest(), "eduId");
			// EducationInstitution edu = EducationInstitution.find(eduId);

			// Page<Course> courses  = Course.findPageByEducation(edu,form().bindFromRequest(),page,null);
			// return ok(views.html.module.education.educationCourses.render(courses));
		}
	}

	/**
	 * 教育机构
	 * 
	 * @return
	 */
	public static Result pageEducations() {
		play.Logger.error(form().bindFromRequest().get("page"));

		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		int page = FormHelper.getPage(form().bindFromRequest());
		
		Page<EducationInstitution> edus  = EducationInstitution.findPageByUser(user,form().bindFromRequest(),page,null);
		return ok(views.html.module.education.educationInstitution.render(edus));
		
	}
	/**
	 * 教育机构
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
		Contract contract = Contract.find(id);//1表示 教育机构协议
		return ok(views.html.module.education.regEducation.render(contract));
		
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
		List<CourseClass> cClass = CourseClass.findAll();
		if (isAddNew) {
			return ok(views.html.module.education.courseDetail.render(null, types, user.edus, cClass));
		}

		if (course == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				course = Course.find(id);
			}
		}
		return ok(views.html.module.education.courseDetail.render(course, types,user.edus, cClass));
	}

	/**
	 * 教育机构详情
	 * 
	 * @return
	 */
	public static Result pageEducationInfo(EducationInstitution edu) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		
		if (isAddNew) {
			return ok(views.html.module.education.educationInfo.render(null, user));
		}

		if (edu == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				edu = EducationInstitution.find(id);
			}
		}
		return ok(views.html.module.education.educationInfo.render(edu, user));
	}

	/**
	 * 收款确认
	 * 
	 * @return
	 */
	public static Result eduReceiptInfo() {
		// get page
		User user = LoginController.getSessionUser();
		if(user!=null){
			
			Long enrollId =  FormHelper.getLong(form().bindFromRequest(),"enrollId");
			Enroll enroll = Enroll.find( enrollId );

			EducationInstitution edu = enroll.edu;

			if( edu == null || edu.creator.id != user.id ){
				return badRequest(Constants.MSG_FORBIDDEN);
			}

			// reset flash
			FormHelper.resetFlash(form().bindFromRequest(), flash());
			if( enroll != null){
				return ok(views.html.module.education.eduReceiptInfo.render(enroll));
			}else{
				return badRequest(Constants.MSG_ENROLL_NOT_EXIST);
			}
		}
		return badRequest(Constants.MSG_NOT_LOGIN);
	}

	/**
	 * 域名
	 * 
	 * @return
	 */
	public static Result pageEducationDomain() {
		play.Logger.error(form().bindFromRequest().get("page"));
		int page = FormHelper.getPage(form().bindFromRequest());
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Page<Domain> domain  = Domain.findPageByEducationUser(user,form().bindFromRequest(),page,null);
		return ok(views.html.module.education.eduDomain.render(domain));
		
	}


	/**
	 * 统计
	 * 输入参数为起止时间   还可以输入一些过滤数据
	 * @return
	 */
	public static Result eduStatistics() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		Long start = FormHelper.getLong(form().bindFromRequest(), "start");
		Long end = FormHelper.getLong(form().bindFromRequest(), "end");
		Statistics st = new Statistics();
		st.startTime = start;
		st.endTime = end;
		List<EducationInstitution> edus = user.edus;
		


		return ok(views.html.module.education.eduStatistics.render(st, edus));

		//return badRequest(start +"--" + end);
		// User user =  LoginController.getSessionUser();
		// if(user == null){
		// 	return badRequest(Constants.MSG_NOT_LOGIN);
		// }
		// Page<Domain> domain  = Domain.findPageByEducationUser(user,form().bindFromRequest(),page,null);
		// return ok(views.html.module.education.eduDomain.render(domain));
		
	}

	/**
	 * 域名详情
	 * 
	 * @return
	 */
	public static Result pageDomainDetail(Domain domain) {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if (domain == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				domain = Domain.find(id);
			}
		}
		return ok(views.html.module.education.domainDetail.render(domain,user.edus));
	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	 @FormValidators(values = {
			@FormValidator(name = "domain", validateType = Type.REQUIRED, msg = "域名不能为空"),
			@FormValidator(name = "edu", validateType = Type.REQUIRED, msg = "所属教育机构不能为空")
	})
	public static Result addOrUpdateDomain() {
		String msg = Validator.check(CommonController.class, "addOrUpdateDomain");
		if (msg != null) {
			return badRequest(msg);
		}
		User user = LoginController.getSessionUser();
		if (user == null) {
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		if(user.edus == null){
			return badRequest(Constants.MSG_EDUCATION_NOT_EXIST);
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
			Long eduId = FormHelper.getLong(form().bindFromRequest(), "edu");
			EducationInstitution edu = EducationInstitution.find(eduId);
			if(edu == null){
				return badRequest(form().bindFromRequest().get().toString());
			}
			if(edu.creator.id != user.id){
				return badRequest(Constants.MSG_NOT_USER_EDUCATION); 
			}
			newDomain.edu = edu;
			Domain dom = Domain.addOrUpdate(newDomain);
			if(dom!=null){
				return ok(views.html.module.education.domainDetail.render(dom,user.edus));
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
	public static Result addOrUpdateEduReceipt() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		
		Long enrollId =  FormHelper.getLong(form().bindFromRequest(),"enrollId");
		Enroll enroll = Enroll.find(enrollId);
		if( enroll == null ){
			return badRequest(Constants.MSG_ENROLL_NOT_EXIST);
		}

		EducationInstitution edu = enroll.edu;
		if( edu == null || edu.creator.id != user.id  ){
			return badRequest(Constants.MSG_FORBIDDEN);
		}

		if( enroll.confirmOfEdu.money!= null && enroll.confirmOfEdu.money > 0 ){
			return badRequest(Constants.MSG_RECEIPT_CONFIRMED);
		}
		enroll.confirmOfEdu.money = FormHelper.getDouble(form().bindFromRequest(),"money");
		enroll.confirmOfEdu.time = System.currentTimeMillis();
		enroll.confirmOfEdu.info =  FormHelper.getString(form().bindFromRequest(),"info");
		enroll.confirmOfEdu.confirmer =  user;
		enroll.confirmOfEdu.update();
		enroll.update();

		CourseDistribution cd = CourseDistribution.findByEnrollId( enroll.id );
		if( cd != null){
			
			cd.rebate.numEduAdmit ++;
			cd.rebate.moneyEduAdmit += enroll.confirmOfEdu.money;
			
			cd.rebate.update();
			cd.update();
		}

		return ok(views.html.module.education.eduReceiptInfo.render(enroll));

	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result addOrUpdateEduRebate() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		
		Long rebateId =  FormHelper.getLong(form().bindFromRequest(),"rebateId");
		Rebate rebate = Rebate.find(rebateId);
		if( rebate == null ){
			return badRequest(Constants.MSG_ENROLL_NOT_EXIST);
		}

		EducationInstitution edu = rebate.distribution.course.edu;
		if( edu == null || edu.creator.id != user.id  ){
			return badRequest(Constants.MSG_FORBIDDEN);
		}

		if( rebate.lastReceiptOfEdu.money!= null && rebate.lastReceiptOfEdu.money > 0 ){
			return badRequest(Constants.MSG_RECEIPT_CONFIRMED);
		}
		rebate.lastReceiptOfEdu.money = FormHelper.getDouble(form().bindFromRequest(),"lastReceiptOfEdu.money");
		rebate.lastReceiptOfEdu.time = System.currentTimeMillis();
		rebate.lastReceiptOfEdu.info =  FormHelper.getString(form().bindFromRequest(),"lastReceiptOfEdu.info");
		rebate.lastReceiptOfEdu.confirmer =  user;
		rebate.rebateToPlatform = ( rebate.lastReceiptOfEdu.money *  rebate.typeToPlatform.ratioOfTotal ) + ( rebate.numEduAdmit * rebate.typeToPlatform.ratioOfPerStudent );
		rebate.lastReceiptOfEdu.update();
		rebate.update();

		return ok(views.html.module.education.eduRebateInfo.render(rebate));

	}

	/**
	 * add or update instructor
	 * 
	 * @return
	 */
	public static Result statisticsEdu() {
		User user =  LoginController.getSessionUser();
		if(user == null){
			return badRequest(Constants.MSG_NOT_LOGIN);
		}
		//return badRequest(form().bindFromRequest()+"");
		Long eduId = FormHelper.getLong(form().bindFromRequest(), "edu");
		EducationInstitution edu = EducationInstitution.find(eduId);


		Statistics statis = Statistics.getEduStatistics(edu, form().bindFromRequest());
		List<EducationInstitution> edus = user.edus;

		return ok(views.html.module.education.eduStatistics.render(statis, edus));

	}

}
