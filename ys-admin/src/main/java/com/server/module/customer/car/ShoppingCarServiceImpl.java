package com.server.module.customer.car;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsDao;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.warehouseManage.stock.WarehouseStockDao;
import com.server.util.ReturnDataUtil;
/**
 * author name: hjc
 * create time: 2018-06-29 11:03:25
 */ 
@Service
public class  ShoppingCarServiceImpl  implements ShoppingCarService{

	public static Logger log = LogManager.getLogger(ShoppingCarServiceImpl.class);
	@Autowired
	private ShoppingCarDao shoppingCarDaoImpl;
	@Autowired
	private WarehouseStockDao warehouseStockDaoImpl;
	@Autowired
	private ShoppingGoodsDao shoppingGoodsDaoImpl;
	
	public ReturnDataUtil listPage(ShoppingCarForm shoppingCarForm){
		log.info("<ShoppingCarServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = shoppingCarDaoImpl.listPage(shoppingCarForm);
		log.info("<ShoppingCarServiceImpl>------<listPage>-----end");
		return listPage;
	}


	public ReturnDataUtil add(ShoppingCarForm shoppingCarForm,ShoppingCarBean newShoppingCarBean) {
		log.info("<ShoppingCarServiceImpl>------<add>-----start");
		ReturnDataUtil returnDataUtil=shoppingCarDaoImpl.listPage(shoppingCarForm);
		//已在购物车的商品
		List<ShoppingCarBean> shoppingCarBeanList= (List<ShoppingCarBean>) returnDataUtil.getReturnObject();
		if(shoppingCarBeanList!=null && shoppingCarBeanList.size()>0) {
			Boolean flag=false;
			for(ShoppingCarBean shoppingCarBean:shoppingCarBeanList) {
				if(shoppingCarBean.getItemId().intValue() == newShoppingCarBean.getItemId().intValue()) {
					log.info("相同商品增加数量");
					shoppingCarBean.setNum(shoppingCarBean.getNum()+newShoppingCarBean.getNum());
					shoppingCarBean.setUpdateTime(new Date());
					shoppingCarBean.setUpdateUser(UserUtils.getUser().getId());
					boolean update=update(shoppingCarBean);
					if (update) {
						returnDataUtil.setReturnObject(true);
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("相同商品增加数量成功！");
					} else {
						returnDataUtil.setReturnObject(false);
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("相同商品增加数量失败！");
					}
					flag=true;
					break;
				}
			}
			if(flag==false) {
				newShoppingCarBean.setCreateUser(UserUtils.getUser().getId());
				ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(newShoppingCarBean.getItemId());
				newShoppingCarBean.setPrice(shoppingGoodsBean.getSalesPrice());
				newShoppingCarBean.setItemName(shoppingGoodsBean.getName());
				ShoppingCarBean sc=insert(newShoppingCarBean);
				if (sc!=null) {
					returnDataUtil.setReturnObject(true);
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("购物车新增商品增加数量成功！");
				} else {
					returnDataUtil.setReturnObject(false);
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("购物车新增商品增加数量失败！");
				}
			}
		}
		else {
			newShoppingCarBean.setCreateUser(UserUtils.getUser().getId());
			ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(newShoppingCarBean.getItemId());
			log.info("----"+shoppingGoodsBean.getName());
			newShoppingCarBean.setPrice(shoppingGoodsBean.getSalesPrice());
			newShoppingCarBean.setItemName(shoppingGoodsBean.getName());
			ShoppingCarBean scb=shoppingCarDaoImpl.insert(newShoppingCarBean);
			if (scb!=null) {
				returnDataUtil.setReturnObject(true);
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("空购物车新增商品增加数量成功！");
			} else {
				returnDataUtil.setReturnObject(false);
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("空购物车新增商品增加数量失败！");
			}
		}
		log.info("<ShoppingCarServiceImpl>------<add>-----end");
		return 	returnDataUtil;

	}

	public ReturnDataUtil addAndBuy(ShoppingCarBean newShoppingCarBean) {
		log.info("<ShoppingCarServiceImpl>------<addAndBuy>-----start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		Long userId=UserUtils.getUser().getId();
		newShoppingCarBean.setCreateUser(userId);
		newShoppingCarBean.setUpdateUser(userId);
		ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsDaoImpl.get(newShoppingCarBean.getItemId());
		newShoppingCarBean.setPrice(shoppingGoodsBean.getSalesPrice());
		newShoppingCarBean.setItemName(shoppingGoodsBean.getName());
		newShoppingCarBean.setDeleteFlag(1);
		newShoppingCarBean.setCustomerId(userId);

		ShoppingCarBean scb=shoppingCarDaoImpl.insert(newShoppingCarBean);
		if (scb!=null) {
			returnDataUtil.setReturnObject(scb);
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("新的订单已生成");
		} else {
			returnDataUtil.setReturnObject(false);
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("新的订单生成失败");
		}
		log.info("<ShoppingCarServiceImpl>------<addAndBuy>-----end");
		return 	returnDataUtil;

	}

	

	public ReturnDataUtil update(ShoppingCarForm shoppingCarForm,ShoppingCarBean newShoppingCarBean) {
		log.info("<ShoppingCarServiceImpl>------<update>-----start");
		ReturnDataUtil returnDataUtil=shoppingCarDaoImpl.listPage(shoppingCarForm);
		//已在购物车的商品 
		List<ShoppingCarBean> shoppingCarBeanList= (List<ShoppingCarBean>) returnDataUtil.getReturnObject();
		//购物车中增加/减少同种商品的数量
		for(ShoppingCarBean shoppingCarBean:shoppingCarBeanList) {
			if(shoppingCarBean.getItemId().intValue() == newShoppingCarBean.getItemId().intValue()) {
				
				if(newShoppingCarBean.getNum().intValue()==0)
				{
					List<Long> shoppingCarIdList=new ArrayList<Long>();
					shoppingCarIdList.add(newShoppingCarBean.getId());
					if( shoppingCarDaoImpl.del(shoppingCarIdList)){
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("删除成功");
					}else{
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("删除失败");
					}
					return returnDataUtil;
				}
				else {
					shoppingCarBean.setUpdateTime(new Date());
					shoppingCarBean.setUpdateUser(UserUtils.getUser().getId());
					shoppingCarBean.setNum(newShoppingCarBean.getNum());
					boolean update=update(shoppingCarBean);
					if (update) {
						returnDataUtil.setReturnObject(true);
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("购物车商品数量增加成功！");
					} else {
						returnDataUtil.setReturnObject(false);
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("购物车商品数量增加失败！");
					}
				}
				break;
			}
		}
//		Map<String, String> map = new HashMap<String, String>();
//		WarehouseStockForm warehouseStockForm=new WarehouseStockForm();
//		warehouseStockForm.setItemId(newShoppingCarBean.getItemId().toString());
//		warehouseStockForm.setCompanyId(newShoppingCarBean.getItemId().toString());
//		warehouseStockForm.setWarehouseId(newShoppingCarBean.getItemId().toString());
//
//		ReturnDataUtil data=warehouseStockDaoImpl.listPage(warehouseStockForm);
//		List<WarehouseStockBean> warehouseStock=(List<WarehouseStockBean>) data.getReturnObject();
//		for(WarehouseStockBean warehouseStockBean:warehouseStock) {
//			if(warehouseStockBean.getQuantity().longValue()<newShoppingCarBean.getNum().intValue()) {
//				map.put(newShoppingCarBean.getItemName(), "库存不足");
//			}
//		}
//		returnDataUtil=shoppingCarDaoImpl.listPage(shoppingCarForm);
//		if (map.size() > 0) {
//			returnDataUtil.setStatus(0);
//			returnDataUtil.setMessage(map.toString());
//		} 
		log.info("<ShoppingCarServiceImpl>------<update>-----end");
		return returnDataUtil;
	}
	
	
	
	
	public ReturnDataUtil  del(List<Long> shoppingCarIdList){
		ReturnDataUtil returnData = new ReturnDataUtil();
		if(shoppingCarIdList!=null && shoppingCarIdList.size()>0 && shoppingCarDaoImpl.del(shoppingCarIdList)){
			returnData.setStatus(1);
			returnData.setMessage("删除成功");
		}else{
			returnData.setStatus(0);
			returnData.setMessage("删除失败");
		}
		return returnData;
	}

	public ReturnDataUtil save(ShoppingCarForm shoppingCarForm,List<ShoppingCarBean> newShoppingCarBeanList) {
		log.info("<ShoppingCarServiceImpl>------<save>-----start");
		ReturnDataUtil returnDataUtil=shoppingCarDaoImpl.listPage(shoppingCarForm);
		//已在购物车的商品
		List<ShoppingCarBean> shoppingCarBeanList= (List<ShoppingCarBean>) returnDataUtil.getReturnObject();
		List<Long> oldList=new ArrayList<Long>();
		Set<Long> newList=new HashSet<Long>();
		for(ShoppingCarBean shoppingCarBean:shoppingCarBeanList) {
			for(ShoppingCarBean newShoppingCarBean:newShoppingCarBeanList) {
				log.info("new"+newShoppingCarBean.getId().intValue());
				newList.add(newShoppingCarBean.getId());
				if(shoppingCarBean.getItemId().equals(newShoppingCarBean.getItemId())) {
					//新数量替换旧数量
					shoppingCarBean.setNum(newShoppingCarBean.getNum());
					shoppingCarBean.setUpdateTime(new Date());
					update(shoppingCarBean);
					break;
				}
			}
			log.info("old"+shoppingCarBean.getId().intValue());
			oldList.add(shoppingCarBean.getId());
		}
			
		oldList.removeAll(newList);
		returnDataUtil=del(oldList);
		returnDataUtil=shoppingCarDaoImpl.listPage(shoppingCarForm);

//		for (Integer i : oldList) {
//			log.info("i"+i);
//			delete(i);
//		}
		log.info("<ShoppingCarServiceImpl>------<save>-----end");
		return returnDataUtil;
	}
	
	public boolean updatePriceAndName(ShoppingGoodsBean entity) {
		return shoppingCarDaoImpl.updatePriceAndName(entity);
	}
	
	public boolean delete(Object id) {
		return shoppingCarDaoImpl.delete(id);
	}
	
	public ShoppingCarBean get(Object id) {
		return shoppingCarDaoImpl.get(id);
	}
	
	public boolean update(ShoppingCarBean entity) {
		return shoppingCarDaoImpl.update(entity);
	}
	
	public boolean updateFlag(ShoppingCarBean entity) {
		return shoppingCarDaoImpl.updateFlag(entity);
	}
	
	public ShoppingCarBean insert(ShoppingCarBean entity) {
		return shoppingCarDaoImpl.insert(entity);
	}

	public boolean updateAllFlag(List<Long> shoppingCarIdList) {
		return shoppingCarDaoImpl.updateAllFlag(shoppingCarIdList);
	}
}

