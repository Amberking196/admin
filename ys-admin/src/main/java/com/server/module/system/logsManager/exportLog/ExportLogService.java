package com.server.module.system.logsManager.exportLog;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-14 10:31:38
 */
public interface ExportLogService {

	public ReturnDataUtil listPage(ExportLogCondition condition);

	public List<ExportLogBean> list(ExportLogCondition condition);

	public boolean update(ExportLogBean entity);

	public boolean del(Object id);

	public ExportLogBean get(Object id);

	public ExportLogBean add(ExportLogBean entity);
}
