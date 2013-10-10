package common;

public class StringHelper {
	public static final String REGEX_PHONE = "(\\d{4}-|\\d{3}-)?(\\d{8}|\\d{7})";
	public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final String REGEX_NUMBER = "-?\\d+";

	public static boolean isValidate(String str) {
		return str != null && str.equals("") == false
				&& !str.equalsIgnoreCase("null");// null也标志为不合法
	}

	public static boolean isDesc(String str) {
		return str != null && str.equalsIgnoreCase("desc");
	}

	// ----------------------各种类型合法性判断-----------------------------
	public static boolean isValidateEmail(String str) {
		return isValidate(str) && str.matches(REGEX_EMAIL);
	}

	public static boolean isValidatePhone(String str) {
		return isValidate(str) && str.matches(REGEX_PHONE);
	}

	public static boolean isValidateNumber(String str) {
		return isValidate(str) && str.matches(REGEX_NUMBER);
	}
	
	
}
