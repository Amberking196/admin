package com.server.module.system.promotionManage.promotionActivity;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-08-22 16:51:38
 */
public interface PromotionActivityService {

	/**
	 * 促销活动列表 查询
	 * @param promotionActivityForm
	 * @return
	 */
	public ReturnDataUtil listPage(PromotionActivityForm promotionActivityForm);

	public List<PromotionActivityBean> list(PromotionActivityForm condition);

	/**
	 * 促销活动 修改
	 * @param entity
	 * @return
	 */
	public boolean update(PromotionActivityBean entity);

	/**
	 * 删除促销活动
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	public PromotionActivityBean get(Object id);

	/**
	 * 促销活动增加
	 * @param entity
	 * @return
	 */
	public PromotionActivityBean add(PromotionActivityBean entity);
}
