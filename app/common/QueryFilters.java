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
public @interface QueryFilters {
	QueryFilter[] values();
}
