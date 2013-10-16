package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;

import com.avaje.ebean.*;
import com.avaje.ebean.Query;
import common.*;

/**
 * 审核，包括账户审核，申请资格审核，发布，修改，删除等审核
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "audit")
public class Audit extends Model {
	public static final int STATUS_WAIT = 0;//等待审核
	public static final int STATUS_SUCCESS = 1;//审核通过
	public static final int STATUS_FAILED = 2;//审核失败
	public static final int STATUS_DISABLED = 3;//禁用
	public static final String TABLE_NAME = "audit";
	@Id
	public Long id;

	// 审核类型，账户注册，教育机构资格申请，讲师资格申请，分销商资格申请，个人模板创建、修改、删除等申请。
	@ManyToOne
	public AuditType type;// 一个审核对应一个类别，一个类别对应多个审核
	
	public int status;//审核的状态

	// -- 审核相关人员
	@ManyToOne
	public User creator;// 发起人，审核发起人，一个发起人对应多个审核，一个审核对应一个发起人

	//@ManyToOne
	//public Course course;// 审核对应的课程，审核发起人，一个课程对应多个审核，一个审核对应一个
	
	@OneToOne
	public CourseDistribution distributon;//审核对应的代理信息。一个代理信息对应一个审核。
	

	public Long createTime;// 发起时间

	@ManyToOne
	public User auditor;// 审核人，一个审核对应一个审核人，一个审核人对应多个审核

	public Long auditTime;// 审核时间
	
	
	public Audit(){
	}
	
	public Audit(User creator, int status, long auditType){
		this.status = status;
		this.creator = creator;
		this.createTime = System.currentTimeMillis();
		this.type = AuditType.find(auditType);
	}
	
	// -- 查询
	public static Model.Finder<Long, Audit> finder = new Model.Finder(
			Long.class, Audit.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Audit> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Audit find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Audit findByCourseIdAndAgentId(Long courseId,Long agentId,int status) {
		return finder.where().eq("creator.agent.id", agentId).eq("course.id",courseId).eq("status",status).findUnique();
	}



}
