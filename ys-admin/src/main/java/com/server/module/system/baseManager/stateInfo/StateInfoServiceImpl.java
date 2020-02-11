package com.server.module.system.baseManager.stateInfo; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import com.server.module.system.adminUser.AdminUserServiceImp;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr 
 * create time: 2018-03-30 11:10:15
 */
@Service
public class StateInfoServiceImpl implements StateInfoService {


	public static Logger log = LogManager.getLogger(AdminUserServiceImp.class); 	    
	@Autowired
	private StateInfoDao stateInfoDaoImpl;

    @Autowired
    private ReturnDataUtil returnDataUtil;
    
	public ReturnDataUtil listPage(StateInfoCondition condition) {
		log.info("<StateInfoServiceImpl>--<listPage>--start"); 
		returnDataUtil= stateInfoDaoImpl.listPage(condition);
		log.info("<StateInfoServiceImpl>--<listPage>--end"); 
		return returnDataUtil;
	}
	@CacheEvict(value = {"stateInfoDtoList","stateInfoName"})
	public StateInfoBean add(StateInfoBean entity) {
		log.info("<StateInfoServiceImpl>--<add>--start");
		entity.setCreateTime(new Date());//设置创建时间
		StateInfoBean re= stateInfoDaoImpl.insert(entity);
		log.info("<StateInfoServiceImpl>--<add>--end"); 
		return re;
	}
	@CacheEvict(value = {"stateInfoDtoList","stateInfoName"})
	public boolean update(StateInfoBean entity) {
		log.info("<StateInfoServiceImpl>--<update>--start"); 
		boolean re= stateInfoDaoImpl.update(entity);
		log.info("<StateInfoServiceImpl>--<update>--end"); 
		return re;
	}
	@CacheEvict(value = {"stateInfoDtoList","stateInfoName"})
	public boolean del(Object id) {
		log.info("<StateInfoServiceImpl>--<del>--start"); 
		boolean re= stateInfoDaoImpl.delete(id);
		log.info("<StateInfoServiceImpl>--<del>--end"); 
		return re;
	}

	public List<StateInfoBean> list(StateInfoCondition condition) {
		return null;
	}

	public StateInfoBean get(Object id) {
		log.info("<StateInfoServiceImpl>--<get>--start"); 
		StateInfoBean re=stateInfoDaoImpl.get(id);
		log.info("<StateInfoServiceImpl>--<get>--end"); 
		return re;
	}
	/**
	 * 获取某类别字典
	 */
	@Cacheable(value = "stateInfoDtoList", key = "#keyName")
	public List<StateInfoDto> findStateInfoByKeyName(String keyName) {
		log.info("<StateInfoServiceImpl>--<findStateInfoByKeyName>--start"); 
		 List<StateInfoDto> relist=stateInfoDaoImpl.findStateInfoByKeyName(keyName);
		log.info("<StateInfoServiceImpl>--<findStateInfoByKeyName>--end"); 
		return relist;
	}
	/**
	 * 获取字典值字典的名称
	 */
	@Cacheable(value = "stateInfoName", key = "#state")
	public String getNameByState(Long state) {
		log.info("<StateInfoServiceImpl>--<getNameByState>--start"); 
		StateInfoBean bean= stateInfoDaoImpl.getStateInfoByState(state);
		log.info("<StateInfoServiceImpl>--<getNameByState>--end"); 
		if(bean!=null)
			return bean.getName();
		return null;
	}
	/**
	 * 获取字典的状态码
	 */
	@Override
	public Long getStateId(String name) {
		// TODO Auto-generated method stub
		log.info("<StateInfoServiceImpl>--<getStateId>--start"); 
		Long re =stateInfoDaoImpl.getStateId(name);
		log.info("<StateInfoServiceImpl>--<getStateId>--end"); 
		return re;
	}
	
	/**
	 * 查询仓库状态
	 */
	@Override
	public List<StateInfoBean> getWarehouseState() {
		log.info("<StateInfoServiceImpl>--<getWarehouseState>--start");
		List<StateInfoBean> list = stateInfoDaoImpl.getWarehouseState();
		log.info("<StateInfoServiceImpl>--<getWarehouseState>--end");
		return list;
	}
	
	/**
	 *  查询出入库的状态
	 */
	public List<StateInfoBean> getWarehouseWarrantState(){
		log.info("<StateInfoServiceImpl>--<getWarehouseWarrantState>--start");
		List<StateInfoBean> list = stateInfoDaoImpl.getWarehouseWarrantState();
		log.info("<StateInfoServiceImpl>--<getWarehouseWarrantState>--end");
		return list;
	}
	/**
	 *  查询入库的类型
	 */
	public List<StateInfoBean> getWarehouseWarrantType(){
		log.info("<StateInfoServiceImpl>--<getWarehouseWarrantType>--start");
		List<StateInfoBean> list = stateInfoDaoImpl.getWarehouseWarrantType();
		log.info("<StateInfoServiceImpl>--<getWarehouseWarrantType>--end");
		return list;
	}
	
	/**
	 *  查询出库的状态
	 */
	public List<StateInfoBean> getWarehouseRemovalState(){
		log.info("<StateInfoServiceImpl>--<getWarehouseRemovalState>--start");
		List<StateInfoBean> list = stateInfoDaoImpl.getWarehouseRemovalState();
		log.info("<StateInfoServiceImpl>--<getWarehouseRemovalState>--end");
		return list;
	}
	
	/**
	 *  查询出库的类型
	 */
	public List<StateInfoBean> getWarehouseRemovalType(){
		log.info("<StateInfoServiceImpl>--<getWarehouseRemovalType>--start");
		List<StateInfoBean> list = stateInfoDaoImpl.getWarehouseRemovalType();
		log.info("<StateInfoServiceImpl>--<getWarehouseRemovalType>--end");
		return list;
	}
	
	/**
	 *  查询归还的类型
	 * @return
	 */
	public List<StateInfoBean> getWarehouseReturnType(){
		log.info("<StateInfoServiceImpl>--<getWarehouseReturnType>--start");
		List<StateInfoBean> list = stateInfoDaoImpl.getWarehouseReturnType();
		log.info("<StateInfoServiceImpl>--<getWarehouseReturnType>--end");
		return list;
	}
	@Override
	public ReturnDataUtil checkStateOnlyOne(StateInfoBean entity) {
		
		return stateInfoDaoImpl.checkStateOnlyOne(entity);
	}
}
