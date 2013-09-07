package models;

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
	@Id
	public Long id;

	public String name;

	public String info;//审核额外信息，可以介绍审核的信息
}
