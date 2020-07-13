package com.server.module.system.repairManage.repairRecord;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: yjr
 * create time: 2019-08-14 10:42:05
 */ 
public interface  RepairRecordService{


public ReturnDataUtil listPage(RepairRecordCondition condition);
public List<RepairRecordBean> list(RepairRecordCondition condition);
public boolean update(RepairRecordBean entity);
public boolean del(Object id);
public RepairRecordBean get(Object id);
public RepairRecordBean add(RepairRecordBean entity);

public RepairRecordBean detail(RepairRecordCondition condition);

}

