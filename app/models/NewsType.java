package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 新闻类别，市场营销，人力资源，企业管理等
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "news_type")
public class NewsType extends Model {
	
	@Id
	public Long id;

	public String name;//类别名
	
	public String info;//额外信息
	
	@OneToMany(cascade=CascadeType.PERSIST)
	public List<News> news;//新闻类别对应的新闻，一个新闻类别对应多个新闻，一个新闻只有一个类别
}
