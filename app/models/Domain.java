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
	public User user;// 对应的分销商，该字段可以为空，表示该域名暂时无推销商

}
