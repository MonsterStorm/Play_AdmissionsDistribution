package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 代理人
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "agent")
public class Agent extends Model {

	@Id
	public Long id;

	@OneToOne
	public User user;// 代理人对应的用户账户，一个用户只能对应一个代理人，一个代理人对应一个用户

	@OneToMany
	public List<Domain> domain;// 代理人对应的域名信息，一个代理人对应多个域名，一个域名隶属于一个代理人（也可以没有代理人）
	
	// -- 基本信息
}
