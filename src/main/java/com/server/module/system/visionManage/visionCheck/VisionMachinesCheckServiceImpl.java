package com.server.module.system.visionManage.visionCheck;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
 import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2019-10-10 10:54:34
 */ 
@Service
public class  VisionMachinesCheckServiceImpl  implements VisionMachinesCheckService{

private static Log log = LogFactory.getLog(VisionMachinesCheckServiceImpl.class);
@Autowired
private VisionMachinesCheckDao visionMachinesCheckDaoImpl;
public ReturnDataUtil listPage(VisionMachinesCheckCondition condition){
	return visionMachinesCheckDaoImpl.listPage(condition);
}
public ReturnDataUtil detail(VisionMachinesCheckCondition condition){
	return visionMachinesCheckDaoImpl.detail(condition);
}
public VisionMachinesCheckBean add(VisionMachinesCheckBean entity) {
	return visionMachinesCheckDaoImpl.insert(entity);
}

public boolean update(VisionMachinesCheckBean entity) {
	return visionMachinesCheckDaoImpl.update(entity);
}

public boolean del(Object id) {
	return visionMachinesCheckDaoImpl.delete(id);
}

public List<VisionMachinesCheckBean> list(VisionMachinesCheckCondition condition) {
	return null;
}

public VisionMachinesCheckBean get(Object id) {
return visionMachinesCheckDaoImpl.get(id);
}
}

