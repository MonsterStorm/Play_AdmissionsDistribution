package models;

import javax.persistence.*;

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

	@Id
	public Long id;
	
	public String name;// 列表名称
}
