package com.server.module.system.synthesizeManage.vendingLineManage;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

@Service
public class VendingLineServiceImpl implements VendingLineService {

	public static Logger log=LogManager.getFormatterLogger(VendingLineServiceImpl.class); 
	@Autowired
	VendingLineDao vendingLineDao;

	@Override
	public ReturnDataUtil addVendingLine(VendingLineBean line) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (line != null && vendingLineDao.addVendingLine(line)) {
			returnData.setStatus(1);
			returnData.setMessage("添加路线成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("添加路线失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil updateVendingLine(VendingLineBean line) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (line != null && vendingLineDao.updateVendingLine(line)) {
			returnData.setStatus(1);
			returnData.setMessage("更新路线成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("更新路线失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil findAllVendingLine() {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingLineDto> vendingList = vendingLineDao.findAllVendingLine();
		if (vendingList != null && vendingList.size() > 0) {
			returnData.setStatus(1);
			returnData.setMessage("查询路线成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil changeLineStatus(Integer id, Integer isUsing) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if (id != null && isUsing != null && vendingLineDao.changeLineStatus(id, isUsing)) {
			returnData.setStatus(1);
			returnData.setMessage("更新路线状态成功");
		} else {
			returnData.setStatus(0);
			returnData.setMessage("更新路线状态失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil findLineByForm(VendingLineForm vendingLineForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingLineDto> lineList = vendingLineDao.findLineByForm(vendingLineForm);
		Long total = vendingLineDao.findLineByFormNum(vendingLineForm);
		if(lineList!=null && lineList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(lineList);
			returnData.setTotal(total);
			returnData.setTotalPage(returnData.getTotalPage());
			returnData.setCurrentPage(vendingLineForm.getCurrentPage());
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}
	
	/**
	 * 根据地区id 查询该地区下所有的线路
	 */
	@Override
	public List<VendingLineBean>  findLine(Integer areaId){
		
		return vendingLineDao.findLine(areaId);
	}

	@Override
	public boolean isOnlyOne(String lineName) {
		
		return vendingLineDao.isOnlyOne(lineName);
	}
	
	/**
	 * 查询线路下的负责人
	 */
	@Override
	public  List<VendingLineBean> findDuty(int lineId){
		return vendingLineDao.findDuty(lineId);
	}

	/**
	 * 删除线路
	 */
	@Override
	public boolean delete(Object id) {
		log.info("<VendingLineServiceImpl>-----<delete>----start");
		boolean delete = vendingLineDao.delete(id);
		log.info("<VendingLineServiceImpl>-----<delete>----start");
		return delete;
	}
	
	/**
	 * 查询登录用户是否是线路负责人
	 */
	public List<VendingLineBean> findLineBean(Long id){
		log.info("<VendingLineServiceImpl>-----<findLineBean>----start");
		List<VendingLineBean> list = vendingLineDao.findLineBean(id);
		log.info("<VendingLineServiceImpl>-----<findLineBean>----end");
		return list;
	}
}
