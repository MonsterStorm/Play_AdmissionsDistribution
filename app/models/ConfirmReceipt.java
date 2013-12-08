package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 确认收款
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "confirm_receipt")
public class ConfirmReceipt extends Model {
	@Id
	public Long id;

	@ManyToOne
	public User confirmer;// 确认者，一个用户可以确认多个收款，一个收款只能对应到一个人

	public Long time;// 确认时间

	public Double money;// 收款金额（元）

	public String info;// 额外信息

	public ConfirmReceipt() {

	}

	public ConfirmReceipt(User user, Double money, String info) {
		confirmer = user;
		this.money = money;
		this.info = info;
		this.time = System.currentTimeMillis();
	}
}
