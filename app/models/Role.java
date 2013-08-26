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

	@ManyToMany
	public List<Function> functions;// 角色对应的功能列表，一个角色有多个功能
}
