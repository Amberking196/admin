package com.server.module.system.carryWaterVouchersManage.carryWaterVouchers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
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
import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.customer.product.ShoppingGoodsVmCodeForm;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-11-03 09:02:25
 */
@Api(value = "CarryWaterVouchersController", description = "提水券管理")
@RestController
@RequestMapping("/carryWaterVouchers")
public class CarryWaterVouchersController {

	private static Logger log = LogManager.getLogger(CarryWaterVouchersController.class);
	@Autowired
	private CarryWaterVouchersService carryWaterVouchersServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private ShoppingGoodsService shoppingGoodsServiceImpl;
	@Autowired
	private ShoppingCarService shoppingCarServiceImpl;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	private CarryWaterVouchersDao carryWaterVouchersDaoImpl;
	
	@ApiOperation(value = "提水券管理列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) CarryWaterVouchersForm carryWaterVouchersForm) {
		log.info("<CarryWaterVouchersController>-------<listPage>-------start");
		if (carryWaterVouchersForm == null) {
			carryWaterVouchersForm = new CarryWaterVouchersForm();
		}
		returnDataUtil = carryWaterVouchersServiceImpl.listPage(carryWaterVouchersForm);
		log.info("<CarryWaterVouchersController>-------<listPage>-------end");
		return returnDataUtil;
	}

