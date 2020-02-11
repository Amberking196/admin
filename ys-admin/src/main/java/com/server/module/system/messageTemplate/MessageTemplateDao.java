package com.server.module.system.messageTemplate;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2019-01-07 11:21:04
 */
public interface MessageTemplateDao {

	/**
	 * 查询所有短信模板信息
	 * @author why
	 * @date 2019年1月7日 上午11:27:50 
	 * @param form
	 * @return
	 */
	public ReturnDataUtil listPage(MessageTemplateForm form);

	/**
	 * 修改短信模板信息
	 * @author why
	 * @date 2019年1月7日 下午2:12:15 
	 * @param entity
	 * @return
	 */
	public boolean update(MessageTemplateBean entity);

	/**
	 * 删除短信模板信息
	 * @author why
	 * @date 2019年1月7日 下午2:12:30 
	 * @param id
	 * @return
	 */
	public boolean delete(Long id);


	/**
	 * 短信模板增加
	 * @author why
	 * @date 2019年1月7日 上午11:59:33 
	 * @param entity
	 * @return
	 */
	public MessageTemplateBean insert(MessageTemplateBean entity);
}
