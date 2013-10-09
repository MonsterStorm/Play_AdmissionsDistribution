package controllers.secure;

import models.*;
import play.mvc.Http.Context;
import play.mvc.*;

import common.*;

import controllers.*;

/**
 * 用于鉴权，判断某个用户是否有权限
 * 
 * @author MonsterStorm
 * 
 */
public abstract class SecuredRole extends Security.Authenticator {
	@Override
	public String getUsername(Context ctx) {
		User user = LoginController.getSessionUser();

		if (user == null) {// 当前用户不存在
			return null;
		}

		// 是否拥有权限
		boolean hasRights = user.hasRole(getRole());

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
	
	public abstract long getRole();
}