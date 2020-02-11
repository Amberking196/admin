package com.server.module.system.machineManage.machinesWayItem;

import com.server.common.utils.excel.annotation.ExcelField;
import com.server.module.system.replenishManage.machineHistory.VendingMachineHistoryBean;
import com.server.module.system.replenishManage.machineHistory.VendingMachineHistoryService;
import com.server.redis.RedisClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.server.module.system.itemManage.itemBasic.ItemBasicBean;
import com.server.module.system.itemManage.itemBasic.ItemBasicService;
import com.server.module.system.machineManage.distorymachine.distorymachineBean;
import com.server.module.system.machineManage.distorymachine.distorymachineDaoImpl;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseService;
import com.server.module.system.machineManage.machineList.MachinesInfoAndBaseDto;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayBean;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayDao;
import com.server.util.ReturnDataUtil;

import jersey.repackaged.com.google.common.collect.Maps;

/**
 * author name: yjr create time: 2018-08-31 14:03:10
 */
@Service
public class VendingMachinesWayItemServiceImpl implements VendingMachinesWayItemService {

	private static Log log = LogFactory.getLog(VendingMachinesWayItemServiceImpl.class);
	@Autowired
	private VendingMachinesWayItemDao vendingMachinesWayItemDaoImpl;
	@Autowired
	private ItemBasicService itemBasicService;
	@Autowired
	private VendingMachinesWayDao vendingMachinesWayDao;
	@Autowired
	private distorymachineDaoImpl distorymachineDao;
	@Autowired
	private MachinesClient machinesClient;
	@Autowired
	private VendingMachineHistoryService vendingMachineHistoryService;
	@Autowired
	private VendingMachinesBaseService vendingMachinesBaseService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;
	
	public ReturnDataUtil listPage(VendingMachinesWayItemCondition condition) {
		return vendingMachinesWayItemDaoImpl.listPage(condition);
	}

	public ReturnDataUtil add(VendingMachinesWayItemBean entity) {
		ReturnDataUtil data = new ReturnDataUtil();

		Long itemId = entity.getBasicItemId();
		String vmCode = entity.getVmCode();

		Integer wayNumber = entity.getWayNumber().intValue();

		ItemBasicBean item = itemBasicService.getItemBasic(itemId);
		entity.setNum(0l);
		entity.setWeight(Integer.parseInt(item.getStandard()));
		List<VendingMachinesWayBean> listWay = vendingMachinesWayDao.listWay(vmCode, wayNumber);
		VendingMachinesWayBean wayBean = null;
		if (listWay.size() > 0) {
			wayBean = listWay.get(0);
			entity.setMachineWayId(wayBean.getId());
		} else {
			// 数据有误
			data.setMessage("不存在该货道");
			data.setStatus(0);
			return data;

		}
		if (wayBean.getState().intValue() != 30001) {
			data.setMessage("货道处于不正常状态，不可编辑");
			data.setStatus(0);
			return data;
		}
		List<VendingMachinesWayItemBean> list = vendingMachinesWayItemDaoImpl.list(wayBean.getId());
		// 检查重量是否重复

		for (VendingMachinesWayItemBean obj : list) {
			System.out.println("-----------" + obj.getWeight() + "    " + entity.getWeight());
			if (obj.getWeight().intValue() == entity.getWeight().intValue()) {
				data.setMessage("该货道已经有" + entity.getWeight() + " 克的商品，不能重复添加");
				data.setStatus(0);
				return data;
			}
		}

		VendingMachinesWayItemBean insert = vendingMachinesWayItemDaoImpl.insert(entity);

		//添加日志销售补货
		VendingMachineHistoryBean historyBean = new VendingMachineHistoryBean();
		historyBean.setVmCode(insert.getVmCode());
		historyBean.setWayNumber(insert.getWayNumber());
		historyBean.setItemId(insert.getBasicItemId());
		historyBean.setNum(insert.getNum());
		historyBean.setRecordTime(new Date());
		historyBean.setCreateTime(new Date());
		vendingMachineHistoryService.add(historyBean);
       //添加日志销售补货
		if(insert.getId()!=null){
			//重启机器
			try {
				//redisClient.set("", "");
				
				 MachinesInfoAndBaseDto machinesInfoAndBaseDto=vendingMachinesInfoService.getMachinesInfoAndBase(insert.getVmCode());
				 String standard=vendingMachinesBaseService.findItemWeight(machinesInfoAndBaseDto.getFactoryNumber());
				 redisClient.set("NEWITEM-"+machinesInfoAndBaseDto.getFactoryNumber(), standard,60);
				 machinesClient.restartMachines(vmCode);
				//设置标识
			}catch (Exception e){
				e.printStackTrace();
				log.error("重启异常");
			}
		}
		sortItem(wayBean.getId());

		return data;
	}

	private void sortItem(Long machineWayId) {
		List<VendingMachinesWayItemBean> list = vendingMachinesWayItemDaoImpl.list(machineWayId);
		int[] weight = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			VendingMachinesWayItemBean obj = list.get(i);
			weight[i] = obj.getWeight();
		}
		Arrays.sort(weight);

