package com.server.module.system.itemManage.itemBasic;

import java.util.List;
import java.util.Map;

import com.server.util.ReturnDataUtil;
/**
 * author name: why
 * create time: 2018-04-10 14:22:54
 */ 
public interface  ItemBasicDao{

	/**
	 *  添加商品
	 * @param bean
	 * @return
	 */
	public ItemBasicBean insert(ItemBasicBean bean);
	
	/**
	 * 查询 商品基础信息
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(ItemBasicCondition condition);

	/**
	 * 查询商品基础信息 for 我的商品界面
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil myListPage(MyItemCondition condition);
    /**
     * 查询我公司及旗下所有子公司的商品
     * @param condition
     * @param companyIds
     */
	public ReturnDataUtil myOwnListPage(MyItemCondition condition, String companyIds);


	/**
	 *  通过商品ID 得到商品信息 
	 * @param id
	 * @return
	 */
	public ItemBasicBean getItemBasic(Object id);
	
	/**
	 *  修改商品信息
	 * @param entity
	 * @return
	 */
	public boolean updateEntity(ItemBasicBean entity);
	
	/**
	 * 判断商品条形码是否存在
	 * @param barCode
	 * @return
	 */
	public  ItemBasicBean checkBarcode(String barCode);
	
	/**
	 * 查询图片路径 进行下载
	 * @return
	 */
	public List<String> getPic();
	
	/**
	 * 根据条形码 查询商品信息
	 * @param barCode
	 * @return
	 */
	public List<ItemBasicBean> findItemBasic(ItemBasicCondition condition);

	/**
	 * 查询所有商品  下拉框用
	 * @return
	 */
	public List<Map<String,Object>> listAllItem();

	/**
	 * 根据商品名 模糊查询
	 * @param name
	 * @return
	 */
	public List<ItemBasicBean> getItemBasic(ItemBasicCondition condition);
	/**
	 * 从商品信息中根据条形码模糊查询商品信息
	 * @param condition
	 * @return
	 */
	public List<ItemBasicBean> findItemBasicByCode(ItemBasicCondition condition);
	/**
	 * 查询相似商品列表
	 * @param condition
	 * @return
	 */
	public List<ItemBasicBean> getItemConnect(ItemBasicCondition condition);
	/**
	 * 绑定相似商品
	 * @param basicItemId,basicItemIds
	 * @return
	 */
    public void addAll(Integer basicItemId,List<Integer> basicItemIds);

	/**
	 * 解除绑定相似商品
	 * @param ids
	 * @return
	 */
    public void delAll(Integer basicItemId,List<Integer> ids);
    
}

