package controllers;

import static play.data.Form.form;

import java.util.*;

import models.*;
import play.data.*;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;

import com.avaje.ebean.*;
import common.*;
import common.FileHelper.ErrorType;
import common.FormValidator.Type;

import controllers.secure.*;

/**
 * 通用controller，存放公用页面
 * 
 * @author MonsterStorm
 * 
 */
@Security.Authenticated(Secured.class)
public class CommonController extends Controller {
	private static final String PAGE_COURSE_DETAIL = "courseDetail";// 课程详情
	private static final String PAGE_EDU_DETAIL = "eduDetail";// 教育机构详情
	private static final String PAGE_MESSAGE_DETAIL = "messageDetail";// 留言详情
	private static final String PAGE_TEMPLATE_TYPE_DETAIL = "templateTypeDetail";// 模板类型详情
	private static final String PAGE_INSTRUCTOR_TYPE_DETAIL = "instructorDetail";// 讲师详情
	private static final String PAGE_USER_DETAIL = "userDetail";// 用户详情
	private static final String PAGE_STUDENT_DETAIL = "studentDetail";// 学员想起
	private static final String PAGE_NEWS_TYPE_DEATIL = "newsTypeDetail";// 新闻类型
	private static final String PAGE_NEWS_DEATIL = "newsDetail";// 新闻类型
	private static final String PAGE_AGENT_DETAIL = "agentDetail";// 代理人详情
	private static final String PAGE_CONTRACT_DETAIL = "contractDetail";// 协议详情
	private static final String PAGE_REBATE_DETAIL = "rebateDetail";// 返利详情
	private static final String PAGE_ADVERTISEMENT_DETAIL = "advertismentDetail";// 广告详情
	private static final String PAGE_COURSE_TYPE_DETAIL = "courseTypeDetail";// 课程类别详情
	private static final String PAGE_CHANGE_PASSWORD_WITHOUT_AUTH = "changePasswordWithoutAuth";// 修改密码
	private static final String PAGE_STUDENT_WORDS_DEATIL = "studentWordsDetail";// 学员感言

