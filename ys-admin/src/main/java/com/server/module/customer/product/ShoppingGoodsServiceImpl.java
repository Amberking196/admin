package com.server.module.customer.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.server.redis.RedisClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.carryWaterVouchersManage.carryWaterVouchers.CarryWaterVouchersBean;
import com.server.module.system.couponManager.coupon.CouponForm;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.userManage.CustomerBean;
import com.server.module.system.userManage.CustomerService;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.UUIDUtil;
import com.server.util.stateEnum.ShoppingGoodsTypeEnum;

/**
 * author name: why create time: 2018-06-29 11:17:55
 */
@Service
public class ShoppingGoodsServiceImpl implements ShoppingGoodsService {

	private static Logger log = LogManager.getLogger(ShoppingGoodsServiceImpl.class);
	@Autowired
	private ShoppingGoodsDao shoppingGoodsDaoImpl;
    @Autowired
    private CustomerService customerServiceImpl;
    @Autowired
    private VendingMachinesInfoService vendingMachinesInfoServiceImpl;
	@Autowired
	private RedisClient redisClient;
	/**
	 * 后台查询商城商品列表
	 */
	@Override
	public ReturnDataUtil listPage(ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsServiceImpl>----<listPage>----start");
		ReturnDataUtil list = shoppingGoodsDaoImpl.listPage(shoppingGoodsForm);
		List<ShoppingGoodsBean>  s= (List<ShoppingGoodsBean>) list.getReturnObject();
		log.info("<ShoppingGoodsServiceImpl>----<listPage>----end");
		return list;
	}
	@Override
	public ShoppingGoodsBean add(ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsServiceImpl>----<add>----start");
		ShoppingGoodsBean insert = shoppingGoodsDaoImpl.insert(entity);
		log.info("<ShoppingGoodsServiceImpl>----<add>----end");
		return insert;
		
	}
	@Override
	public boolean update(ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsServiceImpl>----<update>----start");
		boolean update = shoppingGoodsDaoImpl.update(entity);
		log.info("<ShoppingGoodsServiceImpl>----<update>----end");
		return update;
	}

	public boolean del(Object id) {
		log.info("<ShoppingGoodsServiceImpl>----<del>----start");
		boolean delete = shoppingGoodsDaoImpl.delete(id);
		log.info("<ShoppingGoodsServiceImpl>----<del>----end");
		return delete;
	}

	

	public ShoppingGoodsBean get(Object id) {
		log.info("<ShoppingGoodsServiceImpl>----<get>----start");
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(id);
		log.info("<ShoppingGoodsServiceImpl>----<get>----end");
		return shoppingGoodsBean;
	}

	/**
	 * 判断商城商品名称是否存在
	 */
	@Override
	public boolean checkName(ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsServiceImpl>----<checkName>----start");
		String sql=null;
		if(entity.getTarget()==0) {
			sql="select name from shopping_goods where deleteFlag=0 and name = '"+entity.getName() +"'";
		}
		else if(entity.getTarget()==1){
			sql=("select name from shopping_goods where deleteFlag=0 and areaId = 0 and name = '" + entity.getName() + "'  and companyId = '"
					+ entity.getCompanyId() + "' ");
		}
		else if(entity.getTarget()==2){
			sql=("select name from shopping_goods where deleteFlag=0 and name = '" + entity.getName() + "'  and companyId = '"
					+ entity.getCompanyId() + "' and areaId = '" + entity.getAreaId() + "' ");
		}
		else if(entity.getTarget()==3) {
			sql=("select name from shopping_goods where deleteFlag=0 and name = '" + entity.getName() + "'  and vmCode ='"+entity.getVmCode()+"'");
		}
		boolean checkName = shoppingGoodsDaoImpl.checkName(sql);
		log.info("<ShoppingGoodsServiceImpl>----<checkName>----end");
		return checkName;
	}
	
