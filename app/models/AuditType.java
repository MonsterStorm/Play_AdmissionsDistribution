package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 审核类型
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "audit_type")
public class AuditType extends Model {
	// 预定义审核类型
	public static final long TYPE_AUDITTYPE_USER = 1;
	public static final long TYPE_AUDITTYPE_EDU = 2;
	public static final long TYPE_AUDITTYPE_INSTRUCTOR = 3;
	public static final long TYPE_AUDITTYPE_AGENT = 4;
	public static final long TYPE_AUDITTYPE_STUDENT = 5;
	public static final long TYPE_AUDITTYPE_COURSE = 6;//课程审核
	public static final long TYPE_AUDITTYPE_DOMAIN = 7;//域名审核

	@Id
	public Long id;

	public String name;

	public String info;// 审核额外信息，可以介绍审核的信息

	// -- 查询
	public static Model.Finder<Long, AuditType> finder = new Model.Finder(
			Long.class, AuditType.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<AuditType> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static AuditType find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
}
