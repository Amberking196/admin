package com.server.module.customer.product;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
public interface ShoppingGoodsService {

	/**
	 * 
	 * 查询商城商品列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(ShoppingGoodsForm shoppingGoodsForm);
	/**
	 *
	 * 查询商城商品套餐列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil mealListPage(ShoppingGoodsForm shoppingGoodsForm);

	/**
	 * 查询商品是（否）绑定的机器集合
	 * @author why
	 * @date 2019年2月15日 上午9:14:58 
	 * @param shoppingGoodsVmCodeForm
	 * @return
	 */
	public ReturnDataUtil vmiList(ShoppingGoodsVmCodeForm shoppingGoodsVmCodeForm);

	/**
	 * 修改商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:18:23 
	 * @param entity
	 * @return
	 */
	public boolean update(ShoppingGoodsBean entity);

	/**
	 * 删除商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:18:32 
	 * @param id
	 * @return
	 */
	public boolean del(Object id);

	/**
	 * 查询商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:18:42 
	 * @param id
	 * @return
	 */
	public ShoppingGoodsBean get(Object id);

	/**
	 * 增加商品信息
	 * @author why
	 * @date 2019年2月15日 上午9:18:52 
	 * @param entity
	 * @return
	 */
	public ShoppingGoodsBean add(ShoppingGoodsBean entity);
	
	/**
	 * 判断商城商品名称 是否存在
	 * @param name
	 * @return
	 */
	public boolean checkName(ShoppingGoodsBean entity);
	
	
	/**
	 * 判断商城商品条形码 是否存在
	 * @param barCode
	 * @return
	 */
	public boolean checkBarcode(ShoppingGoodsBean entity);
	
	/**
	 * 查询商品信息 给手机端显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	public ReturnDataUtil list(ShoppingGoodsForm shoppingGoodsForm);

	/**
	 * 查询珠海华发商品信息 手机端显示
	 * @param shoppingGoodsForm
	 * @return
	 */
	public ReturnDataUtil huaFaAppList(ShoppingGoodsForm shoppingGoodsForm);
	
	/**
	 * 查询售货机货道商品 下拉框 展示
	 * @return
	 */
	public ReturnDataUtil itemBasicListPage();
	
	/**
	 * 处理图片名称
	 * @return
	 */
	public String findImgName(String fileName);
	
	/**
	 * 手机端商品详情显示
	 * @param id
	 * @return
	 */
	public ShoppingGoodsBean productDetails(Long id,Long spellgroupId);
	
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
	 * 
	 * 查询商城优惠券商品列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil couponListPage(ShoppingGoodsForm shoppingGoodsForm, CouponForm couponForm);

	/**
	 * 
	 * 校验商城商品机器编码
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil checkVmCode(ShoppingGoodsBean entity);
	/**
	 * 
	 * 绑定机器
	 * @param entity
	 * @return
	 */
	public boolean bindVmCode(ShoppingGoodsVmCodeForm entity);
	/**
	 * 
	 * 绑定提水券
	 * @param entity
	 * @return
	 */
	public ReturnDataUtil bindCarryWater(ShoppingGoodsVmCodeForm entity);

	/**
	 * 根据商品id 查询购物车中商品名称
	 * @author why
	 * @date 2019年3月16日 上午11:43:59 
	 * @param product
	 * @return
	 */
    public List<String> findItemName(String product);
    
    /**
	 * 根据product查shoppingGoogsName
	 * @author why
	 * @date 2019年2月16日 下午5:39:01 
	 * @param product
	 * @return
	 */
	public String findShoppingGoogsName(String product);
}
