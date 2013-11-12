package models;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.persistence.*;

import play.data.*;
import play.db.ebean.*;
import play.mvc.Http.MultipartFormData.*;

import com.avaje.ebean.*;
import common.*;

/**
 * 广告
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Advertisment.TABLE_NAME)
public class Advertisment extends Model {
	public static final String TABLE_NAME = "advertisment";

	@Id
	public Long id;

	public String title;// 广告标题（必选）

	public String logo;// 广告图片（可选，如果有图片则图片展示，没有图片则文字展示）

	public String url;// 广告的链接（文字链接，图片链接，必选）

	public Long createTime;// 创建日期

	public Long lastModified;// 最近修改日期

	@Lob
	public String info;// 备注信息，不用于显示（可选）

	@OneToOne(cascade = CascadeType.ALL)
	// 级联
	public AdvertismentStatistics advertismentStatistics;// 广告统计

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<AdvertismentVisit> visitList;// 点击详情

	// -- 查询
	public static Model.Finder<Long, Advertisment> finder = new Model.Finder(
			Long.class, Advertisment.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Advertisment> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Advertisment find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}

	/**
	 * find page with filter
	 * 
	 * @param page
	 * @param form
	 * @return
	 */
	@QueryFilters(values = {
			@QueryFilter(dataName = "title", paramName = "adTitle", queryType = QueryFilter.Type.LIKE, dataType = String.class)})
	public static Page<Advertisment> findPage(DynamicForm form, Integer page, Integer pageSize) {
		QueryHelper<Advertisment> queryFilter = new QueryFilterHelper<Advertisment>(finder, form).filter(Advertisment.class, "findPage", DynamicForm.class, Integer.class, Integer.class);
		return queryFilter.findPage(page, pageSize);
//		return new QueryHelper<Advertisment>().findPage(finder, form, page,	pageSize);
	}

	/**
	 * 删除一个新闻类型
	 * 
	 * @param form
	 * @return
	 */
	public static Advertisment delete(Long id) {
		return QueryHelper.deleteEntity(finder, id);
	}

	/**
	 * add or update
	 * 
	 * @param form
	 * @param logo
	 * @return
	 */
	public static Advertisment addOrUpdate(Advertisment advertisment,
			FilePart fileLogo) {
		if (advertisment != null) {

			if (fileLogo != null) {
				String logo = FileHelper.saveDefaultAdvertismentLogo(fileLogo);
				advertisment.logo = logo;
			} else {
				if (advertisment.id != null) {// 更新，但不更新logo
					Advertisment oldType = Advertisment.find(advertisment.id);
					advertisment.logo = oldType.logo;// 不更新logo
				}
			}

			if (advertisment.id == null) {// 新增
				advertisment.createTime = System.currentTimeMillis();
				advertisment.lastModified = System.currentTimeMillis();
				advertisment.advertismentStatistics = new AdvertismentStatistics(
						advertisment);// 访问统计
				advertisment.save();
			} else {// 更新
				advertisment.lastModified = System.currentTimeMillis();
				advertisment.update();
			}
			return advertisment;
		}
		return null;
	}
}
