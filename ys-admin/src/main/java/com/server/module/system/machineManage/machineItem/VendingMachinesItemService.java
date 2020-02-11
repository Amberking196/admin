package com.server.module.system.machineManage.machineItem;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: yjr
 * create time: 2018-04-13 08:57:26
 */ 
public interface  VendingMachinesItemService{

	public ReturnDataUtil listPage(VendingMachinesItemCondition condition);
	public List<VendingMachinesItemBean> list(VendingMachinesItemCondition condition);
	public boolean update(VendingMachinesItemBean entity);
	public boolean del(Object id);
	public VendingMachinesItemBean get(Object id);
	public VendingMachinesItemBean add(VendingMachinesItemBean entity);
}