		for (int i = 0; i < weight.length; i++) {

			for (VendingMachinesWayItemBean obj : list) {

				if (weight[i] == obj.getWeight()) {
					obj.setOrderNumber(i + 1);
					vendingMachinesWayItemDaoImpl.update(obj);
					break;
				}
			}
		}

	}

	public ReturnDataUtil update(WayItemForUpdateVo vo) {
		ReturnDataUtil data = new ReturnDataUtil();
		VendingMachinesWayItemBean entity = vendingMachinesWayItemDaoImpl.get(vo.getId());

		/*List<VendingMachinesWayBean> listWay = vendingMachinesWayDao.listWay(entity.getVmCode(),
				entity.getWayNumber().intValue());
		if (listWay.size() > 0) {
			VendingMachinesWayBean wayBean = listWay.get(0);
			if (wayBean.getState().intValue() != 30001) {
				data.setMessage("货道处于不正常状态，不可编辑");
				data.setStatus(0);
				return data;
			}
		}*/
		entity.setFullNum(vo.getFullNum());
		entity.setPrice(vo.getPrice());
		entity.setPromotionPrice(vo.getPromotionPrice());
		entity.setPicId(vo.getPicId());
		entity.setNum(vo.getNum());
		boolean flag = vendingMachinesWayItemDaoImpl.update(entity);
//		if(flag){
//			//重启机器
//			
//			machinesClient.restartMachines(entity.getVmCode());
//		}
		List<VendingMachinesWayItemBean> list=vendingMachinesWayItemDaoImpl.list(entity.getMachineWayId());

		if(list.size()==1) {//自动启用
			VendingMachinesWayBean bean = vendingMachinesWayDao.get(entity.getMachineWayId());
			bean.setState(30001);
			bean.setUpdateTime(new Date());
			boolean update = vendingMachinesWayDao.update(bean);
		}
		data.setReturnObject(flag);
		return data;
	}

	public ReturnDataUtil del(Long id) {
		ReturnDataUtil data = new ReturnDataUtil();

		VendingMachinesWayItemBean entity = vendingMachinesWayItemDaoImpl.get(id);
//		List<VendingMachinesWayBean> listWay = vendingMachinesWayDao.listWay(entity.getVmCode(),
//				entity.getWayNumber().intValue());
		/*if (listWay.size() > 0) {
			VendingMachinesWayBean wayBean = listWay.get(0);
			if (wayBean.getState().intValue() != 30001) {
				data.setMessage("货道处于不正常状态，不可删除");
				data.setStatus(0);
				return data;
			}
		}*/
		if(entity.getNum()>0){
			data.setMessage("该商品未清空，不可删除");
			data.setStatus(0);
			return data;
		}
		Long wayId = entity.getMachineWayId();
		//VendingMachinesWayItemBean temp = new VendingMachinesWayItemBean();
		//temp.setId(id);
		//记录销售补货日志
		vendingMachineHistoryService.updateBean(entity.getVmCode(),entity.getWayNumber().intValue(),entity.getBasicItemId().intValue(),entity.getNum().intValue());
		boolean flag = vendingMachinesWayItemDaoImpl.deleteSQL(id);
		if(flag){
			//重启机器
			data.setMessage(machinesClient.restartMachines(entity.getVmCode()));
		}
		this.sortItem(wayId);
		data.setReturnObject(flag);
		
		List<VendingMachinesWayItemBean> list=vendingMachinesWayItemDaoImpl.list(entity.getMachineWayId());
		if(list.size()<1) {//自动启用
			VendingMachinesWayBean bean = vendingMachinesWayDao.get(wayId);
			bean.setState(30001);
			bean.setUpdateTime(new Date());
			boolean update = vendingMachinesWayDao.update(bean);
		}

		return data;
	}

	public List<VendingMachinesWayItemBean> list(VendingMachinesWayItemCondition condition) {
		return null;
	}

	public VendingMachinesWayItemBean get(Object id) {
		return vendingMachinesWayItemDaoImpl.get(id);
	}
	 @SuppressWarnings("unchecked")
	 public ReturnDataUtil listPageForReplenish(VendingMachinesWayItemCondition condition){
		 ReturnDataUtil data= vendingMachinesWayItemDaoImpl.listPageForReplenish(condition);
		
		List<ReplenishVo> list =(List<ReplenishVo>)data.getReturnObject();
		 
		List<String> codes=Lists.newArrayList();
		 
		 for(ReplenishVo vo : list){
			 codes.add(vo.getVmCode());
		 }
		 
		 List<ReplenishItemVo> itemList=vendingMachinesWayItemDaoImpl.listForReplenish(codes,condition.getType());
		 List<distorymachineBean> listDistory=distorymachineDao.listDistoryBean(codes);
		 
		 int length=0;
		 for(int i=0;i<list.size();i++){
			 ReplenishVo vo=list.get(i);
			 boolean normal=true;
			 for(int k=0;k<listDistory.size();++k){
				 distorymachineBean bean=listDistory.get(k);
				 if(bean.getCode().equals(vo.getVmCode())){
					 vo.setState(bean.getInfo());
					 normal=false;
					 break;
				 }
			 }
			 if(normal)
			 vo.setState("正常");
			 
			 for(int j=0;j<itemList.size();j++){
				 ReplenishItemVo item=itemList.get(j);
				 //log.info("--"+item.getWayNumber()+"++"+item.getVmCode());
				 //log.info("--"+vo.getVmCode()+"++"+item.getVmCode());

				 if(vo.getVmCode().equals(item.getVmCode())){
					 //log.info("加入列表");
					 vo.getItemList().add(item);
				 }
			 }
			 if(vo.getItemList().size()>length){
				 length=vo.getItemList().size();
				 //log.info("++++++++"+length);
			 }
			 
		 }
		 
		 data.setMessage(length+"");
		 return data;
	 }

    @Override
    public VendingMachinesWayItemBean findItemBean( String vmCode ) {
        return vendingMachinesWayItemDaoImpl.findItemBean(vmCode);
    }

    //修改视觉机器的最大容量
    @Override
    public ReturnDataUtil edit( VendingMachinesWayItemBean bean ) {
        return vendingMachinesWayItemDaoImpl.edit(bean);
    }

    public static void main(String[] args) {
		
	}

}
