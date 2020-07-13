package com.server.module.system.statisticsManage.purchaseItemStatistics;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: hjc
 * create time: 2018-08-24 11:02:40
 */
public interface PurchaseItemStatisticsDao {

    public ReturnDataUtil listPage(PurchaseItemStatisticsCondition condition);

    public List<PurchaseItemStatisticsBean> list(PurchaseItemStatisticsCondition condition);

    public boolean update(PurchaseItemStatisticsBean entity);

    public boolean delete(Object id);

    public PurchaseItemStatisticsBean get(Long id);

    public PurchaseItemStatisticsBean insert(PurchaseItemStatisticsBean entity);

	public PurchaseItemStatisticsBean getBeanByItemId(Long itemId);
}

