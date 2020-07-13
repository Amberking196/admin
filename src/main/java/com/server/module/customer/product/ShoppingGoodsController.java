package com.server.module.customer.product;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.module.customer.order.ShoppingBean;
import com.server.redis.RedisClient;
import com.server.util.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.Args;
import org.apache.log4j.chainsaw.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.common.paramconfig.AlipayConfig;
import com.server.module.customer.car.ShoppingCarService;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.couponManager.coupon.CouponAddVo;
import com.server.module.system.couponManager.coupon.CouponBean;
import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.module.system.couponManager.coupon.CouponService;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerDao;
import com.server.module.system.couponManager.couponCustomer.CustomerCouponVo;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.richText.Result;
import com.server.util.richText.ResultRichTextUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
@Api(value = "ShoppingGoodsController", description = "商城商品")
@RestController
@RequestMapping("/shoppingGoods")
public class ShoppingGoodsController {

	private static Logger log = LogManager.getLogger(ShoppingGoodsController.class);
	@Autowired
	private ShoppingGoodsService shoppingGoodsServiceImpl;
	@Autowired
	private ShoppingCarService shoppingCarServiceImpl;
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private CouponService couponServiceImpl;
	@Autowired
	private CouponCustomerDao couponCustomerDao;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private ShoppingGoodsMealDao shoppingGoodsMealDao;

	
	@ApiOperation(value = "商城商品列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsController>----<listPage>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if (shoppingGoodsForm == null) {
			shoppingGoodsForm = new ShoppingGoodsForm();
		}
		returnDataUtil = shoppingGoodsServiceImpl.listPage(shoppingGoodsForm);
		log.info("<ShoppingGoodsController>----<listPage>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "商城商品列表", notes = "listPageDetail", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPageDetail", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPageDetail(@RequestBody(required = false) ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsController>----<listPage>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if (shoppingGoodsForm == null) {
			shoppingGoodsForm = new ShoppingGoodsForm();
		}
		returnDataUtil = shoppingGoodsServiceImpl.mealListPage(shoppingGoodsForm);
		log.info("<ShoppingGoodsController>----<listPage>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "修改商城商品套餐", notes = "saveMeal", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/saveMeal", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil saveMeal(@RequestBody ShoppingGoodsMealList shoppingGoodsMealList) {
		log.info("<ShoppingGoodsController>----<listPage>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();

		List<ShoppingGoodsMeal> newList=shoppingGoodsMealList.getShoppingGoodsMeal();
		ShoppingGoodsForm shoppingGoodsForm=new ShoppingGoodsForm();
		shoppingGoodsForm.setGoodsId(shoppingGoodsMealList.getGoodsId());
		Set<Integer> oldSet=new HashSet<Integer>();
		//用来保存查询传入的结果Id
		Set<Integer> newSet=new HashSet<Integer>();

		List<ShoppingGoodsMeal> oldList= (List<ShoppingGoodsMeal>)shoppingGoodsServiceImpl.mealListPage(shoppingGoodsForm).getReturnObject();

		for(ShoppingGoodsMeal s:newList){
			if(s.getId()==null){
				shoppingGoodsMealDao.insert(s);
			}
			newSet.add(s.getId().intValue());
			for(ShoppingGoodsMeal s1:oldList){
				oldSet.add(s1.getId().intValue());
				if (s.getId().equals(s1.getId())) {
					shoppingGoodsMealDao.update(s);
				}
			}
		}

		//求差集  进行删除
		oldSet.removeAll(newSet);
		for (Integer i : oldSet) {
			shoppingGoodsMealDao.delete(i);
		}

		log.info("<ShoppingGoodsController>----<listPage>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "商城商品添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsController>----<add>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		// 判断商城商品名称是否已经存在
		boolean flag = shoppingGoodsServiceImpl.checkName(entity);
		if (flag) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该公司下，商品名称已存在，请重新输入！");
		} else {
			// 判断商城商品条形码是否已经存在
			boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
			if (flagBarCode) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该公司下，商品条形码已存在，请重新输入！");
			} else {
				if (entity.getVmCode() != null && StringUtil.isNotBlank(entity.getVmCode())
						&& !entity.getVmCode().equals("0")) {
					VendingMachinesInfoBean bean = vendingMachinesInfoService
							.findVendingMachinesByCode(entity.getVmCode());
					if (bean == null) {
						returnDataUtil.setStatus(-99);
						returnDataUtil.setMessage("不存在的机器编码");
						returnDataUtil.setReturnObject(false);
						return returnDataUtil;
					}
				} 
				entity.setCreateUser(UserUtils.getUser().getId());
				ShoppingGoodsBean bean = shoppingGoodsServiceImpl.add(entity);
				String redisKey = ("zhuhaihuafa-shoppinggoods");
				//已进行测试，应该没问题
				if (bean != null  && bean.getState()==5100) {
					if (bean.getCompanyId()==83 || bean.getCompanyId()==84 || bean.getCompanyId()==131 || bean.getCompanyId()==118){
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("商城商品增加成功,并重置redis数据");
						redisClient.del(redisKey);
					}
				}
				else if (bean != null){
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("商城商品增加成功！");
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("商城商品增加失败！");
				}
			}
		}

		log.info("<ShoppingGoodsController>----<add>----end");
		return returnDataUtil;
	}

	
	
	
	
	/**
	 * 后台操作上传 商品图片
	 * 
	 * @param file
	 * @return 上传成功信息
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<shoppingGoods>--<uploadImage>--start");
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 设置图片的名称
		String imgName = shoppingGoodsServiceImpl.findImgName(filePath);
		// 这是文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getShoppingGoodsImg() + imgName;
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			log.info("<shoppingGoods>--<uploadImage>--end");

		}
		log.info("imgname=============" + imgName);
		return imgName;
	}

	/**
	 * 商品详情查看
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "商城商品查看", notes = "findBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findBean(@RequestBody Map<String, Integer> id) {
		log.info("<ShoppingGoodsController>----<findBean>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		ShoppingGoodsBean bean = shoppingGoodsServiceImpl.get(id.get("id"));
		if (bean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("查看成功!");
			returnDataUtil.setReturnObject(bean);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("查看失败!");
		}
		log.info("<ShoppingGoodsController>----<findBean>----end");
		return returnDataUtil;
	}

	/**
	 * 商城商品修改
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "商城商品修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsController>----<update>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(entity.getId());
		String redisKey = ("zhuhaihuafa-shoppinggoods");
		MD5Utils md5Utils = new MD5Utils();
		String detailsKey = "DETAILS" + md5Utils.getMD5(entity.getId().toString());
		if (!(shoppingGoodsBean.getName().equals(entity.getName()))) {
			log.info("<ShoppingGoodsController>----1111111111111111-------------");
			// 判断商城商品名称是否已经存在
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品名称已存在，请重新输入,修改失败！");
			} else {
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				if (shoppingGoodsServiceImpl.checkVmCode(entity).getStatus() == -99) {
					returnDataUtil.setStatus(-99);
					returnDataUtil.setMessage("不存在的机器编码");
					returnDataUtil.setReturnObject(false);
					return returnDataUtil;
				}
				boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
				if (flagUpdate) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("修改成功！");
					returnDataUtil.setReturnObject(flagUpdate);
					//重置商品列表缓存
					redisClient.del(redisKey);
					//重置商品详情缓存
					redisClient.del(detailsKey);
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("修改失败!");
					returnDataUtil.setReturnObject(flagUpdate);
				}
			}
		} else if (!(shoppingGoodsBean.getBarCode().equals(entity.getBarCode()))) {
			log.info("<ShoppingGoodsController>------2222222222222222222222222222222-------------");
			// 判断商城商品条形码是否已经存在
			boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
			if (flagBarCode) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品条形码已存在，请重新输入,修改失败！");
			} else {
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				if (shoppingGoodsServiceImpl.checkVmCode(entity).getStatus() == -99) {
					returnDataUtil.setStatus(-99);
					returnDataUtil.setMessage("不存在的机器编码");
					returnDataUtil.setReturnObject(false);
					return returnDataUtil;
				}
				boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
				if (flagUpdate) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("修改成功");
					returnDataUtil.setReturnObject(flagUpdate);
					redisClient.del(redisKey);
					redisClient.del(detailsKey);
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("修改失败!");
					returnDataUtil.setReturnObject(flagUpdate);
				}
			}
		} else if (!(shoppingGoodsBean.getTarget().equals(entity.getTarget()))
				&& !(shoppingGoodsBean.getName().equals(entity.getName()))&&
				!(shoppingGoodsBean.getBarCode().equals(entity.getBarCode()))) {
			log.info("<ShoppingGoodsController>------333333333333333333333333-------------");
			// 判断商城商品名称是否已经存在
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品名称已存在，请重新输入,修改失败！");
			} else {
				// 判断商城商品条形码是否已经存在
				boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
				if (flagBarCode) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("商品条形码已存在，请重新输入,修改失败！");
				} else {
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(UserUtils.getUser().getId());
					if (shoppingGoodsServiceImpl.checkVmCode(entity).getStatus() == -99) {
						returnDataUtil.setStatus(-99);
						returnDataUtil.setMessage("不存在的机器编码");
						returnDataUtil.setReturnObject(false);
						return returnDataUtil;
					}
					boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
					if (flagUpdate) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("修改成功！");
						returnDataUtil.setReturnObject(flagUpdate);
						redisClient.del(redisKey);
						redisClient.del(detailsKey);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("修改失败!");
						returnDataUtil.setReturnObject(flagUpdate);
					}
				}
			}

		} else if (entity.getTarget().equals(2) && !entity.getAreaId().equals(shoppingGoodsBean.getAreaId())) {
			log.info("<ShoppingGoodsController>------444444444444444444444444444-------------");
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该区域下商品名称已存在，请重新输入,修改失败！");
			} else {
				// 判断商城商品条形码是否已经存在
				boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
				if (flagBarCode) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("该区域下商品条形码已存在，请重新输入,修改失败！");
				} else {
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(UserUtils.getUser().getId());
					boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
					if (flagUpdate) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("修改成功！");
						returnDataUtil.setReturnObject(flagUpdate);
						redisClient.del(redisKey);
						redisClient.del(detailsKey);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("修改失败!");
						returnDataUtil.setReturnObject(flagUpdate);
					}
				}
			}
		} else if (entity.getTarget().equals(3) && !entity.getVmCode().equals(shoppingGoodsBean.getVmCode())) {
			log.info("<ShoppingGoodsController>------5555555555555555555555-------------");
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该机器上商品名称已存在，请重新输入,修改失败！");
			} else {
				// 判断商城商品条形码是否已经存在
				boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
				if (flagBarCode) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("该机器上商品条形码已存在，请重新输入,修改失败！");
				} else {
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(UserUtils.getUser().getId());
					if (shoppingGoodsServiceImpl.checkVmCode(entity).getStatus() == -99) {
						returnDataUtil.setStatus(-99);
						returnDataUtil.setMessage("不存在的机器编码");
						returnDataUtil.setReturnObject(false);
						return returnDataUtil;
					}
					boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
					if (flagUpdate) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("修改成功！");
						returnDataUtil.setReturnObject(flagUpdate);
						redisClient.del(redisKey);
						redisClient.del(detailsKey);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("修改失败!");
						returnDataUtil.setReturnObject(flagUpdate);
					}
				}
			}
		}else {
			log.info("<ShoppingGoodsController>------66666666666666666666-------------");
			entity.setUpdateTime(new Date());
			entity.setUpdateUser(UserUtils.getUser().getId());
			/*if (shoppingGoodsServiceImpl.checkVmCode(entity).getStatus() == -99) {
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("不存在的机器编码");
				returnDataUtil.setReturnObject(false);
				return returnDataUtil;
			}*/
			boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
			if (flagUpdate) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("修改成功！");
				returnDataUtil.setReturnObject(flagUpdate);
				redisClient.del(redisKey);
				redisClient.del(detailsKey);
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("修改失败!");
				returnDataUtil.setReturnObject(flagUpdate);
			}
		}
		if (returnDataUtil.getStatus() == 1) {
			if (shoppingGoodsBean.getSalesPrice().compareTo(entity.getSalesPrice()) != 0
					|| !(shoppingGoodsBean.getName().equals(entity.getName()))) {
				boolean updatePrice = shoppingCarServiceImpl.updatePriceAndName(entity);
				if (updatePrice) {
					returnDataUtil.setMessage("修改成功且购物车中商品已更新");
					//redisClient.del(redisKey);
				} else {
					returnDataUtil.setMessage("修改成功且购物车中无商品更新");
				}
			}
		}
		return returnDataUtil;
	}

