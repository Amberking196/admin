package com.server.module.system.itemManage.itemBasic;
import java.util.Date;

import com.server.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * 
 * author name: why
 * create time: 2018-04-03 14:35:06
 */
@Data
public class ItemBasicDto {

	
	@ExcelField(title = "商品名称")
	private String name;
	
	@ExcelField(title = "条形码")
	private String barCode;
	
	@ExcelField(title = "图片")
	private String pic;
	
	@ExcelField(title = "类型")
	private  String type;
	
	@ExcelField(title = "状态")
	private String stateName;
	
	@ExcelField(title = "品牌")
	private String brand;
	
	@ExcelField(title = "规格")
	private String standard;
	
	@ExcelField(title = "采购方式")
	private String purchaseWay;
	
	@ExcelField(title = "创建时间")
	private Date createTime;
	
}
