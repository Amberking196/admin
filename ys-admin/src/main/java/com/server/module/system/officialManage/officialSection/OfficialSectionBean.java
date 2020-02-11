package com.server.module.system.officialManage.officialSection;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  official_section
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 
@Data
@Entity(tableName="official_section",id="id",idGenerate="auto")
public class OfficialSectionBean{

	private Long id;
	private Long pid;//父栏目id
	private String name;//栏目名称
	private String url;//地址
	private Date createTime;
	private Date updateTime;
	private Long createUser;
	private Long updateUser;
	private Integer deleteFlag;
	@NotField
	private List<OfficialSectionBean> list;
}

