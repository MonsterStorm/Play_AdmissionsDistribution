package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.*;
import common.*;

import play.data.*;
import play.db.ebean.*;

@Entity
@Table(name = "log_login")
public class LogLogin extends Model {

	@Id
	public Long id;

	@ManyToOne
	public User user;// 登录用户

	public Long time;// 登录时间

	public int logType;// 0登录，1登出

	// -- 查询
	public static Model.Finder<Long, LogLogin> finder = new Model.Finder(Long.class, LogLogin.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<LogLogin> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static LogLogin find(Long id) {
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
			@QueryFilter(dataName="logType", paramName="logType", queryType=QueryFilter.Type.EQ, dataType=Integer.class)})
	public static Page<LogLogin> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<LogLogin> queryFilter = new QueryFilterHelper<LogLogin>(finder, form).filter(LogLogin.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<LogLogin>().findPage(finder, form, page, pageSize);
	}
}
