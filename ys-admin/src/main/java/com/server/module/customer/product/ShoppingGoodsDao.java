package com.server.module.customer.product;

import java.util.List;
import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
public interface ShoppingGoodsDao {

	/**
	 * 查询商城商品列表
	 * @param shoppingGoodsForm
	 * @return
	 */
	public ReturnDataUtil listPage(ShoppingGoodsForm shoppingGoodsForm);
	/**
	 * 查询商城商品套餐列表
	 * @param shoppingGoodsForm
	 * @return
	 */
	public ReturnDataUtil mealListPage(ShoppingGoodsForm shoppingGoodsForm);
	/**
	 * 增加商城商品
	 * @param entity
	 * @return
	 */
	public ShoppingGoodsBean insert(ShoppingGoodsBean entity);
	

	/**
	 * 判断商品名称是否存在
	 * @param name
	 * @return
	 */
	public boolean checkName(String sql);
	
	/**
	 * 判断商品条形码是否存在
	 * @param name
	 * @return
	 */
	public boolean checkBarcode(String sql);
	
	/**
	 * 修改商品信息
	 * @param entity
	 * @return
	 */
	public boolean update(ShoppingGoodsBean entity);

	/**
	 * 修改商品信息的提水券关联
	 * @param entity
	 * @return
	 */
	public boolean updateVouchersId(ShoppingGoodsBean entity);
	
	/**
	 * 删除商品信息
	 * @param id
	 * @return
	 */
	public boolean delete(Object id);

	/**
	 * 通过商品id 获取对象信息
	 * @param id
	 * @return
	 */
	public ShoppingGoodsBean get(Object id);
	
	/**
	 * 查询商品信息 给手机端显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	public List<ShoppingGoodsBean> list(ShoppingGoodsForm shoppingGoodsForm,VendingMachinesInfoBean vmi);

	/**
	 * 查询珠海华发商品信息 给手机端显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	public List<ShoppingGoodsBean> huaFaAppList(ShoppingGoodsForm shoppingGoodsForm,VendingMachinesInfoBean vmi);

	/**
	 * 查询售货机货道商品 下拉框 展示
	 * @return
	 */
	public ReturnDataUtil itemBasicListPage();
	
	
	/**
	 * 商品详情 给手机端显示
	 * @param id
	 * @return
	 */
	public ShoppingGoodsBean productDetails(Long goodsId,Long spellgroupId);
	
	
	/**
	 * 增加图片素材
	 * @param bean
	 * @return
	 */
	public boolean addPictureMaterial(PictureMaterialBean bean);
	
	/**
	 * 查询素材
	 * @param pictureMaterialForm
	 * @return
	 */
	public ReturnDataUtil pictureListPage(PictureMaterialForm pictureMaterialForm);
	
	/**
	 * 查询广告商品 
	 * @return
	 */
	public List<ShoppingGoodsBean> findAdvertisingShopping();
	
	/**
	 * 修改库存
	 * @author hebiting
	 * @date 2018年7月26日下午5:07:28
	 * @return
	 */
	public boolean changeInventory(Integer num,Long id);

	/**
	 * 查询优惠券商品列表
	 * @author hjc
	 * @date 2018年8月26日下午5:07:28
	 * @return
	 */
	public ReturnDataUtil couponListPage(ShoppingGoodsForm shoppingGoodsForm, CouponForm couponForm);


	/**
	 * 判断是否是砍价商品
	 * @author why
	 * @date 2018年12月25日 下午5:33:48 
	 * @param id
	 * @return
	 */
	public Long checkBargainGoods(Object id );

	/**
	 * 查询商品是（否）绑定的机器集合
	 * @author why
	 * @date 2019年2月15日 上午9:11:58 
	 * @param shoppingGoodsVmCodeForm
	 * @return
	 */
	public ReturnDataUtil vmiList(ShoppingGoodsVmCodeForm shoppingGoodsVmCodeForm);

	/**
	 * 根据商品id 查询购物车中商品名称
	 * @author why
	 * @date 2019年3月16日 上午11:45:35 
	 * @param product
	 * @return
	 */
	public List<String> findItemName(String product);

	/**
     * 拼团支付 根据商品id查询商品名称
     * @author why
     * @date 2019年2月18日 下午2:38:29 
     * @param product
     * @return
     */
	public String findShoppingGoogsName(String product);

}
