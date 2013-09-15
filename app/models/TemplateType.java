package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 模板类型
 * 
 * @author MonsterStorm
 */
@Entity
@Table(name = "template_type")
public class TemplateType extends Model {

	@Id
	public Long id;

	public String name;// 模板名称

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
}