	/**
	 * 判断商城商品条形码 是否存在
	 */
	@Override
	public boolean checkBarcode(ShoppingGoodsBean entity) {
		log.info("<ShoppingGoodsServiceImpl>----<checkBarcode>----start");
		String sql=null;
		if(entity.getTarget()==0) {
			sql="select name from shopping_goods where deleteFlag=0 and barCode  = '"+entity.getBarCode() +"'";
		}
		else if(entity.getTarget()==1){
			sql=("select name from shopping_goods where deleteFlag=0 and areaId = 0 and barCode = '" + entity.getBarCode() + "'  and companyId = '"
					+ entity.getCompanyId() + "' ");
		}
		else if(entity.getTarget()==2){
			sql=("select name from shopping_goods where deleteFlag=0 and barCode = '" + entity.getBarCode() + "'  and companyId = '"
					+ entity.getCompanyId() + "' and areaId = '" + entity.getAreaId() + "' ");
		}
		else if(entity.getTarget()==3) {
			sql=("select name from shopping_goods where deleteFlag=0 and barCode = '" + entity.getBarCode() + "'  and vmCode ="+entity.getVmCode());
		}
		boolean checkBarcode = shoppingGoodsDaoImpl.checkBarcode(sql);
		log.info("<ShoppingGoodsServiceImpl>----<checkBarcode>----end");
		return checkBarcode;
	}

	/**
	 * 查询商品信息 给手机端 显示
	 */
	@Override
	public ReturnDataUtil list(ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsServiceImpl>----<list>----start");
		User u=UserUtils.getUser();
		VendingMachinesInfoBean vmi=null;
		if(u!=null) {
			Long userId=u.getId();
			CustomerBean customer=customerServiceImpl.findCustomerById(userId);
			if(customer!=null) {
				String vmCode = customer.getVmCode();
				vmi=vendingMachinesInfoServiceImpl.findVendingMachinesByCode(vmCode);
			}
		}
		 List<ShoppingGoodsBean> list = shoppingGoodsDaoImpl.list(shoppingGoodsForm,vmi);
		log.info("<ShoppingGoodsServiceImpl>----<list>----end");
		return new ReturnDataUtil(list);
	}

	/**
	 * 查询珠海华发商品信息 手机端显示
	 */
	@Override
	public ReturnDataUtil huaFaAppList(ShoppingGoodsForm shoppingGoodsForm) {
		log.info("<ShoppingGoodsServiceImpl>----<list>----start");
		User u=UserUtils.getUser();
		System.out.println("============================"+u);
		VendingMachinesInfoBean vmi=null;
		if(u!=null) {
			Long userId=u.getId();
			CustomerBean customer=customerServiceImpl.findCustomerById(userId);
			System.out.println("==================================================="+customer);
			if(customer!=null) {
				String vmCode = customer.getVmCode();
				vmi=vendingMachinesInfoServiceImpl.findVendingMachinesByCode(vmCode);
			}
		}
		List<ShoppingGoodsBean> list = shoppingGoodsDaoImpl.huaFaAppList(shoppingGoodsForm,vmi);
		log.info("<ShoppingGoodsServiceImpl>----<list>----end");
		return new ReturnDataUtil(list);
	}

	/**
	 * 查询售货机货道商品 下拉框 展示
	 */
	@Override
	public ReturnDataUtil itemBasicListPage() {
		log.info("<ShoppingGoodsServiceImpl>----<itemBasicListPage>----start");
		ReturnDataUtil itemBasicListPage = shoppingGoodsDaoImpl.itemBasicListPage();
		log.info("<ShoppingGoodsServiceImpl>----<itemBasicListPage>----end");
		return itemBasicListPage;
	}
	
	/**
	 * 处理图片名称
	 */
	public String findImgName(String fileName) {
		log.info("<ShoppingGoodsServiceImpl>----<findImgName>----start");
		String uuid = UUIDUtil.getUUID();
		String name=fileName.substring(fileName.length()-4, fileName.length());
		String imgName=uuid+name;
		log.info("<ShoppingGoodsServiceImpl>----<findImgName>----end");
		return imgName;
	}

