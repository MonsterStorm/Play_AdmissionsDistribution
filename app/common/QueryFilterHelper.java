package common;

import static play.data.Form.form;

import java.lang.reflect.*;

import play.data.*;
import play.db.ebean.*;

import common.QueryFilter.Type;

public class QueryFilterHelper<T> {
	private Model.Finder<Long, T> finder;

	private DynamicForm form;

	public QueryFilterHelper(Model.Finder<Long, T> finder, DynamicForm form) {
		this.finder = finder;
		this.form = form;
	}

	public QueryHelper<T> filter(Class<?> clazz, String methodName,
			Class<?>... paramTypes) {
		QueryHelper<T> helper = new QueryHelper<T>(finder, form);
		try {
			Method method = clazz.getMethod(methodName, paramTypes);
			QueryFilters filters = method.getAnnotation(QueryFilters.class);

			if (filters != null) {

				int len = filters.values().length;
				for (int i = 0; i < len; i++) {
					QueryFilter configuration = filters.values()[i];
					helper = filter(helper, configuration);
				}
			} else {
				QueryFilter configuration = method
						.getAnnotation(QueryFilter.class);
				if (configuration != null) {
					helper = filter(helper, configuration);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return helper;
	}

	private QueryHelper<T> filter(QueryHelper<T> helper, QueryFilter filter) {
		String paramValue = FormHelper.getString(form().bindFromRequest(),
				filter.paramName());

		if (paramValue == null) {// 要过滤的数据不存在，即客户端为提交该过滤请求
			return helper;
		}

		String dataName = filter.dataName();
		String paramName = filter.paramName();
		Type type = filter.queryType();
		Class<?> clazz = filter.dataType();

		switch (type) {
		case EQ:
			helper.addEq(dataName, paramName, clazz);
			break;
		case UNEQ:
			helper.addNe(dataName, paramName, clazz);
			break;
		case GET:
			helper.addGe(dataName, paramName, clazz);
			break;
		case GT:
			helper.addGt(dataName, paramName, clazz);
			break;
		case LET:
			helper.addLe(dataName, paramName, clazz);
			break;
		case LT:
			helper.addLt(dataName, paramName, clazz);
			break;
		case LIKE:
			helper.addLike(dataName, paramName);
			break;
		case ORDERBY:
			helper.addOrderBy(paramName);
			break;
		}

		return helper;
	}
}
