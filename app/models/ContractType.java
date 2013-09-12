package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 协议类型
 * @author MonsterStorm
 *
 */
@Entity
@Table(name="contract_type")
public class ContractType extends Model{

	@Id
	public Long id;
	
	public String name;//协议类型名称
	
	public String info;//协议类型简介
	
	@OneToMany
	public List<Contract> contracts;
}
