package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

@Entity
@Table(name = "log_login")
public class LogLogin extends Model {

	@Id
	public Long id;

	@ManyToOne
	public User user;// 登录用户

	public Long time;// 登录时间

	public int type;// 0登录，1登出

	// -- 查询
	public static Model.Finder<Long, LogLogin> finder = new Model.Finder(
			Long.class, LogLogin.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<LogLogin> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static LogLogin find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
}
