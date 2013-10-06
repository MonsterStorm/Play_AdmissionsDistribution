package models;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.format.*;
import play.data.format.Formatters.SimpleFormatter;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;

/**
 * 学员感言 管理员添加
 * 
 * @author khx
 * 
 */
@Entity
@Table(name = StudentWords.TABLE_NAME)
public class StudentWords extends Model {
	private static final String TAG = StudentWords.class.getSimpleName();
	public static final String TABLE_NAME = "studentwords";
	@Id
	public Long id;

	public String name;//名称

	public String company;// 公司

	public String words;// 感言


	// -- 查询
	public static Model.Finder<Long, StudentWords> finder = new Model.Finder(
			Long.class, StudentWords.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<StudentWords> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static StudentWords find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	/**
	 * 新增或更新一个留言
	 * 
	 * @param form
	 * @return
	 */
	public static StudentWords addOrUpdate(StudentWords sword) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + sword.id + ", name=" + sword.name );
		if (sword != null) {
			if (sword.id == null) {// 新增
				sword.id = finder.nextId();
				sword.save();
			} else {// 更新
				sword.update();
			}
			return sword;
		}
		return null;
	}
	/**
	 * 删除一个课程类型
	 * @param form
	 * @return
	 */
	public static StudentWords delete(Long id){
		StudentWords sword = find(id);
		if(sword != null){
			sword.delete();
			return sword;
		}
		return null;
	}
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<StudentWords> findPage(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<StudentWords>().findPage(finder, form, page, pageSize);
	}
}
