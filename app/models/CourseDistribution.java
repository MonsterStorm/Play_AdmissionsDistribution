package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;

import controllers.*;

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
		cd.save();
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

	/**
	 * find one by Enrollid
	 * 
	 * @param id
	 * @return
	 */
	public static CourseDistribution findByEnrollId(Long id) {
		Enroll enroll = Enroll.find( id );
		if( enroll == null || enroll.fromAgent == null ){
			return null;
		}
		return finder.where().eq("agent.id", enroll.fromAgent.id).eq("course.id", enroll.course.id).findUnique();
	}


	/**
	 * find user by agent
	 * 
	 * @param agent course
	 * @return
	 */
	public static CourseDistribution findByAgentAndCourse(Agent agent, Course course) {
		return finder.where().eq("agent.id", agent.id).eq("course.id", course.id).findUnique();
	}
	/**
	 * find user by agent
	 * 
	 * @param agent course
	 * @return
	 */
	public static CourseDistribution findByAgentAndCourse(Long agentId, Long courseId) {
		return finder.where().eq("agent.id", agentId).eq("course.id", courseId).findUnique();
	}


	public static CourseDistribution saveDistributon(Course course, Agent agent, Audit audit) {
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
			audit.save();
			cd.audit = audit;
		}

		Rebate rebate = Rebate.createRebate(cd);
		rebate.typeToPlatform = course.eduRebateType;
		rebate.typeToAgent = course.agentRebateType;
		rebate.lastReceiptOfEdu = new ConfirmReceipt();
		rebate.lastReceiptOfPlatform = new ConfirmReceipt();
		rebate.lastReceiptOfAgent = new ConfirmReceipt();
		rebate.lastReceiptOfEdu.save();
		rebate.lastReceiptOfPlatform.save();
		rebate.lastReceiptOfAgent.save();

		rebate.save();
		cd.rebate = rebate;

		course.update();
		agent.update();
		cd.save();
		return cd;
	}

	//通过课程代理审核

	public static CourseDistribution auditAgentDistributon( CourseDistribution cd ) {
		Course course = cd.course;
		Agent agent = cd.agent;
		Audit audit = cd.audit;
		audit.status = Audit.STATUS_SUCCESS;
		audit.auditor = cd.course.edu.creator;
		audit.update();
		
		course.agents.add(agent);
		course.update();

		cd.audit = audit;
		cd.agent = agent;
		cd.course = course;
		cd.update();
		return cd;
	}


	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<CourseDistribution> findPageByEdu(EducationInstitution edu, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("eduId", edu.id.toString());
		form = form.bind(datas);
		return new QueryHelper<CourseDistribution>(finder, form).addEq("course.edu.id", "eduId", Long.class).findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<CourseDistribution> findPageByAgent(Agent agent, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("agentId", agent.id.toString());
		form = form.bind(datas);
		return new QueryHelper<CourseDistribution>(finder, form).addEq("agent.id", "agentId", Long.class).findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<CourseDistribution> findPageByEduUser(User user, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("userId", user.id.toString());
		form = form.bind(datas);
		return new QueryHelper<CourseDistribution>(finder, form).addEq("course.edu.creator.id", "userId", Long.class).findPage(page, pageSize);
	}


	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static List<CourseDistribution> findByEdu(EducationInstitution edu, Long startTime, Long endTime) {
		return finder.where().eq("course.edu.id", edu.id).ge("rebate.lastReceiptOfEdu.time", startTime).le("rebate.lastReceiptOfEdu.time", endTime ).gt("rebate.rebateToPlatform", 0).findList();

		//return new QueryHelper<Enroll>(finder, form).addEq("edu.creator.id", "userId", Long.class).addGe("confirmOfEdu.time", "startTime", Long.class).addLe("confirmOfEdu.time", "endTime", Long.class).findPage(1, 100000);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static List<CourseDistribution> findByAgent(Agent agent, Long startTime, Long endTime) {
		return finder.where().eq("agent.id", agent.id).ge("rebate.lastReceiptOfPlatform.time", startTime).le("rebate.lastReceiptOfPlatform.time", endTime ).gt("rebate.rebateToAgent", 0).findList();

		//return new QueryHelper<Enroll>(finder, form).addEq("edu.creator.id", "userId", Long.class).addGe("confirmOfEdu.time", "startTime", Long.class).addLe("confirmOfEdu.time", "endTime", Long.class).findPage(1, 100000);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static List<CourseDistribution> findByPlatformIncome(Long startTime, Long endTime) {
		return finder.where().ge("rebate.lastReceiptOfEdu.time", startTime).le("rebate.lastReceiptOfEdu.time", endTime ).gt("rebate.rebateToPlatform", 0).findList();

		//return new QueryHelper<Enroll>(finder, form).addEq("edu.creator.id", "userId", Long.class).addGe("confirmOfEdu.time", "startTime", Long.class).addLe("confirmOfEdu.time", "endTime", Long.class).findPage(1, 100000);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static List<CourseDistribution> findByPlatformPay(Long startTime, Long endTime) {
		return finder.where().ge("rebate.lastReceiptOfPlatform.time", startTime).le("rebate.lastReceiptOfPlatform.time", endTime ).gt("rebate.rebateToAgent", 0).findList();

		//return new QueryHelper<Enroll>(finder, form).addEq("edu.creator.id", "userId", Long.class).addGe("confirmOfEdu.time", "startTime", Long.class).addLe("confirmOfEdu.time", "endTime", Long.class).findPage(1, 100000);
	}

}
