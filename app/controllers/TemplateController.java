package controllers;

import static play.data.Form.form;

import java.lang.reflect.*;

import models.*;
import play.api.templates.*;
import play.mvc.*;

import com.avaje.ebean.*;
import common.*;

/**
 * 模板管理类
 * 
 * @author MonsterStorm
 */
public class TemplateController extends Controller {

	public static final String PAGE_USE_TEMPLATE = "useTemplate";// 使用模板页面

	public static Result index(String domainStr) {
		Domain domain = Domain.findByDomain(domainStr);
		if (domain != null && domain.agent != null) {
			try{
				//通过反射调用该类的render方法
				String clazzName = "views.html.template." + FileHelper.buildValidatePath(null, domain.agent.user.id, domain.agent.id) + "index";
				clazzName = clazzName.replaceAll("/", ".");
				final Class clazz = Class.forName(clazzName);
				if(clazz != null){//得到render方法
					Method method = clazz.getMethod("render", null);
					if(method != null){
						return ok((Html)method.invoke(null));
					}
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return Application.index();
	}

	public static Result page(String page) {
		if (PAGE_USE_TEMPLATE.equalsIgnoreCase(page)) {// 使用模板
			return pageUseTemplate();
		} else {
			return badRequest(Constants.MSG_BAD_REQUEST);
		}
	}

	/**
	 * 使用模板页面
	 * 
	 * @return
	 */
	public static Result pageUseTemplate() {
		// get page
		int page = FormHelper.getPage(form().bindFromRequest());

		Page<TemplateType> templateTypes = TemplateType.findPage(form()
				.bindFromRequest(), page, null);

		// reset flash
		FormHelper.resetFlash(form().bindFromRequest(), flash());

		return ok(views.html.module.common.useTemplate.render(templateTypes));
	}

	/**
	 * 使用模板
	 * 
	 * @return
	 */
	public static Result useTemplate() {
		Long templateTypeId = FormHelper
				.getLong(form().bindFromRequest(), "id");
		if (templateTypeId != null) {
			User user = LoginController.getSessionUser();

			if (user != null) {
				if (templateTypeId > 0) {

					Template template = Template.findByUser(user);// 每个用户都有一个默认模板，在创建的时候就产生
					TemplateType templateType = TemplateType
							.find(templateTypeId);
					if (template != null
							&& template.templateType != templateType) {// 如果用户没有应用该模板，则修改用户模板
						template.templateType = templateType;
						template.update();

						if (template.edu != null) {// 应用了系统的默认模板，则copy文件到指定文件夹下
							FileHelper.copyDefaultTemplateFiles(
									String.valueOf(user.id),
									String.valueOf(template.edu.id),
									String.valueOf(templateType.id));
						} else if (template.instructor != null) {
							FileHelper.copyDefaultTemplateFiles(
									String.valueOf(user.id),
									String.valueOf(template.instructor.id),
									String.valueOf(templateType.id));
						} else if (template.agent != null) {
							FileHelper.copyDefaultTemplateFiles(
									String.valueOf(user.id),
									String.valueOf(template.agent.id),
									String.valueOf(templateType.id));
						}
					}// 如果模板相同，则不做操作

				} else {// templateTypeId <= 0，则为设置自定义模板
					if (user.instructor != null) {// 讲师
						user.instructor.template.templateType = null;
						user.instructor.template.update();
					} else if (user.agent != null) {// 代理人
						user.agent.template.templateType = null;
						user.agent.template.update();
					} else {// 教育机构
						Long eduId = FormHelper.getLong(form()
								.bindFromRequest(), "eduId");
						if (eduId != null) {
							EducationInstitution edu = EducationInstitution
									.find(eduId);
							if (edu != null) {
								edu.template.templateType = null;
								edu.template.update();
							}
						}
					}
				}

				return ok(Constants.MSG_SUCCESS);
			} else {
				return badRequest(Constants.MSG_NOT_LOGIN);
			}
		}
		return badRequest(Constants.MSG_BAD_REQUEST);
	}

}