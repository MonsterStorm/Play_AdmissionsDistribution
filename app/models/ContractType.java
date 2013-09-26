package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 协议类型
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "contract_type")
public class ContractType extends Model {

	@Id
	public Long id;

	public String name;// 协议类型名称

	public String info;// 协议类型简介

	@OneToMany
	public List<Contract> contracts;

	// -- 查询
	public static Model.Finder<Long, ContractType> finder = new Model.Finder(Long.class, ContractType.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<ContractType> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static ContractType find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find a unique entity
	 * 
	 * @return
	 */
	public static ContractType findUnique() {
		return finder.findUnique();
	}
}
