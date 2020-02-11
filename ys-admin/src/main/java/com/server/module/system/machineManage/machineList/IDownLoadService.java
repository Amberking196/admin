package com.server.module.system.machineManage.machineList;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
/**
 * 
 * author name: why
 * create time: 2018-04-16 11:00:12
 */
public interface IDownLoadService {

	/**
	 * 批量 下载售货机二维码
	 * @param companyId
	 * @param response
	 * @return
	 */
	public void downLoadQRCode(List<MachinesInfoAndBaseDto> list,HttpServletResponse response);
}
