package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.List;
import java.util.Map;


public interface VendingLineDao {
	/**
	 * 增加线路
	 * @author hebiting
	 * @date 2018年4月9日上午9:52:50
	 * @param line
	 * @return
	 */
	boolean addVendingLine(VendingLineBean line);
	/**
	 * 修改线路
	 * @author hebiting
	 * @date 2018年4月9日上午9:53:08
	 * @param line
	 * @return
	 */
	boolean updateVendingLine(VendingLineBean line);
	/**
	 * 查询所有线路信息
	 * @author hebiting
	 * @date 2018年4月9日上午9:54:33
	 * @return
	 */
	List<VendingLineDto> findAllVendingLine();
	/**
	 * 改变路线状态
	 * @author hebiting
	 * @date 2018年4月9日上午10:58:24
	 * @param id
	 * @return
	 */
	boolean changeLineStatus(Integer id,Integer isUsing);
	/**
	 * 根据条件查询线路信息
	 * @author hebiting
	 * @date 2018年4月13日上午9:19:35
	 * @param vendingLineForm
	 * @return
	 */
	List<VendingLineDto> findLineByForm(VendingLineForm vendingLineForm);
	/**
	 * 根据条件查询线路总数
	 * @author hebiting
	 * @date 2018年4月13日上午9:19:57
	 * @param vendingLineForm
	 * @return
	 */
	Long findLineByFormNum(VendingLineForm vendingLineForm);
	
	/**
	 * 根据地区id 查询该地区下所有的线路
	 * @param Id
	 * @return
	 */
	List<VendingLineBean>  findLine(Integer areaId);
	/**
	 * 判断路线名是否重复
	 * @author hebiting
	 * @date 2018年5月9日下午4:24:27
	 * @param lineName
	 * @return
	 */
	boolean isOnlyOne(String lineName);
	
	/**
	 * 查询线路下的负责人
	 * @param companyIds
	 * @return
	 */
	List<VendingLineBean> findDuty(int lineId);
	
	/**
	 * 删除线路
	 * @param id
	 * @return
	 */
	boolean delete(Object id);
	
	/**
	 * 查询登录用户是否是线路负责人
	 * @param id
	 * @return
	 */
	List<VendingLineBean> findLineBean(Long id);
	
	
	
}
