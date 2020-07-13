package com.server.module.customer.product;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  picture_material
 * author name: why
 * create time: 2018-07-11 23:55:58
 */ 
@Data
@Entity(tableName="picture_material",id="id",idGenerate="auto")
public class PictureMaterialBean{
	
	private Long id;
	private String picName;
	private Date createTime;
	private Long createUser;
	private Date updateTime;
	private Long updateUser;
	private Integer deleteFlag;
	
	@NotField
	private Integer number;

}

