package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.Page;
import common.QueryHelper;

import play.data.DynamicForm;
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
	private static final String TAG = NewsType.class.getSimpleName();
	public static final String TABLE_NAME = "news_type";
	@Id
	public Long id;

	public String name;//类别名
	
	public String info;//额外信息
	
	@OneToMany(cascade=CascadeType.PERSIST)
	public List<News> news;//新闻类别对应的新闻，一个新闻类别对应多个新闻，一个新闻只有一个类别
	
	// -- 查询
	public static Model.Finder<Long, NewsType> finder = new Model.Finder(
			Long.class, NewsType.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<NewsType> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static NewsType find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	/**
	 * 新增或更新一个新闻类型
	 * 
	 * @param form
	 * @return
	 */
	public static NewsType addOrUpdate(NewsType newsType) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + newsType.id + ", name=" + newsType.name + ", info=" + newsType.info);
		if (newsType != null) {
			if (newsType.id == null) {// 新增
				newsType.id = finder.nextId();
				newsType.save();
			} else {// 更新
				newsType.update();
			}
			return newsType;
		}
		return null;
	}
	
	/**
	 * 删除一个新闻类型
	 * @param form
	 * @return
	 */
	public static NewsType delete(Long id){
		NewsType newsType = find(id);
		if(newsType != null){
			newsType.delete();
			return newsType;
		}
		return null;
	}
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<NewsType> findPage(DynamicForm form, int page,	Integer pageSize) {
		return new QueryHelper<NewsType>().findPage(finder, form, page, pageSize);
	}
}
