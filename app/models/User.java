package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import models.interfaces.*;
import play.db.ebean.*;

/**
 * 用户信息表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "user")
public class User extends Model implements IModel {

	@Id
	public Long id;

	public String username;// 账户名，不对外公开

	public String password;// 密码，加密保存

	public String nickname;// 昵称，对于培训机构可以是xxx培训几个，对于老师可以是xxx老师，该名称公开给外界。

	public String mobile;// 移动电话

	public String email;// 邮箱地址

	public String logo;// 个人头像，或者教育机构的照片

	public int status;// 帐号状态，0：待审核（新注册用户为待审核，只有少量权限）1：审核通过（普通）2：审核未通过（提示审核不通过原因）3：禁用（禁用以后登录提示需要管理员激活）

	@OneToOne
	public UserInfo basicInfo;// 用户基本信息，一个用户对应一个基本信息，一个基本信息对应一个用户

	@ManyToOne
	public List<Role> roles;// 角色，一个用户可以同时是多个角色，比如同时是教育机构和代理人，一个用户拥有多个角色，一个角色可以被多个用户拥有

	@OneToMany(cascade = CascadeType.ALL)
	public List<EducationInstitution> edus;// 用户对应的教育机构，一个用户可以对应多个教育机构，一个教育机构只能隶属于一个用户（创建者，但是可以有多个子帐号）

	@OneToOne
	public Instructor instructor;// 讲师，一个用户对应于一个讲师，一个讲师只能是一个用户

	@OneToOne
	public Agent agent;// 代理人，一个用户对应于一个代理人，一个代理人只能是一个用户
	
	@OneToOne
	public Student student;//学员，一个用户对于一个学员，一个学员只能是一个用户

	
	/**
	 * 根据类的属性名称，获取属性值
	 * @param key
	 * @return
	 */
	public Object get(final String property) {
		try {
			Field field = User.class.getField(property);
			if(field != null){
				field.setAccessible(true);//设置可以访问，对于私有变量有用
				return field.get(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// -- 查询
	public static Model.Finder<String, User> finder = new Model.Finder(
			Long.class, User.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<User> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static User find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * 认证用户名/邮箱/手机号，密码组合是否存在，存在则可以正常登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static User authenticate(String account, String password) {
		User user = finder.where().eq("username", account)
				.eq("password", password).findUnique();
		if (user == null) {
			user = finder.where().eq("email", account).eq("password", password)
					.findUnique();
		}
		if (user == null) {
			user = finder.where().eq("mobile", account)
					.eq("password", password).findUnique();
		}
		return user;
	}

	// ------------------------object functions-----------------------
	// to string
	public String toString() {
		return "User(id: " + id + ", username: " + username + ")";
	}

}
