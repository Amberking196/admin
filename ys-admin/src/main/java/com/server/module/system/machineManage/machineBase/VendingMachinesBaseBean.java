package com.server.module.system.machineManage.machineBase;

import com.server.common.persistence.Entity;
import com.server.common.persistence.NotField;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * table name: vending_machines_base 
 * author name: yjr 
 * create time: 2018-03-29 17:51:22
 */
@Data
@Entity(tableName = "vending_machines_base", id = "id", idGenerate = "auto")
public class VendingMachinesBaseBean {

	//@JsonIgnore
	//public String tableName = "vending_machines_base";
	//@JsonIgnore
	//public String selectSql = "select * from vending_machines_base where 1=1 ";
	//@JsonIgnore
	//public String selectSql1 = "select id,machinesTypeId,aisleConfiguration,factoryNumber,mainProgramVersion,ipcNumber,liftingGearNumber,electricCabinetNumber,caseNumber,doorNumber,airCompressorNumber,keyStr,remark from vending_machines_base where 1=1 ";
	private Long id;
	private Integer machinesTypeId;
	private String aisleConfiguration;
	private String factoryNumber;
	private String mainProgramVersion;
	private String ipcNumber;
	private String liftingGearNumber;
	private String electricCabinetNumber;
	private String caseNumber;
	private String doorNumber;
	private String airCompressorNumber;
	private String keyStr;
	private String remark;
	private Integer canOnlineUpdate;
	private String simNumber;//物联卡卡号
	private Date simExpireDate;//卡过期时间
	private String cardSupplier;
	
	@NotField
	private String vmCode;
	@NotField
	private String machinesTypeName;//售货机类型名称
	@NotField
	private Integer companyId;//公司id
	@NotField
	private Integer wayCount; //机器货道数
	@NotField
	private Integer isDisabled; //是否失效  0未实效   1已失效   
}
