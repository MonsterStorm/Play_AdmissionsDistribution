package common;

import static play.data.Form.form;

import java.util.*;

import play.data.*;
import play.data.validation.*;
import play.mvc.Http.Flash;

/**
 * helper for form
 * 
 * @author MonsterStorm
 * 
 */
public class FormHelper {
	
	/**
	 * get String
	 * @param form
	 * @param field
	 * @return
	 */
	public static String getString(DynamicForm form, String field){
		final String value = form.get(field);
		if(StringHelper.isValidate(value)){
			return value;
		}
		return null;
	}
	
	/**
	 * get long
	 * @param form
	 * @param field
	 * @return
	 */
	public static Long getLong(DynamicForm form, String field){
		final String value = form.get(field);
		if(StringHelper.isValidate(value)){
			return Long.valueOf(value);
		}
		return null;
	}
	
	/**
	 * get long
	 * @param form
	 * @param field
	 * @return
	 */
	public static Double getDouble(DynamicForm form, String field){
		final String value = form.get(field);
		if(StringHelper.isValidate(value)){
			return Double.valueOf(value);
		}
		return null;
	}
	
	/**
	 * get integer
	 * @param form
	 * @param field
	 * @return
	 */
	public static Integer getInt(DynamicForm form, String field){
		final String value = form.get(field);
		if(StringHelper.isValidate(value)){
			return Integer.valueOf(value);
		}
		return null;
	}

	/**
	 * get boolean
	 * @param form
	 * @param field
	 * @return
	 */
	public static Boolean getBool(DynamicForm form, String field){
		final String value = form.get(field);
		if(StringHelper.isValidate(value)){
			return Boolean.valueOf(value);
		}
		return null;
	}
	/**
	 * is add new action
	 * @param form
	 * @return
	 */
	public static boolean isAddNew(DynamicForm form){
		final String strAddNew = form.get("addNew");
		if(StringHelper.isValidate(strAddNew) && Boolean.parseBoolean(strAddNew) == true){
			return true;
		}
		return false;
	}
	
	/**
	 * get page
	 * 
	 * @param form
	 * @return
	 */
	public static int getPage(DynamicForm form) {
		String strPage = form.get("page");
		int page = 0;
		if (strPage != null) {
			page = Integer.parseInt(strPage);
		}
		return page;
	}

	/**
	 * reset flash from form
	 * @param form
	 * @param flash
	 */
	public static void resetFlash(DynamicForm form, Flash flash) {
		// put data back to flash
		if (form().bindFromRequest().data() != null) {
			flash.clear();// 先clear再添加新的
			flash.putAll(form().bindFromRequest().data());
		}
	}
	
	/**
	 * get an error msg
	 * @param form
	 * @return
	 */
	public static String getFirstError(Map<String, List<ValidationError>> errors){
		for (Iterator<String> iter = errors.keySet().iterator(); iter.hasNext();) {
			final String key = iter.next();
			final List<ValidationError> values = errors.get(key);
			if(values != null){
				for(ValidationError ve : values){
					if(StringHelper.isValidate(ve.message())){
						return ve.message();
					}
				}
			}
		}
		return null;
	}
	
	
	//-----------------------private methods----------------------------

}
