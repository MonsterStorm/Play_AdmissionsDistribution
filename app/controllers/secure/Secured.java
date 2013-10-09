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
public class Secured extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		User user = LoginController.getSessionUser();
		
		if(user == null){//当前用户不存在
			return null;
		}
		
		//是否拥有权限
		boolean hasRights = true;
		
		final String path = ctx._requestHeader().path();//请求路径
		if (path.startsWith("/admin")) {
			hasRights = user.hasRole(Role.ROLE_ADMIN);
		} else if (path.startsWith("/agent")) {
			hasRights = user.hasRole(Role.ROLE_AGENT);
		} else if (path.startsWith("/student")) {
			hasRights = user.hasRole(Role.ROLE_STUDENT);
		} else if (path.startsWith("/teacher")) {
			hasRights = user.hasRole(Role.ROLE_INSTRUCTOR);
		} else if (path.startsWith("/education")) {
			hasRights = user.hasRole(Role.ROLE_EDU);
		} else if (path.startsWith("/common")){//common
			
		}
		
		if(hasRights){//如果有权限，则返回true，否则
			return ctx.session().get(LoginController.KEY_USER_ACCOUNT);
		} else {
			return null;
		}
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		final String path = ctx._requestHeader().path();// "/admin/index"
		ctx.flash().put("error", Constants.MSG_FORBIDDEN);
		if (path.startsWith("/admin")) {
			return redirect(controllers.routes.Application.admin());
		} else if (path.startsWith("/agent")) {
			return redirect(controllers.routes.Application.agent());
		} else if (path.startsWith("/student")) {
			return redirect(controllers.routes.Application.student());
		} else if (path.startsWith("/teacher")) {
			return redirect(controllers.routes.Application.teacher());
		} else if (path.startsWith("/education")) {
			return redirect(controllers.routes.Application.education());
		} else {
			return redirect(controllers.routes.Application.index());
		}
	}

	// Access rights

	/**
	 * is user has right to execute a function
	 * 
	 * @param function
	 * @return
	 */
	public static boolean hasRights(Long function) {
		return false;
	}
}