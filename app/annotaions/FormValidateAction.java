package annotaions;

import play.mvc.*;
import annotaions.FormValidator.Type;

/**
 * form validation
 * @author MonsterStorm
 *
 */
public class FormValidateAction extends Action<FormValidator> {

	public Result call(Http.Context ctx) throws Throwable {
		Type type = configuration.validateType();
		switch (type){
		case REQUIRED:
			
			break;
		case LENGTH:
			break;
		
		case EMAIL:
			break;
			
		case REGEX:
			break;
		}
		return delegate.call(ctx);
	}
}
