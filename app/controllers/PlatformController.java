package controllers;

import models.*;
import play.data.*;
import play.mvc.*;
import static play.data.Form.*;
import controllers.RegisterController.Register;
import controllers.LoginController.Login;
import com.avaje.ebean.*;
import common.*;
import java.io.*;
import java.util.*;

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
	private static final String PAGE_COURSE = "course";
	private static final String PAGE_COURSE_DETAIL = "courseDetail";
	private static final String PAGE_PLATFORM_ADV = "platformAdv";//图片广告
	private static final String PAGE_PLATFORM_ADV2= "platformAdv2";//侧栏连接广告
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
		} else if (PAGE_REGISTER_AND_LOGIN.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.register_and_login.render());
		} else if(PAGE_CONTACT_US.equalsIgnoreCase(page)){
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_REGISTER.equalsIgnoreCase(page)){
			//普通注册
			return ok(views.html.module.platform.register.render(form(Register.class)));
		} else if(PAGE_REGISTER_STUDENT.equalsIgnoreCase(page)){
			//学生注册
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_REGISTER_AGENT.equalsIgnoreCase(page)){
			//代理人注册
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_REGISTER_EDUCATION.equalsIgnoreCase(page)){
			//教育机构注册
			return ok(views.html.module.platform.contact_us.render());
		}else if(PAGE_LOGIN.equalsIgnoreCase(page)){
			//教育机构注册
			return ok(views.html.module.platform.login.render(form(Login.class)));
		}else if (PAGE_NEWS_DETAIL.equalsIgnoreCase(page)){
			return pageNewsDetail();
		}else if(PAGE_COURSE.equalsIgnoreCase(page)){
			return pagePlatformCourse();
		}else if(PAGE_COURSE_DETAIL.equalsIgnoreCase(page)){
			return pageCourseDetail();
		}else if(PAGE_PLATFORM_ADV.equalsIgnoreCase(page)){
			return pagePlatformAdv();
		}else if(PAGE_PLATFORM_ADV2.equalsIgnoreCase(page)){
			return pagePlatformAdv2();
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
				null);

		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.platform.platformAdv2.render(advertisment));
	}

}