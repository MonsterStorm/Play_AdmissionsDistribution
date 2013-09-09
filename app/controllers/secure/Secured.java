package controllers.secure;

import play.mvc.Http.Context;
import play.mvc.*;
/**
 * 用于鉴权，判断某个用户是否有权限
 * @author MonsterStorm
 *
 */
public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("account");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
    	final String path = ctx._requestHeader().path();//"/admin/index"
    	if(path.startsWith("/admin")){
			return redirect(controllers.routes.Application.admin());
    	} else {
    		return redirect(controllers.routes.Application.index());
    	}
    }
    
    // Access rights
    
    /**
     * is user has right to execute a function
     * @param function
     * @return
     */
    public static boolean hasRights(Long function){
    	return false;
    }
}