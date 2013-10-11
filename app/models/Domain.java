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

	@ManyToOne
	public Agent agent;// 代理机构，一个域名拥有一个代理人（也可以没有），一个代理人可以有多个域名

	// -- 查询
	public static Model.Finder<Long, Domain> finder = new Model.Finder(
			Long.class, Domain.class);

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
}
