package common;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormValidator {

	// 参数名称
	String name();

	// 值
	String value() default "";

	Class<?> type() default Object.class;

	// 类型
	Type validateType() default Type.REQUIRED;

	// 最小值
	int min() default 0;

	// 最大值
	int max() default 0;

	// 最小位数
	int minLen() default 0;

	// 最大位数
	int maxLen() default 0;

	// 提示错误信息，以badRequest()方式返回
	String msg() default "参数错误";

	// 正则表达式
	String regex() default "";

	// 验证类型
	public static enum Type {
		REQUIRED, // 必须字段，非空
		LENGTH, // 长度
		REGEX, // 正则匹配

		EQ, // 相等
		UNEQ, // 不等
		GT, // 大于
		LT, // 小于

		EMAIL, // 邮箱
		PHONE, // 电话
		NUMBER, // 数字
		RANGE// 范围
	};

	public static final String REGEX_PHONE = "(\\d{4}-|\\d{3}-)?(\\d{8}|\\d{7})";
	public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final String REGEX_NUMBER = "-?\\d+";
}
