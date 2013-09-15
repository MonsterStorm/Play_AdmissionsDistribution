package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 角色表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "role")
public class Role extends Model {

	@Id
	public Long id;

	public String name;// 角色名

	public String info;// 角色描述

	@ManyToMany(mappedBy = "roles")
	public List<User> users;// 角色对应的用户，一个角色可以被多个用户使用，一个用户可以有多个角色

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Module> modules;// 角色对应的功能列表，一个角色有多个功能，一个功能可以被多个角色使用

	// -- 查询
	public static Model.Finder<Long, Role> finder = new Model.Finder(
			Long.class, Role.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Role> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Role find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * 判断一个role是否有给定的function权限
	 * 
	 * @param function
	 * @param role
	 * @return
	 */
	public static boolean hasFunction(Long function, Long role) {
		return finder.where().eq("functions.id", function).eq("id", role)
				.findRowCount() > 0;
	}
}
