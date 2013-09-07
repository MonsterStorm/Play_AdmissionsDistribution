package models;

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
}
