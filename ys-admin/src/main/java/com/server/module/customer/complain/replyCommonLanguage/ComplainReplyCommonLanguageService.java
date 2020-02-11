package com.server.module.customer.complain.replyCommonLanguage;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2019-01-04 09:50:27
 */
public interface ComplainReplyCommonLanguageService {

	/**
	 * 查询常用语列表
	 * @author why
	 * @date 2019年2月15日 下午2:59:09 
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(ComplainReplyCommonLanguageForm condition);

	/**
	 * 修改常用语
	 * @author why
	 * @date 2019年2月15日 下午2:59:20 
	 * @param entity
	 * @return
	 */
	public boolean update(ComplainReplyCommonLanguageBean entity);

	/**
	 * 删除常用语
	 * @author why
	 * @date 2019年2月15日 下午3:02:09 
	 * @param id
	 * @return
	 */
	public boolean del(Long id);

	/**
	 * 查询常用语
	 * @author why
	 * @date 2019年2月15日 下午3:02:20 
	 * @param id
	 * @return
	 */
	public ComplainReplyCommonLanguageBean get(Object id);

	/**
	 * 增加常用语
	 * @author why
	 * @date 2019年1月4日 上午10:51:35 
	 * @param entity
	 * @return
	 */
	public ComplainReplyCommonLanguageBean add(ComplainReplyCommonLanguageBean entity);
}
