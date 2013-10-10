package annotaions;

import java.lang.annotation.*;

import play.mvc.*;

@With(FormValidateAction.class)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FormValidator {
	
	//参数名称
	String name();

	//值
	String value();
	
	Class<?> type();
	
	//类型
	Type validateType() default Type.REQUIRED;

	//最小值
	int min();
	
	//最大值
	int max();
	
	//最小位数
	int minLen();
	
	//最大位数
	int maxLen();
	
	//提示错误信息，以badRequest()方式返回
	String msg();
	
	//正则表达式
	String regex();
	
	//验证类型
	public static enum Type {
		REQUIRED, //必须字段，非空
		LENGTH, //长度
		REGEX, //正则匹配
		
		EQ, //相等
		UNEQ, //不等
		GT, //大于
		LT, //小于
		
		EMAIL, //邮箱
		PHONE, //电话
		NUMBER, //数字
		RANGE//范围
	};
}
