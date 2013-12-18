package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

import common.*;

/**
 * 问答，跟course绑定
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = QAndS.TABLE_NAME)
public class QAndS extends Model {
	private static final String TAG = QAndS.class.getSimpleName();
	public static final String TABLE_NAME = "qands";

	@Id
	public Long id;

	@ManyToOne
	public Course course;

	@Lob
	public String question;

	@Lob
	public String answer;

	public Long addTime;

	// -- 查询
	public static Model.Finder<Long, QAndS> finder = new Model.Finder(
			Long.class, QAndS.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<QAndS> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static QAndS find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find by course id
	 * 
	 * @param courseId
	 * @return
	 */
	public static List<QAndS> findByCourseId(Long courseId) {
		return finder.where().eq("course.id", courseId).findList();
	}

	/**
	 * delete an edu
	 * 
	 * @param form
	 * @return
	 */
	public static QAndS delete(Long id) {
		QAndS qands = find(id);
		if (qands != null) {
			qands.delete();
			return qands;
		}
		return null;
	}

	/**
	 * 新增或更新一个课程
	 * 
	 * @param form
	 * @return
	 */
	public static QAndS addOrUpdate(QAndS qands, Course course,
			String[] updateFiledNames) {
		if (qands != null) {
			if (qands.id == null) {// 新增
				qands.id = finder.nextId();
				qands.course = course;
				qands.addTime = System.currentTimeMillis();
				qands.save();
			} else {// 更新
				ModelHelper.update(Course.find(qands.id), qands,
						updateFiledNames);
			}
			return qands;
		}
		return null;
	}

}
