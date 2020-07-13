package com.server.module.system.machineManage.machineBase;

import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;

import java.util.Date;

/**
 * 
 * author name: why
 * create time: 2018-04-13 09:02:10
 */
@Data
public class VendingMachinesBaseDto {

	@ExcelField(title = "售货机基础ID")
	private Long id;
	@ExcelField(title = "售货机类型")	
	private String machinesTypeName;
	@ExcelField(title = "货道配置")	
	private String aisleConfiguration;
	@ExcelField(title = "出厂编号")	
	private String factoryNumber;
	@ExcelField(title = "物联卡卡号")
	private String simNumber;//物联卡卡号
	@ExcelField(title = "卡过期时间")
	private Date simExpireDate;//卡过期时间
	@ExcelField(title = "卡供应商")
	private String cardSupplier;//卡过期时间
	
	
/*	@ExcelField(title = "主控程序版本")	
	private String mainProgramVersion;
	@ExcelField(title = "工控一体机编号")
	private String	ipcNumber;
	@ExcelField(title = "提升机构编号")
	private String liftingGearNumber;
	@ExcelField(title = "电控箱编号")
	private String electricCabinetNumber;
	@ExcelField(title = "箱体编号")
	private String caseNumber;
	@ExcelField(title = "门体编号")	
	private String doorNumber;
	@ExcelField(title = "空压机编号")
	private String airCompressorNumber;
	@ExcelField(title = "钥匙编号")	
	private String keyStr;*/
	


}
