package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 校友
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "scholl_fellow")
public class SchoolFellow extends Model {

	@Id
	public Long id;

	public String name;// 校友名称

	public String company;// 校友公司

	public String position;// 校友职位

}
