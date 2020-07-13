package com.server.module.system.lyManager.lyFileInfo;

import com.server.util.ReturnDataUtil;

/**
 * 
 * author name: why
 * create time: 2018-04-04 10:05:20
 */
public interface LyFileInfoService {

	/**
	 * 查询 文件管理列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(LyFileInfoCondition condition);
	
	/**
	 *  编辑 文件
	 * @param entity
	 * @return
	 */
	public boolean updateEntity(LyFileInfoBean entity);

	/**
	 * 删除文件
	 * @param name
	 * @return
	 */
	public boolean del(LyFileInfoBean name);
}
