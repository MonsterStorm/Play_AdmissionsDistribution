package models;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.format.*;
import play.data.format.Formatters.SimpleFormatter;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;

/**
 * 新闻
 * @author MonsterStorm
 *
 */
@Entity
@Table(name = "news")
public class News extends Model {
	private static final String TAG = News.class.getSimpleName();
	public static final String TABLE_NAME = "news";
	@Id
	public Long id;

	public String title; // 标题

	@Lob
	public String detail; // 详细

	@ManyToOne
	public NewsType newsType; // 新闻类别，一个新闻只有一个类别，一个类别对应多个新闻

	public Long time;// 时间

	static {
		FormFormatter.registerNewsType();
	}

	// -- 查询
	public static Model.Finder<Long, News> finder = new Model.Finder(Long.class, News.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<News> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static News find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	/**
	 * 新增或更新一个新闻
	 * 
	 * @param form
	 * @return
	 */
	public static News addOrUpdate(News news) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + news.id + ", title=" + news.title + ", detail=" + news.detail);
		if (news != null) {
			news.time = System.currentTimeMillis();
			if (news.id == null) {// 新增
				news.id = finder.nextId();
				news.save();
			} else {// 更新
				news.update();
			}
			return news;
		}
		return null;
	}
	
	/**
	 * 删除一个新闻类型
	 * @param form
	 * @return
	 */
	public static News delete(Long id){
		News news = find(id);
		if(news != null){
			news.delete();
			return news;
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
	@QueryFilters(values = {
			@QueryFilter(dataName="title", paramName="newsTitle", queryType=QueryFilter.Type.LIKE, dataType=String.class),
			@QueryFilter(dataName="newsType.name", paramName="newsTypeName", queryType=QueryFilter.Type.EQ, dataType=String.class)
	})
	public static Page<News> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<News> queryFilter = new QueryFilterHelper<News>(finder, form).filter(News.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<News>().findPage(finder, form, page, pageSize);
	}
}
