package com.server.module.system.warehouseManage.stock;
import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;
import java.util.Date;
/**
 * table name:  warehouse_stock
 * author name: yjr
 * create time: 2018-05-22 10:49:58
 */ 
@Data
@Entity(tableName="warehouse_stock",id="id",idGenerate="auto")
public class WarehouseStockBean{
//@JsonIgnore	public String tableName="warehouse_stock";
//@JsonIgnore	public String selectSql="select * from warehouse_stock where 1=1 ";
//@JsonIgnore	public String selectSql1="select id,itemId,warehouseId,itemName,quantity,costPrice,price,createTime,remark from warehouse_stock where 1=1 ";
	private Long id;
	private Long itemId;//商品id
	@ExcelField(title = "商品名")
	private String itemName;//商品名
	private Long warehouseId;//仓库id
	@ExcelField(title = "仓库名")
	private String warehouseName;//仓库名
	private Long companyId;//公司id
	@ExcelField(title = "公司名")
	private String companyName;//公司名
	@ExcelField(title = "商品数量")
	private Long quantity;//库存数量
	@ExcelField(title = "成本价")
	private Double costPrice;//成本价
	private Double price;//销售价
	private Date createTime;
	private String remark;
	private String barCode;
	@ExcelField(title = "单位")
	private String unitName;//商品单位
	@NotField
	private Double zongJiner;//总金额
	@ExcelField(title = "建议采购量")
	private Integer purchaseNumber;//建议采购量
	@ExcelField(title = "告警库存下限")
	private Integer lowerLimit;//告警库存下限
	@ExcelField(title = "库存上限")
	private Integer higherLimit;//库存上限
}

