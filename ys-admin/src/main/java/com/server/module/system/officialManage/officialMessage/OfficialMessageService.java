package com.server.module.system.officialManage.officialMessage;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-08-02 10:24:08
 */ 
public interface  OfficialMessageService{

	/**
	 * 查询官网留言列表
	 * @param officialMessageForm 
	 * return 
	 */
	public ReturnDataUtil listPage(OfficialMessageForm officialMessageForm);
	/**
	 * 标志删除官网留言
	 * @param OfficialMessageBean 
	 * return 
	 */
	public boolean update(OfficialMessageBean entity);
	/**
	 * 添加官网留言
	 * @param OfficialMessageBean 
	 * return 
	 */
	public OfficialMessageBean add(OfficialMessageBean entity);
	public boolean del(Object id);
	public OfficialMessageBean get(Object id);
}

