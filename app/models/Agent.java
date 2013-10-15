package models;

import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;

import com.avaje.ebean.*;
import common.*;

import controllers.*;

/**
 * 代理人
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Agent.TABLE_NAME)
public class Agent extends Model{
	public static final String TABLE_NAME = "agent";
	@Id
	public Long id;

	@OneToOne
	public User user;// 代理人对应的用户账户，一个用户只能对应一个代理人，一个代理人对应一个用户

	@OneToMany(mappedBy="agent", cascade=CascadeType.ALL)
	public List<Domain> domain;// 代理人对应的域名信息，一个代理人对应多个域名，一个域名隶属于一个代理人（也可以没有代理人）
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Course> courses;//代理人代理的课程列表，一个代理人可以代理多个课程，一个课程可以被多个代理人代理

	@OneToOne(cascade=CascadeType.ALL)
	public Template template;//一个代理人有一个专属推广页面
	
	@OneToOne(cascade=CascadeType.ALL)
	public Audit audit;//是否认证

	// -- 基本信息
	public String name;//代理机构名称
	
	public String info;//代理机构简介
	
	public String contact;//联系方式
	
	// -- 查询
	public static Model.Finder<Long, Agent> finder = new Model.Finder(Long.class, Agent.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Agent> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Agent find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Agent> findPage(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Agent>().findPage(finder, form, page, pageSize);
	}
	
	/**
	 * 删除一个新闻类型
	 * @param form
	 * @return
	 */
	public static Agent delete(Long id){
		return QueryHelper.deleteEntity(finder, id);
	}
	
	/**
	 * 新增或更新一个用户
	 * 
	 * @param form
	 * @return
	 */
	public static Agent addOrUpdate(Agent agent) {
		if (agent != null) {
			if (agent.id == null) {// 新增
				agent.user = User.createUserForAgent(agent, Role.ROLE_AGENT, Audit.STATUS_SUCCESS);//绑定到当前用户，为每个教育机构新建一个用户号
				agent.id = finder.nextId();
				
				//审核
				agent.audit = new Audit(agent.user, Audit.STATUS_WAIT, AuditType.TYPE_AUDITTYPE_AGENT);
				
				//模板
				agent.template = new Template(agent, TemplateType.TYPE_DEFAULT);
				
				//域名
				agent.domain.add(new Domain(agent));
				
				agent.save();
			} else {// 更新
				agent.update();
			}
			return agent;
		}
		return null;
	}
	
	/**
	 * course
	 * @param courseId
	 * @param auditStatus
	 * @return
	 */
	public static Agent updateAudit(Long agentId, Integer auditStatus){
		Agent agent = Agent.find(agentId);
		if(agent != null ){
			if(agent.audit != null){
				agent.audit.status = auditStatus;
				agent.audit.auditor = LoginController.getSessionUser();
				agent.audit.auditTime = System.currentTimeMillis();
				agent.update();
				return agent;
			}
		}
		return null;
	}
	
}
