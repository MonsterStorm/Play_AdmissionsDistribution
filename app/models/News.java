package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 新闻
 * @author MonsterStorm
 *
 */
@Entity
@Table(name = "news")
public class News extends Model {

	@Id
	public Long id;

	public String title; // 标题

	@Lob
	public String detail; // 详细

	@ManyToOne
	public NewsType type; // 新闻类别，一个新闻只有一个类别，一个类别对应多个新闻

	public Long time;// 时间
}
