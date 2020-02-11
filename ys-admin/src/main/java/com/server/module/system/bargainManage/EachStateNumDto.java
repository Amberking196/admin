package com.server.module.system.bargainManage;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class EachStateNumDto {
	private Integer num;
	private Integer state;
	private String stateLabel;
	public String getStateLabel() {
		if (state == null) {
			stateLabel = "";
		}
		else if (state == 0) {
			stateLabel = "失败";
		}
		else if (state == 1) {
			stateLabel = "成功";
		}else if (state == 2) {
			stateLabel = "砍价中";
		}
		return stateLabel;
	}
}
