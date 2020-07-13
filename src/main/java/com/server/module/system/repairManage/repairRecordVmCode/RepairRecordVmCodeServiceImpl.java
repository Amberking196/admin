package com.server.module.system.repairManage.repairRecordVmCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2019-08-19 15:44:00
 */ 
@Service
public class  RepairRecordVmCodeServiceImpl  implements RepairRecordVmCodeService{

private static Log log = LogFactory.getLog(RepairRecordVmCodeServiceImpl.class);
@Autowired
private RepairRecordVmCodeDao repairRecordVmCodeDaoImpl;
public ReturnDataUtil listPage(RepairRecordVmCodeCondition condition){
return repairRecordVmCodeDaoImpl.listPage(condition);
}
public RepairRecordVmCodeBean add(RepairRecordVmCodeBean entity) {
return repairRecordVmCodeDaoImpl.insert(entity);
}

public boolean update(RepairRecordVmCodeBean entity) {
return repairRecordVmCodeDaoImpl.update(entity);
}

public boolean del(Object id) {
return repairRecordVmCodeDaoImpl.delete(id);
}

public List<RepairRecordVmCodeBean> list(RepairRecordVmCodeCondition condition) {
return null;
}

public RepairRecordVmCodeBean get(Object id) {
return repairRecordVmCodeDaoImpl.get(id);
}
}

