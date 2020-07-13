package com.server.module.system.baseManager.itemType;

import java.util.List;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-04-10 14:28:15
 */
public interface ItemTypeDao {

	public ReturnDataUtil listPage(ItemTypeCondition condition);

	public List<ItemTypeBean> list(ItemTypeCondition condition);

	public boolean update(ItemTypeBean entity);

	public boolean delete(Object id);

	public ItemTypeBean get(Object id);

	public ItemTypeBean insert(ItemTypeBean entity);
	
	public Long getItemTypeId(String name);
}
