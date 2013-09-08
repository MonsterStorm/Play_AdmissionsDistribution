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

	@ManyToMany
	public List<Role> roles;// 一个模块可以被多个角色拥有，一个角色拥有多个模块

	@ManyToMany
	public List<Function> functions;// 一个模块有多个功能，一个功能可能被多个模块使用
}
