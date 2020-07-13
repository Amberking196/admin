package com.server.module.system.machineManage.machineType;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.server.util.ReturnDataUtil;


/**
 * author name: yjr
 * create time: 2018-03-22 13:31:26
 */
@Service
public class MachinesTypeServiceImpl implements MachinesTypeService {

	public static final String CACHE_KEY = "MachinesTypeBean";

	public static Logger log = LogManager.getLogger(MachinesTypeServiceImpl.class);
	@Autowired
	private MachinesTypeDao machinesTypeDaoImpl;
    
	//根据id查询机器类型
	@Cacheable(value = CACHE_KEY, key = "#id")
	public MachinesTypeBean get(Integer id) {
		try {
			return machinesTypeDaoImpl.get(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	//添加机器类型
	public MachinesTypeBean insert(MachinesTypeBean type) {
		//设置时间
		type.setCreateTime(new Date());
		return machinesTypeDaoImpl.insert(type);
	}

	//修改机器类型
	public boolean update(MachinesTypeBean entity) {
		return machinesTypeDaoImpl.updateEntity(entity);
	}

	//删除机器类型
	public boolean del(MachinesTypeBean entity) {
		return machinesTypeDaoImpl.del(entity);

	}
    //分页查询机器类型
	public ReturnDataUtil listPage(MachinesTypeCondition condition) {
		return machinesTypeDaoImpl.listPage(condition);
	}

	@Override
	public ReturnDataUtil checkOnlyone(MachinesTypeBean entity) {
		
		return machinesTypeDaoImpl.checkOnlyOne(entity);
	}

	@Override
	public MachinesTypeBean findTypeById(Long id) {
		
		return machinesTypeDaoImpl.findTypeById(id);
	}

	@Override
	public ReturnDataUtil findAllByState() {
		// TODO Auto-generated method stub
		return machinesTypeDaoImpl.findAllByState();
	}
	

}
