package com.server.module.system.tblManager.tblRateShortsReport;

import java.util.List;
import java.util.Map;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr 
 * create time: 2018-03-28 08:58:56
 */
public interface TblRateShortsReportService {

	public Map<String, Object> list(TblRateShortsReportCondition condition);

}
