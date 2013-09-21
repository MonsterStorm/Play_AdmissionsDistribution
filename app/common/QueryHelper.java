package common;

import play.data.*;
import play.db.ebean.*;

import com.avaje.ebean.*;

public class QueryHelper<T> {
	
	/**
	 * find page
	 * @param finder
	 * @param form
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page<T> findPage(Model.Finder<Long, T> finder, DynamicForm form, int page, Integer pageSize){
		if(pageSize == null){
			pageSize = Constants.PAGE_SIZE;
		}
		
		Query<T> query = null;
		
		final String auditStatus = form.get("auditStatus");
		if(StringHelper.isValidate(auditStatus) && Integer.valueOf(auditStatus) >= 0){
			query = finder.where().eq("auditStatus", Integer.valueOf(auditStatus)).query();
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

		play.Logger.debug(auditStatus + "," + orderby);
		
		if(query != null){
			return query.findPagingList(pageSize).getPage(page);
		} else {
			return finder.findPagingList(pageSize).getPage(page);//get all
		}
	}
}
