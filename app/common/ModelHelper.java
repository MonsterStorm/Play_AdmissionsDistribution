package common;

import java.lang.reflect.*;
import java.security.*;
import java.util.*;

import play.db.ebean.*;

public class ModelHelper {

	/**
	 * is final
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isFinal(Field field) {
		int modifiers = field.getModifiers();
		return Modifier.isFinal(modifiers);
	}

	/**
	 * update oldModel with values in newModel that is not null
	 * 
	 * @param oldeModle
	 * @param newModel
	 */
	public static void update(Model oldModel, Model newModel, String[] names) {
		if (oldModel.getClass() != newModel.getClass()) {
			throw new InvalidParameterException("更新的对象不一致！");
		}

		if (oldModel == null) {
			newModel.save();
			return;
		}

		if (newModel == null) {
			return;
		}
		
		if((names == null || names.length < 1)){
			newModel.update();
			return;
		}

		List<String> nameList = Arrays.asList(names);
		try {
			Field[] fields = oldModel.getClass().getFields();
			for (int i = 0; i < fields.length; i++) {
				if (nameList.contains(fields[i].getName())) {// 如果在名称列表里面才操作
					if (isFinal(fields[i]) == false) {// 如果该字段不是final
						if (fields[i].isSynthetic() == false) {// 并且不是关联对象（这里只更新基本域，其他域不更新）
							Object value = fields[i].get(newModel);
							if (value == null) {// 为空的基本字段，用老的
								fields[i]
										.set(newModel, fields[i].get(oldModel));
							}
						} else {
							fields[i].set(newModel, fields[i].get(oldModel));
						}
					}
				} else {
					if (isFinal(fields[i]) == false) {
						fields[i].set(newModel, fields[i].get(oldModel));
					}
				}
			}
			newModel.update();
			play.Logger.debug("update successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
