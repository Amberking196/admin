package com.server.module.system.tblManager.tblRateShortsReport;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-28 08:58:56
 */ 
public interface  TblRateShortsReportDao{

public List<TblRateShortsReportBean> list(TblRateShortsReportCondition condition);
public List<TblRateShortsReportBean> listEntity(TblRateShortsReportCondition condition);
public TblRateShortsReportBean getEntity(Object id);
public boolean updateEntity(TblRateShortsReportBean entity);
}