	/**
	 * 手机端商品详情显示
	 */
	@Override
	public ShoppingGoodsBean productDetails(Long id,Long spellgroupId) {
		log.info("<ShoppingGoodsServiceImpl>----<productDetails>----start");
		ShoppingGoodsBean bean = shoppingGoodsDaoImpl.productDetails(id,spellgroupId);
		log.info("[是否是团购商品：0非团购   1团购 ]："+bean.getIsConglomerateCommodity());
		if(bean.getIsConglomerateCommodity()==0) {//非团购商品 进行判断是否是砍价商品
			Long goodsBargainId = shoppingGoodsDaoImpl.checkBargainGoods(id);
			bean.setGoodsBargainId(goodsBargainId);
		}
		if(bean.getPurchaseTime()==null) {
			bean.setPurchaseTime(0L);
		}
		if(bean.getSalesQuantity()==null) {
			bean.setSalesQuantity(0L);
		}
		log.info("<ShoppingGoodsServiceImpl>----<productDetails>----end");
		return bean;
	}
	/**
	 * 增加图片素材
	 */
	@Override
	public boolean addPictureMaterial(PictureMaterialBean bean) {
		log.info("<ShoppingGoodsServiceImpl>----<addPictureMaterial>----start");
		 boolean addPictureMaterial = shoppingGoodsDaoImpl.addPictureMaterial(bean);
		log.info("<ShoppingGoodsServiceImpl>----<addPictureMaterial>----end");
		return addPictureMaterial;
	}
	
	/**
	 * 查询图片素材
	 */
	public ReturnDataUtil pictureListPage(PictureMaterialForm pictureMaterialForm) {
		log.info("<ShoppingGoodsServiceImpl>----<pictureListPage>----start");
		ReturnDataUtil pictureListPage = shoppingGoodsDaoImpl.pictureListPage(pictureMaterialForm);
		log.info("<ShoppingGoodsServiceImpl>----<pictureListPage>----end");
		return pictureListPage;
	}
	
	/**
	 * 查询广告商品 
	 */
	public List<ShoppingGoodsBean> findAdvertisingShopping(){
		log.info("<ShoppingGoodsServiceImpl>----<findAdvertisingShopping>----start");
		List<ShoppingGoodsBean> list = shoppingGoodsDaoImpl.findAdvertisingShopping();
		log.info("<ShoppingGoodsServiceImpl>----<findAdvertisingShopping>----end");
		return list;
	}

	@Override
	public ReturnDataUtil couponListPage(ShoppingGoodsForm shoppingGoodsForm, CouponForm couponForm) {
		log.info("<ShoppingGoodsServiceImpl>----<couponListPage>----start");
		ReturnDataUtil returnDataUtil = shoppingGoodsDaoImpl.couponListPage(shoppingGoodsForm,  couponForm);
		log.info("<ShoppingGoodsServiceImpl>----<couponListPage>----end");
		return returnDataUtil;
	}
	
	@Override
    public ReturnDataUtil checkVmCode(ShoppingGoodsBean entity) {
        ReturnDataUtil re=new ReturnDataUtil();
		  if(entity.getVmCode()!=null && StringUtil.isNotBlank(entity.getVmCode())  && !entity.getVmCode().equals("0")){
	          VendingMachinesInfoBean bean=vendingMachinesInfoServiceImpl.findVendingMachinesByCode(entity.getVmCode());
	          if(bean==null){
	              re.setStatus(-99);
	              re.setMessage("不存在的机器编码");
	              re.setReturnObject(false);
	              return re;
	          }
	      }
        return re;
    }
	
	@Override
	public ReturnDataUtil vmiList(ShoppingGoodsVmCodeForm shoppingGoodsVmCodeForm) {
		log.info("<ShoppingGoodsServiceImpl>----<vmilist>----start");
		ReturnDataUtil returnDataUtil = shoppingGoodsDaoImpl.vmiList(shoppingGoodsVmCodeForm);
		log.info("<ShoppingGoodsServiceImpl>----<vmilist>----end");
		return returnDataUtil;
	}
	public boolean bindVmCode(ShoppingGoodsVmCodeForm entity) {
		log.info("<ShoppingGoodsServiceImpl>----<bindVmCode>----start");
	    ShoppingGoodsBean sgb=shoppingGoodsDaoImpl.get(entity.getId());	
		sgb.setTarget(entity.getTarget());
		sgb.setCompanyId(entity.getCompanyId()==null?null:entity.getCompanyId().longValue());
		sgb.setAreaId(entity.getAreaId()==null?null:entity.getCompanyId().longValue());
		
	    boolean flag=false;
	    if(entity.getTarget()==3) {
		    List<String> newVmCodeList = new ArrayList(Arrays.asList(StringUtils.split(entity.getVmCode(),",")));
		    String vmCode=sgb.getVmCode();
		    List<String> oldVmCodeList = Lists.newArrayList();
		    if(StringUtils.isNotBlank(vmCode)) {
			     oldVmCodeList = new ArrayList(Arrays.asList(StringUtils.split(vmCode,",")));
		    }
			if(entity.getIsBind()==1){
			    oldVmCodeList.addAll(newVmCodeList);
			    oldVmCodeList = oldVmCodeList.stream().distinct().collect(Collectors.toList()); 
			    //List<String> listAll = new ArrayList<String>(new LinkedHashSet<>(oldVmCodeList));
				String nowVmCode = StringUtils.join(oldVmCodeList,",");
				sgb.setVmCode(nowVmCode);
			    flag=shoppingGoodsDaoImpl.update(sgb);
			}else if(entity.getIsBind()==0){
			    oldVmCodeList.removeAll(newVmCodeList);
			    oldVmCodeList = oldVmCodeList.stream().distinct().collect(Collectors.toList()); 
			    // List<String> listAll = new ArrayList<String>(new LinkedHashSet<>(oldVmCodeList));
				String nowVmCode = StringUtils.join(oldVmCodeList,",");
				sgb.setVmCode(nowVmCode);
				flag=shoppingGoodsDaoImpl.update(sgb);
			}
	    }else {
		    flag=shoppingGoodsDaoImpl.update(sgb);
	    }

		log.info("<ShoppingGoodsServiceImpl>----<bindVmCode>----end");
		return flag;
	}
	
