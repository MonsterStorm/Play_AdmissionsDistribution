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

	public String name;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	public List<News> news;
}
