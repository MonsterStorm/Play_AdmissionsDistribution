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
	public static Page<Rebate> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<Rebate> queryFilter = new QueryFilterHelper<Rebate>(finder, form).filter(Rebate.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		
		Integer status;
		if(form == null){
			status = -1;
		} else {
			status = FormHelper.getInt(form, "status"); 
		}
		
		//排序
		String orderBy = FormHelper.getString(form, "time-asc");
		if(StringHelper.isValidate(orderBy) == false){
			orderBy = FormHelper.getString(form, "time-desc");
		}
		
		User user = LoginController.getSessionUser();
		
		play.Logger.debug("status: " + status + ", role:" + user.hasRole(Role.ROLE_EDU) + "," + user.hasRole(Role.ROLE_ADMIN) + "," + user.hasRole(Role.ROLE_AGENT));
		
		if (status != null && status == 1){//未收款
			if(user.hasRole(Role.ROLE_EDU)){//教育机构
				return queryFilter.addEq("lastReceiptOfEdu.confirmer", null, null).addOrderBy("lastReceiptOfEdu." + orderBy).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_ADMIN)){//平台
				return queryFilter.addEq("lastReceiptOfPlatform.confirmer", null, null).addOrderBy("lastReceiptOfPlatform." + orderBy).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_AGENT)){//代理人
				return queryFilter.addEq("lastReceiptOfAgent.confirmer", null, null).addOrderBy("lastReceiptOfAgent." + orderBy).findPage(page, pageSize);
			}
		} else if (status != null && status == 2){//已经收款
			if(user.hasRole(Role.ROLE_EDU)){//教育机构
				return queryFilter.addNe("lastReceiptOfEdu.confirmer", null, null).addOrderBy("lastReceiptOfEdu." + orderBy).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_ADMIN)){//平台
				return queryFilter.addNe("lastReceiptOfPlatform.confirmer", null, null).addOrderBy("lastReceiptOfPlatform." + orderBy).findPage(page, pageSize);
			} else if (user.hasRole(Role.ROLE_AGENT)){//代理人
				return queryFilter.addNe("lastReceiptOfAgent.confirmer", null, null).addOrderBy("lastReceiptOfAgent." + orderBy).findPage(page, pageSize);
			}
		} else {//all
			return queryFilter.findPage(page, pageSize);
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


	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Rebate> findPageByEdu(EducationInstitution edu, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("eduId", edu.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Rebate>(finder, form).addEq("distribution.course.edu.id", "eduId", Long.class).findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Rebate> findPageByAgent(Agent agent, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("agentId", agent.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Rebate>(finder, form).addEq("distribution.agent.id", "agentId", Long.class).findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Rebate> findPageByEduUser(User user, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("userId", user.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Rebate>(finder, form).addEq("distribution.course.edu.creator.id", "userId", Long.class).findPage(page, pageSize);
	}
}
