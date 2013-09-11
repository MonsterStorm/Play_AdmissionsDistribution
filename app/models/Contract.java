package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

@Entity
@Table(name = "contract")
public class Contract extends Model {

	@Id
	public Long id;

	public String name;// 协议名称

	@Lob
	public String detail;// 协议详细信息

	@Lob
	public String info;// 协议说明

	public Long createTime;// 协议创建日期

	public Long lastModified;// 协议最近修改日期

	// -- 其他基本信息

	// -- 查询
	public static Model.Finder<String, Contract> finder = new Model.Finder(
			Long.class, Contract.class);

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
}
