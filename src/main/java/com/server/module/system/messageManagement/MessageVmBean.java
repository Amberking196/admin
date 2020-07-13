package com.server.module.system.messageManagement;



import com.server.common.utils.excel.annotation.ExcelField;

import lombok.Data;
/**
 * 留言售货机列表查询结果Bean
 * @author 26920
 *
 */
@Data
public class MessageVmBean {
	@ExcelField(title = "售货机编号")
	private String code;
	
	@ExcelField(title = "所属机构")
	private String companyName;

	@ExcelField(title = "位置")
	private String locatoinName;

	//公司标识
	private Integer companyId;
	
	
	
	
}