	/**
	 * common pages
	 * 
	 * @param page
	 * @return
	 */
	public static Result page(String page) {
		if (PAGE_COURSE_DETAIL.equalsIgnoreCase(page)) {// 课程
			return pageCourseDetail(null);
		} else if (PAGE_EDU_DETAIL.equalsIgnoreCase(page)) {// 教育机构
			return pageEduDetail(null);
		} else if (PAGE_STUDENT_DETAIL.equalsIgnoreCase(page)) {// 用户详情
			return pageStudentDetail(null);
		} else if (PAGE_MESSAGE_DETAIL.equalsIgnoreCase(page)) {// 留言
			return pageMessageDetail();
		} else if (PAGE_TEMPLATE_TYPE_DETAIL.equalsIgnoreCase(page)) {// 模板类型
			return pageTemplateTypeDetail(null);
		} else if (PAGE_INSTRUCTOR_TYPE_DETAIL.equalsIgnoreCase(page)) {// 讲师详情
			return pageInstructorDetail(null);
		} else if (PAGE_USER_DETAIL.equalsIgnoreCase(page)) {// 用户详情
			return pageUserDetail(null);
		} else if (PAGE_AGENT_DETAIL.equalsIgnoreCase(page)) {// 代理人详情
			return pageAgentDetail(null);
		} else if (PAGE_NEWS_TYPE_DEATIL.equalsIgnoreCase(page)) {// 新闻类型
			return pageNewsTypeDetail(null);
		} else if (PAGE_NEWS_DEATIL.equalsIgnoreCase(page)) {// 新闻类型
			return pageNewsDetail(null);
		} else if (PAGE_CONTRACT_DETAIL.equalsIgnoreCase(page)) {// 协议详情
			return pageContractDetail(null);
		} else if (PAGE_REBATE_DETAIL.equalsIgnoreCase(page)) {// 返利详情
			return pageRebateDetail(null);
		} else if (PAGE_ADVERTISEMENT_DETAIL.equalsIgnoreCase(page)) {// 广告详情
			return pageAdvertismentDetail(null);
		} else if (PAGE_COURSE_TYPE_DETAIL.equalsIgnoreCase(page)) {// 广告详情
			return pageCourseTypeDetail(null);
		} else if (PAGE_CHANGE_PASSWORD_WITHOUT_AUTH.equalsIgnoreCase(page)) {// 广告详情
			return pageChangePasswordWithoutAuth();
		} else if (PAGE_STUDENT_WORDS_DEATIL.equalsIgnoreCase(page)) {// 用户详情
			return pageStudentWordsDetail(null);
		} else {
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
		if (EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构新增或更新
			return addOrUpdateEdu();
		} else if (User.TABLE_NAME.equalsIgnoreCase(table)) {// 用户信息的新增或者更新
			return addOrUpdateUser();
		} else if (Course.TABLE_NAME.equalsIgnoreCase(table)) {// 课程信息的新增或者更新
			return addOrUpdateCourse();
		} else if (Instructor.TABLE_NAME.equalsIgnoreCase(table)) {// 讲师信息的新增或者更新
			return addOrUpdateInstructor();
		} else if (Student.TABLE_NAME.equalsIgnoreCase(table)) {// 学员信息的新增或者更新
			return addOrUpdateStudent();
		} else if (NewsType.TABLE_NAME.equalsIgnoreCase(table)) {// 新闻类型的新增或者更新
			return addOrUpdateNewsType();
		} else if (News.TABLE_NAME.equalsIgnoreCase(table)) {// 新闻类型的新增或者更新
			return addOrUpdateNews();
		} else if (Agent.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人信息的新增或者更新
			return addOrUpdateAgent();
		} else if (Contract.TABLE_NAME.equalsIgnoreCase(table)) {// 协议信息的新增或者更新
			return addOrUpdateContract();
		} else if (TemplateType.TABLE_NAME.equalsIgnoreCase(table)) {// 添加或删除模板类型
			return addOrUpdateTemplateType();
		} else if (Advertisment.TABLE_NAME.equalsIgnoreCase(table)) {// 添加或删除广告
			return addOrUpdateAdvertisment();
		} else if (CourseType.TABLE_NAME.equalsIgnoreCase(table)) {// 添加或删除课程类型
			return addOrUpdateCourseType();
		} else if (StudentWords.TABLE_NAME.equalsIgnoreCase(table)) {// 添加或删除学员感言
			return addOrUpdateStudentWords();
		} else if (Message.TABLE_NAME.equalsIgnoreCase(table)) {// 添加或删除留言
			return addOrUpdateMessage();
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	/**
	 * delete an entity
	 */
	public static Result deleteEntity() {
		String table = form().bindFromRequest().get("table");
		if (EducationInstitution.TABLE_NAME.equalsIgnoreCase(table)) {// 教育机构删除
			return deleteEdu();
		} else if (Course.TABLE_NAME.equalsIgnoreCase(table)) {// 课程信息删除
			return deleteCourse();
		} else if (Instructor.TABLE_NAME.equalsIgnoreCase(table)) {// 讲师信息删除
			return deleteInstructor();
		} else if (Student.TABLE_NAME.equalsIgnoreCase(table)) {// 学员信息删除
			return deleteStudent();
		} else if (NewsType.TABLE_NAME.equalsIgnoreCase(table)) {// 学员信息删除
			return deleteNewsType();
		} else if (News.TABLE_NAME.equalsIgnoreCase(table)) {// 学员信息删除
			return deleteNews();
		} else if (Contract.TABLE_NAME.equalsIgnoreCase(table)) {// 合同信息删除
			return deleteContract();
		} else if (Agent.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人信息删除
			return deleteAgent();
		} else if (CourseType.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人信息删除
			return deleteCourseType();
		} else if (Advertisment.TABLE_NAME.equalsIgnoreCase(table)) {// 代理人信息删除
			return deleteAdvertisment();
		} else if (StudentWords.TABLE_NAME.equalsIgnoreCase(table)) {// 学员感言删除
			return deleteStudentWords();
		} else if (Domain.TABLE_NAME.equalsIgnoreCase(table)) {// 学员感言删除
			return deleteDomain();
		} else if (TemplateType.TABLE_NAME.equalsIgnoreCase(table)) {// 模板类型删除
			return deleteTemplateType();
		} else {
			return badRequest(Constants.MSG_PAGE_NOT_FOUND);
		}
	}

	public static Result updateEntity() {
		String table = form().bindFromRequest().get("table");
		if (User.TABLE_NAME.equalsIgnoreCase(table)) {// 用户信息更新
			return updatePasswordWithoutAuth();
		}
		return badRequest(Constants.MSG_PAGE_NOT_FOUND);
	}

	/**
	 * add or update education institution
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "机构名称不能为空")
	})
	public static Result addOrUpdateEdu() {
		String msg = Validator.check(CommonController.class, "addOrUpdateEdu");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<EducationInstitution> form = form(EducationInstitution.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			EducationInstitution edu = EducationInstitution.addOrUpdate(form.get());
			if (edu != null) {
				return pageEduDetail(edu);
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
	 * add or update user
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "username", validateType = Type.REQUIRED, msg = "帐号不能为空"),
			@FormValidator(name = "mobile", validateType = Type.REQUIRED, msg = "移动电话不能为空"),
			@FormValidator(name = "mobile", validateType = Type.PHONE, msg = "请填写正确的电话号码"),
			@FormValidator(name = "email", validateType = Type.REQUIRED, msg = "邮箱地址不能为空"),
			@FormValidator(name = "email", validateType = Type.EMAIL, msg = "请填写正确的邮箱地址")
	})
	public static Result addOrUpdateUser() {
		String msg = Validator.check(CommonController.class, "addOrUpdateUser");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<User> form = form(User.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			final String username = FormHelper.getString(form().bindFromRequest(), "username");
			final String mobile = FormHelper.getString(form().bindFromRequest(), "mobile");
			final String email = FormHelper.getString(form().bindFromRequest(), "email");
			if (User.isUsernameRegistered(username)) {
				return badRequest(Constants.MSG_USER_USERNAME_EXIST);
			} else if (User.isMobileRegistered(mobile)){
				return badRequest(Constants.MSG_USER_USERNAME_EXIST);
			} else if (User.isEmailRegistered(email)){
				return badRequest(Constants.MSG_USER_USERNAME_EXIST);
			}
			
			MultipartFormData body = request().body().asMultipartFormData();
			if (body != null) {
				FilePart logo = body.getFile("logo");
				ErrorType errorType = FileHelper.checkValidate(logo);
				switch (errorType) {
				case ERROR_NONE: // 文件正常
				case ERROR_FILE_EMPTY: // 文件空，执行其他保存
					
					User user = User.addOrUpdate(form.get(), logo);
					if (user != null) {
						return pageUserDetail(user);
					}
				case ERROR_FILE_TOO_LARGE: // 文件太大
					return internalServerError(Constants.MSG_FILE_TOO_LARGE);
				case ERROR_FILE_TOO_SMALL: // 文件太小
					return internalServerError(Constants.MSG_FILE_TOO_SMALL);
				case ERROR_INVALIDATE_TYPE: // 文件类型不合法
					return internalServerError(Constants.MSG_FILE_INVALIDATE_TYPE);
				case ERROR_INVALIDATE_NAME: // 文件名不合法
					return internalServerError(Constants.MSG_FILE_INVALIDATE_NAME);
				case ERROR_INTERNAL:// 内部错误
					return internalServerError(Constants.MSG_FILE_INTERNAL);
				}
			} else {
				User user = User.addOrUpdate(form.get(), null);
				if (user != null) {
					return pageUserDetail(user);
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
	 * add or update course
	 */
	@FormValidators(values = {
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "课程名称不能为空"),
			@FormValidator(name = "courseType", validateType = Type.REQUIRED, msg = "课程类别不能为空"),
			@FormValidator(name = "money", validateType = Type.REQUIRED, msg = "学费不能为空"),
			@FormValidator(name = "money", validateType = Type.NUMBER, msg = "学费只能是数字"),
			@FormValidator(name = "startTime", validateType = Type.REQUIRED, msg = "开课时间不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "课程简介不能为空"),
			@FormValidator(name = "detail", validateType = Type.REQUIRED, msg = "课程详情不能为空")
	})
	public static Result addOrUpdateCourse() {
		String msg = Validator.check(CommonController.class, "addOrUpdateCourse");
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
	 * add or update instructor
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "user.nickname", validateType = Type.REQUIRED, msg = "讲师名称不能为空"),
			@FormValidator(name = "jobTitle", validateType = Type.REQUIRED, msg = "职称不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "简介不能为空")
	})
	public static Result addOrUpdateInstructor() {
		String msg = Validator.check(CommonController.class, "addOrUpdateInstructor");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<Instructor> form = form(Instructor.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Instructor instructor = Instructor.addOrUpdate(form.get());
			if (instructor != null) {
				return pageInstructorDetail(instructor);
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
			@FormValidator(name = "user.nickname", validateType = Type.REQUIRED, msg = "学员名称不能为空"),
			@FormValidator(name = "user.mobile", validateType = Type.REQUIRED, msg = "手机号码不能为空"),
			@FormValidator(name = "user.email", validateType = Type.REQUIRED, msg = "用户邮箱不能为空")
	})
	public static Result addOrUpdateStudent() {
		String msg = Validator.check(CommonController.class, "addOrUpdateStudent");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<Student> form = form(Student.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Student student = form.get();
			User user = student.user;
			if (user == null) {
				return badRequest("用户信息不全");
			} else {

				if (User.hasUserByMobile(user.mobile)) {// 手机号被占用
					return badRequest("手机号已被注册");
				}

				if (User.hasUserByEmail(user.email)) {// 邮箱被注册
					return badRequest("邮箱已被注册");
				}
			}

			student = Student.addOrUpdate(form.get());
			if (student != null) {
				return pageStudentDetail(student);
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
	 * add or update news
	 */
	@FormValidators(values = {
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "新闻类型不能为空")
	})
	public static Result addOrUpdateNewsType() {
		String msg = Validator
				.check(CommonController.class, "addOrUpdateNewsType");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<NewsType> form = form(NewsType.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			NewsType newsType = NewsType.addOrUpdate(form.get());
			if (newsType != null) {
				return pageNewsTypeDetail(newsType);
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
	 * add or update news
	 */
	@FormValidators(values = {
			@FormValidator(name = "title", validateType = Type.REQUIRED, msg = "新闻标题不能为空"),
			@FormValidator(name = "newsType", validateType = Type.REQUIRED, msg = "新闻类型不能为空"), 
			@FormValidator(name = "detail", validateType = Type.REQUIRED, msg = "新闻详情不能为空")
	})
	public static Result addOrUpdateNews() {
		String msg = Validator.check(CommonController.class, "addOrUpdateNews");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<News> form = form(News.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			News news = News.addOrUpdate(form.get());
			if (news != null) {
				return pageNewsDetail(news);
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
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@FormValidators(values = {
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "代理人名不能为空"),
			@FormValidator(name = "contact", validateType = Type.REQUIRED, msg = "联系方式不能为空") })
	public static Result addOrUpdateAgent() {
		String msg = Validator
				.check(CommonController.class, "addOrUpdateAgent");
		if (msg != null) {
			return badRequest(msg);
		}

		Form<Agent> form = form(Agent.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Agent agent = Agent.addOrUpdate(form.get());
			if (agent != null) {
				return pageAgentDetail(agent);
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
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "协议名称不能为空"),
			@FormValidator(name = "contractType", validateType = Type.REQUIRED, msg = "协议类型不能为空"),
			@FormValidator(name = "detail", validateType = Type.REQUIRED, msg = "协议详情不能为空") 
	})
	public static Result addOrUpdateContract() {
		String msg = Validator.check(CommonController.class, "addOrUpdateContract");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<Contract> form = form(Contract.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Contract contract = Contract.addOrUpdate(form.get());
			if (contract != null) {
				return pageContractDetail(contract);
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
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "模板名称不能为空"),
			@FormValidator(name = "info", validateType = Type.REQUIRED, msg = "模板简介不能为空")
	})
	public static Result addOrUpdateTemplateType() {
		String msg = Validator.check(CommonController.class, "addOrUpdateTemplateType");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<TemplateType> form = form(TemplateType.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			MultipartFormData body = request().body().asMultipartFormData();
			if (body != null) {
				FilePart logo = body.getFile("logo");
				ErrorType errorType = FileHelper.checkValidate(logo);
				switch (errorType) {
				case ERROR_NONE: // 文件正常
				case ERROR_FILE_EMPTY: // 文件空，执行其他保存
					TemplateType templateType = TemplateType.addOrUpdate(
							form.get(), logo);
					if (templateType != null) {
						return pageTemplateTypeDetail(templateType);
					}
					break;
				case ERROR_FILE_TOO_LARGE: // 文件太大
					return internalServerError(Constants.MSG_FILE_TOO_LARGE);
				case ERROR_FILE_TOO_SMALL: // 文件太小
					return internalServerError(Constants.MSG_FILE_TOO_SMALL);
				case ERROR_INVALIDATE_TYPE: // 文件类型不合法
					return internalServerError(Constants.MSG_FILE_INVALIDATE_TYPE);
				case ERROR_INVALIDATE_NAME: // 文件名不合法
					return internalServerError(Constants.MSG_FILE_INVALIDATE_NAME);
				case ERROR_INTERNAL:// 内部错误
					return internalServerError(Constants.MSG_FILE_INTERNAL);
				}
			} else {
				TemplateType templateType = TemplateType.addOrUpdate(
						form.get(), null);
				if (templateType != null) {
					return pageTemplateTypeDetail(templateType);
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
			@FormValidator(name = "title", validateType = Type.REQUIRED, msg = "广告标题不能为空"),
			@FormValidator(name = "url", validateType = Type.REQUIRED, msg = "链接地址不能为空")
	})
	public static Result addOrUpdateAdvertisment() {
		String msg = Validator.check(CommonController.class, "addOrUpdateAdvertisment");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<Advertisment> form = form(Advertisment.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			MultipartFormData body = request().body().asMultipartFormData();
			if (body != null) {
				FilePart logo = body.getFile("logo");
				ErrorType errorType = FileHelper.checkValidate(logo);
				switch (errorType) {
				case ERROR_NONE: // 文件正常
				case ERROR_FILE_EMPTY: // 文件空，执行其他保存
					Advertisment advertisment = Advertisment.addOrUpdate(
							form.get(), logo);
					if (advertisment != null) {
						return pageAdvertismentDetail(advertisment);
					}
					break;
				case ERROR_FILE_TOO_LARGE: // 文件太大
					return internalServerError(Constants.MSG_FILE_TOO_LARGE);
				case ERROR_FILE_TOO_SMALL: // 文件太小
					return internalServerError(Constants.MSG_FILE_TOO_SMALL);
				case ERROR_INVALIDATE_TYPE: // 文件类型不合法
					return internalServerError(Constants.MSG_FILE_INVALIDATE_TYPE);
				case ERROR_INVALIDATE_NAME: // 文件名不合法
					return internalServerError(Constants.MSG_FILE_INVALIDATE_NAME);
				case ERROR_INTERNAL:// 内部错误
					return internalServerError(Constants.MSG_FILE_INTERNAL);
				}
			} else {
				Advertisment advertisment = Advertisment.addOrUpdate(
						form.get(), null);
				if (advertisment != null) {
					return pageAdvertismentDetail(advertisment);
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
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "课程类型名不能为空")
	})
	public static Result addOrUpdateCourseType() {
		String msg = Validator.check(CommonController.class, "addOrUpdateCourseType");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<CourseType> form = form(CourseType.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			CourseType courseType = CourseType.addOrUpdate(form.get());
			if (courseType != null) {
				return pageCourseTypeDetail(courseType);
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
	 * add or update studentWords
	 * 
	 * @return
	 */
	@FormValidators(values = {
			@FormValidator(name = "name", validateType = Type.REQUIRED, msg = "学员姓名不能为空"),
			@FormValidator(name = "company", validateType = Type.REQUIRED, msg = "公司名称不能为空"), 
			@FormValidator(name = "words", validateType = Type.REQUIRED, msg = "学员感言不能为空")
	})
	public static Result addOrUpdateStudentWords() {
		String msg = Validator.check(CommonController.class, "addOrUpdateStudentWords");
		if (msg != null) {
			return badRequest(msg);
		}
		
		Form<StudentWords> form = form(StudentWords.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			StudentWords studentWords = StudentWords.addOrUpdate(form.get());
			if (studentWords != null) {
				return pageStudentWordsDetail(studentWords);
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
	 * add or update message
	 * 
	 * @return
	 */
	public static Result addOrUpdateMessage() {
		Form<Message> form = form(Message.class).bindFromRequest();
		if (form != null && form.hasErrors() == false) {
			Message message = Message.addOrUpdate(form.get());
			if (message != null) {
				return pageContactUs(message);
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
	 * delete edu
	 * 
	 * @return
	 */
	public static Result deleteEdu() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			EducationInstitution edu = EducationInstitution.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * 删除课程
	 * 
	 * @return
	 */
	public static Result deleteCourse() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Course course = Course.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete instructor
	 * 
	 * @return
	 */
	public static Result deleteInstructor() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Instructor instructor = Instructor.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete instructor
	 * 
	 * @return
	 */
	public static Result deleteStudent() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Student student = Student.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete instructor
	 * 
	 * @return
	 */
	public static Result deleteNewsType() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			NewsType newsType = NewsType.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete instructor
	 * 
	 * @return
	 */
	public static Result deleteNews() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			News news = News.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete instructor
	 * 
	 * @return
	 */
	public static Result deleteContract() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Contract contract = Contract.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete agent
	 * 
	 * @return
	 */
	public static Result deleteAgent() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Agent agent = Agent.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete courseType
	 * 
	 * @return
	 */
	public static Result deleteCourseType() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			CourseType courseTyoe = CourseType.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete Advertisment
	 * 
	 * @return
	 */
	public static Result deleteAdvertisment() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Advertisment ad = Advertisment.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * 课程详情
	 * 
	 * @return
	 */
	public static Result pageCourseDetail(Course course) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		List<CourseType> types = CourseType.findAll();
		if (isAddNew) {
			return ok(views.html.module.common.courseDetail.render(null, types));
		}

		if (course == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				course = Course.find(id);
			}
		}
		return ok(views.html.module.common.courseDetail.render(course, types));
	}

	/**
	 * 教育机构课程详情
	 * 
	 * @return
	 */
	public static Result pageEducationCourseDetail(Course course) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		List<CourseType> types = CourseType.findAll();
		if (isAddNew) {
			return ok(views.html.module.common.courseDetail.render(null, types));
		}

		if (course == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				course = Course.find(id);
			}
		}
		return ok(views.html.module.common.educationCourseDetail.render(course,
				types));
	}

	/**
	 * delete 学员感言
	 * 
	 * @return
	 */
	public static Result deleteStudentWords() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			StudentWords studentWords = StudentWords.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * delete 学员感言
	 * 
	 * @return
	 */
	public static Result deleteDomain() {
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			Domain domain = Domain.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}
	
	/**
	 * delete 模板类型
	 * 
	 * @return
	 */
	public static Result deleteTemplateType(){
		Long id = FormHelper.getLong(form().bindFromRequest(), "id");
		if (id != null) {
			TemplateType templateType = TemplateType.delete(id);
			return ok(Constants.MSG_SUCCESS);
		} else {
			return internalServerError(Constants.MSG_INTERNAL_ERROR);
		}
	}

	/**
	 * 教育机构详情
	 * 
	 * @return
	 */
	public static Result pageEduDetail(EducationInstitution edu) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.eduDetail.render(null));
		}

		if (edu == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				edu = EducationInstitution.find(id);
			}
		}
		return ok(views.html.module.common.eduDetail.render(edu));
	}

	/**
	 * 学员详情
	 * 
	 * @return
	 */
	public static Result pageStudentDetail(Student student) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.studentDetail.render(null));
		}

		if (student == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				student = Student.find(id);
			}
		}
		// form(Student.class).fill(student))
		return ok(views.html.module.common.studentDetail.render(student));
	}

	/**
	 * 留言详情
	 * 
	 * @return
	 */
	public static Result pageMessageDetail() {
		Long id = Long.valueOf(form().bindFromRequest().get("id"));
		Message message = Message.find(id);
		return ok(views.html.module.common.messageDetail.render(message));
	}

	/**
	 * 模板类型详情
	 * 
	 * @return
	 */
	public static Result pageTemplateTypeDetail(TemplateType templateType) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.templateTypeDetail.render(null));
		}

		if (templateType == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				templateType = TemplateType.find(id);
			}
		}
		return ok(views.html.module.common.templateTypeDetail
				.render(templateType));
	}

	/**
	 * 讲师详情
	 * 
	 * @return
	 */
	public static Result pageInstructorDetail(Instructor instructor) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.instructorDetail.render(null));
		}

		if (instructor == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				instructor = Instructor.find(id);
			}
		}
		return ok(views.html.module.common.instructorDetail.render(instructor));
	}

	/**
	 * 用户详情
	 * 
	 * @return
	 */
	public static Result pageUserDetail(User user) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.userDetail.render(null));
		}

