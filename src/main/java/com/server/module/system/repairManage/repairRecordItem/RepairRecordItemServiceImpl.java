package com.server.module.system.repairManage.repairRecordItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2019-08-19 15:38:16
 */ 
@Service
public class  RepairRecordItemServiceImpl  implements RepairRecordItemService{

private static Log log = LogFactory.getLog(RepairRecordItemServiceImpl.class);
@Autowired
private RepairRecordItemDao repairRecordItemDaoImpl;
public ReturnDataUtil listPage(RepairRecordItemCondition condition){
return repairRecordItemDaoImpl.listPage(condition);
}
public RepairRecordItemBean add(RepairRecordItemBean entity) {
return repairRecordItemDaoImpl.insert(entity);
}

public boolean update(RepairRecordItemBean entity) {
return repairRecordItemDaoImpl.update(entity);
}

public boolean del(Object id) {
return repairRecordItemDaoImpl.delete(id);
}

public List<RepairRecordItemBean> list(RepairRecordItemCondition condition) {
return null;
}

public RepairRecordItemBean get(Object id) {
return repairRecordItemDaoImpl.get(id);
}
}

