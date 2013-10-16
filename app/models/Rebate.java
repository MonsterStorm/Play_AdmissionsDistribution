package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.*;
import common.*;

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
	@OneToOne(cascade=CascadeType.ALL)
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
		return new QueryHelper<Rebate>().findPage(finder, form, page, pageSize);
	}
}
