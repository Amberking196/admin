package com.server.module.system.lyManager.lyFileInfo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * table name: ly_file_info
 * author name: why 文件管理 bean
 * create time: 2018-04-04 09:44:15
 */
@Entity(tableName="ly_file_info",id="id",idGenerate="assign")
@Data
public class LyFileInfoBean {

	@JsonIgnore
	public String tableName="ly_file_info";
	@JsonIgnore
	public String selectSql="select * from ly_file_info where 1=1 ";
	@JsonIgnore
	public String selectsql1="select name,type,path,remark,size,version,createTime,udaateTime from ly_file_info where 1=1 ";
	
	//名称
	private String name;
	//类型
	private String type;
	//路径
	private String path;
	//备注
	private String remark;
	//大小
	private Integer size;
	//版本号
	private String version;	
	//创建时间
	private Date   createTime;
	//更新时间
	private Date   updateTime;
}
