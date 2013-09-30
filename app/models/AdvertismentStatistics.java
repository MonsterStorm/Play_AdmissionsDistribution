package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 广告访问统计
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = AdvertismentStatistics.TABLE_NAME)
public class AdvertismentStatistics extends Model {
	public static final String TABLE_NAME = "advertisment_statistics";

	@Id
	public Long id;

	@OneToOne
	public Advertisment advertisment;

	public Long totalVisit;// 总的访问量

	public Long startTime;// 统计开始时间（即广告启用的时间）

	public Long lastVisited;// 最近访问时间
	
	public AdvertismentStatistics(Advertisment advertisment){
		this.advertisment = advertisment;
		this.totalVisit = 0L;
		this.startTime = System.currentTimeMillis();
		this.lastVisited = null;
	}
}
