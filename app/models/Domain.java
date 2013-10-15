package models;

import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;

import com.avaje.ebean.*;
import com.avaje.ebean.Query;
import common.*;

/**
 * 域名
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = "domain")
public class Domain extends Model {
	private static final String TAG = Domain.class.getSimpleName();
	
	public static final String TABLE_NAME = "domain";
	
	@Id
	public Long id;

	public String domain;// 域名

	@ManyToOne(targetEntity=Agent.class)
	public Agent agent;// 代理机构，一个域名拥有一个代理人（也可以没有），一个代理人可以有多个域名

	@ManyToOne(targetEntity=EducationInstitution.class)
	public EducationInstitution edu;// 教育机构，一个域名拥有一个教育机构（也可以没有），一个教育机构可以有多个域名

	@ManyToOne(targetEntity=Instructor.class)
	public Instructor instructor;// 讲师，一个域名拥有一个讲师（也可以没有），一个讲师可以有多个域名

	// -- 查询
	public static Model.Finder<Long, Domain> finder = new Model.Finder(Long.class, Domain.class);

	public Domain(){}
	
	public Domain(EducationInstitution edu){
		this.edu = edu;
		this.domain = buildRandomDomain("e", String.valueOf(edu.hashCode()));
	}
	
	public Domain(Instructor instructor){
		this.instructor = instructor;
		this.domain = buildRandomDomain("i", String.valueOf(instructor.hashCode()));
	}
	
	public Domain(Agent agent){
		this.agent = agent;
		this.domain = buildRandomDomain("i", String.valueOf(agent.hashCode()));
	}
	
	public String buildRandomDomain(String prefix, String id){
		return prefix + id;
	}
	
	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Domain> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Domain find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * find one by domain
	 * @param domain
	 * @return
	 */
	public static Domain findByDomain(String domain){
		return finder.where().eq("domain", domain).findUnique();
	}

	/**
	 * 新增或更新一个课程
	 * 
	 * @param form
	 * @return
	 */
	public static Domain addOrUpdate(Domain domain) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + domain.id + ", domain=" + domain.domain);
		if (domain != null) {
			if (domain.id == null) {// 新增
				domain.id = finder.nextId();
				domain.save();
			} else {// 更新
				domain.update();
			}
			return domain;
		}
		return null;
	}

	/**
	 * delete an edu
	 * @param form
	 * @return
	 */
	public static Domain delete(Long id){
		Domain domain = find(id);
		if(domain != null){
			domain.delete();
			return domain;
		}
		return null;
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Domain> findPage(DynamicForm form, int page,	Integer pageSize) {
		return new QueryHelper<Domain>().findPage(finder, form, page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Domain> findPageByAgent(Agent agent, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("agentId", agent.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Domain>(finder, form).addEq("agent.id", "agentId", Long.class).findPage(page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Domain> findPageByEducationUser(User  user, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("creatorId", user.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Domain>(finder, form).addEq("edu.creator.id", "creatorId", Long.class).addOrderBy("orderby").findPage(page, pageSize);
//		return new QueryHelper<Course>(finder, form).addEqual("edu.creator.id", user.id.toString(), Long.class).addOrderBy("orderby").findPage(finder, form, page, pageSize);
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	public static Page<Domain> findPageByTeacher(Instructor instructor, DynamicForm form, int page, Integer pageSize) {
		Map<String, String> datas = form.data();
		datas.put("instructorId", instructor.id.toString());
		form = form.bind(datas);
		return new QueryHelper<Domain>(finder, form).addEq("instructor.id", "instructorId", Long.class).findPage(page, pageSize);
	}
}