	/**
	 * 增加提水券同时增加商城商品
	 * 
	 * @author why
	 * @date 2018年11月3日 上午10:46:42
	 * @param Param
	 * @return
	 */
	@ApiOperation(value = "提水券管理添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody Param param) {
		log.info("<CarryWaterVouchersController>-------<add>-------start");
		ShoppingGoodsBean shoppingGoodsBean = param.getShoppingGoodsBean();
		CarryWaterVouchersVo carryWaterVouchersVo = param.getCarryWaterVouchersVo();
		boolean checkName = shoppingGoodsServiceImpl.checkName(shoppingGoodsBean);
		if (checkName) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("提水券名称重复，请重新输入！");
			return returnDataUtil;
		} else {
			if (carryWaterVouchersVo.getVmCode() != null && StringUtil.isNotBlank(carryWaterVouchersVo.getVmCode())
					&& !carryWaterVouchersVo.getVmCode().equals("0")) {
				VendingMachinesInfoBean bean = vendingMachinesInfoService
						.findVendingMachinesByCode(carryWaterVouchersVo.getVmCode());
				if (bean == null) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("输入有误，不存在的机器编码");
					return returnDataUtil;
				}
			}
			CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersServiceImpl.add(carryWaterVouchersVo);
			if (carryWaterVouchersBean != null) {
				// 商品类型 为 提水券
				shoppingGoodsBean.setTypeId(26L);
				// 自动生成商品条形码 单位张
				String str = UUID.randomUUID().toString();
				String barCode = str.substring(str.length() - 12, str.length());
				shoppingGoodsBean.setBarCode(barCode);
				shoppingGoodsBean.setStandard("1");
				shoppingGoodsBean.setBrand("1");
				shoppingGoodsBean.setUnit(60515L);
				shoppingGoodsBean.setCreateUser(UserUtils.getUser().getId());
				shoppingGoodsBean.setVouchersId(carryWaterVouchersBean.getId());
				ShoppingGoodsBean bean = shoppingGoodsServiceImpl.add(shoppingGoodsBean);
				if (bean != null) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("提水券增加成功！");
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("提水券增加失败！");
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("提水券增加失败！");
			}
		}
		log.info("<CarryWaterVouchersController>-------<add>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "提水券管理修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody Param param) {
		log.info("<CarryWaterVouchersController>-------<update>-------start");
		ShoppingGoodsBean entity = param.getShoppingGoodsBean();
		CarryWaterVouchersVo vo = param.getCarryWaterVouchersVo();
		CarryWaterVouchersBean bean=new CarryWaterVouchersBean();
		BeanUtils.copyProperties(vo, bean);
		bean.setName(vo.getCarryWaterName());
		bean.setId(vo.getId());
		bean.setUpdateTime(new Date());
		bean.setUpdateUser(UserUtils.getUser().getId());
		Long shoppingGoodsId = param.getCarryWaterVouchersVo().getShoppingGoodsId();
		log.info("<shoppingGoodsId>========"+shoppingGoodsId);
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsServiceImpl.get(shoppingGoodsId);
		log.info(shoppingGoodsBean.getName()+"=============="+entity.getName());
		//判断售货机编号是否存在
		if (vo.getVmCode() != null && StringUtil.isNotBlank(vo.getVmCode())
				&& !vo.getVmCode().equals("0")) {
			VendingMachinesInfoBean bean2 = vendingMachinesInfoService.findVendingMachinesByCode(vo.getVmCode());
			if (bean2== null) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("输入有误，不存在的机器编码");
				return returnDataUtil;
			}
		}
		//如果提水券名称发生改变
		if (!(shoppingGoodsBean.getName().equals(entity.getName()))) {
			// 判断提水券名称是否已经存在
			boolean checkName = shoppingGoodsServiceImpl.checkName(entity);
			if(checkName) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("提水券名称重复，请重新输入！");
				return returnDataUtil;
			}else {
				boolean flag = (boolean) carryWaterVouchersServiceImpl.update(bean).getReturnObject();
				entity.setId(shoppingGoodsId);
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean update = shoppingGoodsServiceImpl.update(entity);
				if (update && flag) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("提水券修改成功");
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("提水券修改失败");
				}
			}
			//如果范围发生改变
		}else if (!(shoppingGoodsBean.getTarget().equals(entity.getTarget()))) {
			// 判断提水券名称是否已经存在
			boolean checkName = shoppingGoodsServiceImpl.checkName(entity);
			if(checkName) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("提水券名称已存在，请重新输入，修改失败！");
				return returnDataUtil;
			}else {
				boolean flag = (boolean) carryWaterVouchersServiceImpl.update(bean).getReturnObject();
				entity.setId(shoppingGoodsId);
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean update = shoppingGoodsServiceImpl.update(entity);
				if (update && flag) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("提水券修改成功");
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("提水券修改失败");
				}
			}
		} else if (entity.getTarget() == 2 && !entity.getAreaId().equals(shoppingGoodsBean.getAreaId())) {
			// 判断提水券名称是否已经存在
			boolean checkName = shoppingGoodsServiceImpl.checkName(entity);
			if(checkName) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该区域下提水券名称重复，请重新输入！");
				return returnDataUtil;
			}else {
				boolean flag = (boolean) carryWaterVouchersServiceImpl.update(bean).getReturnObject();
				entity.setId(shoppingGoodsId);
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean update = shoppingGoodsServiceImpl.update(entity);
				if (update && flag) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("提水券修改成功");
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("提水券修改失败");
				}
			}
		} else if (entity.getTarget() == 3 && !entity.getVmCode().equals(shoppingGoodsBean.getVmCode())) {
			// 判断提水券名称是否已经存在
			boolean checkName = shoppingGoodsServiceImpl.checkName(entity);
			if(checkName) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该机器下提水券名称重复，请重新输入！");
				return returnDataUtil;
			}else {
				boolean flag = (boolean) carryWaterVouchersServiceImpl.update(bean).getReturnObject();
				entity.setId(shoppingGoodsId);
				entity.setUpdateTime(new Date());
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean update = shoppingGoodsServiceImpl.update(entity);
				if (update && flag) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("提水券修改成功");
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("提水券修改失败");
				}
			}
		}else {//如果提水券名称未发生改变
			boolean flag = (boolean) carryWaterVouchersServiceImpl.update(bean).getReturnObject();
			entity.setId(shoppingGoodsId);
			entity.setUpdateTime(new Date());
			entity.setUpdateUser(UserUtils.getUser().getId());
			boolean update = shoppingGoodsServiceImpl.update(entity);
			if (update && flag) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("提水券修改成功");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("提水券修改失败");
			}
		}
		if (returnDataUtil.getStatus() == 1) {
			//更新购物车
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
		log.info("<CarryWaterVouchersController>-------<update>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "提水券管理删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody List<Integer> ids) {
		log.info("<CarryWaterVouchersController>-------<del>-------start");
		boolean flag=false;
		if(ids.get(0)==null) {
			flag=true;
		}else {
			flag = shoppingGoodsServiceImpl.del(ids.get(0));
		}
		boolean del = carryWaterVouchersServiceImpl.del(ids.get(1));
		if (del && flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("删除成功");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("删除失败");
		}
		log.info("<CarryWaterVouchersController>-------<del>-------end");
		return returnDataUtil;
	}

	/**
	 * 后台操作上传图片
	 *
	 * @param file
	 * @return 上传成功信息
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<CouponController>--<uploadImage>--start");
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 得到图片的后缀名
		String fileNameExtension = filePath.substring(filePath.indexOf("."), filePath.length());
		// 生成实际存储的真实文件名
		String imgName = UUID.randomUUID().toString() + fileNameExtension;
		// 这是文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getShoppingGoodsImg() + imgName;
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			return imgName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			log.info("<CouponController>--<uploadImage>--end");
		}
	}
	
	/**
	 * 单独增加提货券
	 * @author hjc
	 * @date 2019年1月13日 上午10:46:42
	 * @param Param
	 * @return
	 */
	@ApiOperation(value = "提货券管理添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/carryGoodsAdd", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil carryGoodsAdd(@RequestBody Param param) {
		log.info("<CarryWaterVouchersController>-------<add>-------start");
		CarryWaterVouchersVo carryWaterVouchersVo = param.getCarryWaterVouchersVo();
		CarryWaterVouchersBean carryWaterVouchersBean = carryWaterVouchersServiceImpl.add(carryWaterVouchersVo);
		if (carryWaterVouchersBean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("增加成功！");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("增加失败！");
		}
		log.info("<CarryWaterVouchersController>-------<add>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "提货券管理修改", notes = "carryGoodsUpdate", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/carryGoodsUpdate", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil carryGoodsUpdate(@RequestBody Param param) {
		log.info("<CarryWaterVouchersController>-------<update>-------start");
		CarryWaterVouchersVo vo = param.getCarryWaterVouchersVo();
		CarryWaterVouchersBean bean=new CarryWaterVouchersBean();
		BeanUtils.copyProperties(vo, bean);
		bean.setName(vo.getCarryWaterName());
		bean.setId(vo.getId());
		bean.setUpdateTime(new Date());
		bean.setUpdateUser(UserUtils.getUser().getId());
		DateTime dt1 = new DateTime(bean.getEndTime().getTime());
		dt1 = dt1.withHourOfDay(23);
		dt1 = dt1.withMinuteOfHour(59);
		dt1 = dt1.withSecondOfMinute(59);
		bean.setEndTime(dt1.toDate());

		DateTime dt2 = new DateTime(bean.getStartTime().getTime());
		dt2 = dt2.withHourOfDay(0);
		dt2 = dt2.withMinuteOfHour(0);
		dt2 = dt2.withSecondOfMinute(0);
		bean.setStartTime(dt2.toDate());
		
		boolean update = carryWaterVouchersDaoImpl.update(bean);
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("修改成功！");
			returnDataUtil.setReturnObject(update);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("修改失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<CarryWaterVouchersController>-------<update>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "商城拼团商品售货机查询", notes = "carryGoodsVmCode", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/carryGoodsVmCode", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil carryGoodsVmCode(@RequestBody(required = false) ShoppingGoodsVmCodeForm entity) {
		log.info("<CarryWaterVouchersController>----<carryGoodsVmCode>----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(entity==null) {
			entity=new ShoppingGoodsVmCodeForm();
		}
		entity.setType(1);
		returnDataUtil = shoppingGoodsServiceImpl.vmiList(entity);
		log.info("<CarryWaterVouchersController>----<carryGoodsVmCode>----end");
		return returnDataUtil;
	}
	
	/**
	 * @author hjc
	 * @date 2019年1月13日 上午10:46:42
	 * @param ShoppingGoodsVmCodeForm
	 * @return
	 */
	@ApiOperation(value = "提货券绑定机器", notes = "bindVmCode", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/bindVmCode", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil bindVmCode(@RequestBody ShoppingGoodsVmCodeForm entity) {
		log.info("<CarryWaterVouchersController>-------<bindVmCode>-------start");
		boolean flag = carryWaterVouchersServiceImpl.bindVmCode(entity);
		if (flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("绑定/解绑机器成功！");
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("绑定机器失败！绑定的售货机太多");
		}
		log.info("<CarryWaterVouchersController>-------<bindVmCode>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "查询商品已绑定的提水券", notes = "queryBindCarryWater", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/queryBindCarryWater", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil queryBindCarryWater(@RequestBody ShoppingGoodsVmCodeForm entity){
		log.info("<CarryWaterVouchersController>-------<bindVmCode>-------start");
		ReturnDataUtil returnDataUtil = carryWaterVouchersServiceImpl.queryBindCarryWater(entity);
		log.info("<CarryWaterVouchersController>-------<bindVmCode>-------end");
		return returnDataUtil;
	}
}
