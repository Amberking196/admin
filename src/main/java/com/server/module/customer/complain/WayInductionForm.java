package com.server.module.customer.complain;

import lombok.Data;

@Data
public class WayInductionForm {
	/**
	 * 用户手机
	 */
	private String phone ;
	/**
	 * 1：禁止向该机器用户发送未关门短信;其他无
	 */
	private String inductionBad;
}
