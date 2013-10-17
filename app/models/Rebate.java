package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.*;
import common.*;
import controllers.*;

import play.data.*;
import play.db.ebean.*;

/**
 * 返点（每次教育机构点击确认收款，则更新，每次平台点确认收款，则更新）
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Rebate.TABLE_NAME)
public class Rebate extends Model {
	public static final String TABLE_NAME = "rebate";
	@Id
	public Long id;

	//@OneToOne
	//public Course course;// 一个课程有一个返点，一个返点对应一个课程
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public CourseDistribution distribution;//一个返利对应一个课程代理，一个课程代理对应一个返利

	public int numOfStudents;// 学员人数

	public double totalMoney;// 总共学费
	
	//--------教育机构确认收款
	public int numEduAdmit;//教育机构确认收款的人数
	
	public double moneyEduAdmit;//教育机构确认收款数

	@OneToOne(cascade=CascadeType.ALL)
	public ConfirmReceipt lastReceiptOfEdu;//教育机构最后确认收款信息
	
	@OneToOne(cascade = CascadeType.ALL)// 级联删除返点类型
	public RebateType typeToPlatform;// 给平台的返利类型，返点与返点类型一一对应

	public double rebateToPlatform;// 给平台的返利
	
	//--------平台确认收款
	public int numPlatformAdmit;//平台确认收款人数
	
	public double moneyPlatformAdmit;//平台确认收款数
	
	@OneToOne(cascade=CascadeType.ALL)
	public ConfirmReceipt lastReceiptOfPlatform;//教育机构最后确认收款信息

	@OneToOne(cascade = CascadeType.ALL)// 级联删除返点类型
	public RebateType typeToAgent;// 给代理的返利类型，返点与返点类型一一对应

	public double rebateToAgent;// 给代理的返利

	//--------代理人确认收款
	public int numAgentAdmit;//代理人确认收款人数
	
	public double moneyAgentAdmit;//代理人确认收款数
	
	@OneToOne(cascade=CascadeType.ALL)
	public ConfirmReceipt lastReceiptOfAgent;//代理人最后确认收款信息
	
	/**
	 * create a rebate
	 * @param distribution
	 * @return
	 */
	public static Rebate createRebate(CourseDistribution distribution){
		Rebate rebate = new Rebate();
		rebate.distribution = distribution;
		return rebate;
	}

	// -- 查询
	public static Model.Finder<Long, Rebate> finder = new Model.Finder(Long.class, Rebate.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Rebate> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Rebate find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * delete an edu
	 * @param form
	 * @return
	 */
	public static Rebate delete(Long id){
		Rebate rebate = find(id);
		if(rebate != null){
			rebate.delete();
			return rebate;
		}
		return null;
	}
	
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Rebate> findPage(DynamicForm form, int page, Integer pageSize) {
		Integer status;
		if(form == null){
			status = -1;
		} else {
			status = FormHelper.getInt(form, "status"); 
		}
		User user = LoginController.getSessionUser();
		
		play.Logger.debug("status: " + status + ",role:" + user.hasRole(Role.ROLE_EDU) + "," + user.hasRole(Role.ROLE_ADMIN) + "," + user.hasRole(Role.ROLE_AGENT));
		
		if (status != null && status == 1){//未收款
			if(user.hasRole(Role.ROLE_EDU)){//教育机构
				return new QueryHelper<Rebate>(finder, form).addEq("lastReceiptOfEdu", null, null).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_ADMIN)){//平台
				return new QueryHelper<Rebate>(finder, form).addEq("lastReceiptOfPlatform", null, null).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_AGENT)){//代理人
				return new QueryHelper<Rebate>(finder, form).addEq("lastReceiptOfAgent", null, null).findPage(page, pageSize);
			}
		} else if (status != null && status == 2){//已经收款
			if(user.hasRole(Role.ROLE_EDU)){//教育机构
				return new QueryHelper<Rebate>(finder, form).addNe("lastReceiptOfEdu", null, null).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_ADMIN)){//平台
				return new QueryHelper<Rebate>(finder, form).addNe("lastReceiptOfPlatform", null, null).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_AGENT)){//代理人
				return new QueryHelper<Rebate>(finder, form).addNe("lastReceiptOfAgent", null, null).findPage(page, pageSize);
			}
		} else {//all
			return new QueryHelper<Rebate>().findPage(finder, form, page, pageSize);
		}
		return null;
	}
	
	/**
	 * confirm receipt
	 * @param user
	 * @return
	 */
	public static Rebate updateConfirmReceipt(DynamicForm form, User user){
		Long rebateId = FormHelper.getLong(form, "id");
		Double money = FormHelper.getDouble(form, "money");
		String info = FormHelper.getString(form, "info");
		
		Rebate rebate = Rebate.find(rebateId);
		if(rebate != null && user != null){
			
			if(user.hasRole(Role.ROLE_EDU)){//教育机构
				if(rebate.lastReceiptOfEdu == null){
					rebate.lastReceiptOfEdu = new ConfirmReceipt(user, money, info);
				} else {
					rebate.lastReceiptOfEdu.time = System.currentTimeMillis();
					rebate.lastReceiptOfEdu.money = money;
					rebate.lastReceiptOfEdu.info = info;
				}
				rebate.update();
			} else if (user.hasRole(Role.ROLE_ADMIN)){//管理员 
				if(rebate.lastReceiptOfPlatform == null){
					rebate.lastReceiptOfPlatform = new ConfirmReceipt(user, money, info);
				} else {
					rebate.lastReceiptOfPlatform.time = System.currentTimeMillis();
					rebate.lastReceiptOfEdu.money = money;
					rebate.lastReceiptOfEdu.info = info;
				}
				rebate.update();
			} else if (user.hasRole(Role.ROLE_AGENT)){//代理人
				if(rebate.lastReceiptOfAgent == null){
					rebate.lastReceiptOfAgent = new ConfirmReceipt(user, money, info);
				} else {
					rebate.lastReceiptOfAgent.time = System.currentTimeMillis();
					rebate.lastReceiptOfEdu.money = money;
					rebate.lastReceiptOfEdu.info = info;
				}
				rebate.update();
			}
		}
		return rebate;
	}
}
