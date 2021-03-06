package models;

import java.io.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;
import play.mvc.Http.MultipartFormData.FilePart;

import com.avaje.ebean.*;
import common.*;

/**
 * 模板类型
 * 
 * @author MonsterStorm
 */
@Entity
@Table(name = TemplateType.TABLE_NAME)
public class TemplateType extends Model {
	public static final long TYPE_DEFAULT = 1;// 默认模板

	public static final String TABLE_NAME = "template_type";
	@Id
	public Long id;

	public String name;// 模板名称

	// public int tType;// 模板类型 1 代理人 2教育机构 3讲师

	@Lob
	public String info;// 简介

	public Long lastModified;// 最近修改时间

	public String url;// 模板路径，该路径由系统管理员指定，一个用户申请后会复制该路径下的文件到自己的目录

	public String logo;// 模板缩略图

	// -- 查询
	public static Model.Finder<Long, TemplateType> finder = new Model.Finder(
			Long.class, TemplateType.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<TemplateType> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static TemplateType find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * delete an edu
	 * 
	 * @param form
	 * @return
	 */
	public static TemplateType delete(Long id) {
		TemplateType templateType = find(id);
		if (templateType != null) {
			templateType.delete();
			return templateType;
		}
		return null;
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = { @QueryFilter(dataName = "name", paramName = "templateName", queryType = QueryFilter.Type.LIKE, dataType = String.class),
			@QueryFilter(dataName="lastModified", paramName="lastModified", queryType=QueryFilter.Type.BETWEEN, dataType=Long.class) })
	public static Page<TemplateType> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<TemplateType> queryFilter = new QueryFilterHelper<TemplateType>(finder, form).filter(TemplateType.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<TemplateType>().findPage(finder, form, page,	pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<TemplateType> findAgentPage(DynamicForm form, int page, Integer pageSize) {
		// Map<String, String> datas = form.data();
		// datas.put("agentId", agent.id.toString());
		// form = form.bind(datas);
		return new QueryHelper<TemplateType>(finder, form)
				.addEq("type", Constants.TEMPLATE_TYPE_AGENT, Long.class)
				.addOrderBy("orderby").findPage(page, pageSize);

	}
	
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<TemplateType> findEduPage(DynamicForm form, int page, Integer pageSize) {
		// Map<String, String> datas = form.data();
		// datas.put("agentId", agent.id.toString());
		// form = form.bind(datas);
		return new QueryHelper<TemplateType>(finder, form)
				.addEq("type", Constants.TEMPLATE_TYPE_EDU, Long.class)
				.addOrderBy("orderby").findPage(page, pageSize);

	}

	/**
	 * add or update
	 * 
	 * @param form
	 * @param logo
	 * @return
	 */
	public static TemplateType addOrUpdate(TemplateType templateType,
			FilePart fileLogo) {
		if (templateType != null) {

			if (fileLogo != null) {
				String logo = FileHelper.saveDefaultTemplateTypeLogo(
						String.valueOf(templateType.id), fileLogo);
				templateType.logo = logo;
			} else {
				if (templateType.id != null) {// 更新，不更新logo
					TemplateType oldType = TemplateType.find(templateType.id);
					templateType.logo = oldType.logo;// 不更新logo
				}
			}

			if (templateType.id == null) {// 新增
				templateType.lastModified = System.currentTimeMillis();
				templateType.save();
				templateType.url = FileHelper.PATH_TEMPLATES_DEFAULT
						+ templateType.id + File.separator;
				templateType.update();// 更新一下链接
			} else {// 更新
				templateType.lastModified = System.currentTimeMillis();
				templateType.update();
			}
			return templateType;
		}
		return null;
	}
}
