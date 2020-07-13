package com.server.module.system.warehouseManage.warehouseGiveBack;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.dbpool.BaseDB;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.stock.WarehouseStockDao;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogBean;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogDao;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoBean;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoDao;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillDao;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemDao;
import com.server.util.ReturnDataUtil;

/**
 * author name: yjr create time: 2018-05-24 11:34:32
 */
@Service
public class WarehouseGiveBackServiceImpl implements WarehouseGiveBackService {

	private static Logger log = LogManager.getLogger(WarehouseGiveBackServiceImpl.class);
	@Autowired
	private WarehouseGiveBackDao warehouseGiveBackDaoImpl;
	@Autowired
	private WarehouseStockDao warehouseStockDaoImpl;
	@Autowired
	private WarehouseStockLogDao stockLogDao;
	@Autowired
	private WarehouseInfoDao warehouseInfoDaoImpl;
	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private WarehouseOutputBillDao warehouseOutputBillDaoImpl;
	@Autowired
	private WarehouseBillItemDao warehouseBillItemDaoImpl;

	public ReturnDataUtil listPage(WarehouseGiveBackForm warehouseGiveBackForm) {
		log.info("<WarehouseGiveBackServiceImpl>------<listPage>-------start");
		ReturnDataUtil listPage = warehouseGiveBackDaoImpl.listPage(warehouseGiveBackForm);
		log.info("<WarehouseGiveBackServiceImpl>------<listPage>-------end");
		return listPage;
	}

	public WarehouseGiveBackBean add(WarehouseGiveBackBean entity) {
		log.info("<WarehouseGiveBackServiceImpl>------<add>-------start");
		WarehouseGiveBackBean insert = warehouseGiveBackDaoImpl.insert(entity);
		/*if (insert != null) {
			// 改变库存以及增加日志
			changeStock(entity);
			log.info("<WarehouseGiveBackServiceImpl>------<add>-------end");
			return insert;
		} else {
			return null;
		}*/
		return insert;

	}

	// 改变库存以及增加日志
	public void changeStock(WarehouseGiveBackBean entity) {
		log.info("<WarehouseGiveBackServiceImpl>------<changeStock>-------start");
		Connection conn = BaseDB.openConnection();
		WarehouseStockBean stock = warehouseStockDaoImpl.getStock(entity.getWarehouseId(), entity.getItemId());
		// 得到生成的入库单号 生成入库记录
		WarehouseOutputBillBean bean = new WarehouseOutputBillBean();
		String findChangeId = warehouseOutputBillDaoImpl.findChangeId();
		bean.setWarehouseId(entity.getWarehouseId());
		bean.setWarehouseName(stock.getWarehouseName());
		bean.setNumber(findChangeId);
		bean.setState(60203);
		bean.setType(entity.getType());
		bean.setOutput(0);
		bean.setAuditor(UserUtils.getUser().getId().intValue());
		bean.setOperator(UserUtils.getUser().getId().intValue());
		bean.setCreateTime(new Date());
		WarehouseInfoBean warehouseInfoBean = warehouseInfoDaoImpl.get(entity.getWarehouseId());
		CompanyBean companyBean = companyDaoImpl.findCompanyById(warehouseInfoBean.getCompanyId().intValue());
		bean.setCompanyId(warehouseInfoBean.getCompanyId().intValue());
		bean.setCompanyName(companyBean.getName());
		WarehouseOutputBillBean billBean = warehouseOutputBillDaoImpl.insert(bean);
		WarehouseOutputBillBean byId = warehouseOutputBillDaoImpl.getById(entity.getBillId());
		if (billBean != null) {
			WarehouseOutputBillBean billBean2 = warehouseOutputBillDaoImpl.get(findChangeId);
			WarehouseBillItemBean warehouseBillItemBean = new WarehouseBillItemBean();
			warehouseBillItemBean.setBillId(billBean2.getId().intValue());
			warehouseBillItemBean.setItemId(entity.getItemId());
			warehouseBillItemBean.setItemName(entity.getItemName());
			warehouseBillItemBean.setQuantity(entity.getQuantity());
			warehouseBillItemBean.setBarCode(stock.getBarCode());
			warehouseBillItemBean.setUnitName(stock.getUnitName());
			//warehouseBillItemBean.setPrice(stock.getCostPrice());
			Double costPrice = stock.getCostPrice();
			Integer quantity2 = entity.getQuantity();
			double money = costPrice * quantity2;
			//warehouseBillItemBean.setMoney(money);
			warehouseBillItemBean.setCreateTime(new Date());
			warehouseBillItemBean
					.setRemark("本次入库为，出库单为：" + byId.getNumber() + "的单号,归还了" + entity.getQuantity() + "个商品");
			warehouseBillItemDaoImpl.insert(warehouseBillItemBean);

			// 改变库存
			Long quantity = stock.getQuantity();
			stock.setQuantity(stock.getQuantity() + entity.getQuantity());
			warehouseStockDaoImpl.update(conn, stock);
			// 日志保存
			WarehouseStockLogBean warehouseStockLogBean = new WarehouseStockLogBean();
			warehouseStockLogBean.setBillId(entity.getBillId());
			warehouseStockLogBean.setBillItemId(entity.getItemId());
			warehouseStockLogBean.setItemName(entity.getItemName());
			warehouseStockLogBean.setPreQuantity(quantity);
			warehouseStockLogBean.setNum(entity.getQuantity().longValue());
			warehouseStockLogBean.setOutput(0);
			warehouseStockLogBean.setStockId(stock.getId());
			warehouseStockLogBean.setItemId(entity.getItemId());
			warehouseStockLogBean.setWarehouseId(entity.getWarehouseId());
			warehouseStockLogBean.setWarehouseName(stock.getWarehouseName());
			warehouseStockLogBean.setQuantity(stock.getQuantity());
			warehouseStockLogBean.setType(entity.getType());
			stockLogDao.insert(conn, warehouseStockLogBean);
			try {
				conn.setAutoCommit(false);
				conn.commit();
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		BaseDB.closeConnection(conn);
		log.info("<WarehouseGiveBackServiceImpl>------<changeStock>-------end");
	}

}
