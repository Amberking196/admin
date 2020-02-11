package com.server.module.system.visionManage.visionCheck;

import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: yjr
 * create time: 2019-10-10 10:54:34
 */ 
public interface  VisionMachinesCheckDao{

public ReturnDataUtil listPage(VisionMachinesCheckCondition condition);
public ReturnDataUtil detail(VisionMachinesCheckCondition condition);

public List<VisionMachinesCheckBean> list(VisionMachinesCheckCondition condition);
public boolean update(VisionMachinesCheckBean entity);
public boolean delete(Object id);
public VisionMachinesCheckBean get(Object id);
public VisionMachinesCheckBean insert(VisionMachinesCheckBean entity);
}

