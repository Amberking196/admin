package com.server.module.system.repairManage.repairRecordVmCode;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: yjr
 * create time: 2019-08-19 15:44:00
 */ 
public interface  RepairRecordVmCodeService{


public ReturnDataUtil listPage(RepairRecordVmCodeCondition condition);
public List<RepairRecordVmCodeBean> list(RepairRecordVmCodeCondition condition);
public boolean update(RepairRecordVmCodeBean entity);
public boolean del(Object id);
public RepairRecordVmCodeBean get(Object id);
public RepairRecordVmCodeBean add(RepairRecordVmCodeBean entity);
}

