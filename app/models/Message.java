package models;

import java.util.*;

import javax.persistence.*;

import com.avaje.ebean.*;
import common.*;

import play.data.*;
import play.db.ebean.*;

/**
 * 留言
 * 
 * @author MonsterStorm
 * 
 */
@Entity
@Table(name = Message.TABLE_NAME)
public class Message extends Model {
	private static final String TAG = Message.class.getSimpleName();
	public static final String TABLE_NAME = "message";
	@Id
	public Long id;

	public String name;// 留言人

	public String phone;// 留言电话

	public String qq;// 留言qq

	public String email;// 留言email

	public String address;// 留言地址

	public Long time;// 留言时间
	
	@Lob
	public String title;//标题
	
	@Lob
	public String content;//留言内容

	// -- 查询
	public static Model.Finder<Long, Message> finder = new Model.Finder(Long.class, Message.class);

	/**
	 * find all user
	 * 
	 * @return
	 */
	public static List<Message> findAll() {
		return finder.findList();
	}

	/**
	 * find one by id
	 * 
	 * @param id
	 * @return
	 */
	public static Message find(Long id) {
		return finder.where().eq("id", id).findUnique();
	}
	
	/**
	 * delete an edu
	 * @param form
	 * @return
	 */
	public static Message delete(Long id){
		Message message = find(id);
		if(message != null){
			message.delete();
			return message;
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
	public static Page<Message> findPage(DynamicForm form, int page, Integer pageSize) {
		return new QueryHelper<Message>().findPage(finder, form, page, pageSize);
	}
	
	/**
	 * 新增或更新一个留言
	 * 
	 * @param form
	 * @return
	 */
	public static Message addOrUpdate(Message message) {
		play.Logger.debug(TAG + ".addOrUpdate: id=" + message.id + ", name=" + message.name );
		if (message != null) {
			if (message.id == null) {// 新增
				message.id = finder.nextId();
				message.save();
			} else {// 更新
				message.update();
			}
			return message;
		}
		return null;
	}
}
