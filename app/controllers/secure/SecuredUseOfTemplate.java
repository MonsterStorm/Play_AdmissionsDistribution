package controllers.secure;

import common.*;

import models.*;
import play.mvc.Http.Context;
import play.mvc.*;
import controllers.*;

/**
 * 用于鉴权，判断某个用户是否有权限
 * 
 * @author MonsterStorm
 * 
 */
public class SecuredUseOfTemplate extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		User user = LoginController.getSessionUser();

		if (user == null) {// 当前用户不存在
			return null;
		}

		// 是否拥有权限
		boolean hasRights = true;

		hasRights = user.hasRole(Role.ROLE_AGENT)
				|| user.hasRole(Role.ROLE_EDU)
				|| user.hasRole(Role.ROLE_INSTRUCTOR);// 只有代理人，教育机构，讲师有使用模板权限

		if (hasRights) {// 如果有权限，则返回true，否则
			return ctx.session().get(LoginController.KEY_USER_ACCOUNT);
		} else {
			return null;
		}
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		ctx.flash().put("error", Constants.MSG_FORBIDDEN);
		return forbidden(views.html.basic.common.form_validate_info.render());
	}
}