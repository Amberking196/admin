package com.server.module.system.messageTemplate;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2019-01-07 11:21:04
 */
public interface MessageTemplateService {

	/**
	 * 查询所有短信模板信息
	 * @author why
	 * @date 2019年1月7日 上午11:30:58 
	 * @param form
	 * @return
	 */
	public ReturnDataUtil listPage(MessageTemplateForm form);

	/**
	 * 修改短信模板
	 * @author why
	 * @date 2019年1月7日 下午2:19:33 
	 * @param entity
	 * @return
	 */
	public boolean update(MessageTemplateBean entity);

	/**
	 * 删除短信模板
	 * @author why
	 * @date 2019年1月7日 下午2:19:42 
	 * @param id
	 * @return
	 */
	public boolean del(Long id);


	/**
	 * 增加短信模板
	 * @author why
	 * @date 2019年1月7日 下午2:06:17 
	 * @param entity
	 * @return
	 */
	public MessageTemplateBean add(MessageTemplateBean entity);
}
