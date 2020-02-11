package com.server.module.system.machineManage.machineType;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-22 13:31:26
 */ 
public interface  MachinesTypeDao{
	    public MachinesTypeBean get(Integer id)  throws Exception;
	    public ReturnDataUtil listPage(MachinesTypeCondition condition);
		public List<MachinesTypeBean> listEntity(MachinesTypeCondition condition);
		public MachinesTypeBean getEntity(Object id);
		public boolean updateEntity(MachinesTypeBean entity);
		public MachinesTypeBean insert(MachinesTypeBean type);
		public boolean del(MachinesTypeBean type);
		/**
		 * 判断类型是否唯一，如果返回state=-1，说明该类型已经存在
		 * @param entity
		 * @return
		 */
		public ReturnDataUtil checkOnlyOne(MachinesTypeBean entity);
		/**
		 * 根据类型id查询类型信息
		 * @param id
		 * @return
		 */
		public MachinesTypeBean findTypeById(Long id);
		/**
		 * 查询机器类型有效的类型
		 * @return
		 */
		public ReturnDataUtil findAllByState();
}

