package com.server.module.system.forumManage.forumFilterManage;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * 
 * @author why
 * @date: 2019年3月14日 上午11:27:33
 */
@Data
@Entity(tableName = "forum_filter", id = "id", idGenerate = "auto")
public class ForumFilterBean {

	//主键id
	private Long id;
	//敏感字
	private String noun;
	//创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//删除标识 0未删除   1已删除
	private Integer deleteFlag;

}
