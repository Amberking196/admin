package com.server.module.system.warehouseManage.warehouseAdmin;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;


/**
 * table name: warehouse_admin author name: yjr create time: 2018-09-03 14:23:48
 */
@Data
@Entity(tableName = "warehouse_admin", id = "id", idGenerate = "auto")
public class WarehouseAdminBean {
	
	public static final int  AdminTypeAdmin=1;
	public static final int  AdminTypeBuShui=2;


	private Long id;
	private Long warehouseInfoId;
	private String warehouseName;
	private String userId;
	private Integer adminType;// 1 仓库管理员  2 仓库补水员
	private String userName;
	private Date createTime;
	@NotField
	private String adminTypeLabel;
    
	public String getAdminTypeLabel(){
		if(adminType==1){
			return "仓库管理员";
		}
		if(adminType==2){
			return "补水员";
		}
		return "";
	}

}
