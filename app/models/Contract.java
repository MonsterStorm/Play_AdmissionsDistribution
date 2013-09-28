package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;

@Entity
@Table(name = Contract.TABLE_NAME)
public class Contract extends Model {
	public static final String TABLE_NAME = "contract";
	@Id
	public Long id;

	public String name;// 协议名称

	@Lob
	public String detail;// 协议详细信息

	@Lob
	public String info;// 协议说明
	
	@ManyToOne
	public ContractType contractType;//协议类型，一个协议只有一个类型，一个类型的协议有多个

	public Long createTime;// 协议创建日期

	public Long lastModified;// 协议最近修改日期

	// -- 其他基本信息

	
	static {
		FormFormatter.registerContractType();
	}
	
	// -- 查询
	public static Model.Finder<Long, Contract> finder = new Model.Finder(Long.class, Contract.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Contract> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Contract find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * find a unique entity
	 * @return
	 */
	public static Contract findUnique(){
		return finder.findUnique();
	}
	
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Contract> findPage(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Contract>(finder, form).addEq("contractType.id", "contractType", Long.class).addOrderBy("orderby").findPage(page, pageSize);
	}
	
	/**
	 * 删除一个新闻类型
	 * @param form
	 * @return
	 */
	public static Contract delete(Long id){
		return QueryHelper.deleteEntity(finder, id);
	}
	
	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static Contract addOrUpdate(Contract contract) {
		if (contract != null) {
			if (contract.id == null) {// 新增
				contract.id = finder.nextId();
				contract.createTime = System.currentTimeMillis();
				contract.lastModified = System.currentTimeMillis();
				contract.save();
			} else {// 更新
				contract.lastModified = System.currentTimeMillis();
				contract.update();
			}
			return contract;
		}
		return null;
	}
}
