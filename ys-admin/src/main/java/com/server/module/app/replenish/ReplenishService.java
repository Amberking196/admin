package com.server.module.app.replenish;

import java.util.List;

public interface ReplenishService {

	//根据条件查询补货信息
	//public List<ReplenishDto> queryReplenish(ReplenishForm form);
	public List<ReplenishDto> queryReplenish(String inCodes,int version);

}
