package com.server.module.system.promotionManage.timeQuantum;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-22 10:56:46
 */
public interface TimeQuantumDao {

	/**
	 * 时间段列表查询
	 * @param timeQuantumForm
	 * @return
	 */
	public ReturnDataUtil listPage(TimeQuantumForm timeQuantumForm);

	/**
	 *  通过多个id 查询时间段
	 * @param ids
	 * @return
	 */
	public List<TimeQuantumBean> list(String ids);

	/**
	 * 时间段列表修改
	 * @param entity
	 * @return
	 */
	public boolean update(TimeQuantumBean entity);

	/**
	 * 时间段 删除
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	
	public TimeQuantumBean get(Object id);

	/**
	 * 时间段增加
	 * @param entity
	 * @return
	 */
	public TimeQuantumBean insert(TimeQuantumBean entity);
	
	
	/**
	 * 条件查询时间段信息
	 * @author why
	 * @date 2018年10月30日 下午5:24:24 
	 * @param timeQuantumForm
	 * @return
	 */
	public List<TimeQuantumBean> findBean(TimeQuantumForm timeQuantumForm);
}
