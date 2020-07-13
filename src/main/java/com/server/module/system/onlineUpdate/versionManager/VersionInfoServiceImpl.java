package com.server.module.system.onlineUpdate.versionManager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.module.app.home.ResultEnum;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@Service
public class VersionInfoServiceImpl implements VersionInfoService{
	
	private final static Logger log = LogManager.getLogger(VersionInfoServiceImpl.class);

	@Autowired
	private VersionInfoDao versionInfoDao;

	@Override
	public ReturnDataUtil insert(VersionInfoBean versionInfo) {
		log.info("<VersionInfoServiceImpl--insert--start>");
		if(StringUtils.isBlank(versionInfo.getVersionInfo())){
			return ResultUtil.error(ResultEnum.MACHINES_PROGRAM_VERSION_CANT_BLANK);
		}
		VersionInfoBean byVersionInfo = versionInfoDao.getByVersionInfo(versionInfo.getVersionInfo());
		if(byVersionInfo != null){
			return ResultUtil.error(ResultEnum.MACHINES_PROGRAM_VERSION_EXISTS);
		}
		if(StringUtils.isNotBlank(versionInfo.getPversion())){
			VersionInfoBean pversion = versionInfoDao.getByVersionInfo(versionInfo.getPversion());
			if(pversion == null){
				return ResultUtil.error(ResultEnum.MACHINES_PROGRAM_PVERSION_NOT_EXISTS);
			}
			versionInfo.setPid(pversion.getId());
		}
		Integer insert = versionInfoDao.insert(versionInfo);
		log.info("<VersionInfoServiceImpl--insert--end>");
		if(insert>0){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}

	@Override
	public ReturnDataUtil update(VersionInfoBean versionInfo) {
		log.info("<VersionInfoServiceImpl--update--start>");
		if(StringUtils.isBlank(versionInfo.getVersionInfo())){
			return ResultUtil.error(ResultEnum.MACHINES_PROGRAM_VERSION_CANT_BLANK);
		}
		if(StringUtils.isNotBlank(versionInfo.getPversion())){
			VersionInfoBean pversion = versionInfoDao.getByVersionInfo(versionInfo.getPversion());
			if(pversion == null){
				return ResultUtil.error(ResultEnum.MACHINES_PROGRAM_PVERSION_NOT_EXISTS);
			}
			versionInfo.setPid(pversion.getId());
		}
		boolean update = versionInfoDao.update(versionInfo);
		log.info("<VersionInfoServiceImpl--update--end>");
		if(update){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}

	@Override
	public List<VersionInfoBean> getCanUpdateVersion(VersionInfoForm v) {
		log.info("<VersionInfoServiceImpl--getCanUpdateVersion--start>");
		List<VersionInfoBean> canUpdateVersion = versionInfoDao.getCanUpdateVersion(v);
		log.info("<VersionInfoServiceImpl--getCanUpdateVersion--end>");
		return canUpdateVersion;
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public ReturnDataUtil getByForm(VersionInfoForm form) {
		log.info("<VersionInfoServiceImpl--getByForm--start>");
		List<VersionInfoBean> byForm = versionInfoDao.getByForm(form);
		if(byForm != null && byForm.size()>0){
			Long total = versionInfoDao.getTotal();
			return ResultUtil.success(byForm,form.getCurrentPage(),total);
		}
		log.info("<VersionInfoServiceImpl--getByForm--end>");
		return ResultUtil.selectError();
	}
}
