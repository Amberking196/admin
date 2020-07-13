package com.server.module.system.warehouseManage.warehouseRemoval;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.server.dbpool.BaseDB;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.stock.WarehouseStockDao;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogBean;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogDao;
import com.server.module.system.warehouseManage.warehouseGiveBack.WarehouseGiveBackBean;
import com.server.module.system.warehouseManage.warehouseGiveBack.WarehouseGiveBackDao;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
@Service
public class WarehouseRemovalServiceImpl implements WarehouseRemovalService {

	private static Logger log = LogManager.getLogger(WarehouseRemovalServiceImpl.class);
	@Autowired
	private WarehouseRemovalDao warehouseRemovalDaoImpl;
	@Autowired
	private WarehouseStockDao warehouseStockDaoImpl;
	@Autowired
	private WarehouseStockLogDao stockLogDao;

	/***
	 * 出库单列表查询
	 */
	public ReturnDataUtil listPage(WarehouseRemovalForm warehouseRemovalForm) {
		log.info("<WarehouseRemovalServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = warehouseRemovalDaoImpl.listPage(warehouseRemovalForm);
		log.info("<WarehouseRemovalServiceImpl>------<listPage>-----end");
		return listPage;
	}

	/**
	 * 增加出库单
	 */
	public WarehouseOutputBillBean add(WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalServiceImpl>------<add>-----start");
		WarehouseOutputBillBean insert = warehouseRemovalDaoImpl.insert(entity);
		log.info("<WarehouseRemovalServiceImpl>------<add>-----end");
		return insert;
	}

	/**
	 * 根据单号 查询详情
	 * 
	 * @param changeId
	 * @return
	 */
	public WarehouseOutputBillBean get(String changeId) {
		log.info("<WarehouseRemovalServiceImpl>------<get>-----start");
		WarehouseOutputBillBean bean = warehouseRemovalDaoImpl.get(changeId);
		log.info("<WarehouseRemovalServiceImpl>------<get>-----end");
		return bean;
	}

	/**
	 * 修改出库单
	 */
	public boolean update(WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalServiceImpl>------<update>-----start");
		boolean update = warehouseRemovalDaoImpl.update(entity);
		log.info("<WarehouseRemovalServiceImpl>------<update>-----end");
		return update;
	}

	/**
	 * 生成出库单号
	 */
	@Override
	public String findChangeId() {
		log.info("<WarehouseRemovalServiceImpl>------<findChangeId>-----start");
		String findChangeId = warehouseRemovalDaoImpl.findChangeId();
		log.info("<WarehouseRemovalServiceImpl>------<findChangeId>-----end");
		return findChangeId;
	}

	/**
	 * 判断库存是否足够
	 */
	public List<WarehouseStockBean> checkQuantity(Long warehouseId, String itemIds) {
		log.info("<WarehouseRemovalServiceImpl>------<checkQuantity>-----start");
		List<WarehouseStockBean> list = warehouseRemovalDaoImpl.checkQuantity(warehouseId, itemIds);
		log.info("<WarehouseRemovalServiceImpl>------<checkQuantity>-----end");
		return list;
	}

	// 改变库存以及增加日志
	public void changeStock(WarehouseOutputBillBean bean) {
		log.info("<WarehouseGiveBackServiceImpl>------<changeStock>-------start");
		for (WarehouseBillItemBean entity : bean.getList()) {
			Connection conn = BaseDB.openConnection();
			WarehouseStockBean stock = warehouseStockDaoImpl.getStock(bean.getWarehouseId(), entity.getItemId());
			stock.setQuantity(stock.getQuantity() + entity.getQuantity());
			warehouseStockDaoImpl.update(conn, stock);
			// 日志保存
			WarehouseStockLogBean warehouseStockLogBean = new WarehouseStockLogBean();
			warehouseStockLogBean.setBillId(bean.getId());
			warehouseStockLogBean.setBillItemId(entity.getId());
			warehouseStockLogBean.setItemName(entity.getItemName());
			warehouseStockLogBean.setPreQuantity(stock.getQuantity());
			warehouseStockLogBean.setNum(entity.getQuantity().longValue());
			warehouseStockLogBean.setOutput(0);
			warehouseStockLogBean.setStockId(stock.getId());
			warehouseStockLogBean.setItemId(entity.getItemId());
			warehouseStockLogBean.setWarehouseId(bean.getWarehouseId());
			warehouseStockLogBean.setWarehouseName(bean.getWarehouseName());
			warehouseStockLogBean.setQuantity(stock.getQuantity());
			warehouseStockLogBean.setType(bean.getType());
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
			BaseDB.closeConnection(conn);
		}

		log.info("<WarehouseGiveBackServiceImpl>------<changeStock>-------end");
	}

	public WarehouseOutputBillBean getBean(Object id) {
		log.info("<WarehouseGiveBackServiceImpl>------<getBean>-------start");
		WarehouseOutputBillBean bean = warehouseRemovalDaoImpl.getBean(id);
		log.info("<WarehouseGiveBackServiceImpl>------<getBean>-------end");
		return bean;
	}
}
