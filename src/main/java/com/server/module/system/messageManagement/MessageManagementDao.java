package com.server.module.system.messageManagement;

import com.server.module.system.machineManage.machineList.VendingMachinesInfoCondition;
import com.server.util.ReturnDataUtil;

/**
 *留言管理Dao接口
 * @author 26920
 *
 */
public interface MessageManagementDao {
	/**
	 * 根据条件分页查询留言
	 * @param form
	 * @return
	 */
	ReturnDataUtil messageListPage(MessageManagementForm form);
	/**
	 * 根据条件查询有留言的售货机列表
	 * @param condition
	 * @return
	 */
	ReturnDataUtil listPage(VendingMachinesInfoCondition condition);
	/**
	 * 根据条件查询查询留言下的评论信息
	 * @param form
	 * @return
	 */
	ReturnDataUtil messageCommentListPage(MessageCommentForm form);
	
}
