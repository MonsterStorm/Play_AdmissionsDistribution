package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.*;
import common.*;

import play.data.*;
import play.db.ebean.*;

@Entity
@Table(name = "log_operation")
public class LogOperation extends Model {

	@Id
	public Long id;

	@ManyToOne
	public User user;// 操作用户

	public Long time;// 操作时间

	@ManyToOne
	public Function function;// 操作内容，某个功能

	// -- 查询
	public static Model.Finder<Long, LogOperation> finder = new Model.Finder(
			Long.class, LogOperation.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<LogOperation> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static LogOperation find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = { @QueryFilter(dataName = "user.username", paramName = "username", queryType = QueryFilter.Type.LIKE, dataType = String.class),
			@QueryFilter(dataName="time", paramName="time", queryType=QueryFilter.Type.BETWEEN, dataType=Long.class) })
	public static Page<LogOperation> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<LogOperation> queryFilter = new QueryFilterHelper<LogOperation>(finder, form).filter(LogOperation.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<LogOperation>().findPage(finder, form, page, pageSize);
	}
}
