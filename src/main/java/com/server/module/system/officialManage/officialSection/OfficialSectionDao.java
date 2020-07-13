package com.server.module.system.officialManage.officialSection;

import java.util.List;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 
public interface  OfficialSectionDao{

	/**
	 * 查询官网栏目列表
	 * @param officialSectionForm 
	 * return 
	 */
	public List<OfficialSectionBean> listPage(OfficialSectionForm officialSectionForm);
	public boolean update(OfficialSectionBean entity);
	public boolean delete(Object id);
	public OfficialSectionBean get(Object id);
	public OfficialSectionBean insert(OfficialSectionBean entity);
	public List<OfficialSectionBean> findSonSectionList(OfficialSectionBean officialSectionBean);
}

