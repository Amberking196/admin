package com.server.module.system.purchase.purchaseBill;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.dbpool.BaseDB;
import com.server.module.system.purchase.purchaseApply.PurchaseApplyAndItemBean;
import com.server.module.system.purchase.purchaseApply.PurchaseApplyBillBean;
import com.server.module.system.purchase.purchaseApply.PurchaseApplyBillDao;
import com.server.module.system.purchase.purchaseApplyItem.PurchaseApplyBillItemBean;
import com.server.module.system.purchase.purchaseApplyItem.PurchaseApplyBillItemDao;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemDao;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsService;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-09-03 16:23:53
 */
@Service
public class PurchaseBillServiceImpl implements PurchaseBillService {

	private static Logger log = LogManager.getLogger(PurchaseBillServiceImpl.class);
	@Autowired
	private PurchaseApplyBillItemDao purchaseApplyBillItemDaoImpl;
	@Autowired
	private PurchaseBillDao purchaseBillDaoImpl;
	@Autowired
	private PurchaseApplyBillDao purchaseApplyBillDaoImpl;
	@Autowired
	private PurchaseBillItemDao purchaseBillItemDaoImpl;
	@Autowired
	private PurchaseItemStatisticsService purchaseItemStatisticsServiceImpl;
	/**
	 * 采购单列表查询
	 */
	public ReturnDataUtil listPage(PurchaseBillForm PurchaseBillForm) {
		log.info("<PurchaseBillServiceImpl>----<listPage>------start");
		ReturnDataUtil listPage = purchaseBillDaoImpl.listPage(PurchaseBillForm);
		log.info("<PurchaseBillServiceImpl>----<listPage>------end");
		return listPage;
	}

	/**
	 * 根据采购单号 查询 采购单的商品
	 */
	public PurchaseBillBean getItemBean(String number) {
		log.info("<PurchaseBillServiceImpl>----<getBean>------start");
		PurchaseBillBean bean = purchaseBillDaoImpl.getItemBean(number);
		if(bean!=null) {
			//通过采购单 id 得到商品信息
			List<PurchaseBillItemBean> list = purchaseBillItemDaoImpl.list(bean.getId());
			bean.setList(list);
			bean.setSupplierId(list.get(0).getSupplierId().intValue());
			bean.setSupplierName(list.get(0).getSupplierName());
			log.info("<PurchaseBillServiceImpl>----<getBean>------end");
			return bean;
		}else {
			log.info("<PurchaseBillServiceImpl>----<getBean>------end");
			return null;
		}
		
	}
	
	public PurchaseBillBean add(PurchaseBillBean entity) {
		return purchaseBillDaoImpl.insert(entity);
	}

	public boolean update(PurchaseBillBean entity) {
		log.info("<PurchaseBillServiceImpl>------<update>----start");
		boolean update = purchaseBillDaoImpl.update(entity);
		log.info("<PurchaseBillServiceImpl>------<update>----end");
		return update;
	}

	/**
	 * 删除采购单
	 */
	public boolean del(Object id) {
		log.info("<PurchaseBillServiceImpl>-----<del>-------start");
		boolean delete = purchaseBillDaoImpl.delete(id);
		log.info("<PurchaseBillServiceImpl>-----<del>-------end");
		return delete;
	}

	public List<PurchaseBillBean> list(PurchaseBillForm condition) {
		return null;
	}

	public PurchaseBillBean get(Object id) {
		return purchaseBillDaoImpl.get(id);
	}

	@Override
	public ReturnDataUtil checkFalse(PurchaseApplyBillBean bean) {
		log.info("<PurchaseBillServiceImpl>------<checkFalse>----start");
		ReturnDataUtil data=purchaseApplyBillDaoImpl.checkFalse(bean);
		log.info("<PurchaseBillServiceImpl>------<checkFalse>----end");
		return data;
	}