		if (user == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				user = User.find(id);
			}
		}

		// List<Role> roles = Role.findAll();

		return ok(views.html.module.common.userDetail.render(user));
	}

	/**
	 * 新闻类型详情
	 * 
	 * @return
	 */
	public static Result pageNewsTypeDetail(NewsType newsType) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.newsTypeDetail.render(null));
		}

		if (newsType == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				newsType = NewsType.find(id);
			}
		}
		return ok(views.html.module.common.newsTypeDetail.render(newsType));
	}

	/**
	 * 新闻详情
	 * 
	 * @return
	 */
	public static Result pageNewsDetail(News news) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		List<NewsType> types = NewsType.findAll();
		if (isAddNew) {
			return ok(views.html.module.common.newsDetail.render(null, types));
		}

		if (news == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				news = News.find(id);
			}
		}
		return ok(views.html.module.common.newsDetail.render(news, types));
	}

	/**
	 * 用户详情
	 * 
	 * @return
	 */
	public static Result pageContractDetail(Contract contract) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());
		List<ContractType> types = ContractType.findAll();

		if (isAddNew) {
			return ok(views.html.module.common.contractDetail.render(null,
					types));
		}

		if (contract == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				contract = Contract.find(id);
			}
		}

		return ok(views.html.module.common.contractDetail.render(contract,
				types));
	}

	/**
	 * 用户详情
	 * 
	 * @return
	 */
	public static Result pageRebateDetail(Rebate rebate) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (rebate == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				rebate = Rebate.find(id);
			}
		}

		return ok(views.html.module.common.rebateDetail.render(rebate));
	}

	/**
	 * 用户详情
	 * 
	 * @return
	 */
	public static Result pageAdvertismentDetail(Advertisment advertisment) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (advertisment == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				advertisment = Advertisment.find(id);
			}
		}

		return ok(views.html.module.common.advertismentDetail
				.render(advertisment));
	}

	/**
	 * 代理人详情
	 * 
	 * @return
	 */
	public static Result pageAgentDetail(Agent agent) {
		if (agent == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				agent = Agent.find(id);
			}
		}
		return ok(views.html.module.common.agentDetail.render(agent));
	}

	/**
	 * 课程类型详情
	 * 
	 * @return
	 */
	public static Result pageCourseTypeDetail(CourseType courseType) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.courseTypeDetail.render(null));
		}

		if (courseType == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				courseType = CourseType.find(id);
			}
		}
		return ok(views.html.module.common.courseTypeDetail.render(courseType));
	}

	/**
	 * 学员感言详情
	 * 
	 * @return
	 */
	public static Result pageStudentWordsDetail(StudentWords studentWords) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			return ok(views.html.module.common.studentWordsDetail.render(null));
		}

		if (studentWords == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				studentWords = StudentWords.find(id);
			}
		}
		return ok(views.html.module.common.studentWordsDetail
				.render(studentWords));
	}

	/**
	 * 留言页面
	 * 
	 * @return
	 */
	public static Result pageContactUs(Message message) {
		boolean isAddNew = FormHelper.isAddNew(form().bindFromRequest());

		if (isAddNew) {
			Page<Message> msg = Message
					.findPage(form().bindFromRequest(), 1, 5);
			return ok(views.html.module.platform.contact_us.render(msg));
		}

		if (message == null) {
			Long id = FormHelper.getLong(form().bindFromRequest(), "id");
			if (id != null) {
				message = Message.find(id);
			}
		}
		Page<Message> msg = Message.findPage(form().bindFromRequest(), 1, 5);
		return ok(views.html.module.platform.contact_us.render(msg));
	}

	/**
	 * 打开修改密码页面
	 * 
	 * @return
	 */
	public static Result pageChangePasswordWithoutAuth() {
		User user = LoginController.getSessionUser();
		if (user != null) {
			return ok(views.html.module.common.changePasswordWithoutAuth
					.render(user));
		}
		return badRequest(Constants.MSG_NOT_LOGIN);
	}

	/**
	 * 修改用户密码，非认证方式
	 * 
	 * @param user
	 * @return
	 */
	public static Result updatePasswordWithoutAuth() {
		User user = LoginController.getSessionUser();
		if (user != null) {
			DynamicForm form = form().bindFromRequest();
			String oPassword = FormHelper.getString(form, "opassword");
			String nPassword = FormHelper.getString(form, "npassword");
			String rPassword = FormHelper.getString(form, "rpassword");

			if (StringHelper.isValidate(oPassword)
					&& User.buildPassword(oPassword).equals(user.password)) {// 老密码正确
				if (StringHelper.isValidate(nPassword)
						&& StringHelper.isValidate(rPassword)
						&& nPassword.equals(rPassword)) {// 新密码，重复密码一致
					user.password = User.buildPassword(nPassword);
					user.save();
					LoginController.updateSession(user);
					return ok(Constants.MSG_SUCCESS);// 成功
				} else {
					return badRequest(Constants.MSG_PASSWORD_NOT_SAME);// 两次密码不一致
				}
			} else {
				return badRequest(Constants.MSG_OLD_PASSWORD_ERROR);
			}
		}
		return badRequest(Constants.MSG_NOT_LOGIN);
	}
}
