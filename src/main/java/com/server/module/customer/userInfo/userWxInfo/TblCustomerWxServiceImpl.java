package com.server.module.customer.userInfo.userWxInfo;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-11-15 15:05:14
 */
@Service
public class TblCustomerWxServiceImpl implements TblCustomerWxService {

	private static Logger log = LogManager.getLogger(TblCustomerWxServiceImpl.class);
	@Autowired
	private TblCustomerWxDao tblCustomerWxDaoImpl;

	public ReturnDataUtil listPage(TblCustomerWxForm condition) {
		return tblCustomerWxDaoImpl.listPage(condition);
	}

	public TblCustomerWxBean add(TblCustomerWxBean entity) {
		return tblCustomerWxDaoImpl.insert(entity);
	}

	public boolean update(TblCustomerWxBean entity) {
		return tblCustomerWxDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return tblCustomerWxDaoImpl.delete(id);
	}

	public List<TblCustomerWxBean> list(TblCustomerWxForm condition) {
		return null;
	}

	/**
	 * 查询微信用户信息
	 */
	public TblCustomerWxBean get(Long customerId) {
		log.info("<TblCustomerWxServiceImpl>------<get>-----start");
		TblCustomerWxBean tblCustomerWxBean = tblCustomerWxDaoImpl.get(customerId);
		log.info("<TblCustomerWxServiceImpl>------<get>-----end");
		return tblCustomerWxBean;
	}
	
	public String findCusteomerAddress() {
		log.info("<TblCustomerWxServiceImpl>------<findCusteomerAddress>-----start");
		String address = tblCustomerWxDaoImpl.findCusteomerAddress();
		log.info("<TblCustomerWxServiceImpl>------<findCusteomerAddress>-----end");
		return address;
	}
	
	@Override
	public List<TblCustomerWxBean> myInviteRewards(){
		log.info("<TblCustomerWxServiceImpl>----<myInviteRewards>----start>");
		List<TblCustomerWxBean> list = tblCustomerWxDaoImpl.myInviteRewards();
		log.info("<TblCustomerWxServiceImpl>----<myInviteRewards>----end>");
		return list;
	}
}
