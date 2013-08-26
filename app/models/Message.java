package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 留言
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "message")
public class Message extends Model {
	@Id
	public Long id;

	public String name;// 留言人

	public String phone;// 留言电话

	public String qq;// 留言qq

	public String email;// 留言email

	public String address;// 留言地址

	public Long time;// 留言时间
}
