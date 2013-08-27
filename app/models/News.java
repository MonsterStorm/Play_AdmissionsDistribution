package models;

import javax.persistence.*;

import play.db.ebean.*;

@Entity
@Table(name = "news")
public class News extends Model {

	@Id
	public Long id;

	public String title; // 标题

	@Lob
	public String detail; // 详细

	@ManyToOne
	public NewsType type; // 资讯类别

	public Long time;// 时间
}
