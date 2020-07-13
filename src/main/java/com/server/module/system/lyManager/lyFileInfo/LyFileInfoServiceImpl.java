package com.server.module.system.lyManager.lyFileInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-04 10:05:20
 */
@Service
public class LyFileInfoServiceImpl implements LyFileInfoService {

	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 
	@SuppressWarnings("unused")
	@Autowired
	private LyFileInfoDao LyFileInfoDaoImpl;
	
	/**
	 * 查询 文件管理列表
	 */
	@Override
	public ReturnDataUtil listPage(LyFileInfoCondition condition) {
		// TODO Auto-generated method stub
		return LyFileInfoDaoImpl.listPage(condition);
	}

	/**
	 * 编辑文件
	 */
	@Override
	public boolean updateEntity(LyFileInfoBean entity) {
		// TODO Auto-generated method stub
		return LyFileInfoDaoImpl.updateEntity(entity);
	}

	/**
	 * 删除文件
	 */
	@Override
	public boolean del(LyFileInfoBean name) {
		// TODO Auto-generated method stub
		return LyFileInfoDaoImpl.del(name);
	}

}
