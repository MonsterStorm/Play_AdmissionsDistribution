package models;

import javax.persistence.*;

import play.db.ebean.*;

@Entity
@Table(name = "log_operation")
public class LogOperation extends Model {

	@Id
	public Long id;

	public User user;// 操作用户

	public Long time;// 操作时间

	public Function function;// 操作内容，某个功能
}
