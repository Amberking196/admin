package com.server.module.system.machineManage.machineList;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 
 * author name: why 批量下载二维码
 * create time: 2018-04-16 10:54:20
 */
public interface IDownLoadDao {

	/**
	 *  批量 下载售货机二维码
	 * @param list
	 * @param response
	 * @return
	 */
	public void downLoadQRCode(List<MachinesInfoAndBaseDto> list,HttpServletResponse response);
	
	
}
