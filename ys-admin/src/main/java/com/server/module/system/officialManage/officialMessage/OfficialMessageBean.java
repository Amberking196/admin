package com.server.module.system.officialManage.officialMessage;

import com.server.common.persistence.Entity;
import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  official_message
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 */ 
@Data
@Entity(tableName="official_message",id="id",idGenerate="auto")
public class OfficialMessageBean{

	private Long id;
	private String name;
	private String mail;
	private String phone;
	private String content;
	private Date createTime;
	private Date updateTime;
	private Long createUser;
	private Long updateUser;
	private Integer deleteFlag;

}

