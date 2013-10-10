package models;

import javax.persistence.*;

import com.ning.http.client.*;
import common.*;

import play.data.*;
import play.db.ebean.*;
import play.mvc.Http.Request;
import play.mvc.Http.RequestBody;

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
	public String realname; // 真实姓名

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

	public UserInfo() {
	}

	public void updateForUser(User user) {
		if (user != null) {
			this.user = user;

			if (StringHelper.isValidate(realname) == false) {
				this.realname = user.nickname;
			}

			if (this.registerTime == null) {
				this.registerTime = System.currentTimeMillis();
			}
		}
	}

	public UserInfo(DynamicForm form) {
		final String realname = form.get("realname");
		final String address = form.get("address");
		final String idcard = form.get("idcard");
		final String birthday = form.get("birthday");
		final String sex = form.get("sex");
		final String phone = form.get("phone");
		final String qq = form.get("qq");
		final String info = form.get("info");
		this.realname = realname;
		this.address = address;
		this.idcard = idcard;
		if (birthday != null)
			this.birthday = Long.parseLong(birthday);
		this.sex = sex;
		this.phone = phone;
		this.qq = qq;
		this.info = info;
		this.registerTime = System.currentTimeMillis();
	}

	public static void update(UserInfo userInfo, DynamicForm form) {
		final String address = form.get("address");
		final String idcard = form.get("idcard");
		final String birthday = form.get("birthday");
		final String sex = form.get("sex");
		final String phone = form.get("phone");
		final String qq = form.get("qq");
		final String info = form.get("info");

		userInfo.address = address;
		userInfo.idcard = idcard;
		if (birthday != null)
			userInfo.birthday = Long.parseLong(birthday);
		userInfo.sex = sex;
		userInfo.phone = phone;
		userInfo.qq = qq;
		userInfo.info = info;
	}
}
