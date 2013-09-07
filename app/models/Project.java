package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 培训项目
 * v1.0: 这个版本教育机构创建的就只是课程，不存在培训项目
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "project")
@Deprecated
public class Project extends Model {

	@Id
	public Long id;

	public String name;// 项目名称

	public String info;// 项目描述

	public int status;// 状态，0，1，2，3，4（普通（填写了信息，但是为提交审核），审核中，审核未通过，审核通过（在架），下架（课程不再对外开放，再次上架需要再次提交审核））
}
