package com.server.module.system.logsManager.exportLog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-14 10:31:38
 */
@Service
public class ExportLogServiceImpl implements ExportLogService {

	private static Log log = LogFactory.getLog(ExportLogServiceImpl.class);
	@Autowired
	private ExportLogDao exportLogDaoImpl;

	public ReturnDataUtil listPage(ExportLogCondition condition) {
		log.info("---------<ExportLogServiceImpl>--------<listPage>--------start");
		ReturnDataUtil data=exportLogDaoImpl.listPage(condition);
		log.info("---------<ExportLogServiceImpl>--------<listPage>--------end");
		return data;
	}

	public ExportLogBean add(ExportLogBean entity) {
		return exportLogDaoImpl.insert(entity);
	}

	public boolean update(ExportLogBean entity) {
		return exportLogDaoImpl.update(entity);
	}

	public boolean del(Object id) {
		return exportLogDaoImpl.delete(id);
	}

	public List<ExportLogBean> list(ExportLogCondition condition) {
		return null;
	}

	public ExportLogBean get(Object id) {
		return exportLogDaoImpl.get(id);
	}
}
