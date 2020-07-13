package com.server.module.system.machineManage.machineList;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;
import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;
import lombok.Setter;
@Data
public class MachinesInfoAndBaseDto {
	
	@ExcelField(title = "售货机编号")
	private String code;
	
	@ExcelField(title = "所属机构")
	private String companyName;
	@ExcelField(title = "机器类型")
	private String machinesTypeName;
	@ExcelField(title = "售货机基础id")
	private Long machinesBaseId;
	@ExcelField(title = "出厂编号")
	private String factoryNumber;
	@ExcelField(title = "负责人",align=2)
	private String linkman;
	@ExcelField(title = "初始化日期",align=2)
	private Date locatoinDate;
	@ExcelField(title = "状态",align=2)
	private String stateName;
	@ExcelField(title = "位置")
	private String locatoinName;

	//公司标识
	private Integer companyId;
	//经度
	private Double lon;
	//纬度
	private Double lat;
	//禁用货道号
	private String banWayNumber;
	//售货机名称
	private String name;
	//销售商品类型
	private String itemType;
	//责任人ID
	private Long respManId;
	//错误警告
	private Integer errorWarn;
	//是否在H5显示
	private Integer isShowH5;
	//线路iD
	private Integer lineId;
	//状态
	private Integer state;
	//二维码
	private String qrCode;
	//线路名
	private String lineName;
	//标识 自增
	private Long id;
	//售货机类型
	private Integer machinesTypeId;
	//货道配置
	private String aisleConfiguration;
	
	@ExcelField(title = "版本号")
	private String mainProgramVersion;
	//工控一体机编号
	private String ipcNumber;
	//提升机构编号
	private String liftingGearNumber;
	//电控箱编号
	private String electricCabinetNumber;
	//箱体编号
	private String caseNumber;
	//门体编号
	private String doorNumber;
	//空压机编号
	private String airCompressorNumber;
	//钥匙编号
	private String keyStr;
	//备注信息
	private String remark;
	//区域id
	private Integer areaId;
	//区域名称
	private String areaName;
	//是否可以入驻阿里
	private boolean canEnter;
	//正式运行天数
	@ExcelField(title = "运行天数")
	private String startUsingDay;

	private Integer district;
	private String districtName;

	//机器的类型，1为称重机，2 为视觉机
	private Integer machineType;

	// 将备注也查询出来
	private String untieRemark;
	//售货机创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	//售货机正式启用时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startUsingTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date editTime;

	private Integer machineVersion;
	//补货公司id
	@Setter
	private Integer replenishCompanyId;
	//补货公司名称
	private String replenishCompanyName;
	@NotField
	public Double distance;
	//是否可在线升级 1：可在线升级 0：不能
	private Integer canOnlineUpdate;
	@NotField
	private String card;
	//机器主板类型
	@NotField
	private String mainboardType;
	public Integer getReplenishCompanyId() {
		if(replenishCompanyId==null || replenishCompanyId==0) {
			this.replenishCompanyId=this.companyId;
		}
		return this.replenishCompanyId;
	}

}
