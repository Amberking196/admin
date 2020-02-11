package com.server.module.system.itemManage.TblStatisticsItemSale;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-02 09:36:44
 */
public interface TblStatisticsItemSaleDao {

	public ReturnDataUtil listPage(TblStatisticsItemSaleCondition condition);

	/**
	 * 查询商品上架机器数
	 * @return
	 */
	public List<TblStatisticsItemSaleBean> getItemSaleBean();

	
	
	public boolean update(TblStatisticsItemSaleBean entity);

	public boolean delete(Object id);

	public TblStatisticsItemSaleBean get(Object id);

	public TblStatisticsItemSaleBean insert(TblStatisticsItemSaleBean entity);
}