	/**
	 * 商城商品列表 手机端显示
	 * 
	 * @param shoppingGoodsForm
	 * @return
	 */
	@ApiOperation(value = "商城商品列表 手机端显示", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody(required = false) ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsController>----<list>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if (shoppingGoodsForm == null) {
			shoppingGoodsForm = new ShoppingGoodsForm();
		}
		returnDataUtil = shoppingGoodsServiceImpl.list(shoppingGoodsForm);
		log.info("<ShoppingGoodsController>----<list>----end");
		return returnDataUtil;
	}

	/**
	 * 珠海华发商城商品列表 手机端显示
	 *
	 * @param shoppingGoodsForm
	 * @return
	 */
	@ApiOperation(value = "珠海华发 手机端显示", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/huaFaAppList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil huaFaAppList(@RequestBody(required = false) ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsController>----<huaFaAppList>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		String key = "zhuhaihuafa-shoppinggoods";
		String shoppingGoods = redisClient.get(key);
		if (shoppingGoods != null && shoppingGoods.length() != 0){
				returnDataUtil.setStatus(1);
				//List<ShoppingGoodsBean> list = JSONObject.parseArray(shoppingGoods, ShoppingGoodsBean.class);
				returnDataUtil.setMessage("从redis中查询成功！！！");
				returnDataUtil.setReturnObject(JSON.parseArray(shoppingGoods));
			}
			else if (shoppingGoods ==null ){
				// 查询列表数据，并写入 redis
			    if (shoppingGoodsForm ==null){
					shoppingGoodsForm = new ShoppingGoodsForm();
					returnDataUtil.setMessage("查询列表数据，并写入 redis");
					returnDataUtil = shoppingGoodsServiceImpl.huaFaAppList(shoppingGoodsForm);
					redisClient.set(key,JSON.toJSONString(returnDataUtil.getReturnObject()));
				}else{
					redisClient.del(key);
					returnDataUtil.setMessage("查询列表数据，并写入 redis");
					returnDataUtil = shoppingGoodsServiceImpl.huaFaAppList(shoppingGoodsForm);
					redisClient.set(key,JSON.toJSONString(returnDataUtil.getReturnObject()));
				}
		}
		log.info("<ShoppingGoodsController>----<huaFaAppList>----end");
		return returnDataUtil;
	}

	/**
	 * 查询售货机货道商品 下拉框 展示
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询售货机货道商品 下拉框 展示", notes = "itemBasicListPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/itemBasicListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil itemBasicListPage() {
		log.info("<ShoppingGoodsController>----<itemBasicListPage>----start");
		ReturnDataUtil returnDataUtil = shoppingGoodsServiceImpl.itemBasicListPage();
		log.info("<ShoppingGoodsController>----<itemBasicListPage>----start");
		return returnDataUtil;
	}

	/**
	 * 关联货道商品
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "关联货道商品", notes = "itemBasicListPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/relevance", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil relevance(@RequestBody ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsController>----<relevance>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(entity.getId());
		if (entity != null) {
			// if (entity.getCompanyId().equals(shoppingGoodsBean.getCompanyId())) {
			if (entity.getName().equals(shoppingGoodsBean.getName())) {
				boolean update = shoppingGoodsServiceImpl.update(entity);
				if (update) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("关联成功!");
					returnDataUtil.setReturnObject(update);
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("关联失败!");
					returnDataUtil.setReturnObject(update);
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("选择商品名称不一致,关联失败!");
			}
			/*
			 * } else { returnDataUtil.setStatus(0);
			 * returnDataUtil.setMessage("选择商品公司不一致,关联失败!"); }
			 */
		}
		log.info("<ShoppingGoodsController>----<relevance>----end");
		return returnDataUtil;
	}

	/**
	 * 手机端商品详情显示
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "手机端商品详情显示", notes = "手机端商品详情显示", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/productDetails", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil productDetails(@RequestBody Map<String, Long> id) {
		log.info("<ShoppingGoodsController>----<productDetails>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		ShoppingGoodsBean bean = shoppingGoodsServiceImpl.productDetails(id.get("id"),id.get("spellgroupId"));
		if (bean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("手机端商品详情,查看成功!");
			returnDataUtil.setReturnObject(bean);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("手机端商品详情,查看失败!");
		}
		log.info("<ShoppingGoodsController>----<productDetails>----end");
		return returnDataUtil;
	}

	/**
	 * 珠海华发app商品详情显示
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "珠海华发app商品详情显示", notes = "珠海华发app商品详情显示", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/huaFaProductDetails", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil huaFaProductDetails(@RequestBody Map<String, Long> id) {
		log.info("<ShoppingGoodsController>----<huaFaProductDetails>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		MD5Utils md5Utils = new MD5Utils();
		String redisKey = "DETAILS" + md5Utils.getMD5(id.get("id").toString());
        System.out.println("=========================================================="+redisKey);
		String goodsDetails = redisClient.get(redisKey);
		if (goodsDetails != null){
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("从redis中获取商品详情显示成功！！！");
			returnDataUtil.setReturnObject(JSON.parse(goodsDetails));
		}else {
			ShoppingGoodsBean bean = shoppingGoodsServiceImpl.productDetails(id.get("id"),id.get("spellgroupId"));
			if (bean != null  ) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("手机端商品详情,查看成功!");
				returnDataUtil.setReturnObject(bean);
				redisClient.set(redisKey, JSON.toJSONString(bean) );
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("手机端商品详情,查看失败!");
			}
		}
		log.info("<ShoppingGoodsController>----<huaFaProductDetails>----end");
		return returnDataUtil;
	}

	/**
	 * 后台操作 上传 图片素材
	 * 
	 * @param file
	 * @return 上传成功信息
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadDetailes")
	public ReturnDataUtil uploadDetailes(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<shoppingGoods>--<uploadDetailes>--start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 设置图片的名称
		String imgName = shoppingGoodsServiceImpl.findImgName(filePath);
		// 这是文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getPictureMaterialImg() + imgName;
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			PictureMaterialBean bean = new PictureMaterialBean();
			bean.setPicName(imgName);
			bean.setCreateUser(UserUtils.getUser().getId());
			boolean flag = shoppingGoodsServiceImpl.addPictureMaterial(bean);
			if (flag) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setReturnObject(imgName);
				returnDataUtil.setMessage("图片上传成功!");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setReturnObject(imgName);
				returnDataUtil.setMessage("图片上传失败!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			log.info("<shoppingGoods>--<uploadDetailes>--end");
		}
		return returnDataUtil;
	}

	/**
	 * 图片素材列表
	 * 
	 * @param pictureMaterialForm
	 * @return
	 */
	@ApiOperation(value = "图片素材列表", notes = "pictureListPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/pictureListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil pictureListPage(@RequestBody(required = false) PictureMaterialForm pictureMaterialForm) {
		log.info("<ShoppingGoodsController>----<listPage>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if (pictureMaterialForm == null) {
			pictureMaterialForm = new PictureMaterialForm();
		}
		returnDataUtil = shoppingGoodsServiceImpl.pictureListPage(pictureMaterialForm);
		log.info("<ShoppingGoodsController>----<listPage>----end");
		return returnDataUtil;
	}

	/**
	 * 商城商品删除
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "商城商品删除", notes = "delete", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(@RequestBody List<Integer> id) {
		log.info("<ShoppingGoodsController>----<delete>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		String payIds = StringUtils.join(id, ",");
		boolean flag = shoppingGoodsServiceImpl.del(payIds);
		System.out.println("==============================="+flag);
		ShoppingGoodsBean entity = shoppingGoodsServiceImpl.get(payIds);
		String redisKey = ("zhuhaihuafa-shoppinggoods");
		if (flag = true && entity.getState()==5100 ) {
			if (entity.getCompanyId()==83 || entity.getCompanyId()==84 || entity.getCompanyId()==131 || entity.getCompanyId()==118){
				returnDataUtil.setStatus(1);
				returnDataUtil.setReturnObject(flag);
				returnDataUtil.setMessage("商品删除成功并重置redis!");
				redisClient.del(redisKey);
			}
		} else if (flag = true){
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(flag);
			returnDataUtil.setMessage("商品删除成功!");
			redisClient.del(redisKey);
		}
		else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(flag);
			returnDataUtil.setMessage("商品删除失败!");
		}
		log.info("<ShoppingGoodsController>----<delete>----end");
		return returnDataUtil;
	}

	/**
	 * 富文本图片上传
	 * 
	 * @param myFileNames
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@ApiOperation(value = "富文本图片上传", notes = "富文本图片上传")
	@RequestMapping("/uploadImg")
	public Result uploadImg(List<MultipartFile> myFileNames) throws IllegalStateException, IOException {
		log.info("<shoppingGoods>----<uploadImg>----start");
		String realName = "";
		// 保存图片路径返回给前端
		String[] str = new String[myFileNames.size()];
		if (myFileNames != null && myFileNames.size() > 0) {
			for (int i = 0; i < myFileNames.size(); i++) {
				// 得到上传的图片名称
				String fileName = myFileNames.get(i).getOriginalFilename();
				// 得到图片的后缀名
				String fileNameExtension = fileName.substring(fileName.indexOf("."), fileName.length());
				// 生成实际存储的真实文件名
				realName = UUID.randomUUID().toString() + fileNameExtension;
				// 自己定义的上传目录
				String realPath = alipayConfig.getRichTextImg().trim();

				File uploadFile = new File(realPath, realName);
				myFileNames.get(i).transferTo(uploadFile);
				String url = alipayConfig.getImageAccessPath() + realName;
				str[i] = url;
			}
		}
		log.info("<shoppingGoods>----<uploadImg>----end");
		return ResultRichTextUtil.success(str);
	}

	/**
	 * 广告商品 下拉列表
	 * 
	 * @return
	 */
	@ApiOperation(value = "广告商品 下拉列表", notes = "findAdvertisingShopping", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findAdvertisingShopping", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findAdvertisingShopping() {
		log.info("<ShoppingGoodsController>----<findAdvertisingShopping>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		List<ShoppingGoodsBean> list = shoppingGoodsServiceImpl.findAdvertisingShopping();
		returnDataUtil.setReturnObject(list);
		log.info("<ShoppingGoodsController>----<findAdvertisingShopping>----end");
		return returnDataUtil;
	}

	/**
	 * 编辑广告商品图片
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "编辑广告商品图片", notes = "updateAdvertisingPic", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateAdvertisingPic", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateAdvertisingPic(@RequestBody ShoppingGoodsBean entity) {
		boolean update = shoppingGoodsServiceImpl.update(entity);
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(update);
			returnDataUtil.setMessage("广告商品图片设置成功!");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(update);
			returnDataUtil.setMessage("广告商品图片设置失败!");
		}
		return returnDataUtil;
	}

	@ApiOperation(value = "广告商品列表", notes = "listAdvertisingPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listAdvertisingPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listAdvertisingPage(@RequestBody(required = false) ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsController>----<listAdvertisingPage>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if (shoppingGoodsForm == null) {
			shoppingGoodsForm = new ShoppingGoodsForm();
		}
		shoppingGoodsForm.setIsHomeShow(1);
		returnDataUtil = shoppingGoodsServiceImpl.listPage(shoppingGoodsForm);
		log.info("<ShoppingGoodsController>----<listAdvertisingPage>----end");
		return returnDataUtil;
	}

	
	@ApiOperation(value = "商城优惠券商品列表", notes = "couponListPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/couponListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil couponListPage(@RequestBody(required = false) Param param) {
		log.info("<ShoppingGoodsController>----<couponListPage>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		CouponForm couponForm = new CouponForm();
		ShoppingGoodsForm shoppingGoodsForm = new ShoppingGoodsForm();
		if (param == null) {
			returnDataUtil = shoppingGoodsServiceImpl.couponListPage(shoppingGoodsForm, couponForm);
		} else {
			ShoppingGoodsBean shoppingGoodsBean = param.getShoppingGoodsBean();
			if (shoppingGoodsBean.getCurrentPage() == null) {
				shoppingGoodsBean.setCurrentPage(1);
			}
			BeanUtils.copyProperties(shoppingGoodsBean, shoppingGoodsForm);
			returnDataUtil = shoppingGoodsServiceImpl.couponListPage(shoppingGoodsForm, couponForm);
		}

		log.info("<ShoppingGoodsController>----<couponListPage>----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "商城优惠券商品添加", notes = "addCoupon", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/addCoupon", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil addCoupon(@RequestBody Param param) {
		log.info("<ShoppingGoodsController>----<addCoupon>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		// ShoppingGoodsBean
		// shoppingGoodsBean=JSON.parseObject(JSON.toJSONString(models.get("shoppingGoodsBean")),ShoppingGoodsBean.class);
		// CouponAddVo
		// couponAddVo=JSON.parseObject(JSON.toJSONString(models.get("couponBean")),CouponAddVo.class);
		// 2018-8-7 转换失败
		ShoppingGoodsBean shoppingGoodsBean = param.getShoppingGoodsBean();
		CouponAddVo couponAddVo = param.getCouponBean();
		boolean flag = shoppingGoodsServiceImpl.checkName(shoppingGoodsBean);
		boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(shoppingGoodsBean);
		if (flag || flagBarCode) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该商品名称或条形码已存在，请重新输入！");
		} else {
			shoppingGoodsBean.setCreateUser(UserUtils.getUser().getId());
			if (shoppingGoodsBean.getVmCode() != null && StringUtil.isNotBlank(shoppingGoodsBean.getVmCode())
					&& !shoppingGoodsBean.getVmCode().equals("0")) {
				VendingMachinesInfoBean bean = vendingMachinesInfoService
						.findVendingMachinesByCode(shoppingGoodsBean.getVmCode());
				if (bean == null) {
					returnDataUtil.setStatus(-99);
					returnDataUtil.setMessage("不存在的机器编码");
					return returnDataUtil;
				}
			} 
			ShoppingGoodsBean bean = shoppingGoodsServiceImpl.add(shoppingGoodsBean);
			CouponBean couponBean = (CouponBean) couponServiceImpl.add(couponAddVo).getReturnObject();
			if (bean != null && couponBean != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("商城优惠券商品增加成功！");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商城优惠券商品增加失败！");
			}

		}
		log.info("<ShoppingGoodsController>----<addCoupon>----end");
		return returnDataUtil;
	}

	/**
	 * 商城优惠券商品修改
	 * 
	 * @param param
	 * @return
	 */
	@ApiOperation(value = "商城优惠券商品修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateCouponProduct", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateCouponProduct(@RequestBody Param param) {
		log.info("<ShoppingGoodsController>----<updateCouponProduct>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		ShoppingGoodsBean entity = param.getShoppingGoodsBean();
		CouponAddVo couponAddVo = param.getCouponBean();
		CouponBean couponBean = new CouponBean();
		BeanUtils.copyProperties(couponAddVo, couponBean);
		couponBean.setId(couponAddVo.getCouponId());
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(entity.getId());

		if (!(shoppingGoodsBean.getName().equals(entity.getName()))) {
			// 判断商城商品名称是否已经存在
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品名称已存在，请重新输入,修改失败！");
				return returnDataUtil;
			} else {
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				returnDataUtil = couponServiceImpl.update(couponBean);
				boolean newCouponBean = (boolean) returnDataUtil.getReturnObject();
				if (returnDataUtil.getStatus() == -99) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setReturnObject(false);
					return returnDataUtil;
				}
				boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
				if (flagUpdate && newCouponBean) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("修改成功！");
					returnDataUtil.setReturnObject(flagUpdate);
					return returnDataUtil;
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("修改失败!");
					returnDataUtil.setReturnObject(flagUpdate);
					return returnDataUtil;
				}
			}
		} else if (!(shoppingGoodsBean.getBarCode().equals(entity.getBarCode()))) {
			// 判断商城商品条形码是否已经存在
			boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
			if (flagBarCode) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品条形码已存在，请重新输入,修改失败！");
				return returnDataUtil;
			} else {
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				returnDataUtil = couponServiceImpl.update(couponBean);
				boolean newCouponBean = (boolean) returnDataUtil.getReturnObject();
				if (returnDataUtil.getStatus() == -99) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setReturnObject(false);
					return returnDataUtil;
				}
				boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
				if (flagUpdate && newCouponBean) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("修改成功");
					returnDataUtil.setReturnObject(flagUpdate);
					return returnDataUtil;
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("修改失败!");
					returnDataUtil.setReturnObject(flagUpdate);
					return returnDataUtil;
				}
			}
		} else if (!(shoppingGoodsBean.getTarget().equals(entity.getTarget()))) {
			// 判断商城商品名称是否已经存在
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品名称已存在，请重新输入,修改失败！");
				return returnDataUtil;
			} else {
				// 判断商城商品条形码是否已经存在
				boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
				if (flagBarCode) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("商品条形码已存在，请重新输入,修改失败！");
					return returnDataUtil;
				} else {
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(UserUtils.getUser().getId());
					returnDataUtil = couponServiceImpl.update(couponBean);
					boolean newCouponBean = (boolean) returnDataUtil.getReturnObject();
					if (returnDataUtil.getStatus() == -99) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setReturnObject(false);
						return returnDataUtil;
					}
					boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
					if (flagUpdate && newCouponBean) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("修改成功！");
						returnDataUtil.setReturnObject(flagUpdate);
						return returnDataUtil;
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("修改失败!");
						returnDataUtil.setReturnObject(flagUpdate);
						return returnDataUtil;
					}
				}
			}

		} else if (entity.getTarget() == 2 && !entity.getAreaId().equals(shoppingGoodsBean.getAreaId())) {
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该区域下商品名称已存在，请重新输入,修改失败！");
				return returnDataUtil;
			} else {
				// 判断商城商品条形码是否已经存在
				boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
				if (flagBarCode) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("该区域下商品条形码已存在，请重新输入,修改失败！");
					return returnDataUtil;
				} else {
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(UserUtils.getUser().getId());
					returnDataUtil = couponServiceImpl.update(couponBean);
					boolean newCouponBean = (boolean) returnDataUtil.getReturnObject();
					if (returnDataUtil.getStatus() == -99) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setReturnObject(false);
						return returnDataUtil;
					}
					boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
					if (flagUpdate && flagUpdate) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("修改成功！");
						returnDataUtil.setReturnObject(flagUpdate);
						return returnDataUtil;
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("修改失败!");
						returnDataUtil.setReturnObject(flagUpdate);
						return returnDataUtil;
					}
				}
			}
		} else if (entity.getTarget() == 3 && !entity.getVmCode().equals(shoppingGoodsBean.getVmCode())) {
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该机器上商品名称已存在，请重新输入,修改失败！");
				return returnDataUtil;
			} else {
				// 判断商城商品条形码是否已经存在
				boolean flagBarCode = shoppingGoodsServiceImpl.checkBarcode(entity);
				if (flagBarCode) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("该机器上商品条形码已存在，请重新输入,修改失败！");
					return returnDataUtil;
				} else {
					entity.setUpdateTime(new Date());
					entity.setUpdateUser(UserUtils.getUser().getId());
					returnDataUtil = couponServiceImpl.update(couponBean);
					boolean newCouponBean = (boolean) returnDataUtil.getReturnObject();
					if (returnDataUtil.getStatus() == -99) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setReturnObject(false);
						return returnDataUtil;
					}
					boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
					if (flagUpdate && newCouponBean) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("修改成功！");
						returnDataUtil.setReturnObject(flagUpdate);
						return returnDataUtil;
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("修改失败!");
						returnDataUtil.setReturnObject(flagUpdate);
						return returnDataUtil;
					}
				}
			}
		} else {
			entity.setUpdateTime(new Date());
			entity.setUpdateUser(UserUtils.getUser().getId());
			boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
			boolean newCouponBean = (boolean) couponServiceImpl.update(couponBean).getReturnObject();
			if (flagUpdate && newCouponBean) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("修改成功！");
				returnDataUtil.setReturnObject(flagUpdate);
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("修改失败!");
				returnDataUtil.setReturnObject(flagUpdate);
			}
		}
		if (returnDataUtil.getStatus() == 1) {
			if (shoppingGoodsBean.getSalesPrice().compareTo(entity.getSalesPrice()) != 0
					|| !(shoppingGoodsBean.getName().equals(entity.getName()))) {
				boolean updatePrice = shoppingCarServiceImpl.updatePriceAndName(entity);
				if (updatePrice) {
					returnDataUtil.setMessage("修改成功且购物车中商品已更新");
				} else {
					returnDataUtil.setMessage("修改成功且购物车中无商品更新");
				}
			}
		}
		return returnDataUtil;
	}

	@ApiOperation(value = "商城优惠券删除", notes = "deleteAll", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/deleteAll", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil deleteAll(@RequestBody List<Integer> ids) {
		log.info("<ShoppingGoodsController>----<deleteAll>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = shoppingGoodsServiceImpl.del(ids.get(0));
		List<CustomerCouponVo> list = couponCustomerDao.listByCouponId(ids.get(1));
		if (list.size() > 0) {
			log.info("该优惠劵已经下发给用户，不能删除");
		} else {
			couponServiceImpl.del(ids.get(1));
		}
		if (flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(flag);
			returnDataUtil.setMessage("商品删除成功!");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(flag);
			returnDataUtil.setMessage("商品删除失败!");
		}
		log.info("<ShoppingGoodsController>----<deleteAll>----end");
		return returnDataUtil;
	}
	
	
	@ApiOperation(value = "商城拼团商品添加", notes = "spellGroupGoodsAdd", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/spellGroupGoodsAdd", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil spellGroupAdd(@RequestBody ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsController>----<spellGroupGoodsAdd>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		entity.setCreateUser(UserUtils.getUser().getId());
		ShoppingGoodsBean bean = shoppingGoodsServiceImpl.add(entity);
		if (bean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("商城商品增加成功！");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("商城商品增加失败！");
		}
		log.info("<ShoppingGoodsController>----<spellGroupGoodsAdd>----end");
		return returnDataUtil;
	}
	
	/**
	 * 商城商品修改
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "商城拼团商品修改", notes = "spellGroupGoodsUpdate", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/spellGroupGoodsUpdate", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil spellGroupUpdate(@RequestBody ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsController>----<spellGroupGoodsUpdate>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(entity.getId());
		if (!(shoppingGoodsBean.getName().equals(entity.getName()))) {
			boolean flag = shoppingGoodsServiceImpl.checkName(entity);
			if (flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品名称已存在，请重新输入,修改失败！");
			} else {
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				if(shoppingGoodsBean.getTarget()!=3 && entity.getTarget()==3) {//从范围公司/区域->机器重设机器编码
					entity.setVmCode("");
				}
				returnDataUtil.setReturnObject(shoppingGoodsServiceImpl.update(entity));
				returnDataUtil.setMessage("修改成功！");
			}
		}else {
				if(shoppingGoodsBean.getTarget()!=3 && entity.getTarget()==3) {//从范围公司/区域->机器重设机器编码
					entity.setVmCode("");
				}
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean flagUpdate = shoppingGoodsServiceImpl.update(entity);
				if (flagUpdate) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("修改成功！");
					returnDataUtil.setReturnObject(flagUpdate);
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("修改失败！");
					returnDataUtil.setReturnObject(flagUpdate);
				}
		}
		
		if (returnDataUtil.getStatus() == 1) {
			if (shoppingGoodsBean.getSalesPrice().compareTo(entity.getSalesPrice()) != 0
					|| !(shoppingGoodsBean.getName().equals(entity.getName()))) {
				boolean updatePrice = shoppingCarServiceImpl.updatePriceAndName(entity);
				if (updatePrice) {
					returnDataUtil.setMessage("修改成功且购物车中商品已更新");
				} else {
					returnDataUtil.setMessage("修改成功且购物车中无商品更新");
				}
			}
		}
		return returnDataUtil;
	}
	

	@ApiOperation(value = "商城商品绑定的售货机查询", notes = "spellGroupGoodsVmCode", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/spellGroupGoodsVmCode", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil spellGroupGoodsVmCode(@RequestBody(required = false) ShoppingGoodsVmCodeForm entity) {
		log.info("<ShoppingGoodsController>----<spellGroupGoodsVmCode>----start");
		if(entity==null) {
			entity=new ShoppingGoodsVmCodeForm();
		}
		entity.setType(0);
		ReturnDataUtil returnDataUtil = shoppingGoodsServiceImpl.vmiList(entity);
		log.info("<ShoppingGoodsController>----<spellGroupVmCode>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "拼团售货机查询", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/spellGroupVmCode", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil shoppingSpellGroupVmCode(@RequestBody(required = false) ShoppingGoodsVmCodeForm entity) {
		log.info("<ShoppingGoodsController>----<spellGroupVmCode>----start");
		if(entity==null) {
			entity=new ShoppingGoodsVmCodeForm();
		}
		entity.setType(2);
		ReturnDataUtil returnDataUtil = shoppingGoodsServiceImpl.vmiList(entity);
		log.info("<ShoppingGoodsController>----<spellGroupVmCode>----end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "商品绑定售货机", notes = "bindVmCode", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bindVmCode", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bindVmCode(@RequestBody ShoppingGoodsVmCodeForm entity) {
		log.info("<ShoppingGoodsController>----<bindVmCode>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = shoppingGoodsServiceImpl.bindVmCode(entity);
		if (flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("商品绑定/解绑售货机成功！");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("商品绑定售货机失败！绑定的售货机太多");
		}
		log.info("<ShoppingGoodsController>----<bindVmCode>----end");
		return returnDataUtil;
	}
	
	/**
	 * @author hjc
	 * @date 2019年1月13日 上午10:46:42
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "商品关联提水券提货券", notes = "bindCarryWater", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bindCarryWater", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bindCarryWater(@RequestBody ShoppingGoodsVmCodeForm entity) {
		log.info("<ShoppingGoodsController>----<bindCarryWater>----start");
		ReturnDataUtil returnDataUtil = shoppingGoodsServiceImpl.bindCarryWater(entity);
		log.info("<ShoppingGoodsController>----<bindCarryWater>----end");
		return returnDataUtil;
	}
}
