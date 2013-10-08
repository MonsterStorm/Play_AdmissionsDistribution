package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 模板类，模板路径定义为在templates目录下添加用户名路径，所有用户相关的文件放到该目录下
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "template")
public class Template extends Model {

	@Id
	public Long id;

	@OneToOne
	public User user;// 模板到用户，用于快速找到模板对于的用户

	@OneToOne
	public EducationInstitution edu;// 一个用户拥有一个模板

	@OneToOne
	public Instructor instructor;// 讲师有一个模板

	@OneToOne
	public Agent agent;// 代理人有一个模板

	public String indexPath;// index首页的路径

	@ManyToOne
	public TemplateType templateType;// 模板类型，多个模板可以对应于一个类型，对于自定义模板（该字段为null，或者为type中定义的一种）

	// -- 查询
	public static Model.Finder<Long, Template> finder = new Model.Finder(
			Long.class, Template.class);

	public Template() {
	}

	public Template(User user, TemplateType templateType) {
		this.user = user;
		this.templateType = templateType;
		if (user.edus != null && user.edus.size() > 0) {
			this.edu = user.edus.get(0);//这里简化起见，先设置为第一个
		} else if (user.instructor != null) {
			this.instructor = user.instructor;
		} else if (user.agent != null) {
			this.agent = user.agent;
		}
	}

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Template> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Template find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find user's template
	 * 
	 * @param user
	 * @return
	 */
	public static Template findByUser(User user) {
		if (user != null) {
			return finder.where().eq("user", user).findUnique();
		}
		return null;
	}
}
