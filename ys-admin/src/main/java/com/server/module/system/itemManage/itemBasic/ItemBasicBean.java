package com.server.module.system.itemManage.itemBasic;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * table name:  item_basic
 * author name: why
 * create time: 2018-04-10 14:22:54
 */ 
@Data
@Entity(tableName="item_basic",id="id",idGenerate="auto")
public class ItemBasicBean{


	/*@JsonIgnore	
	public String tableName="item_basic";
	@JsonIgnore	
	public String selectSql="select * from item_basic where 1=1 ";
	@JsonIgnore	
	public String selectSql1="select id,typeId,loginInfoId,pic,name,spell,barCode,state,purchaseWay,brand,standard,isUpdate,showUrl,unit,pack,createTime,updateTime from item_basic where 1=1 ";
	*/
	
	//自增标识
	private Long id;
	//商品类型标识
	private Long typeId;
	//操作人
	private Long loginInfoId;
	//商品图片
	private String pic;
	//商品名称
	private String name;
	//首字母拼音
	private String spell;
	//条形码
	private String barCode;
	//状态
	private Long state;
	//采购方式
	private String purchaseWay;
	//品牌
	private String brand;
	//规格
	private String standard;
	//是否有更改0 =没有
	private Long isUpdate;
	//
	private String showUrl;
	//单位(对应state_Info)
	private Long unit;
	//商品简称
	private String simpleName;
	//包装规格
	private String pack;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	//货道容量
	private Integer wayCapacity;
	//视觉名称
	private String extraName;
	
	
	//类型
	@NotField
	private  String type;
	//商品单位名称
	@NotField
	private String unitName;
	//商品状态名称
	@NotField
	private String stateName;
	
	//商品单价
	@NotField
	private Double price;

	private Long companyId;
	private String companyName;
	//库存数量
	private Integer number;

}

