package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

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
	public static Model.Finder<Long, QAndS> finder = new Model.Finder(Long.class, QAndS.class);

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
	 * @param courseId
	 * @return
	 */
	public static List<QAndS> findByCourseId(Long courseId){
		return finder.where().eq("course.id", courseId).findList();
	}

}
