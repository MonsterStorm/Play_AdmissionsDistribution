package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 报名信息
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "enroll")
public class Enroll extends Model {

	@Id
	public Long id;

	// -- 报名所属
	@ManyToOne
	public User fromAgent;// 来源代理人，一个代理人可以有多个报名信息，一个报名信息只能隶属于一个代理人，如果是直接通过平台过来，则该字段为空

	@ManyToOne
	public EducationInstitution edu;// 所属教育机构，一个教育机构可以有多个报名信息，一个报名信息隶属于一个教育机构。

	// -- 报名确认信息
	public Audit auditOfAgent;// 代理人审核

	public Audit auditOfEdu;// 教育机构的审核信息

	// -- 收款确认信息
	public ConfirmReceipt confirmOfEdu;// 教育机构收款信息

	public ConfirmReceipt confirmOfPlatform;// 平台收款信息

	public ConfirmReceipt confirmOfAgent;// 代理人收款信息

	// -- 其他基本信息
	public Long enrollTime;// 报名时间

	public String enrollIp;// 登记时的ip

	public String enrollDomain;// 来源域名，如www.google.com，用于分销的统计

	public String name;// 报名人姓名

	public int sex;// 性别，0女，1男

	public String idcard;// 身份证号

	public Long birth;// 出生日期，存时间串

	public String companyName;// 公司名称

	public String position; // 职务

	public String phone; // 座机号码

	public String mobile;// 手机号码

	public String address;// 联系地址

	public String qq; // qq号码

	public String email;// 邮箱

	public String info;// 额外信息

}
