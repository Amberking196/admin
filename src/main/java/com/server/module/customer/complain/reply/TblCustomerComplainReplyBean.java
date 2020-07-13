package com.server.module.customer.complain.reply;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * table name: tbl_customer_complain_reply author name: yjr create time:
 * 2018-12-04 11:27:38
 */
@Data
@Entity(tableName = "tbl_customer_complain_reply", id = "id", idGenerate = "auto")
public class TblCustomerComplainReplyBean {

	private Long id;
	private Long complainId;
	private Long pid;
	private Integer src;
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Long createUser;
	private String createName;

}
