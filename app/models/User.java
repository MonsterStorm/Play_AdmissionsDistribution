package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;
import play.mvc.Http.MultipartFormData.FilePart;

import com.avaje.ebean.*;
import common.*;

import controllers.*;

/**
 * 用户信息表
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = User.TABLE_NAME)
public class User extends Model {
	public static final String TABLE_NAME = "user";
	public static final String LOGO_DEFAULT = "default.png";

	@Id
	public Long id;

	public String username;// 账户名，不对外公开

	public String password;// 密码，加密保存

	public String nickname;// 昵称，对于培训机构可以是xxx培训几个，对于老师可以是xxx老师，该名称公开给外界。

	public String mobile;// 移动电话

	public String email;// 邮箱地址

	public String logo;// 个人头像，或者教育机构的照片

	@OneToOne(cascade = CascadeType.ALL)
	public Audit audit;// 帐号状态，0：待审核（新注册用户为待审核，只有少量权限）1：审核通过（普通）2：审核未通过（提示审核不通过原因）3：禁用（禁用以后登录提示需要管理员激活）

	@OneToOne(cascade = CascadeType.ALL)
	public UserInfo basicInfo;// 用户基本信息，一个用户对应一个基本信息，一个基本信息对应一个用户

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Role> roles = new ArrayList<Role>();// 角色，一个用户可以同时是多个角色，比如同时是教育机构和代理人，一个用户拥有多个角色，一个角色可以被多个用户拥有

	@OneToMany(mappedBy = "creator", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<EducationInstitution> edus;// 用户对应的教育机构，一个用户可以对应多个教育机构，一个教育机构只能隶属于一个用户（创建者，但是可以有多个子帐号）

	@OneToMany(mappedBy = "parentAccount", cascade = CascadeType.ALL)
	public List<User> bypassAccounts;// 子帐号

	@ManyToOne
	public User parentAccount;// 根帐号

	@OneToOne(cascade = CascadeType.ALL)
	public Instructor instructor;// 讲师，一个用户对应于一个讲师，一个讲师只能是一个用户

	@OneToOne(cascade = CascadeType.ALL)
	public Agent agent;// 代理人，一个用户对应于一个代理人，一个代理人只能是一个用户

	@OneToOne(cascade = CascadeType.ALL)
	public Student student;// 学员，一个用户对于一个学员，一个学员只能是一个用户

	public User() {
	}

	public User(DynamicForm form) {
		final String username = form.get("username");
		final String password = form.get("password");
		final String nickname = form.get("nickname");
		final String mobile = form.get("mobile");
		final String email = form.get("email");
		final String logo = form.get("logo");

		UserInfo userInfo = new UserInfo(form);
		userInfo.user = this;

		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.mobile = mobile;
		this.email = email;

		Audit audit = new Audit();
		audit.save();
		audit.status = Audit.STATUS_WAIT;// 新增用户的状态为未认证
		this.audit = audit;
		this.logo = User.LOGO_DEFAULT;// 默认头像
		this.basicInfo = userInfo;
	}

	/**
	 * update info
	 * 
	 * @param user
	 * @param form
	 */
	public static void update(User user, DynamicForm form) {
		final String nickname = form.get("nickname");
		final String mobile = form.get("mobile");
		final String email = form.get("email");
		final String logo = form.get("logo");

		user.nickname = nickname;
		user.mobile = mobile;
		user.email = email;

		UserInfo.update(user.basicInfo, form);

		user.update();
	}

	/**
	 * create a user for edu
	 * 
	 * @param edu
	 * @param roleId
	 * @param auditStatus
	 * @return
	 */
	public static User createUserForEdu(EducationInstitution edu, long roleId,
			int auditStatus) {
		if (edu == null) {
			return null;
		}

		User user = new User();

		user.edus.add(edu);

		randomAccount(user);

		buildUserFields(user, roleId, auditStatus, AuditType.TYPE_AUDITTYPE_EDU);

		return user;
	}

	/**
	 * create a user for agent
	 * 
	 * @param agent
	 * @param roleId
	 * @param auditStatus
	 * @return
	 */
	public static User createUserForAgent(Agent agent, long roleId,
			int auditStatus) {
		if (agent == null) {
			return null;
		}

		User user = new User();
		user.agent = agent;

		randomAccount(user);

		buildUserFields(user, roleId, auditStatus,
				AuditType.TYPE_AUDITTYPE_AGENT);

		return user;
	}

	/**
	 * create a random user for student
	 * 
	 * @param user
	 * @param roleId
	 * @return
	 */
	public static void updateRandomUserForStudent(Student student, long roleId,
			int auditStatus) {
		if (student == null)
			return;

		User user = student.user;

		if (user == null) {
			user = new User();
		}

		// 生产随机帐号密码
		randomAccount(user);

		// 学员信息
		user.student = student;

		buildUserFields(user, roleId, auditStatus,
				AuditType.TYPE_AUDITTYPE_STUDENT);
	}

	/**
	 * create a random user for student
	 * 
	 * @param user
	 * @param roleId
	 * @return
	 */
	public static User getRandomUserForStudent(Student student, long roleId,
			int auditStatus) {
		if (student == null)
			return null;

		User user = student.user;

		if (user == null) {
			user = new User();
		}

		// 生产随机帐号密码
		randomAccount(user);

		// 学员信息
		user.student = student;

		buildUserFields(user, roleId, auditStatus,
				AuditType.TYPE_AUDITTYPE_STUDENT);
		return user;
	}

	/**
	 * create a random user for instructor
	 * 
	 * @param instructor
	 * @param roleId
	 * @param auditStatus
	 */
	public static void updateRandomUserForInstructor(Instructor instructor,
			long roleId, int auditStatus) {
		if (instructor == null) {
			return;
		}

		User user = instructor.user;

		// 生成随机帐号
		randomAccount(user);

		// 讲师信息
		user.instructor = instructor;

		buildUserFields(user, roleId, auditStatus,
				AuditType.TYPE_AUDITTYPE_INSTRUCTOR);
	}

	/**
	 * build user fields
	 * 
	 * @param user
	 * @param roleId
	 * @param auditStatus
	 */
	public static void buildUserFields(User user, long roleId, int auditStatus,
			long type) {
		// 角色
		Role role = Role.find(roleId);
		if (role != null) {
			user.roles.add(role);
		}

		// 基本信息
		if (user.basicInfo != null) {
			user.basicInfo.updateForUser(user);
		}

		// 审核信息
		Audit audit = new Audit(user, auditStatus,
				AuditType.TYPE_AUDITTYPE_USER);
		AuditType auditType = AuditType.find(type);// 学员审核
		audit.type = auditType;
		user.audit = audit;
	}

	/**
	 * create random account and password for this user
	 * 
	 * @param user
	 */
	public synchronized static void randomAccount(User user) {
		if (user != null) {
			// 随机生成用户名跟密码
			if (StringHelper.isValidate(user.username) == false) {
				user.username = "User" + System.currentTimeMillis();
			}
			if (StringHelper.isValidate(user.password) == false) {
				user.password = buildRandomPassword();
			}
		}
	}

	/**
	 * 根据类的属性名称，获取属性值
	 * 
	 * @param key
	 * @return
	 */
	public Object get(final String property) {
		try {
			Field field = User.class.getField(property);
			if (field != null) {
				field.setAccessible(true);// 设置可以访问，对于私有变量有用
				return field.get(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * is user has an role
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean hasRole(long roleId) {
		return Role.findByUserId(roleId, this.id) != null;
	}

	/**
	 * get roles string, return :role1_id:role2_id:...:rolen_id:;
	 * 
	 * @return
	 */
	public String getRoles() {
		StringBuilder roleStr = new StringBuilder();
		if (this.roles != null) {
			for (int i = 0; i < this.roles.size(); i++) {
				if (i == 0) {
					roleStr.append(":");
				}
				roleStr.append(roles.get(i).id);
				if (i == this.roles.size() - 1) {
					roleStr.append(":");
				}
			}
		}
		if (roleStr.length() > 0) {
			return roleStr.toString();
		} else {
			return null;
		}
	}

	/**
	 * get Model string
	 * 
	 * @return
	 */
	public String getModles() {
		StringBuilder moduleStr = new StringBuilder();
		if (this.roles != null) {
			boolean isFirst = true;
			for (int i = 0; i < this.roles.size(); i++) {
				Role role = this.roles.get(i);
				if (role != null && role.modules != null) {
					List<Module> modules = role.modules;
					for (int j = 0; j < modules.size(); j++) {
						if (isFirst) {
							moduleStr.append(":");
							isFirst = false;
						}
						moduleStr.append(modules.get(i).id);
					}
				}
			}
			if (isFirst == false) {
				moduleStr.append(":");
			}
		}

		if (moduleStr.length() > 0) {
			return moduleStr.toString();
		} else {
			return null;
		}
	}

	/**
	 * get function string
	 * 
	 * @return
	 */
	public String getFunctions() {
		StringBuilder functionStr = new StringBuilder();
		if (this.roles != null) {
			boolean isFirst = true;
			for (int i = 0; i < this.roles.size(); i++) {// roles
				Role role = this.roles.get(i);
				if (role != null && role.modules != null) {
					List<Module> modules = role.modules;
					for (int j = 0; j < modules.size(); j++) {// models
						Module module = modules.get(j);
						if (module != null && module.functions != null) {
							List<Function> functions = module.functions;
							for (int k = 0; k < functions.size(); k++) {// functions
								if (isFirst) {
									functionStr.append(":");
									isFirst = false;
								}
								functionStr.append(functions.get(i).id);
							}
						}
					}
				}
			}
			if (isFirst == false) {
				functionStr.append(":");
			}
		}

		if (functionStr.length() > 0) {
			return functionStr.toString();
		} else {
			return null;
		}
	}

	/**
	 * update template of user
	 * 
	 * @param template
	 */
	public void updateTemplate(Template template) {
		if (this.edus != null && this.edus.size() > 0) {
			this.edus.get(0).template = template;
		} else if (this.instructor != null) {
			this.instructor.template = template;
		} else if (this.agent != null) {
			this.agent.template = template;
		}

		this.update();
	}

	// -- 查询
	public static Model.Finder<Long, User> finder = new Model.Finder(
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
	 * find user by account
	 * 
	 * @param account
	 * @return
	 */
	public static User findByUsername(String username) {
		return finder.where().eq("username", username).findUnique();
	}

	/**
	 * is a username is registered
	 * 
	 * @param username
	 * @return
	 */
	public static boolean isUsernameRegistered(String username) {
		return finder.where().eq("username", username).findRowCount() > 0;
	}

	/**
	 * is a mobile is registered
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobileRegistered(String mobile) {
		return finder.where().eq("mobile", mobile).findRowCount() > 0;
	}

	/**
	 * is a email is registered
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmailRegistered(String email) {
		return finder.where().eq("email", email).findRowCount() > 0;
	}

	/**
	 * find user by email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean hasUserByEmail(String email) {
		return finder.where().eq("email", email).findRowCount() > 0;
	}

	/**
	 * find user by mobile
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean hasUserByMobile(String mobile) {
		return finder.where().eq("mobile", mobile).findRowCount() > 0;
	}

	/**
	 * find user by agent
	 * 
	 * @param agent
	 * @return
	 */
	public static User findByAgent(Agent agent) {
		return finder.where().eq("agent.id", agent.id).findUnique();
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = {
			@QueryFilter(dataName = "username", paramName = "username", queryType = QueryFilter.Type.LIKE, dataType = String.class),
			@QueryFilter(dataName = "nickname", paramName = "nickname", queryType = QueryFilter.Type.LIKE, dataType = String.class),
			@QueryFilter(dataName = "mobile", paramName = "mobile", queryType = QueryFilter.Type.LIKE, dataType = String.class),
			@QueryFilter(dataName = "email", paramName = "email", queryType = QueryFilter.Type.LIKE, dataType = String.class),
			@QueryFilter(dataName = "audit.status", paramName = "auditStatus", queryType = QueryFilter.Type.EQ, dataType = Integer.class),
			@QueryFilter(dataName = "audit.createTime", paramName = "auditCreateTime", queryType = QueryFilter.Type.BETWEEN, dataType = Long.class) })
	public static Page<User> findPage(DynamicForm form, Integer page,
			Integer pageSize) {
		QueryHelper<User> queryFilter = new QueryFilterHelper<User>(finder,
				form).filter(User.class, "findPage", DynamicForm.class,
				Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
		// return new QueryHelper<User>(finder, form).addEq("audit.status",
		// "auditStatus", Integer.class).findPage(page, pageSize);
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

	/**
	 * 本来是通过角色来判断，这里通过是否是教育机构，是否是讲师，是否是代理人来判断
	 * 
	 * @return
	 */
	public boolean hasTemplate() {
		return (this.edus != null && this.edus.size() > 0)
				|| this.instructor != null || this.agent != null;
	}

	/**
	 * 判断一个帐号是否是父帐号
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isParentAccount(Long id) {
		User user = find(id);
		return user.parentAccount == null;
	}

	/**
	 * 判断一个帐号是否是子帐号
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isBypassAccount(Long id) {
		User user = find(id);
		return user.parentAccount != null;
	}

	/**
	 * 判断一个帐号是否有子帐号
	 * 
	 * @param id
	 * @return
	 */
	public static boolean hasBypassAccounts(Long id) {
		User user = find(id);
		return user.bypassAccounts != null && user.bypassAccounts.size() > 0;
	}

	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static User addOrUpdate(DynamicForm form) {
		final String id = form.get("id");
		final String username = form.get("username");
		if (StringHelper.isValidate(id)) {// 更新
			User user = find(Long.valueOf(id));
			if (user != null) {
				update(user, form);
				LoginController.updateSession(user);
				return user;
			}
		}
		if (User.findByUsername(username) == null) {// 插入
			User user = new User(form);
			Role role = Role.find(Role.ROLE_REGISTED_USER);
			if (role != null) {
				user.roles.add(role);
			}
			Role role2 = Role.find(Role.ROLE_STUDENT);
			if (role2 != null) {
				user.roles.add(role2);
			}

			user.save();
			return user;
		}
		return null;
	}

	/**
	 * 
	 * @param form
	 * @param fileLogo
	 * @return
	 */
	public static User addOrUpdate(User user, FilePart fileLogo) {
		if (user != null) {
			if (fileLogo != null) {
				if (user.id != null) {// 更新
					user.logo = FileHelper.saveUserLogo(
							String.valueOf(user.id), fileLogo);
				} else {// 新增
					user.logo = FileHelper.saveUserLogo(
							String.valueOf(finder.nextId()), fileLogo);
				}
			} else {
				if (user.id != null) {// 更新，不更新logo
					User oldUser = User.find(user.id);
					user.logo = oldUser.logo;// 不更新logo
				}
			}

			if (user.id == null) {// 新增，由于没有密码字段，因而需要自动生成，并发送到用户的邮箱
				if (StringHelper.isValidate(user.password) == false) {// 密码不合法
					user.password = buildRandomPassword();
				}

				User.buildUserFields(user, Role.ROLE_REGISTED_USER,
						Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_USER);

				user.save();
				user.update();// 更新一下链接
			} else {// 更新
				User oldUser = User.find(user.id);
				if (oldUser != null) {
					oldUser.nickname = user.nickname;
					oldUser.logo = user.logo;
					if (StringHelper.isValidate(user.email))
						oldUser.email = user.email;
					if (StringHelper.isValidate(user.mobile))
						oldUser.mobile = user.mobile;
					oldUser.update();
					
					LoginController.updateSession(oldUser);
				}
			}
			return user;
		}
		return null;
	}

	// ------------------------object functions-----------------------
	// to string
	public String toString() {
		return "User(id: " + id + ", username: " + username + ")";
	}

	/**
	 * 注册时验证 用户名 邮箱 是否存在 若存在则无法注册add by khx
	 * 
	 * @param username
	 * @param email
	 * @return
	 */
	public static int canRegister(String username, String email) {
		User user = finder.where().eq("username", username).findUnique();
		if (user != null) {
			return Constants.INT_USERNAME_EXIST;
		}
		user = finder.where().eq("email", username).findUnique();
		if (user != null) {
			return Constants.INT_USERNAME_EXIST;
		}
		user = finder.where().eq("email", email).findUnique();
		if (user != null) {
			return Constants.INT_EMAIL_EXIST;
		}
		user = finder.where().eq("username", email).findUnique();
		if (user != null) {
			return Constants.INT_EMAIL_EXIST;
		}
		return Constants.INT_CAN_REGISTER;
	}

	/**
	 * 生成密码
	 * 
	 * @return
	 */
	public static String buildPassword(String password) {
		// hash操作
		return password;
	}

	/**
	 * 生成一个随机密码,6位随机大小写字母
	 * 
	 * @return
	 */
	public static String buildRandomPassword() {
		Random random = new Random(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {// 在26个字母中随机生成六位数
			if (random.nextBoolean()) {
				sb.append((char) ('a' + random.nextInt(27) % 26));
			} else {
				sb.append((char) ('A' + random.nextInt(27) % 26));
			}
		}
		return sb.toString();
	}

}
