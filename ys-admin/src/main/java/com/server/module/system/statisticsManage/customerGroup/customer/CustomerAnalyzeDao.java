package com.server.module.system.statisticsManage.customerGroup.customer;

import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2018-10-18 09:08:12
 */ 
public interface  CustomerAnalyzeDao{

public ReturnDataUtil listPage(CustomerAnalyzeCondition condition);
public List<CustomerAnalyzeBean> list(CustomerAnalyzeCondition condition);
public boolean update(CustomerAnalyzeBean entity);
public boolean delete(Object id);
public CustomerAnalyzeBean get(Object id);
public CustomerAnalyzeBean insert(CustomerAnalyzeBean entity);
}

