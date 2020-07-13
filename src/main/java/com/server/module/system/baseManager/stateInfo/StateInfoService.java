package com.server.module.system.baseManager.stateInfo;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-03-30 11:10:15
 */
public interface StateInfoService {

	public ReturnDataUtil listPage(StateInfoCondition condition);

	public List<StateInfoBean> list(StateInfoCondition condition);

	public boolean update(StateInfoBean entity);

	public boolean del(Object id);

	public StateInfoBean get(Object id);

	public StateInfoBean add(StateInfoBean entity);

	List<StateInfoDto> findStateInfoByKeyName(String keyName);

	public String getNameByState(Long state);

	public Long getStateId(String name);
	
	/**
	 *  查询仓库的状态
	 * @return
	 */
	public List<StateInfoBean> getWarehouseState();
	
	/**
	 *  查询出入库的状态
	 * @return
	 */
	public List<StateInfoBean> getWarehouseWarrantState();
	
	/**
	 *  查询入库的类型
	 * @return
	 */
	public List<StateInfoBean> getWarehouseWarrantType();
	
	/**
	 *  查询出库的状态
	 * @return
	 */
	public List<StateInfoBean> getWarehouseRemovalState();
	
	/**
	 *  查询出库的类型
	 * @return
	 */
	public List<StateInfoBean> getWarehouseRemovalType();
	
	/**
	 *  查询归还的类型
	 * @return
	 */
	public List<StateInfoBean> getWarehouseReturnType();
	/**
	 * 验证字典状态是否唯一
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil checkStateOnlyOne(StateInfoBean entity);
}
