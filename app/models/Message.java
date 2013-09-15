package models;

import java.util.*;

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
	
	@Lob
	public String title;//标题
	
	@Lob
	public String content;//留言内容

	// -- 查询
	public static Model.Finder<Long, Message> finder = new Model.Finder(
			Long.class, Message.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Message> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Message find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
}
