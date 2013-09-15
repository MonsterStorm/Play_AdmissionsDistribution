package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 模块，一个模块拥有多个功能
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "module")
public class Module extends Model {

	@Id
	public Long id;

	public String name;

	@Lob
	public String info;

	@ManyToMany(mappedBy="modules")
	public List<Role> roles;// 一个模块可以被多个角色拥有，一个角色拥有多个模块

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Function> functions;// 一个模块有多个功能，一个功能可能被多个模块使用

	// -- 查询
	public static Model.Finder<Long, Module> finder = new Model.Finder(
			Long.class, Module.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Module> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Module find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
}
