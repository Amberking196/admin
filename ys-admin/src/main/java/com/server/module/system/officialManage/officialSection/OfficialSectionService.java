package com.server.module.system.officialManage.officialSection;

import java.util.List;
import com.server.util.ReturnDataUtil;/**
 * author name: hjc
 * create time: 2018-08-01 10:02:19
 */ 
public interface  OfficialSectionService{

	/**
	 * 查询官网栏目列表
	 * @param officialSectionForm 
	 * return 
	 */
	public ReturnDataUtil listPage(OfficialSectionForm officialSectionForm);
	/**
	 * 更新栏目
	 * @param OfficialSectionBean 
	 * return 
	 */
	public boolean update(OfficialSectionBean entity);
	/**
	 * 标志删除栏目
	 * @param id 
	 * return 
	 */
	public ReturnDataUtil del(Object id);
	public OfficialSectionBean get(Object id);
	public OfficialSectionBean add(OfficialSectionBean entity);
	public ReturnDataUtil updateSonSection(OfficialSectionBean officialSectionBean);
	//List<OfficialSectionBean> findSonSectionList(OfficialSectionBean officialSectionBean);
}

