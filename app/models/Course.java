package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 课程类
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "course")
public class Course extends Model {

	@Id
	public Long id;

	public String name;// 课程名称

	public Double money;// 课程费用

	public Long startTime; // 开课时间

	public String contact;// 联系方式，包括联系人，电话等

	public CourseType type;// 课程类别

	public String info;// 课程简介

	public String detail;// 课程详细信息
}
