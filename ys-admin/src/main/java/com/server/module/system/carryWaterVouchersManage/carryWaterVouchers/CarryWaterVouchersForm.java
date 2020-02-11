package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import com.server.module.commonBean.PageAssist;

import lombok.Data;

/**
 * table name: carry_water_vouchers author name: why create time: 2018-11-03
 * 09:02:25
 */
@Data
public class CarryWaterVouchersForm extends PageAssist {

	//名称
	private String name;
	//公司id
	private Long companyId;
	//售货机编号
	private String vmCode;
	//状态 5100	正常销售  5101	暂停销售
	private Long state;
	//提水券类型   0提水券 1提货券
	private Integer type;

}
