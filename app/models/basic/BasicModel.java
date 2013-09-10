/*package models.basic;
import java.lang.reflect.*;


*//**
 * 基础类，但是在Play下会有错
 * @author MonsterStorm
 *
 *//*
public class BasicModel {
	
	*//**
	 * 根据类的属性名称，获取属性值
	 * @param key
	 * @return
	 *//*
	public Object get(final String property) {
		try {
			Field field = BasicModel.class.getField(property);
			if(field != null){
				field.setAccessible(true);//设置可以访问，对于私有变量有用
				return field.get(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
*/