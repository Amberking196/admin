package com.server.module.system.repairManage.repairMaterial;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: yjr
 * create time: 2019-08-13 14:38:18
 */ 
public interface  RepairMaterialService{


		public ReturnDataUtil listPage(RepairMaterialCondition condition);
		public List<RepairMaterialBean> list(RepairMaterialCondition condition);
		public boolean update(RepairMaterialBean entity);
		public boolean del(Object id);
		public RepairMaterialBean get(Object id);
		public RepairMaterialBean add(RepairMaterialBean entity);
}

