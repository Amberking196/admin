package com.server.module.system.officialManage.officialMessage;

import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 */ 
public interface  OfficialMessageDao{

	/**
	 * 查询官网留言列表
	 * @param officialMessageForm 
	 * return 
	 */
	public ReturnDataUtil listPage(OfficialMessageForm officialMessageForm);
	public boolean update(OfficialMessageBean entity);
	public boolean delete(Object id);
	public OfficialMessageBean get(Object id);
	public OfficialMessageBean insert(OfficialMessageBean entity);
}