	@Override
	public ReturnDataUtil auditing(PurchaseApplyAndItemBean bean) {
		log.info("<PurchaseBillServiceImpl>------<auditing>----start");
		//对多表进行操作
		Connection conn = null;
		ReturnDataUtil data = new ReturnDataUtil();
		List<PurchaseApplyBillItemBean> items = bean.getItems();
		try {
			conn = BaseDB.openConnection();// 取得同一个连接对象
			// 开启手动事物
			conn.setAutoCommit(false);
			//修改采购申请单的状态.并添加商品供应商信息，完善采购单的信息
			purchaseApplyBillDaoImpl.update(bean,conn);//更新采购申请单
			for (PurchaseApplyBillItemBean purchaseApplyBillItemBean : items) {
				purchaseApplyBillItemDaoImpl.update(purchaseApplyBillItemBean,conn);//更新供应商
			}
			//向采购单中添加数据
			//按供应商的种类生成不同的采购单
			Map map=new HashMap();//key为供应商id，value为商品的List集合
			for (PurchaseApplyBillItemBean entity : items) {
				if(map.containsKey(entity.getSupplierId())) {//有该供应商的列表
					List<PurchaseApplyBillItemBean> list=(List<PurchaseApplyBillItemBean>) map.get(entity.getSupplierId());//用来存储供应商一样的商品
					list.add(entity);
				}else {//没有该供应商，创建新的
					List<PurchaseApplyBillItemBean> list=new ArrayList<PurchaseApplyBillItemBean>();
					list.add(entity);//把商品添加大list中
					map.put(entity.getSupplierId(), list);
				}
			}
			//遍历map，向采购单中添加商品集合，并生成新的采购单
			int i=0;
			for(Object c:map.values()) {
				//先生成新的采购单
				//补充数据
				i++;
				PurchaseBillBean purchaseBean=new PurchaseBillBean();
				purchaseBean.setApplyId(bean.getId());
				purchaseBean.setApplyNumber(bean.getNumber());
				purchaseBean.setWarehouseId(bean.getWarehouseId());
				purchaseBean.setAuditor(bean.getAuditor());
				purchaseBean.setAuditorName(bean.getAuditorName());
				purchaseBean.setOperator(bean.getOperator());
				purchaseBean.setOperatorName(bean.getOperatorName());
				SimpleDateFormat sf=new SimpleDateFormat("YYYYMMddHHmmss");
				String text=sf.format(new Date());
				String tempStr=text.substring(0, text.length()-1);
				String newStr=tempStr+i;
				//验证采购单号的唯一性
				boolean falg=true;
				while(falg) {
					boolean isOne=purchaseBillDaoImpl.checkOnlyOne(newStr);
					if(isOne) {//存在该单号
						falg=true;
						tempStr=text.substring(0, text.length()-2);
						Random random=new Random();
						newStr=tempStr+random.nextInt(10)+random.nextInt(10);
					}else {//该单号不存在可以添加
						falg=false;
					}
				}
				purchaseBean.setNumber(newStr);
				//purchaseBean.setNumber("6666");//需要修改
				purchaseBean.setState(0L);
				purchaseBean.setStorageState(0);
				purchaseBean.setRemark(bean.getRemark());
				purchaseBean.setAuditOpinion(bean.getAuditOpinion());
				purchaseBean.setWarehouseName(bean.getWarehouseName());
				data=purchaseBillDaoImpl.addPurchaseBill(purchaseBean,conn);//返回id
				Integer id=(Integer)data.getReturnObject();
				
				//把id添加到items中
				List<PurchaseApplyBillItemBean> list=(List)c;
				for (PurchaseApplyBillItemBean entity : list) {
					PurchaseBillItemBean billBean=new PurchaseBillItemBean();
					billBean.setBillId(id.longValue());
					billBean.setItemId(entity.getItemId());
					billBean.setBarCode(entity.getBarCode());
					billBean.setItemName(entity.getItemName());
					billBean.setUnitName(entity.getUnitName());
					billBean.setSupplierId(entity.getSupplierId());
					billBean.setPrice(entity.getPrice());
					billBean.setApplyQuantity(entity.getQuantity().longValue());
					billBean.setQuantity(entity.getRealQuantity().longValue());//实际采购量
					billBean.setStorageState(0);
					billBean.setStorageQuantity(0L);//入库数量
					billBean.setRemark(entity.getRemark());
					//把数据添加到数据库中
					data=purchaseBillItemDaoImpl.addItem(billBean,conn);
				}
				
			}
			log.info("<PurchaseBillServiceImpl>------<auditing>----end");
			data.setStatus(0);
			conn.commit();
		} catch (SQLException e) {
			data.setStatus(-1);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();

		} finally {
			BaseDB.closeConnection(conn);
		}
		//更新商品的成本统计
		if(data.getStatus()==0) {
			for (PurchaseApplyBillItemBean entity : items) {
				PurchaseBillItemBean billBean=new PurchaseBillItemBean();
				billBean.setItemId(entity.getItemId());
				billBean.setPrice(entity.getPrice());
				billBean.setQuantity(entity.getRealQuantity().longValue());//实际采购量
				//purchaseItemStatisticsServiceImpl.addToStatistics(billBean);
			}
		}
		return data;
	}
}
