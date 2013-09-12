package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 返点类型
 * @author MonsterStorm
 *
 */
@Entity
@Table(name="rebate_type")
public class RebateType extends Model{

	@Id
	public Long id;

	public double ratioOfTotal;//总金额的返点，为0表示总金额不返点
	
	public double ratioOfPerStudent;//每个学生的返点，为0表示每个学生不返点
	
	@OneToOne
	public Rebate rebate;//一个返点类型对应一个返点，反之一个返点对应一个返点类型
}
