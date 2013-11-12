package common;

import java.lang.annotation.*;

/**
 * 自定义注解，用于查询的过滤
 * 
 * @author MonsterStorm
 * 
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryFilter {
	
	String dataName() default "";//需要过滤的数据字段名称
	
	String paramName() default "";//接受到的对应的参数名称，可以为空，为空就使用dataName代替
	
	Type queryType();
	
	Class<?> dataType() default Object.class;

	// 查询类型
	public static enum Type {
		EQ("=="), // 相等
		UNEQ("!="), // 不等
		GT(">>"), // 大于
		GET(">="), // 大于等于
		LT("<<"), // 小于
		LET("<="), // 小于等于
		LIKE("%%"),//Like
		ORDERBY("><");//排序

		public String value;

		Type(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	};

}
