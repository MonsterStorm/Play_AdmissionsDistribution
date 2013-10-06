package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 域名
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "domain")
public class Domain extends Model {

	@Id
	public Long id;

	public String domain;// 域名

	@ManyToOne
	public Agent agent;// 代理机构，一个域名拥有一个代理人（也可以没有），一个代理人可以有多个域名

	// -- 查询
	public static Model.Finder<Long, Domain> finder = new Model.Finder(
			Long.class, Domain.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Domain> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Domain find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * find one by domain
	 * @param domain
	 * @return
	 */
	public static Domain findByDomain(String domain){
		return finder.where().eq("domain", domain).findUnique();
	}
}
