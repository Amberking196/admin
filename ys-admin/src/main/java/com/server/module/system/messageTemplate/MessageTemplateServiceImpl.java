package com.server.module.system.messageTemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2019-01-07 11:21:04
 * 消息模板服务逻辑类
 */
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

	private static Logger log = LogManager.getLogger(MessageTemplateServiceImpl.class);
	@Autowired
	private MessageTemplateDao messageTemplateDaoImpl;

	/**
	 *
	 * 分页查询
	 * @param form 参数对象
	 * @return
	 */
	public ReturnDataUtil listPage(MessageTemplateForm form) {
		log.info("<MessageTemplateServiceImpl>----<listPage>----start");
		ReturnDataUtil listPage = messageTemplateDaoImpl.listPage(form);
		log.info("<MessageTemplateServiceImpl>----<listPage>----end");
		return listPage;
	}

	/**
	 * 添加方法
	 * @param entity 消息模板对象
	 * @return
	 */
	public MessageTemplateBean add(MessageTemplateBean entity) {
		log.info("<MessageTemplateServiceImpl>----<add>----start");
		MessageTemplateBean bean = messageTemplateDaoImpl.insert(entity);
		log.info("<MessageTemplateServiceImpl>----<add>----end");
		return bean;
	}
	/**
	 * 修改方法
	 * @param entity 消息模板对象
	 * @return
	 */
	public boolean update(MessageTemplateBean entity) {
		log.info("<MessageTemplateServiceImpl>----<update>----start");
		boolean update = messageTemplateDaoImpl.update(entity);
		log.info("<MessageTemplateServiceImpl>----<update>----end");
		return update;
	}

	/**
	 * 删除方法
	 * @param id 消息模板id
	 * @return
	 */
	public boolean del(Long id) {
		log.info("<MessageTemplateServiceImpl>----<del>----start");
		boolean delete = messageTemplateDaoImpl.delete(id);
		log.info("<MessageTemplateServiceImpl>----<del>----end");
		return delete;
	}

	

	
}