	public ReturnDataUtil bindCarryWater(ShoppingGoodsVmCodeForm entity) {
		boolean update=false;
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
	    ShoppingGoodsBean sgb=shoppingGoodsDaoImpl.get(entity.getId());	
		String[] carryWaterArray=StringUtils.split(entity.getCarryWaterId(),",");
		if(sgb.getTypeId().intValue()==ShoppingGoodsTypeEnum.MEAL.getIndex()) {//套餐类型可以绑多个
			List<String> newVouchersList = new ArrayList<String>(Arrays.asList(StringUtils.split(entity.getCarryWaterId(),",")));
    		String vouchers=sgb.getVouchersIds();
    		List<String> oldVouchersList = Lists.newArrayList();
    		if(StringUtils.isNotBlank(vouchers)) {
    			oldVouchersList = new ArrayList<String>(Arrays.asList(StringUtils.split(vouchers,",")));
    		}
			if(entity.getIsBind()==1){
			    oldVouchersList.addAll(newVouchersList);
				String nowVouchers = StringUtils.join(oldVouchersList,",");
				sgb.setVouchersIds(nowVouchers);
				update=shoppingGoodsDaoImpl.update(sgb);
			}else if(entity.getIsBind()==0){
			    oldVouchersList.removeAll(newVouchersList);
				String nowVouchers = StringUtils.join(oldVouchersList,",");
				sgb.setVouchersIds(nowVouchers);
				update=shoppingGoodsDaoImpl.update(sgb);
			}
		}else {//正常类型只能绑定一个
			if(carryWaterArray.length>1) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("只有套餐类型的商品才支持绑定/解绑多个");
				return returnDataUtil;
			}else if(carryWaterArray.length==1){
				if(entity.getIsBind()==1){
					sgb.setVouchersId(Long.valueOf(entity.getCarryWaterId()));
					update=shoppingGoodsDaoImpl.update(sgb);
				}else {
					//sgb.setVouchersId(null);无法解绑
					update=shoppingGoodsDaoImpl.updateVouchersId(sgb);
				}
			}
		}
		if(update) {
			returnDataUtil.setMessage("绑定/解绑成功");
			return returnDataUtil;			
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("绑定/解绑失败");
			return returnDataUtil;			
		}
	}
	
	@Override
	public List<String> findItemName(String product) {
		log.info("<ShoppingGoodsServiceImpl>----<findItemName>----start");
		List<String> list = shoppingGoodsDaoImpl.findItemName(product);
		log.info("<ShoppingGoodsServiceImpl>----<findItemName>----end");
		return list;
	}
	
	@Override
	public String findShoppingGoogsName(String product) {
		log.info("<ShoppingGoodsServiceImpl>----<findShoppingGoogsName>----start");
		String name = shoppingGoodsDaoImpl.findShoppingGoogsName(product);
		log.info("<ShoppingGoodsServiceImpl>----<findShoppingGoogsName>----end");
		return name;
	}
}
