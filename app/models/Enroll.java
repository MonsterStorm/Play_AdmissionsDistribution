package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 报名信息，
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "enroll")
public class Enroll extends Model {

	@Id
	public Long id;

	public Long enrollTime;// 报名时间

	public String enrollIp;// 登记时的ip

	public String enrollDomin;// 来源域名，如www.google.com，用于分销的统计

	public String name;// 报名人姓名

	public String sex;// 性别

	public String idCard;// 身份证号

	public String birth;// 出生日期

	public String companyName;// 公司名称

	public String position; // 职务

	public String phone; // 座机号码

	public String mobile;// 手机号码

	public String address;// 联系地址

	public String qq; // qq号码

	public String email;// 邮箱

	public String info;// 额外信息

}
