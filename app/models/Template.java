package models;

import javax.persistence.*;

import play.db.ebean.*;

/**
 * 模板类，模板路径定义为在templates目录下添加用户名路径，所有用户相关的文件放到该目录下
 * @author MonsterStorm
 *
 */
@Entity
@Table(name="template")
public class Template extends Model{
	
	@Id
	public Long id;
	
	@OneToOne
	public User user;//一个用户拥有一个模板
	
	@ManyToOne
	public TemplateType type;//模板类型，多个模板可以对应于一个类型，对于自定义模板（该字段为null，或者为type中定义的一种）
}
