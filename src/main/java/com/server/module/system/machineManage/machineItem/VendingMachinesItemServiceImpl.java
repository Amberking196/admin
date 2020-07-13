package com.server.module.system.machineManage.machineItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2018-04-13 08:57:26
 */ 
@Service
public class  VendingMachinesItemServiceImpl  implements VendingMachinesItemService{

	public static Logger log = LogManager.getLogger(VendingMachinesItemServiceImpl.class);  
@Autowired
private VendingMachinesItemDao vendingMachinesItemDaoImpl;
public ReturnDataUtil listPage(VendingMachinesItemCondition condition){
return vendingMachinesItemDaoImpl.listPage(condition);
}
public VendingMachinesItemBean add(VendingMachinesItemBean entity) {
return vendingMachinesItemDaoImpl.insert(entity);
}

public boolean update(VendingMachinesItemBean entity) {
return vendingMachinesItemDaoImpl.update(entity);
}

public boolean del(Object id) {
return vendingMachinesItemDaoImpl.delete(id);
}

public List<VendingMachinesItemBean> list(VendingMachinesItemCondition condition) {
return null;
}

public VendingMachinesItemBean get(Object id) {
return vendingMachinesItemDaoImpl.get(id);
}
}

