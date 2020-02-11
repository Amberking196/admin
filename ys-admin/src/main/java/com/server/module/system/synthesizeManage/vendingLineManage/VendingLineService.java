package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.List;
import java.util.Map;

import com.server.util.ReturnDataUtil;

public interface VendingLineService {

	/**
	 * 增加线路
	 * @author hebiting
	 * @date 2018年4月9日上午9:52:50
	 * @param line
	 * @return
	 */
	ReturnDataUtil addVendingLine(VendingLineBean line);
	/**
	 * 修改线路
	 * @author hebiting
	 * @date 2018年4月9日上午9:53:08
	 * @param line
	 * @return
	 */
	ReturnDataUtil updateVendingLine(VendingLineBean line);
	/**
	 * 查询所有线路信息
	 * @author hebiting
	 * @date 2018年4月9日上午9:54:33
	 * @return
	 */
	ReturnDataUtil findAllVendingLine();
	/**
	 * 改变路线状态
	 * @author hebiting
	 * @date 2018年4月9日上午10:58:24
	 * @param id
	 * @return
	 */
	ReturnDataUtil changeLineStatus(Integer id,Integer isUsing);
	/**
	 * 根据条件查询线路信息
	 * @author hebiting
	 * @date 2018年4月13日上午9:19:35
	 * @param vendingLineForm
	 * @return
	 */
	ReturnDataUtil findLineByForm(VendingLineForm vendingLineForm);
	
	/**
	 * 根据地区id 查询该地区下所有的线路
	 * @param Id
	 * @return
	 */
	List<VendingLineBean>  findLine(Integer areaId);
	/**
	 * 线路名是否唯一
	 * @author hebiting
	 * @date 2018年5月9日下午4:27:42
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
	public boolean delete(Object id);
	
	/**
	 * 查询登录用户是否是线路负责人
	 * @param id
	 * @return
	 */
	List<VendingLineBean> findLineBean(Long id);
}
