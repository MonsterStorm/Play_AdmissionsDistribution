package common;

public class StringHelper {
	
	public static boolean isValidate(String str){
		return str != null && str.equals("") == false && !str.equalsIgnoreCase("null") ;//null也标志为不合法
	}
	
	public static boolean isDesc(String str){
		return str != null && str.equalsIgnoreCase("desc");
	}
}
