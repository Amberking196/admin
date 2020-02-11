package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.util.List;

import com.server.util.ReturnDataUtil;

public interface VendingAreaService {

	/**
	 * 添加区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午4:50:52
	 * @param areaBean
	 * @return
	 */
	ReturnDataUtil addVendingArea(VendingAreaBean areaBean);
	/**
	 * 更新区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午4:51:23
	 * @param areaBean
	 * @return
	 */
	ReturnDataUtil updateVendingArea(VendingAreaBean areaBean);
	/**
	 * 更改区域是否启用？（1：启用，0：禁用）
	 * @author hebiting
	 * @date 2018年4月9日下午4:52:42
	 * @param areaId
	 * @param isUsing
	 * @return
	 */
	ReturnDataUtil changeAreaStatus(Integer areaId,Integer isUsing);
	/**
	 * 查询所有区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:13:45
	 * @return
	 */
	ReturnDataUtil findAllVendingArea();
	/**
	 * 根据pid查询子区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:25:43
	 * @return
	 */
	ReturnDataUtil findAreaByPid(Integer pid);
	/**
	 * 根据pid深度查询所有子区域
	 * @author hebiting
	 * @date 2018年4月9日下午5:47:28
	 * @param pid
	 * @return
	 */
	List<VendingAreaDto> treeArea(Integer pid);
	/**
	 * 根据子区域id，查询子区域及所有父区域
	 * @author hebiting
	 * @date 2018年4月11日上午11:35:35
	 * @param sid
	 * @return
	 */
	VendingAreaDto findFaAreaAndSonArea(Integer sid);
	
	/**
	 * 根据条件查询区域信息
	 * @author hebiting
	 * @date 2018年4月9日下午5:25:43
	 * @return
	 */
	ReturnDataUtil findAreaByForm(VendingAreaForm areaForm);
	
	/**
	 * 查询区域名是否唯一
	 * @author hebiting
	 * @date 2018年4月24日下午4:27:49
	 * @return
	 */
	boolean areaNameIsOnlyOne(String areaName);
	
	/**
	 * 删除区域
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);
}
