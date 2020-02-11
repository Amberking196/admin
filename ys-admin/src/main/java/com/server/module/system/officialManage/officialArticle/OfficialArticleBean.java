package com.server.module.system.officialManage.officialArticle;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: official_article author name: why create time: 2018-08-01
 * 15:07:48
 */
@Data
@Entity(tableName = "official_article", id = "id", idGenerate = "auto")
public class OfficialArticleBean {

	// 主键id
	private Long id;
	//序号
	@NotField
	private Integer number;
	// 栏目ID
	private Long sid;
	// 标题
	private String title;
	// 作者
	private String author;
	// 摘要
	private String remark;
	// 内容
	private String content;
	// 点击量
	private Long num;
	// 优先级
	private Integer level;
	// 配图地址
	private String url;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;
	// 创建用户
	private Long createUser;
	// 更新用户
	private Long updateUser;
	// 是否删除 0 未删除 1 已删除
	private Integer deleteFlag;
	//文章的类型
	private Integer type;
	
	//栏目名称
	@NotField
	private String sectionName;
	//时间
	@NotField
	private String time;

}
