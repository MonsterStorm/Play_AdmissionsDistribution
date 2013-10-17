package common;

import play.data.*;
import play.db.ebean.*;

import com.avaje.ebean.*;
/**
 * 查询帮助类
 * @author MonsterStorm
 *
 * @param <T>
 */
/**
 * 查询帮助类
 * @author MonsterStorm
 *
 * @param <T>
 */
public class QueryHelper<T> {
	private static final String TAG = QueryHelper.class.getSimpleName();
	
	private Query<T> query;
	private Model.Finder<Long, T> finder;
	private DynamicForm form;
	
	public QueryHelper(){}
	
	public QueryHelper(Model.Finder<Long, T> finder, DynamicForm form){
		this.finder = finder;
		this.form = form;
	}
	
	/**
	 * 添加等于字段
	 * @param field
	 * @param paramStr
	 * @param clazz
	 * @return
	 */
	public QueryHelper<T> addEq(String field, String paramStr, Class<?> clazz){
		if(query == null){
			if(paramStr == null || clazz == null){
				query = finder.where().eq(field, null).query();
			} else {
				final String valueStr = FormHelper.getString(form, paramStr);
				if(StringHelper.isValidate(valueStr)){
					if(clazz == Integer.class){
						query = finder.where().eq(field, Integer.valueOf(valueStr)).query();
					} else if (clazz == Boolean.class){
						query = finder.where().eq(field, Boolean.valueOf(valueStr)).query();
					} else if (clazz == Float.class){
						query = finder.where().eq(field, Float.valueOf(valueStr)).query();
					} else if (clazz == Long.class){
						query = finder.where().eq(field, Long.valueOf(valueStr)).query();
					} else if (clazz == String.class){
						query = finder.where().eq(field, valueStr).query();
					}
				}
			}
		} else {
			if(paramStr == null || clazz == null){
				query = query.where().eq(field, null).query();
			} else {
				final String valueStr = FormHelper.getString(form, paramStr);
				if(StringHelper.isValidate(valueStr)){
					if(clazz == Integer.class){
						query = query.where().eq(field, Integer.valueOf(valueStr)).query();
					} else if (clazz == Boolean.class){
						query = query.where().eq(field, Boolean.valueOf(valueStr)).query();
					} else if (clazz == Float.class){
						query = query.where().eq(field, Float.valueOf(valueStr)).query();
					} else if (clazz == Long.class){
						query = query.where().eq(field, Long.valueOf(valueStr)).query();
					} else if (clazz == String.class){
						query = query.where().eq(field, valueStr).query();
					}
				}
			}
		}
		return this;
	}
	
	/**
	 * 添加等于字段
	 * @param field
	 * @param paramStr
	 * @param clazz
	 * @return
	 */
	public QueryHelper<T> addNe(String field, String paramStr, Class<?> clazz){
		if(query == null){
			if(paramStr == null || clazz == null){
				query = finder.where().ne(field, null).query();
			} else {
				final String valueStr = FormHelper.getString(form, paramStr);
				if(StringHelper.isValidate(valueStr)){
					if(clazz == Integer.class){
						query = finder.where().ne(field, Integer.valueOf(valueStr)).query();
					} else if (clazz == Boolean.class){
						query = finder.where().ne(field, Boolean.valueOf(valueStr)).query();
					} else if (clazz == Float.class){
						query = finder.where().ne(field, Float.valueOf(valueStr)).query();
					} else if (clazz == Long.class){
						query = finder.where().ne(field, Long.valueOf(valueStr)).query();
					} else if (clazz == String.class){
						query = finder.where().ne(field, valueStr).query();
					}
				}
			}
		} else {
			if(paramStr == null || clazz == null){
				query = query.where().ne(field, null).query();
			} else {
				final String valueStr = FormHelper.getString(form, paramStr);
				if(StringHelper.isValidate(valueStr)){
					if(clazz == Integer.class){
						query = query.where().ne(field, Integer.valueOf(valueStr)).query();
					} else if (clazz == Boolean.class){
						query = query.where().ne(field, Boolean.valueOf(valueStr)).query();
					} else if (clazz == Float.class){
						query = query.where().ne(field, Float.valueOf(valueStr)).query();
					} else if (clazz == Long.class){
						query = query.where().ne(field, Long.valueOf(valueStr)).query();
					} else if (clazz == String.class){
						query = query.where().ne(field, valueStr).query();
					}
				}
			}
		}
		return this;
	}

	
	/**
	 * add order by, xxx-asc, xxx-desc
	 * @param paramStr
	 * @return
	 */
	public QueryHelper<T> addOrderBy(String paramStr){
		final String orderby = FormHelper.getString(form, paramStr);
		if(StringHelper.isValidate(orderby)){
			String[] orders = orderby.split("-");
			boolean isDesc = false;
			if(orders.length > 1){
				isDesc = StringHelper.isDesc(orders[1]);
			}
			if(isDesc){
				if(query == null){
					query = finder.order().desc(orders[0]);
				} else {
					query = query.orderBy().desc(orders[0]);
				}
			} else {
				if(query == null){
					query = finder.order().asc(orders[0]);
				} else {
					query = query.orderBy().asc(orders[0]);
				}
			}
		}
		return this;
	}
	
