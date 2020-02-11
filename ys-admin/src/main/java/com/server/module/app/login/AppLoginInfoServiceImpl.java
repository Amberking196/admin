package com.server.module.app.login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("aliLoginInfoService")
public class AppLoginInfoServiceImpl implements AppLoginInfoService{

	public static Logger log = LogManager.getLogger(AppLoginInfoServiceImpl.class); 
	@Autowired
	@Qualifier("aliLoginInfoDao")
	private AppLoginInfoDao loginInfoDao;



	@Override
	public LoginInfoBean findLoginInfo(String alipayUserId) {
		log.info("<AliLoginInfoServiceImpl--findLoginInfo--start>");
		LoginInfoBean findLoginInfo = loginInfoDao.findLoginInfo(alipayUserId);
		log.info("<AliLoginInfoServiceImpl--findLoginInfo--end>");
		return findLoginInfo;
	}

	@Override
	public LoginInfoBean queryByPhone(String phone) {
		log.info("<AliLoginInfoServiceImpl--queryByPhone--start>");
		LoginInfoBean queryByPhone = loginInfoDao.queryByPhone(phone);
		log.info("<AliLoginInfoServiceImpl--queryByPhone--end>");
		return queryByPhone;
	}

	@Override
	public LoginInfoBean queryById(Long id) {
		log.info("<AliLoginInfoServiceImpl--queryById--start>");
		LoginInfoBean queryById = loginInfoDao.queryById(id);
		log.info("<AliLoginInfoServiceImpl--queryById--end>");
		return queryById;
	}
}
