package com.server.module.system.purchase.purchaseApply;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.dbpool.BaseDB;
import com.server.module.system.purchase.purchaseApplyItem.PurchaseApplyBillItemBean;
import com.server.module.system.purchase.purchaseApplyItem.PurchaseApplyBillItemDao;
import com.server.module.system.purchase.purchaseBill.PurchaseBillBean;
import com.server.module.system.purchase.purchaseBill.PurchaseBillDao;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemDao;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-08-31 17:27:57
 */
@Service
public class PurchaseApplyBillServiceImpl implements PurchaseApplyBillService {

	private static Log log = LogFactory.getLog(PurchaseApplyBillServiceImpl.class);
	@Autowired
	private PurchaseApplyBillDao purchaseApplyBillDaoImpl;
	@Autowired
	private PurchaseApplyBillItemDao purchaseApplyBillItemDaoImpl;
	@Autowired
	private PurchaseBillDao purchaseBillDaoImpl;
	@Autowired
	private PurchaseBillItemDao purchaseBillItemDaoImpl;

	/**
	 * 采购申请单列表
	 * @param condition
	 * @return
	 */
	public ReturnDataUtil listPage(PurchaseApplyBillCondition condition) {
		return purchaseApplyBillDaoImpl.listPage(condition);
	}

	/**
	 * 采购申请单添加
	 * @param entity
	 * @return
	 */
	public PurchaseApplyBillBean add(PurchaseApplyBillBean entity) {
		return purchaseApplyBillDaoImpl.insert(entity);
	}
	/**
	 * 采购申请单修改
	 * @param entity
	 * @return
	 */
	public boolean update(PurchaseApplyBillBean entity) {
		return purchaseApplyBillDaoImpl.update(entity);
	}

	/**
	 * 采购申请单删除
	 * @param id
	 * @return
	 */
	public boolean del(Object id) {
		return purchaseApplyBillDaoImpl.delete(id);
	}

	public List<PurchaseApplyBillBean> list(PurchaseApplyBillCondition condition) {
		return null;
	}

	/**
	 * 采购申请单获取
	 * @param id
	 * @return
	 */
	public PurchaseApplyBillBean get(Object id) {
		return purchaseApplyBillDaoImpl.get(id);
	}

	@Override
	public ReturnDataUtil addPurchaseApply(PurchaseApplyAndItemBean bean) {
		log.info("<PurchaseApplyBillServiceImpl>------<addPurchaseApply>----start");
		Connection conn = null;
		ReturnDataUtil data = null;
		try {
			conn = BaseDB.openConnection();// 取得同一个连接对象
			// 开启手动事物
			conn.setAutoCommit(false);
			// 设置初始值
			// 设置单号
			SimpleDateFormat sf=new SimpleDateFormat("YYYYMMddHHmmss");
			String text=sf.format(new Date());
			//创建PurchaseApplyBillBean
			PurchaseApplyBillBean domain=new PurchaseApplyBillBean();
			domain.setWarehouseId(bean.getWarehouseId());
			domain.setOperator(bean.getOperator());
			domain.setOperatorName(bean.getOperatorName());
			domain.setRemark(bean.getRemark());
			domain.setAuditOpinion(bean.getAuditOpinion());
			domain.setWarehouseName(bean.getWarehouseName());
			domain.setNumber(text);
			//domain.setNumber("666666");
			domain.setState(1L);// 状态 0 未提交 1 已提交 2 未通过 3 已通过
			domain.setDeleteFlag(0);// 0 未删除 1 已删除'
			domain.setAuditor(0L);// 采购审核人
			domain.setAuditorName("");// 审核人名字
			data = purchaseApplyBillDaoImpl.addPurchaseApply(domain, conn);
			if (data.getStatus() != 0) {// 保存失败
				return data;
			}
			// 取出采购单的自增id
			Integer id = (Integer) data.getReturnObject();
			for (PurchaseApplyBillItemBean entity : bean.getItems()) {
				entity.setBillId(id.longValue());
				// 保存数据
				data = purchaseApplyBillItemDaoImpl.addItem(conn, entity);
				// stockLogDao.insert(conn, warehouseStockLogBean);
			}
			log.info("<PurchaseApplyBillServiceImpl>------<addPurchaseApply>----end");
			conn.commit();
			data.setStatus(0);
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
		return data;
	}

	@Override
	public ReturnDataUtil submitPurchaseApply(Integer id) {
		log.info("<PurchaseApplyBillServiceImpl>------<submitPurchaseApply>----start");
		ReturnDataUtil data=purchaseApplyBillDaoImpl.submitPurchaseApply(id);
		log.info("<PurchaseApplyBillServiceImpl>------<submitPurchaseApply>----end");
		return data;
	}

	@Override
	public ReturnDataUtil del(Integer id) {
		log.info("<PurchaseApplyBillServiceImpl>------<submitPurchaseApply>----start");
		ReturnDataUtil data=purchaseApplyBillDaoImpl.delete(id);
		log.info("<PurchaseApplyBillServiceImpl>------<submitPurchaseApply>----end");
		return data;
	}

	@Override
	public PurchaseApplyBillBean getBeanById(Integer id) {
		log.info("<PurchaseApplyBillServiceImpl>------<getBeanById>----start");
		PurchaseApplyBillBean bean=purchaseApplyBillDaoImpl.getBeanById(id);
		log.info("<PurchaseApplyBillServiceImpl>------<getBeanById>----end");
		
		return bean;
	}

	@Override
	public ReturnDataUtil getPurchaseBillList(Integer id) {
		log.info("<PurchaseApplyBillServiceImpl>------<getPurchaseBillList>----start");
		//根据申请单id查询多个采购单
		ReturnDataUtil data=new ReturnDataUtil();
		List<PurchaseBillBean> itemBean = purchaseBillDaoImpl.getItemBean(id);
		//根据采购单id查询多个商品
		for (PurchaseBillBean purchaseBillBean : itemBean) {
			List<PurchaseBillItemBean> list = purchaseBillItemDaoImpl.list(purchaseBillBean.getId());
			String name=null;
			for (PurchaseBillItemBean bean : list) {
				name=bean.getSupplierName();
			}
			purchaseBillBean.setList(list);
			purchaseBillBean.setSupplierName(name);
			//purchaseBillBean.setSupplierName(supplierName);
		}
		if(itemBean.size()>0) {
			data.setStatus(0);
			data.setReturnObject(itemBean);
		}
		log.info("<PurchaseApplyBillServiceImpl>------<getPurchaseBillList>----end");
		return data;
	}
}
