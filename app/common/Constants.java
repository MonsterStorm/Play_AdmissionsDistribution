package common;
/**
 * 常量参数
 * @author MonsterStorm
 *
 */
public class Constants {
	public static final String MSG_PAGE_NOT_FOUND = "页面不存在";
	public static final String MSG_BAD_REQUEST = "请求错误";
	public static final String MSG_INTERNAL_ERROR = "服务器内部错误";
	public static final String MSG_FORBIDDEN = "无权访问";
	public static final String MSG_SUCCESS = "成功";
	public static final String MSG_USER_USERNAME_EXIST = "该用户名已经被占用";
	
	//分页大小
	public static final int PAGE_SIZE = 10;
	
	
	//----------------------------------表单错误提示-----------------------------
	public static final String MSG_FORM_COURSE_REQUIRED_NAME = "课程名不能为空";
	public static final String MSG_FORM_COURSE_REQUIRED_MONEY = "学费不能为空"; 

	//---------------------------------注册相关错误提示----------------------------------
	public static final String MSG_USERNAME_EXIST = "该用户名已经存在";
	public static final String MSG_EMAIL_EXIST = "该邮箱已被注册";
	public static final String MSG_PASSWORD_NOT_SAME = "两次输入的密码不一致";
	
	public static final int INT_CAN_REGISTER = 0;
	public static final int INT_USERNAME_EXIST = 1;
	public static final int INT_EMAIL_EXIST = 2;
	public static final int INT_PASSWORD_NOT_SAME = 3;
	
}
