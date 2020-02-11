package com.server.module.system.machineManage.machinesAdvertising;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: vending_machines_advertising author name: why create time:
 * 2018-11-02 10:38:21
 */
@Data
@Entity(tableName = "vending_machines_advertising", id = "id", idGenerate = "auto")
public class VendingMachinesAdvertisingBean {

	//主键id
	private Long id;
	//公司id
	private Long companyId;
	//首页图片
	private String homeImg;
	//主题图片
	private String themeImg;
	//首页跳转地址
	private String homeUrl;
	//主题活动一跳转地址
	private String themeUrlOne;
	//主题活动二跳转地址
	private String themeUrlTwo;
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
	private Integer staySecond;//停留时长
	private Integer showStyle;//展示方式  0 全屏 1 半屏
	private Integer height;//图片高度
	private Integer width;//图片宽度
	private Integer advType;//广告类型：0 首页弹窗  1公司弹窗
	//时间类型 1,有效时间 2,固定时间
	private Integer dateType;
	//开始时间
	private String startTime;
	//结束时间
	private String endTime;
	
	@NotField
	private String advTypeLabel;
	public String getAdvTypeLabel(){
		if(advType==null){
			return "";
		}
		if(advType==0){
			return "首页弹窗";
		}
		if(advType==1){
			return "公司弹窗";
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
	@NotField
	private String showStyleLabel;

	public String getShowStyleLabel(){
		if(showStyle==null){
			return "";
		}
		if(showStyle==0){
			return "全屏";
		}
		if(showStyle==1){
			return "半屏";
		}
		return "";
	}
	
	//公司名称
	@NotField
	private String companyName;

}
