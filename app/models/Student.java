package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;
import controllers.*;

/**
 * 学员，该类包含学员的基本信息，填写报名信息的时候为该表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Student.TABLE_NAME)
public class Student extends Model {
	public static final String TABLE_NAME = "student";
	
	@Id
	public Long id;

	@OneToOne
	public User user;

	// -- 报名所属
	@ManyToOne
	public User fromAgent;// 来源代理人，一个代理人可以有多个报名信息，一个报名信息只能隶属于一个代理人，如果是直接通过平台过来，则该字段为空

	@ManyToOne
	public EducationInstitution edu;// 所属教育机构，一个教育机构可以有多个报名信息，一个报名信息隶属于一个教育机构。

	// -- 报名确认信息
	@OneToOne
	public Audit auditOfAgent;// 代理人审核
	
	@OneToOne
	public Audit auditOfEdu;// 教育机构的审核信息

	// -- 收款确认信息
	@OneToOne
	public ConfirmReceipt confirmOfEdu;// 教育机构收款信息

	@OneToOne
	public ConfirmReceipt confirmOfPlatform;// 平台收款信息

	@OneToOne
	public ConfirmReceipt confirmOfAgent;// 代理人收款信息

	// --其他属性--
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

	// -- 查询
	public static Model.Finder<Long, Student> finder = new Model.Finder(Long.class, Student.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Student> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Student find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Student> findPage(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Student>().findPage(finder, form, page, pageSize);
	}
	
	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static Student addOrUpdate(Student student) {
		if (student != null) {
			if (student.id == null) {// 新增
				User user = LoginController.getSessionUser();//创建用户必须是当前用户
				student.user = user;//绑定到当前用户
				student.id = finder.nextId();
				student.save();
			} else {// 更新
				student.update();
			}
			return student;
		}
		return null;
	}
}
