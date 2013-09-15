package models;

import javax.persistence.*;

import play.db.ebean.*;

@Entity
@Table(name = "reply")
public class Reply extends Model {

	@Id
	public Long id;

	@ManyToOne
	public User user;// 一个用户可以做出很多回复，一个回复只能被一个用户拥有

	public String title;// 标题

	@Lob
	public String content;// 回复内容

	public Long time;// 回复时间
	
	@ManyToOne
	public Message message;//所属的消息，一个留言可以有很多回复，一个回复只能属于一个留言
}
