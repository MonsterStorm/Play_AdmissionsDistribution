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
public class SecuredAdmin extends SecuredRole {
	@Override
	public long getRole() {
		return Role.ROLE_ADMIN;
	}
}