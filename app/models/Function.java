package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 功能，一个函数，一个模块都是一个功能
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "function")
public class Function extends Model {

	@Id
	public Long id;

	public String name;// 功能名

	public String info;// 功能描述

	@ManyToMany
	public List<Module> modules;// 一个功能可以被多个模块拥有，一个模块可以拥有多个功能

}
