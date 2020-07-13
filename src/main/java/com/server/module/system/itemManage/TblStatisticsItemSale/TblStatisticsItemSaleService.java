package com.server.module.system.itemManage.TblStatisticsItemSale;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-02 09:36:44
 */
public interface TblStatisticsItemSaleService {

	public ReturnDataUtil listPage(TblStatisticsItemSaleCondition condition);

	public List<TblStatisticsItemSaleBean> list(TblStatisticsItemSaleCondition condition);

	public boolean update(TblStatisticsItemSaleBean entity);

	public boolean del(Object id);

	public TblStatisticsItemSaleBean get(Object id);

	public TblStatisticsItemSaleBean add();
	
	/**
	 * 查询商品上架机器数
	 * @return
	 */
	public List<TblStatisticsItemSaleBean> getItemSaleBean();
}
