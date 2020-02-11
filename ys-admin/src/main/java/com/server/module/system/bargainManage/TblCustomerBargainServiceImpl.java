package com.server.module.system.bargainManage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;

import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-12-21 16:20:26
 */ 
@Service
public class  TblCustomerBargainServiceImpl  implements TblCustomerBargainService{

	private final static Logger log = LogManager.getLogger(TblCustomerBargainServiceImpl.class);
	@Autowired
	private TblCustomerBargainDao tblCustomerBargainDaoImpl;
	
	
	public ReturnDataUtil listPage(TblCustomerBargainForm condition){
		log.info("<TblCustomerBargainServiceImpl>------<listPage>-----start");
		ReturnDataUtil  returnDataUtil=tblCustomerBargainDaoImpl.listPage(condition);
		List<TblCustomerBargainBean> tblCustomerBargainBeanList=(List<TblCustomerBargainBean>) returnDataUtil.getReturnObject();
		List<EachStateNumDto> eachStateNum=tblCustomerBargainDaoImpl.eachStateNum(condition);
		HashMap h=new HashMap();
		h.put("list", tblCustomerBargainBeanList);
		h.put("eachStateNum", eachStateNum);
		returnDataUtil.setReturnObject(h);
		log.info("<TblCustomerBargainServiceImpl>------<listPage>-----end");
		return returnDataUtil;

	}
	
	public ReturnDataUtil listPageWithOutStateNum(TblCustomerBargainForm condition){
		
		ReturnDataUtil  returnDataUtil=tblCustomerBargainDaoImpl.listPage(condition);
		return returnDataUtil;
	}
	
	public TblCustomerBargainDetailDto detail(TblCustomerBargainForm tblCustomerBargainForm) {
		TblCustomerBargainDetailDto dto=tblCustomerBargainDaoImpl.detail(tblCustomerBargainForm);
		List<TblCustomerBargainDetailBean> bean=tblCustomerBargainDaoImpl.detailList(tblCustomerBargainForm);
		dto.setTblCustomerBargainDetailBeanList(bean);
		return dto;
	}

	public Boolean updateSendMessage(String phone, Integer id) {
		return tblCustomerBargainDaoImpl.updateSendMessage( phone,  id) ;

	}
}
