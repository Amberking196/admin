package com.server.module.system.replenishManage.machineHistory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-11-06 14:32:26
 */
@Service
public class VendingMachineHistoryServiceImpl implements VendingMachineHistoryService {

    private static Log log = LogFactory.getLog(VendingMachineHistoryServiceImpl.class);
    @Autowired
    private VendingMachineHistoryDao vendingMachineHistoryDaoImpl;

    /**
     * 机器商品日志列表
     * @param condition
     * @return
     */
    public ReturnDataUtil listPage(VendingMachineHistoryCondition condition) {
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	if(condition.getType()==null || condition.getType()==1) {
    		returnDataUtil =vendingMachineHistoryDaoImpl.listPage(condition);
    		//returnDataUtil =vendingMachineHistoryDaoImpl.listPageGroupByItemId(condition);
//    		List<VendingMachineHistoryBean> list1=vendingMachineHistoryDaoImpl.saleNumNowGroupByVmCode(condition);
//    		List<VendingMachineHistoryBean> list2=vendingMachineHistoryDaoImpl.replenishNumNowGroupByVmCode(condition);
//    		List<VendingMachineHistoryBean> list=(List<VendingMachineHistoryBean>) returnDataUtil.getReturnObject();
//    		for(VendingMachineHistoryBean v:list) {
//    			for(VendingMachineHistoryBean v1:list1) {
//    				if(v.getVmCode().equals(v1.getVmCode())) {
//    					v.setSaleNum(v1.getNum());
//    					break;
//    				}
//    			}
//    			for(VendingMachineHistoryBean v2:list2) {
//    				if(v.getVmCode().equals(v2.getVmCode())) {
//    					v.setReplenishNum(v2.getNum());
//    					break;
//    				}
//    			}
//    			//补-售 -期末+起初
//    			v.setBalanceNum(v.getReplenishNum()-v.getSaleNum()-v.getEndNum()+v.getNum());
//    		}
    	}else {
    		//childList76
    		//取实时补货以及销售数据
    		returnDataUtil =vendingMachineHistoryDaoImpl.listPageGroupByItemId(condition);
    		List<VendingMachineHistoryBean> list1=vendingMachineHistoryDaoImpl.saleNumNowGroupByItem(condition);
    		List<VendingMachineHistoryBean> list2=vendingMachineHistoryDaoImpl.replenishNumNowGroupByItem(condition);
    		List<VendingMachineHistoryBean> list=(List<VendingMachineHistoryBean>) returnDataUtil.getReturnObject();
    		for(VendingMachineHistoryBean v:list) {
    			for(VendingMachineHistoryBean v1:list1) {
    				if(v.getItemId().equals(v1.getItemId())) {
    					v.setSaleNum(v1.getNum());
    					break;
    				}
    			}
    			for(VendingMachineHistoryBean v2:list2) {
    				if(v.getItemId().equals(v2.getItemId())) {
    					v.setReplenishNum(v2.getNum());
    					break;
    				}
    			}
    			//补-售 -期末+起初
    			v.setBalanceNum(v.getReplenishNum()-v.getSaleNum()-v.getEndNum()+v.getNum());
    		}
    	}	
        return returnDataUtil;
    }

    /**
     * 机器商品日志添加
     * @param entity
     * @return
     */
    public VendingMachineHistoryBean add(VendingMachineHistoryBean entity) {
        return vendingMachineHistoryDaoImpl.insert(entity);
    }
    //加入该货道商品的统计信息
    public void  updateBean(String vmCode,Integer wayNumber){
        vendingMachineHistoryDaoImpl.updateBean(vmCode,wayNumber);
    }

    /**
     * 机器商品日志修改
     * @param entity
     * @return
     */
    public boolean update(VendingMachineHistoryBean entity) {
        return vendingMachineHistoryDaoImpl.update(entity);
    }

    /**
     * 机器商品日志删除
     * @param id
     * @return
     */
    public boolean del(Object id) {
        return vendingMachineHistoryDaoImpl.delete(id);
    }

    public List<VendingMachineHistoryBean> list(VendingMachineHistoryCondition condition) {
        return null;
    }

    public VendingMachineHistoryBean get(Object id) {
        return vendingMachineHistoryDaoImpl.get(id);
    }

    /**
     * 记录对应机器货道的相关机器商品日志
     * @param vmCode
     * @param wayNumber
     */
    public void  addBean(String vmCode,Integer wayNumber){
        vendingMachineHistoryDaoImpl.addBean(vmCode,wayNumber);
    }

    /**
     * 修改机器商品日志相应数量
     * @param vmCode
     * @param wayNumber
     * @param basicItemId
     * @param num
     */
    public void  updateBean(String vmCode,Integer wayNumber,Integer basicItemId,Integer num){
        vendingMachineHistoryDaoImpl.updateBean(vmCode,wayNumber,basicItemId,num);
    }
}

