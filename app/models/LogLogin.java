package models;

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
}
