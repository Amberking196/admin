package com.server.module.system.warehouseManage.supplierManage;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
/**
 * table name:  supplier
 * author name: hjc
 * create time: 2018-05-21 16:58:08
 */ 
@Data
@Entity(tableName="supplier",id="id",idGenerate="auto")
public class SupplierBean{

	//供应商id
	private Long id;
	//地址
	private String address;
	//供应商名称
	private String companyName;
	//电话
	private String phone;
	//姓名
	private String name;
	//创建时间
	private Date createDate;
	private Date createTime;
	//备注
	private String remark;
	//是否被删除
	private Integer delFlag;
	//所属公司
	private Integer companyId;
	//所属公司名称
	@NotField
	private String companyName1;

}

