package common;
/**
 * 常量参数
 * @author MonsterStorm
 *
 */
public class Constants {
	public static final String MSG_NOT_LOGIN = "尚未登录，请先登录";
	public static final String MSG_PAGE_NOT_FOUND = "页面不存在";
	public static final String MSG_BAD_REQUEST = "请求错误";
	public static final String MSG_INTERNAL_ERROR = "服务器内部错误";
	public static final String MSG_FORBIDDEN = "无权访问";
	public static final String MSG_SUCCESS = "成功";
	public static final String MSG_USER_USERNAME_EXIST = "该用户名已经被占用";
	public static final String MSG_USER_MOBILE_EXIST = "该移动电话号码已经被占用";
	public static final String MSG_USER_EMAIL_EXIST = "该邮箱地址已经被占用";
	public static final String MSG_USER_ENROLLED = "您已报名该课程";
	public static final String MSG_COURSE_NOT_EXIST = "该课程不存在";
	public static final String MSG_EDUCATION_EXIST = "该教育机构已存在";
	public static final String MSG_STUDENT_NOT_EXIST = "学生不存在";
	public static final String MSG_TEACHER_NOT_EXIST = "讲师不存在";
	public static final String MSG_EDUCATION_NOT_EXIST = "教育机构不存在";
	public static final String MSG_AGENT_NOT_EXIST = "代理人不存在";
	public static final String MSG_COURSE_NOT_SELECT = "未选择课程";
	public static final String MSG_COURSE_AGENTED = "已代理该课程";
	public static final String MSG_COURSE_REGED = "已有一个申请";
	public static final String MSG_DOMAIN_EXIST = "域名已存在";
	public static final String MSG_NOT_USER_EDUCATION = "教育机构与用户不匹配";
	public static final String MSG_NOT_IN_WAIT_AUDIT = "不在等待审核状态";

	public static final String MSG_ALREADY_TEACHER = "你已经是讲师，无需申请";
	public static final String MSG_ALREADY_AGENT = "你已经是代理人，无需申请";

	//申请
	public static final String MSG_AGENT_APPLYED_COURSE = "已有一个申请";

	
	//文件
	public static final String MSG_FILE_TOO_LARGE = "文件太大"; // 文件太大
	public static final String MSG_FILE_TOO_SMALL = "文件太小"; // 文件太小
	public static final String MSG_FILE_INVALIDATE_TYPE = "文件类型不合法"; // 文件类型不合法
	public static final String MSG_FILE_INVALIDATE_NAME = "文件名不合法"; // 文件名不合法
	public static final String MSG_FILE_EMPTY = "文件不能为空"; // 文件空
	public static final String MSG_FILE_INTERNAL = "内部错误";//内部错误
	
	//修改密码
	public static final String MSG_OLD_PASSWORD_ERROR = "原始密码错误";//老密码错误
	
	//分页大小
	public static final int PAGE_SIZE = 10;

	//模板类型对应角色
	public static final String TEMPLATE_TYPE_AGENT = "1";
	
	
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
