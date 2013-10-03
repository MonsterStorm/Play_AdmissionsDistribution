package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.Page;
import common.QueryHelper;

import play.data.DynamicForm;
import play.db.ebean.*;

/**
 * 
 * 课程类别，总裁管理，工商管理，金融私募，房地产等
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "course_type")
public class CourseType extends Model {
	private static final String TAG = CourseType.class.getSimpleName();
	public static final String TABLE_NAME = "course_type";
	@Id
	public Long id;

	public String name;// 列表名称

	// -- 查询
	public static Model.Finder<Long, CourseType> finder = new Model.Finder(
			Long.class, CourseType.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<CourseType> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static CourseType find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	/**
	 * 新增或更新一个课程类型
	 * 
	 * @param form
	 * @return
	 */
	public static CourseType addOrUpdate(CourseType courseType) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + courseType.id + ", name=" + courseType.name );
		if (courseType != null) {
			if (courseType.id == null) {// 新增
				courseType.id = finder.nextId();
				courseType.save();
			} else {// 更新
				courseType.update();
			}
			return courseType;
		}
		return null;
	}
	/**
	 * 删除一个课程类型
	 * @param form
	 * @return
	 */
	public static CourseType delete(Long id){
		CourseType courseType = find(id);
		if(courseType != null){
			courseType.delete();
			return courseType;
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
	public static Page<CourseType> findPage(DynamicForm form, int page,	Integer pageSize) {
		return new QueryHelper<CourseType>().findPage(finder, form, page, pageSize);
	}
}
