package com.server.module.system.repairManage.repairRecordItem;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: yjr
 * create time: 2019-08-19 15:38:16
 */ 
public interface  RepairRecordItemService{


public ReturnDataUtil listPage(RepairRecordItemCondition condition);
public List<RepairRecordItemBean> list(RepairRecordItemCondition condition);
public boolean update(RepairRecordItemBean entity);
public boolean del(Object id);
public RepairRecordItemBean get(Object id);
public RepairRecordItemBean add(RepairRecordItemBean entity);
}

