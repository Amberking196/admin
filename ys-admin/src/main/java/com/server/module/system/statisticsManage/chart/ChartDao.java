package com.server.module.system.statisticsManage.chart;

import java.util.List;
import java.util.Map;

public interface ChartDao {
	public List<DateCountVo> listMachinesInfo(ChartForm form);
	public List<DateCountVo> listUserInfo(ChartForm form);
	/**
	 * 订单的曲线图
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listOrderInfo(ChartForm form);
	/**
	 * 24小时用户订单曲线图
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listOrderByHourInfo(ChartForm form);

	public List<SaleGoodsDateCountVo> listGoodsSaleInfo(ChartForm form);

	public Map<Integer,String> listGoods();
	/**
	 * 商品销售量曲线
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listGoodsSaleCount(ChartForm form);
	/**
	 * 商品销售额曲线图
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listCostSalePrice(ChartForm form);
	/**
	 * 顾客数量曲线图
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listCustomerNumber(ChartForm form);
	/**
	 * 有销量的机器集合
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listMachineCount(ChartForm form);
	/**
	 * 根据条件查询符合条件的（起始条件）
	 * @param form
	 * @return
	 */
	public Integer MachineCountByFrom(ChartForm form);
	/**
	 * 根据条件查询用户数量（起始时间）
	 * @param form
	 * @return
	 */
	public Integer UserCountByFrom(ChartForm form);
	/**
	 * 设备总数叠加查询
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listMachinesInfoByForm(ChartForm form);

	/**
	 * 机器上线时间
	 * @param form
	 * @return
	 */
	public List<DateCountVo> listMachinesStartUsingByForm(ChartForm form);
}
