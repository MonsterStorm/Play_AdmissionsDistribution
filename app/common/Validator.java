package common;

import static play.data.Form.form;

import java.lang.reflect.*;

import play.mvc.*;

import common.FormValidator.Type;

public class Validator {

	/**
	 * check all
	 * @param clazz
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	public static String check(Class<?> clazz,
			String methodName, Class<?>... paramTypes) {
		try {
			Method method = clazz.getMethod(methodName, paramTypes);
			FormValidators validators = method
					.getAnnotation(FormValidators.class);

			if (validators != null && validators.values() != null
					&& validators.values().length > 0) {

				int len = validators.values().length;
				for (int i = 0; i < len; i++) {
					FormValidator configuration = validators.values()[i];

					String msg = check(configuration);
					if (msg != null) {
						return msg;
					}
				}
			} else {
				FormValidator validator = method.getAnnotation(FormValidator.class);
				if(validator != null){
					return check(validator);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * check single
	 * @param configuration
	 * @return
	 */
	private static String check(FormValidator configuration) {
		Type type = configuration.validateType();

		if (StringHelper.isValidate(configuration.name()) == false) {
			return Constants.MSG_BAD_REQUEST;
		}

		String value = FormHelper.getString(form().bindFromRequest(), configuration.name());

		switch (type) {
		case REQUIRED:
			if (StringHelper.isValidate(value) == false) {
				return configuration.msg();
			}
			break;
		case EQ: {
			break;
		}
		case LENGTH:
			if (StringHelper.isValidate(value) == false
					|| value.length() < configuration.minLen()
					|| value.length() > configuration.maxLen()) {
				return configuration.msg();
			}
			break;
		case NUMBER: {
			if (StringHelper.isValidate(value) == false
					|| value.matches(FormValidator.REGEX_NUMBER) == false) {
				return configuration.msg();
			}
			break;
		}
		case PHONE: {
			if (StringHelper.isValidate(value) == false
					|| value.matches(FormValidator.REGEX_PHONE) == false) {
				return configuration.msg();
			}
			break;
		}
		case EMAIL:
			if (StringHelper.isValidate(value) == false
					|| value.matches(FormValidator.REGEX_EMAIL) == false) {
				return configuration.msg();
			}
			break;
		case REGEX:
			if (StringHelper.isValidate(value) == false
					|| value.matches(configuration.regex()) == false) {
				return configuration.msg();
			}
			break;
		}

		return null;
	}
}
