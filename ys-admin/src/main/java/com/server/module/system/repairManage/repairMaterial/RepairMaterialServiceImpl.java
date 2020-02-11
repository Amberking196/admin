package com.server.module.system.repairManage.repairMaterial;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2019-08-13 14:38:18
 */ 
@Service
public class  RepairMaterialServiceImpl  implements RepairMaterialService{

private static Log log = LogFactory.getLog(RepairMaterialServiceImpl.class);
@Autowired
private RepairMaterialDao repairMaterialDaoImpl;
public ReturnDataUtil listPage(RepairMaterialCondition condition){
	return repairMaterialDaoImpl.listPage(condition);
}
public RepairMaterialBean add(RepairMaterialBean entity) {
	return repairMaterialDaoImpl.insert(entity);
}

public boolean update(RepairMaterialBean entity) {
	return repairMaterialDaoImpl.update(entity);
}

public boolean del(Object id) {
	return repairMaterialDaoImpl.delete(id);
}

public List<RepairMaterialBean> list(RepairMaterialCondition condition) {
	return null;
}

public RepairMaterialBean get(Object id) {
	return repairMaterialDaoImpl.get(id);
}
}

