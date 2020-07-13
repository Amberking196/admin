package com.server.module.system.machineManage.machinesPic;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: vending_machines_picertising author name: hjc create time:
 * 2019-3-14 10:38:21
 */
@Data
@Entity(tableName = "vending_machines_pic", id = "id", idGenerate = "auto")
public class VendingMachinesPicBean {

	//主键id
	private Long id;
	//公司id
	private Long companyId;
	//首页图片
	private String homeImg;
	//创建时间
	private Date createTime;
	//创建用户
	private Long createUser;
	//更新时间
	private Date updateTime;
	//更新用户
	private Long updateUser;
	//是否删除 0 未删除  1 已删除
	private Integer deleteFlag;

	private Integer state;//0 启用 1 停用
	private String  name;//图片描述
	private Integer height;//图片高度
	private Integer width;//图片宽度
	private Integer target;
	private Long basicItemId;
	private Integer picType;//广告类型：0 第二桶半价 1买一赠一
	@NotField
	private String picTypeLabel;
	@NotField
	private String itemName;
	public String getpicTypeLabel(){
		if(picType==null){
			return "";
		}
		if(picType==0){
			return "第二桶半价";
		}
		if(picType==1){
			return "买一赠一";
		}
		return "";
	}
	@NotField
	private String stateLabel;
	public String getStateLabel(){
		if(state!=null){
			if(state==0){
				return "启用";
			}else{
				return "停用";
			}
		}
		return "";
	}
	
	
	//公司名称
	@NotField
	private String companyName;

}
