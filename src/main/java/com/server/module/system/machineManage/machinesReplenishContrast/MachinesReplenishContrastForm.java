package com.server.module.system.machineManage.machinesReplenishContrast;

import com.server.module.commonBean.PageAssist;

import lombok.Data;


/**
 * 
 * @author why
 * @date: 2019年5月14日 下午5:48:20
 */
@Data
public class MachinesReplenishContrastForm extends PageAssist {

	private String vmCode;
	private Integer wayNumber;
	private Integer companyId;
}
