package com.server.module.system.tblManager.tblRateShortsReport;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.util.RateShorts2Map;

/**
 * author name: yjr create time: 2018-03-28 08:58:56
 */
@Service
public class TblRateShortsReportServiceImpl implements TblRateShortsReportService {

	public static Logger log = LogManager.getLogger(TblRateShortsReportServiceImpl.class);
	@Autowired
	private TblRateShortsReportDao tblRateShortsReportDaoImpl;

	public Map<String, Object> list(TblRateShortsReportCondition condition) {
		List<TblRateShortsReportBean> list=tblRateShortsReportDaoImpl.list(condition);
		Map<String, Object> res = RateShorts2Map.convert(list);
		return res;

	}
}
