package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.Page;
import common.QueryHelper;

import play.data.DynamicForm;
import play.db.ebean.*;

/**
 * 
 * 新课程类别，高校课程 政府项目等
 * 
 * @author khx
 * 
 */
@Entity
@Table(name = "course_class")
public class CourseClass extends Model {
	private static final String TAG = CourseClass.class.getSimpleName();
	public static final String TABLE_NAME = "course_class";
	@Id
	public Long id;

	public String name;// 列表名称

	// -- 查询
	public static Model.Finder<Long, CourseClass> finder = new Model.Finder(
			Long.class, CourseClass.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<CourseClass> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static CourseClass find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	/**
	 * 新增或更新一个课程类型
	 * 
	 * @param form
	 * @return
	 */
	public static CourseClass addOrUpdate(CourseClass courseClass) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + courseClass.id + ", name=" + courseClass.name );
		if (courseClass != null) {
			if (courseClass.id == null) {// 新增
				courseClass.id = finder.nextId();
				courseClass.save();
			} else {// 更新
				courseClass.update();
			}
			return courseClass;
		}
		return null;
	}
	/**
	 * 删除一个课程类型
	 * @param form
	 * @return
	 */
	public static CourseClass delete(Long id){
		CourseClass courseClass = find(id);
		if(courseClass != null){
			courseClass.delete();
			return courseClass;
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
	public static Page<CourseClass> findPage(DynamicForm form, int page,	Integer pageSize) {
		return new QueryHelper<CourseClass>().findPage(finder, form, page, pageSize);
	}
}
