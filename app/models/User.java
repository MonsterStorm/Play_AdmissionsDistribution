package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 用户信息表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "user")
public class User extends Model {

	@Id
	public Long id;

	public String username;// 账户名，不对外公开

	public String password;// 密码，加密保存

	public String nickname;// 昵称，对于培训机构可以是xxx培训几个，对于老师可以是xxx老师，该名称公开给外界。

	// -------------用于普通用户--------------
	public String idcard;// 身份证

	public Long birthday;// 出生日期

	public String sex;// 性别

	public String phone;// 电话

	public String mobile;// 移动电话

	public String qq; // qq

	public String email;// 邮箱

	public String address;// 地址
	// -------------用于普通用户--------------

	public String info;// 介绍，对于讲师或者教育机构有用

	public String logo;// 个人头像，或者教育机构的照片

	// 注册
	public Long registerTime;// 注册时间

	public String registerIp;// 注册ip

	// 登录
	public Long lastLoginTime;// 最后登录时间

	public String lastLoginIp;// 最后登录ip

	public int status;// 帐号状态，0：待审核（新注册用户为待审核，只有少量权限）1：审核通过（普通）2：审核未通过（提示审核不通过原因）3：禁用（禁用以后登录提示需要管理员激活）

	@ManyToMany
	public List<CourseType> courseTypes;// 对于讲师，可以讲授的课程类别

	@ManyToMany
	public List<Course> courses;// 对于讲师，可以讲授的课程

	@ManyToOne
	public Role role;// 角色，这里只做一个用户拥有一种角色处理

	@OneToMany(cascade = CascadeType.ALL)
	public List<Domain> domains;// 分销商对应的域名，一个分销商可以有多个域名，一个域名只能归属于一个分销商，级联删除

	
	
	// -- 查询

	public static Model.Finder<String, User> find = new Model.Finder(String.class, User.class);

	/**
	 * Authenticate a User.
	 */
	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}
}
