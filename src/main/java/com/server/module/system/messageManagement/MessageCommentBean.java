package com.server.module.system.messageManagement;

import java.util.Date;

import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * 查询评论返回的bean
 * @author 26920
 *
 */
@Data
@Entity(tableName = "customer_message_comment", id = "id", idGenerate = "auto")
public class MessageCommentBean {
	  //评论Id
	  private int id;
	 //留言Id
	 private int messageId;
	 //评论人电话
	 private String commentPhone;
	 //被评论人电话
	 private String byCommentPhone;
	 //评论内容
	 private String commentContent;
	 //创建时间
	 private Date createTime;
	 //创建用户
	 private int createUser;
	 //更新时间
	 private Date updateTime;
	 //更新用户
	 private int updateUser;
	 //'是否删除 0 未删除  1 已删除'
	 private Short deleteFlag;
}