	/**
	 * find page
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page<T> findPage(int page, Integer pageSize){
		play.Logger.debug(TAG + ".findPage: page=" + page + ", pageSize=" + pageSize + ", query=" + (query != null ? query.getRawSql() : ""));
		if(pageSize == null){
			pageSize = Constants.PAGE_SIZE;
		}
		
		if(query != null){
			return query.findPagingList(pageSize).getPage(page);
		} else {
			if(finder != null){
				return finder.findPagingList(pageSize).getPage(page);
			}
		}
		return null;
	}
	
	/**
	 * find page
	 * @param finder
	 * @param form
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page<T> findPage(Model.Finder<Long, T> finder, DynamicForm form, int page, Integer pageSize){
		play.Logger.debug(TAG + ".findPage: page=" + page + ", pageSize=" + pageSize);
		if(pageSize == null){
			pageSize = Constants.PAGE_SIZE;
		}
		
		Query<T> query = null;
		
		final String auditStatus = FormHelper.getString(form, "auditStatus");
		if(StringHelper.isValidate(auditStatus) && Integer.valueOf(auditStatus) >= 0){
			query = finder.where().eq("audit.status", Integer.valueOf(auditStatus)).query();
		}
		
		final String orderby = form.get("orderby");
		if(StringHelper.isValidate(orderby)){
			String[] orders = orderby.split("-");
			boolean isDesc = false;
			if(orders.length > 1){
				isDesc = StringHelper.isDesc(orders[1]);
			}
			if(isDesc){
				if(query == null){
					query = finder.order().desc(orders[0]);
				} else {
					query = query.orderBy().desc(orders[0]);
				}
			} else {
				if(query == null){
					query = finder.order().asc(orders[0]);
				} else {
					query = query.orderBy().asc(orders[0]);
				}
			}
		}
		
		if(query != null){
			return query.findPagingList(pageSize).getPage(page);
		} else {
			return finder.findPagingList(pageSize).getPage(page);//get all
		}
	}
	
	/**
	 * delete an entity
	 * @param finder
	 * @param id
	 * @return
	 */
	public static <T extends Model> T deleteEntity(Model.Finder<Long, T> finder, Long id){
		T t = finder.where().eq("id", id).findUnique();
		if(t != null){
			t.delete();
			return t;
		}
		return null;
  
	}
	
	/**
	 * 添加等于字段
	 * @param field
	 * @param paramStr
	 * @param clazz
	 * @return
	 */
	/*public QueryHelper<T> addEqual(String field, String paramStr, Class<?> clazz){
		if(query == null){
			final String valueStr =paramStr;
			if(StringHelper.isValidate(valueStr)){
				if(clazz == Integer.class){
					query = finder.where().eq(field, Integer.valueOf(valueStr)).query();
				} else if (clazz == Boolean.class){
					query = finder.where().eq(field, Boolean.valueOf(valueStr)).query();
				} else if (clazz == Float.class){
					query = finder.where().eq(field, Float.valueOf(valueStr)).query();
				} else if (clazz == Long.class){
					query = finder.where().eq(field, Long.valueOf(valueStr)).query();
				} else if (clazz == String.class){
					query = finder.where().eq(field, valueStr).query();
				}
			}
		} else {
			final String valueStr = FormHelper.getString(form, paramStr);
			if(StringHelper.isValidate(valueStr)){
				if(clazz == Integer.class){
					query = query.where().eq(field, Integer.valueOf(valueStr)).query();
				} else if (clazz == Boolean.class){
					query = query.where().eq(field, Boolean.valueOf(valueStr)).query();
				} else if (clazz == Float.class){
					query = query.where().eq(field, Float.valueOf(valueStr)).query();
				} else if (clazz == Long.class){
					query = query.where().eq(field, Long.valueOf(valueStr)).query();
				} else if (clazz == String.class){
					query = query.where().eq(field, valueStr).query();
				}
			}
		}
		return this;
	}*/
}
