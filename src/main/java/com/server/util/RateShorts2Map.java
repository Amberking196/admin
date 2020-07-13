package com.server.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.server.module.system.tblManager.tblRateShortsReport.TblRateShortsReportBean;

//import com.ly.dao.pojo.RateShortsReport;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lixiaoying 2017年11月28日 下午3:16:46
 */
@Slf4j
public class RateShorts2Map {

	private static DecimalFormat DF = new DecimalFormat("##.00%");

	/**
	 * RateShortsReport集合转为map
	 */
	public static Map<String, Object> convert(List<TblRateShortsReportBean> dataList) {

		Map<String, Object> res = new HashMap<>();
		List<Map<String, Object>> rowList = new ArrayList<>();

		Map<String, List<TblRateShortsReportBean>> rowMap = new LinkedHashMap<String, List<TblRateShortsReportBean>>();
		/**
		 * 聚合数据
		 */
		for (TblRateShortsReportBean rateShortsReport : dataList) {
			String vmCode = rateShortsReport.getVmCode();
			List<TblRateShortsReportBean> columnList = (List<TblRateShortsReportBean>) (rowMap.get(vmCode) == null ? new ArrayList<>() : rowMap.get(vmCode));
			columnList.add(rateShortsReport);
			rowMap.put(vmCode, columnList);
		}

		/**
		 * 分配数据
		 */
		for (List<TblRateShortsReportBean> rows : rowMap.values()) {
			Map<String, Object> row = new HashMap<String, Object>();
			TblRateShortsReportBean first = rows.get(0);
			try {
				row = MapUtil.objectToMap(first);
			} catch (Exception e) {
				log.error("【异常】e={}", e);
			}
			List<Map<String, String>> rateShortsList = new ArrayList<>();
			List<String> titleList = new ArrayList<>();
			for (TblRateShortsReportBean rateShortsReport : rows) {
				String date = DateUtils.getDateStr(rateShortsReport.getReportDate(), DateUtils.DATE_PATTERN_YYYY_MM_DD);
				titleList.add(date);
				Map<String, String> data = new HashMap();
				data.put("rate", rateShortsReport.getRateShorts()==null?"":DF.format(rateShortsReport.getRateShorts().floatValue()));
				data.put("date", date);
				rateShortsList.add(data);
			}
			row.remove("report_date");
			row.remove("rateShorts");
			row.remove("id");
			row.put("rateShortsList", rateShortsList);
			res.put("titleList", titleList);
			rowList.add(row);
		}
		res.put("rowList", rowList);
		return res;
	}

	public static void main(String[] args) {
		TblRateShortsReportBean  rateShortsReport = new TblRateShortsReportBean();
		rateShortsReport.setRateShorts(1f);
		System.out.println(DF.format(rateShortsReport.getRateShorts().floatValue()));
	}
}
