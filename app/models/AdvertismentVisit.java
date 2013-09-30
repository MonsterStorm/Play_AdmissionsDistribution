package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 广告点击统计
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = AdvertismentVisit.TABLE_NAME)
public class AdvertismentVisit extends Model {
	public static final String TABLE_NAME = "advertisment_visit";

	@Id
	public Long id;

	public String ip;// 来源ip

	public String domain;// 来源域名

	public String browser;// 浏览器名称
	
	public Long time;// 点击时间

	@ManyToOne
	public Advertisment advertisment;
}
