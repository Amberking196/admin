package com.server.module.system.logsManager.messageLog;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;

/**
 * table name: message_log author name: yjr create time: 2018-03-24 14:45:24
 */
@Entity(tableName = "message_log", id = "id", idGenerate = "auto")
public class MessageLogBean {

	@JsonIgnore
	public String tableName = "message_log";
	@JsonIgnore
	public String selectSql = "select * from message_log where 1=1 ";
	@JsonIgnore
	public String selectSql1 = "select id,vmCode,payCode,sendTime,content from message_log where 1=1 ";
	private Long id;
	private String vmCode;
	private String payCode;
	private Date sendTime;
	private String content;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	public String getVmCode() {
		return vmCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}
