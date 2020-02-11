package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.util.List;

public interface VendingAreaDao {

	/**
	 * 添加区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午4:50:52
	 * @param areaBean
	 * @return
	 */
	boolean addVendingArea(VendingAreaBean areaBean);
	/**
	 * 更新区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午4:51:23
	 * @param areaBean
	 * @return
	 */
	boolean updateVendingArea(VendingAreaBean areaBean);
	/**
	 * 更改区域是否启用？（1：启用，0：禁用）
	 * @author hebiting
	 * @date 2018年4月9日下午4:52:42
	 * @param areaId
	 * @param isUsing
	 * @return
	 */
	boolean changeAreaStatus(Integer areaId,Integer isUsing);
	/**
	 * 查询所有区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:13:45
	 * @return
	 */
	List<VendingAreaDto> findAllVendingArea();
	/**
	 * 根据pid查询子区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:25:43
	 * @return
	 */
	List<VendingAreaDto> findAreaByPid(Integer pid);
	/**
	 * 根据id查询区域信息
	 * @author hebiting
	 * @date 2018年4月11日上午11:29:38
	 * @param id
	 * @return
	 */
	VendingAreaDto findAreaById(Integer id);
	
	/**
	 * 根据条件查询区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:25:43
	 * @return
	 */
	List<VendingAreaDto> findAreaByForm(VendingAreaForm areaForm);
	/**
	 * 根据条件查询区域信息总数
	 * @author hebiting
	 * @date 2018年4月9日下午5:25:43
	 * @return
	 */
	Long findAreaByFormNum(VendingAreaForm areaForm);
	/**
	 * 查询companyId公司下的所有的区域名
	 * @author hebiting
	 * @date 2018年4月24日下午4:27:49
	 * @return
	 */
	List<String> findAllAreaName();
	
	/**
	 * 删除区域
	 * @param id
	 * @return
	 */
	boolean delete(Object id);
	
}
