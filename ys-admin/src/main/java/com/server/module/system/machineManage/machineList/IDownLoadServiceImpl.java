package com.server.module.system.machineManage.machineList;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 
 * author name: why
 * create time: 2018-04-16 11:03:56
 */
@Service
public class IDownLoadServiceImpl implements IDownLoadService {

	@Autowired
	private IDownLoadDao IDownLoadDaoImpl;
	
	 /**
	  * 批量 下载售货机二维码
	  */
	@Override
	public void downLoadQRCode(List<MachinesInfoAndBaseDto> list,HttpServletResponse response) {
		// TODO Auto-generated method stub
		 IDownLoadDaoImpl.downLoadQRCode(list, response);
	}

}
