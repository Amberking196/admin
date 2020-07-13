package com.server.module.customer.product.goodsBargain;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-21 10:31:41
 */
public interface GoodsBargainDao {

	/**
	 * 商品砍价设置列表
	 * @author why
	 * @date 2019年2月15日 上午9:58:57 
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil listPage(GoodsBargainCondition condition);

    /**
     * 商品砍价设置列表
     * @author why
     * @date 2019年2月15日 上午9:59:14 
     * @param condition
     * @return
     */
    public List<GoodsBargainBean> list(GoodsBargainCondition condition);

    /**
     * 修改商品砍价设置
     * @author why
     * @date 2019年2月15日 上午9:59:23 
     * @param entity
     * @return
     */
    public boolean update(GoodsBargainBean entity);

    /**
     * 删除商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:17:04 
     * @param id
     * @return
     */
    public boolean delete(Object id);
    
    /**
     * 查询商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:17:18 
     * @param id
     * @return
     */
    public GoodsBargainBean get(Object id);

    /**
     * 增加商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:17:32 
     * @param entity
     * @return
     */
    public GoodsBargainBean insert(GoodsBargainBean entity);
}

