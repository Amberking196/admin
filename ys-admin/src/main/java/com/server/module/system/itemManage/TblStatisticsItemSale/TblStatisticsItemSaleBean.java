package com.server.module.system.itemManage.TblStatisticsItemSale;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;

import lombok.Data;

/**
 * table name: tbl_statistics_item_sale 
 * author name: WHY
 * create time: 2018-05-02 09:36:44
 */
@Data
@Entity(tableName = "tbl_statistics_item_sale", id = "id", idGenerate = "auto")
public class TblStatisticsItemSaleBean {

	
	//自增标识
	private Long id;
	//商品标识
	private Long basicItemId;
	//企业标识
	private Long companyId;
	//上架机器数
	private Long   itemOnVmNum;
	
	private String  reportDate;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;

}
