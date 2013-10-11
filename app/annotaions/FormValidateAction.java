//package annotaions;
//
//import static play.data.Form.form;
//
//import common.*;
//
//import play.mvc.*;
//import annotaions.FormValidator.Type;
//
///**
// * form validation，失败返回badRequest
// * 
// * @author MonsterStorm
// * 
// */
//public class FormValidateAction extends Action<FormValidator> {
//	
//	 @Override
//     public Result call(Http.Context ctx) throws Throwable {
//		
//		play.Logger.debug("name:" + configuration.name());
//		
//		Type type = configuration.validateType();
//
//		play.Logger.debug("type:" + type + ", name:" + configuration.name());
//
//		if (StringHelper.isValidate(configuration.name()) == false) {
//			return badRequest(Constants.MSG_BAD_REQUEST);
//		}
//
//		String value = FormHelper.getString(form().bindFromRequest(),
//				configuration.name());
//
//		switch (type) {
//		case REQUIRED:
//			if (StringHelper.isValidate(value) == false) {
//				return badRequest(configuration.msg());
//			}
//			break;
//		case EQ: {
//			break;
//		}
//		case LENGTH:
//			if (StringHelper.isValidate(value) == false
//					|| value.length() < configuration.minLen()
//					|| value.length() > configuration.maxLen()) {
//				return badRequest(configuration.msg());
//			}
//			break;
//		case NUMBER: {
//			if (StringHelper.isValidate(value) == false
//					|| value.matches(FormValidator.REGEX_NUMBER) == false) {
//				return badRequest(configuration.msg());
//			}
//			break;
//		}
//		case PHONE: {
//			if (StringHelper.isValidate(value) == false
//					|| value.matches(FormValidator.REGEX_PHONE) == false) {
//				return badRequest(configuration.msg());
//			}
//			break;
//		}
//		case EMAIL:
//			if (StringHelper.isValidate(value) == false
//					|| value.matches(FormValidator.REGEX_EMAIL) == false) {
//				return badRequest(configuration.msg());
//			}
//			break;
//		case REGEX:
//			if (StringHelper.isValidate(value) == false
//					|| value.matches(configuration.regex()) == false) {
//				return badRequest(configuration.msg());
//			}
//			break;
//		}
//		return delegate.call(ctx);
//	}
//}
