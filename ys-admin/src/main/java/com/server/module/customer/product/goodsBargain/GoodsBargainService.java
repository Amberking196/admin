package com.server.module.customer.product.goodsBargain;

import java.util.List;

import com.server.util.ReturnDataUtil;

/**
 * author name: yjr
 * create time: 2018-12-21 10:31:41
 */
public interface GoodsBargainService {

	/**
	 * 商品砍价设置列表
	 * @author why
	 * @date 2019年2月15日 上午10:18:13 
	 * @param condition
	 * @return
	 */
    public ReturnDataUtil listPage(GoodsBargainCondition condition);

    /**
     * 商品砍价设置列表
     * @author why
     * @date 2019年2月15日 上午10:18:22 
     * @param condition
     * @return
     */
    public List<GoodsBargainBean> list(GoodsBargainCondition condition);

    /**
     * 修改商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:18:25 
     * @param entity
     * @return
     */
    public boolean update(GoodsBargainBean entity);

    /**
     * 删除商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:18:38 
     * @param id
     * @return
     */
    public boolean del(Object id);

    /**
     * 查询商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:18:43 
     * @param id
     * @return
     */
    public GoodsBargainBean get(Object id);

    /**
     * 增加商品砍价设置
     * @author why
     * @date 2019年2月15日 上午10:18:48 
     * @param entity
     * @return
     */
    public GoodsBargainBean add(GoodsBargainBean entity);
}

