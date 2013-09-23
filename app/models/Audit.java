package models;

import javax.persistence.*;

import play.db.ebean.*;

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
	
	@Id
	public Long id;

	// 审核类型，账户注册，教育机构资格申请，讲师资格申请，分销商资格申请，个人模板创建、修改、删除等申请。
	@ManyToOne
	public AuditType type;// 一个审核对应一个类别，一个类别对应多个审核
	
	public int status;//审核的状态

	// -- 审核相关人员
	@ManyToOne
	public User creator;// 发起人，审核发起人，一个发起人对应多个审核，一个审核对应一个发起人

	public Long createTime;// 发起时间

	@ManyToOne
	public User auditor;// 审核人，一个审核对应一个审核人，一个审核人对应多个审核

	public Long auditTime;// 审核时间

}
