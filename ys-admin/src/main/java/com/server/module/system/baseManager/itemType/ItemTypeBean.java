package com.server.module.system.baseManager.itemType;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  item_type
 * author name: yjr
 * create time: 2018-04-10 14:28:15
 */ 
@Data
@Entity(tableName="item_type",id="id",idGenerate="auto")
public class ItemTypeBean{
	private Long id;
	private Long pid;
	private String name;
	private Integer state;
	@JsonIgnore
	private Date createTime;
	@JsonIgnore
	private Date updateTime;
	

	@NotField
	private List<ItemTypeBean> list;
}

