package controllers.secure;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;
/**
 * 用于鉴权，判断某个用户是否有权限
 * @author MonsterStorm
 *
 */
public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
    
    // Access rights
    
}