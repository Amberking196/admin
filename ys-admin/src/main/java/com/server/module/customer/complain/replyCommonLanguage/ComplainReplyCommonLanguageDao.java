package com.server.module.customer.complain.replyCommonLanguage;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2019-01-04 09:50:27
 */
public interface ComplainReplyCommonLanguageDao {

	/**
	 * 查询常用语列表
	 * @author why
	 * @date 2019年1月4日 上午11:18:27 
	 * @param form
	 * @return
	 */
	public ReturnDataUtil listPage(ComplainReplyCommonLanguageForm form);

	/**
	 * 编辑常用语
	 * @author why
	 * @date 2019年1月4日 上午11:33:10 
	 * @param entity
	 * @return
	 */
	public boolean update(ComplainReplyCommonLanguageBean entity);

	/**
	 * 删除常用语
	 * @author why
	 * @date 2019年2月15日 下午2:57:17 
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 查询常用语
	 * @author why
	 * @date 2019年2月15日 下午2:57:33 
	 * @param id
	 * @return
	 */
	public ComplainReplyCommonLanguageBean get(Object id);

	/**
	 * 增加常用语
	 * @author why
	 * @date 2019年1月4日 上午10:49:34 
	 * @param entity
	 * @return
	 */
	public ComplainReplyCommonLanguageBean insert(ComplainReplyCommonLanguageBean entity);
}
