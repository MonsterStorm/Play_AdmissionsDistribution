package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 课程 经销，一个代理人商代理一个课程就产生一个经销行为
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = CourseDistribution.TABLE_NAME)
public class CourseDistribution extends Model {
	public static final String TABLE_NAME = "course_distribution";

	@Id
	public Long id;

	@ManyToOne(targetEntity=Course.class, fetch=FetchType.EAGER)
	@JoinColumn(name="course_id")
	public Course course;// 一个课程有多个代理商经销，一个经销只对应一个课程

	@ManyToOne
	public Agent agent;// 经销商，一个经销商经销一个课程就有一个记录，一个经销商可以有多个经销记录

	@OneToOne
	public Audit audit;// 课程经销审核，一个代理人代理一个课程需要通过审核

	@OneToOne
	public Rebate rebate;// 经销对应的返利信息，一个经销有一个返利，一个返利对应一个经销

	// -- 查询
	public static Model.Finder<Long, CourseDistribution> finder = new Model.Finder(
			Long.class, CourseDistribution.class);

	public static void createDistributon(Course course, Agent agent, Audit audit) {
		CourseDistribution cd = new CourseDistribution();
		if (course != null) {
			course.distributions.add(cd);
			cd.course = course;
		}

		if (agent != null) {
			agent.distributons.add(cd);
			cd.agent = agent;
		}

		if (audit != null) {
			audit.distributon = cd;
			cd.audit = audit;
		}

		cd.rebate = Rebate.createRebate(cd);
	}

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<CourseDistribution> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static CourseDistribution find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
}
