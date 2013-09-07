package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 用户信息表，用于缓存用户信息，简化User表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "user_info")
public class UserInfo extends Model {

	@Id
	public Long id;

	@OneToOne
	public User user;// 用户信息所属的用户，一个用户只有一个用户信息，一个用户信息隶属与一个用户

	// -------------用于普通用户--------------
	public String idcard;// 身份证

	public Long birthday;// 出生日期

	public String sex;// 性别

	public String phone;// 电话

	public String qq; // qq

	public String address;// 地址
	// -------------用于普通用户--------------

	public String info;// 介绍，对于讲师或者教育机构有用

	// 注册
	public Long registerTime;// 注册时间

	public String registerIp;// 注册ip

	// 登录
	public Long lastLoginTime;// 最后登录时间

	public String lastLoginIp;// 最后登录ip
}
