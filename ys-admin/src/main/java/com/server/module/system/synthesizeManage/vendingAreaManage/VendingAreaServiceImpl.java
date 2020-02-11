package com.server.module.system.synthesizeManage.vendingAreaManage;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

@Service
public class VendingAreaServiceImpl implements VendingAreaService{

	public static Logger log=LogManager.getLogger(VendingAreaServiceImpl.class);
	@Autowired
	VendingAreaDao vendingAreaDao;

	@Override
	public ReturnDataUtil addVendingArea(VendingAreaBean areaBean) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(areaBean!=null && vendingAreaDao.addVendingArea(areaBean)){
			returnData.setStatus(1);
			returnData.setMessage("添加区域成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("添加区域失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil updateVendingArea(VendingAreaBean areaBean) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(areaBean!=null && vendingAreaDao.updateVendingArea(areaBean)){
			returnData.setStatus(1);
			returnData.setMessage("更新区域成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("更新区域失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil changeAreaStatus(Integer areaId, Integer isUsing) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(areaId!=null && isUsing!=null && vendingAreaDao.changeAreaStatus(areaId,isUsing)){
			returnData.setStatus(1);
			returnData.setMessage("更新区域状态成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("更新区域状态失败");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil findAllVendingArea() {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingAreaDto> areaList = vendingAreaDao.findAllVendingArea();
		if(areaList!=null && areaList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询区域成功");
			returnData.setReturnObject(areaList);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}

	@Override
	public ReturnDataUtil findAreaByPid(Integer pid) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingAreaDto> areaList = vendingAreaDao.findAreaByPid(pid);
		if(areaList!=null && areaList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询子区域成功");
			returnData.setReturnObject(areaList);
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}
	@Override
	public List<VendingAreaDto> treeArea(Integer pid){
		List<VendingAreaDto> areaList = vendingAreaDao.findAreaByPid(pid);
		if(areaList!=null && areaList.size()>0){
			for (VendingAreaDto vendingAreaDto : areaList) {
				List<VendingAreaDto> sonList = treeArea(vendingAreaDto.getId());
				vendingAreaDto.setSonArea(sonList);
			}
		}
		return areaList;
	}
	
	public VendingAreaDto findFaAreaAndSonArea(Integer sid){
		VendingAreaDto areaDto = vendingAreaDao.findAreaById(sid);
		if(areaDto!=null && areaDto.getPid()!=null && areaDto.getPid()>0){
			VendingAreaDto faAreaDto = findFaAreaAndSonArea(areaDto.getPid());
			areaDto.setFaArea(faAreaDto);
		}
		return areaDto;
	}

	@Override
	public ReturnDataUtil findAreaByForm(VendingAreaForm areaForm) {
		ReturnDataUtil returnData = new ReturnDataUtil();
		List<VendingAreaDto> areaList = vendingAreaDao.findAreaByForm(areaForm);
		Long total = vendingAreaDao.findAreaByFormNum(areaForm);
		if(areaList!=null && areaList.size()>0){
			returnData.setStatus(1);
			returnData.setMessage("查询成功");
			returnData.setReturnObject(areaList);
			returnData.setTotal(total);
			returnData.setCurrentPage(areaForm.getCurrentPage());
			returnData.setTotalPage(returnData.getTotalPage());
		}else{
			returnData.setStatus(0);
			returnData.setMessage("查询无数据");
		}
		return returnData;
	}

	@Override
	public boolean areaNameIsOnlyOne(String areaName) {
		List<String> areaList = vendingAreaDao.findAllAreaName();
		for (String name : areaList) {
			if(areaName.equals(name)){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean delete(Object id) {
		log.info("<VendingAreaServiceImpl>----<deleete>----start");
		boolean delete = vendingAreaDao.delete(id);
		log.info("<VendingAreaServiceImpl>----<deleete>----end");
		return delete;
	}


}
