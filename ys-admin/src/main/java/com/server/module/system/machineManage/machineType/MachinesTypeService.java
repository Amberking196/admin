package com.server.module.system.machineManage.machineType;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-03-22 13:31:26
 */ 
public interface  MachinesTypeService{
	
	public MachinesTypeBean get(Integer id);
	public MachinesTypeBean insert(MachinesTypeBean type);
	public boolean update(MachinesTypeBean type);	
	public boolean del(MachinesTypeBean type);
	public ReturnDataUtil listPage(MachinesTypeCondition condition);
	/**
	 * 判断添加的类型是否已经存在state=-1，说明存在
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil checkOnlyone(MachinesTypeBean entity);
	/**
	 * 根据Id查询类型
	 * @param id
	 * @return
	 */
	public MachinesTypeBean findTypeById(Long id);
	/**
	 * 查询机器类型有效的机器类型
	 * @return
	 */
	public ReturnDataUtil findAllByState();

}

