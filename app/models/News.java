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
		Formatters.register(NewsType.class, new SimpleFormatter<NewsType>() {

			@Override
			public NewsType parse(String arg, Locale l) throws ParseException {
				try{
					Long newsTypeId = Long.parseLong(arg);
					NewsType newsType = NewsType.find(newsTypeId);
					return newsType;
				} catch (Exception e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String print(NewsType type, Locale l) {
				if(type != null){
					return type.name;
				}
				return null;
			}

		});
	}

	// -- 查询
	public static Model.Finder<Long, News> finder = new Model.Finder(
			Long.class, News.class);

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
	public static Page<News> findPage(DynamicForm form, int page,	Integer pageSize) {
		return new QueryHelper<News>().findPage(finder, form, page, pageSize);
	}
}
