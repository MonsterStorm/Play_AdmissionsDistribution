package controllers.secure;

import models.*;

/**
 * 用于鉴权，判断某个用户是否有权限
 * 
 * @author MonsterStorm
 * 
 */
public class SecuredInstructor extends SecuredRole {
	@Override
	public long getRole() {
		return Role.ROLE_INSTRUCTOR;
	}
}