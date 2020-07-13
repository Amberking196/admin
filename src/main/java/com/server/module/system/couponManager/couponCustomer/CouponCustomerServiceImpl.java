package com.server.module.system.couponManager.couponCustomer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.server.module.system.statisticsManage.payRecord.ListSaleNumDTO;
import com.server.module.system.statisticsManage.payRecord.SaleNumItemDTO;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-06-28 09:15:49
 */
@Service
public class CouponCustomerServiceImpl implements CouponCustomerService {

    private static Logger log = LogManager.getLogger(CouponCustomerServiceImpl.class);
    @Autowired
    private CouponCustomerDao couponCustomerDaoImpl;

    public ReturnDataUtil listPage(CouponCustomerForm condition) {
        return couponCustomerDaoImpl.listPage(condition);
    }

    public CouponCustomerBean add(CouponCustomerBean entity) {
        return couponCustomerDaoImpl.insert(entity);
    }

    public boolean update(CouponCustomerBean entity) {
        return couponCustomerDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return couponCustomerDaoImpl.delete(id);
    }

    public List<CouponCustomerBean> list(CouponCustomerForm condition) {
        return null;
    }

    public CouponCustomerBean get(Object id) {

        return couponCustomerDaoImpl.get(id);
    }
  /*  SELECT c.phone,c.vmCode,c.createTime ,m.couponId FROM coupon_machine m INNER JOIN tbl_customer c ON m.vmCode=c.vmCode
    WHERE m.couponId=1 AND NOT EXISTS( SELECT cc.id FROM coupon_customer cc WHERE cc.`couponId`=m.couponId)

    SELECT c.phone,c.vmCode,c.createTime ,m.couponId,cu.id FROM coupon_machine m INNER JOIN tbl_customer c ON m.vmCode=c.vmCode LEFT JOIN coupon_customer cu ON c.id=cu.customer
    WHERE m.couponId=1 AND  EXISTS( SELECT cc.id FROM coupon_customer cc WHERE cc.`couponId`=m.couponId)


    SELECT c.phone,c.vmCode,c.createTime ,m.couponId,cu.id FROM tbl_customer c LEFT JOIN coupon_customer cu ON c.id=cu.customerId INNER JOIN  coupon_machine m  ON m.vmCode=c.vmCode
    WHERE m.couponId=1 AND NOT EXISTS( SELECT cc.id FROM coupon_customer cc WHERE cc.`couponId`=m.couponId)
*/
    @Override
    public ReturnDataUtil listPageForCustomer(CouponCustomerForm condition) {
    	log.info("<CouponCustomerServiceImpl>----<listPageForCustomer>------start");
    	ReturnDataUtil listPageForCustomer = couponCustomerDaoImpl.listPageForCustomer(condition);
    	log.info("<CouponCustomerServiceImpl>----<listPageForCustomer>------end");
        return listPageForCustomer;
    }

    @Override
    public List<CustomerCouponVo> list(int customerId) {

        return couponCustomerDaoImpl.list(customerId);
    }

    public ReturnDataUtil conponCustomerNoteList(ConponCustomerNoteForm conponCustomerNoteForm) {
    	log.info("<CouponCustomerServiceImpl>----<conponCustomerNoteList>------start");
    	ReturnDataUtil returnDataUtil=couponCustomerDaoImpl.conponCustomerNoteList(conponCustomerNoteForm);
    	 List<CouponCustomerVo> list = (List<CouponCustomerVo>)returnDataUtil.getReturnObject();
         List<CouponCustomerDto> list1 = Lists.newArrayList();
         int len = list.size();
         if(len>0){
        	 CouponCustomerDto dto = new CouponCustomerDto();
        	 dto.setCustomerId(list.get(0).getCustomerId());
 			dto.setVmCode(list.get(0).getVmCode());
 			dto.setPhone(list.get(0).getPhone());
 			dto.setStateLabel(list.get(0).getStateLabel());
 			list1.add(dto);
 		}

 		for (int i = 0; i < len; i++) {
 			CouponCustomerVo obj = list.get(i);
 			CouponCustomerDto dto = list1.get(list1.size()-1);
 			if(obj.getVmCode().equals(dto.getVmCode())&&obj.getCustomerId().equals(dto.getCustomerId())){
 				CouponCustomerVo saleItem = genCouponCustomerVo(obj);
 				dto.getList().add(saleItem);
 			} else {//
 				CouponCustomerDto temp = new CouponCustomerDto();
 				temp.setCustomerId(obj.getCustomerId());
 				temp.setVmCode(obj.getVmCode());
 				temp.setPhone(obj.getPhone());
 				temp.setStateLabel(obj.getStateLabel());
 				CouponCustomerVo saleItem = genCouponCustomerVo(obj);
 				temp.getList().add(saleItem);
 				list1.add(temp);
 			}
 		}
 		returnDataUtil.setReturnObject(list1);
    	log.info("<CouponCustomerServiceImpl>----<conponCustomerNoteList>------end");
    	return returnDataUtil;
    }
    
    
    public boolean updateIsSendState(ConponCustomerNoteForm conponCustomerNoteForm) {
    	log.info("<CouponCustomerServiceImpl>----<updateIsSendState>------start");
    	boolean flag=couponCustomerDaoImpl.updateIsSendState(conponCustomerNoteForm);
    	log.info("<CouponCustomerServiceImpl>----<updateIsSendState>------end");
    	return flag;
    }
    
    private CouponCustomerVo genCouponCustomerVo(CouponCustomerVo obj) {
    	CouponCustomerVo saleItem = new CouponCustomerVo();
		saleItem.setName(obj.getName());
		saleItem.setCreateTime(obj.getCreateTime());
		saleItem.setStartTime(obj.getStartTime());
		saleItem.setEndTime(obj.getEndTime());
		saleItem.setDays(obj.getDays());
		saleItem.setStateLabel(obj.getStateLabel());
		saleItem.setQuantity(obj.getQuantity());
		return saleItem;
	}
}

