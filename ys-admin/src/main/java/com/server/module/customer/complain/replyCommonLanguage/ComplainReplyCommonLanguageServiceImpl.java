package com.server.module.customer.complain.replyCommonLanguage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2019-01-04 09:50:27
 */
@Service
public class ComplainReplyCommonLanguageServiceImpl implements ComplainReplyCommonLanguageService {

	private static Logger log = LogManager.getLogger(ComplainReplyCommonLanguageServiceImpl.class);
	@Autowired
	private ComplainReplyCommonLanguageDao complainReplyCommonLanguageDaoImpl;

	public ReturnDataUtil listPage(ComplainReplyCommonLanguageForm condition) {
		return complainReplyCommonLanguageDaoImpl.listPage(condition);
	}

	public ComplainReplyCommonLanguageBean add(ComplainReplyCommonLanguageBean entity) {
		log.info("<ComplainReplyCommonLanguageServiceImpl>------<add>----start");
		ComplainReplyCommonLanguageBean bean = complainReplyCommonLanguageDaoImpl.insert(entity);
		log.info("<ComplainReplyCommonLanguageServiceImpl>------<add>----end");
		return bean;
	}

	public boolean update(ComplainReplyCommonLanguageBean entity) {
		return complainReplyCommonLanguageDaoImpl.update(entity);
	}

	public boolean del(Long id) {
		log.info("<ComplainReplyCommonLanguageServiceImpl>------<del>----start");
		boolean delete = complainReplyCommonLanguageDaoImpl.delete(id);
		log.info("<ComplainReplyCommonLanguageServiceImpl>------<del>----end");
		return delete;
	}

	

	public ComplainReplyCommonLanguageBean get(Object id) {
		return complainReplyCommonLanguageDaoImpl.get(id);
	}
}
