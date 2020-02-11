package com.server.module.system.logsManager.itemLog;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;
import com.server.common.utils.excel.annotation.ExcelField;
/**
 * table name:  item_log
 * author name: yjr
 * create time: 2018-03-24 09:24:43
 */ 
@Entity(tableName="item_log",id="id",idGenerate="auto")
public class ItemLogBean{

    @JsonIgnore
	public String tableName="item_log";
    @JsonIgnore
	public String selectSql="select * from item_log where 1=1 ";
    @JsonIgnore
	public String selectSql1="select id,vmCode,machinesItemId,itemName,barCode,price,costPrice,endTime,createTime,operator,oldPrice,oldCostPrice,oldEndTime from item_log where 1=1 ";
	private Long id;
	@ExcelField(title="商品编码", align=2, sort=20)
	private String vmCode;
	private Long machinesItemId;
	@ExcelField(title="商品名称", align=2, sort=25)
	private String itemName;
	private String barCode;
	private BigDecimal price;
	private BigDecimal costPrice;
	private Date endTime;
	private Date createTime;
	private String operator;
	private BigDecimal oldPrice;
	private BigDecimal oldCostPrice;
	private Date oldEndTime;

	public void setId(Long id){
		this.id=id;
	}
	public Long getId(){
		return id;
	}
	public void setVmCode(String vmCode){
		this.vmCode=vmCode;
	}
	public String getVmCode(){
		return vmCode;
	}
	public void setMachinesItemId(Long machinesItemId){
		this.machinesItemId=machinesItemId;
	}
	public Long getMachinesItemId(){
		return machinesItemId;
	}
	public void setItemName(String itemName){
		this.itemName=itemName;
	}
	public String getItemName(){
		return itemName;
	}
	public void setBarCode(String barCode){
		this.barCode=barCode;
	}
	public String getBarCode(){
		return barCode;
	}
	public void setPrice(BigDecimal price){
		this.price=price;
	}
	public BigDecimal getPrice(){
		return price;
	}
	public void setCostPrice(BigDecimal costPrice){
		this.costPrice=costPrice;
	}
	public BigDecimal getCostPrice(){
		return costPrice;
	}
	public void setEndTime(Date endTime){
		this.endTime=endTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setOperator(String operator){
		this.operator=operator;
	}
	public String getOperator(){
		return operator;
	}
	public void setOldPrice(BigDecimal oldPrice){
		this.oldPrice=oldPrice;
	}
	public BigDecimal getOldPrice(){
		return oldPrice;
	}
	public void setOldCostPrice(BigDecimal oldCostPrice){
		this.oldCostPrice=oldCostPrice;
	}
	public BigDecimal getOldCostPrice(){
		return oldCostPrice;
	}
	public void setOldEndTime(Date oldEndTime){
		this.oldEndTime=oldEndTime;
	}
	public Date getOldEndTime(){
		return oldEndTime;
	}
}

