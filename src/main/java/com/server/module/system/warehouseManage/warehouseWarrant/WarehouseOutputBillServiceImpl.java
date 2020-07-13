package com.server.module.system.warehouseManage.warehouseWarrant;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.dbpool.BaseDB;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.purchase.purchaseBill.PurchaseBillBean;
import com.server.module.system.purchase.purchaseBill.PurchaseBillDao;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemDao;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsBean;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsDao;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsService;
import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.stock.WarehouseStockDao;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogBean;
import com.server.module.system.warehouseManage.stockLog.WarehouseStockLogDao;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoBean;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoDao;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemDao;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
@Service
public class WarehouseOutputBillServiceImpl implements WarehouseOutputBillService {

	private static Logger log = LogManager.getLogger(WarehouseOutputBillServiceImpl.class);
	@Autowired
	private WarehouseOutputBillDao warehouseOutputBillDaoImpl;

	@Autowired
	private WarehouseInfoDao warehouseInfoDaoImpl;
	@Autowired
	private CompanyDao companyDaoImpl;
	@Autowired
	private PurchaseBillItemDao purchaseBillItemDaoImpl;
	@Autowired
	private WarehouseBillItemDao warehouseBillItemDaoImpl;
	@Autowired
	private PurchaseBillDao purchaseBillDaoImpl;
	@Autowired
	private WarehouseStockDao warehouseStockDaoImpl;
	@Autowired
	private WarehouseStockLogDao warehouseStockLogDaoImpl;
	@Autowired
	private PurchaseItemStatisticsService pisService;
	@Autowired
	private PurchaseItemStatisticsDao purchaseItemStatisticsDao;
	/***
	 * 入库单列表查询
	 */
	public ReturnDataUtil listPage(WarehouseOutputBillForm warehouseOutputBillForm) {
		log.info("<WarehouseOutputBillServiceImpl>------<listPage>-----start");
		ReturnDataUtil listPage = warehouseOutputBillDaoImpl.listPage(warehouseOutputBillForm);
		log.info("<WarehouseOutputBillServiceImpl>------<listPage>-----end");
		return listPage;
	}

	/**
	 * 增加入库单
	 */
	public WarehouseOutputBillBean add(WarehouseOutputBillBean entity) {
		log.info("<WarehouseOutputBillServiceImpl>------<add>-----start");
		WarehouseOutputBillBean insert = warehouseOutputBillDaoImpl.insert(entity);
		log.info("<WarehouseOutputBillServiceImpl>------<add>-----end");
		return insert;
	}

	/**
	 * 根据单号 查询详情
	 * 
	 * @param changeId
	 * @return
	 */
	public WarehouseOutputBillBean get(String changeId) {
		log.info("<WarehouseOutputBillServiceImpl>------<get>-----start");
		WarehouseOutputBillBean bean = warehouseOutputBillDaoImpl.get(changeId);
		log.info("<WarehouseOutputBillServiceImpl>------<get>-----end");
		return bean;
	}

	/**
	 * 修改入库单
	 */
	public boolean update(WarehouseOutputBillBean entity) {
		log.info("<WarehouseOutputBillServiceImpl>------<update>-----start");
		boolean update = warehouseOutputBillDaoImpl.update(entity);
		log.info("<WarehouseOutputBillServiceImpl>------<update>-----end");
		return update;
	}

	/**
	 * 生成入库单号
	 */
	@Override
	public String findChangeId() {
		log.info("<WarehouseOutputBillServiceImpl>------<findChangeId>-----start");
		String findChangeId = warehouseOutputBillDaoImpl.findChangeId();
		log.info("<WarehouseOutputBillServiceImpl>------<findChangeId>-----end");
		return findChangeId;
	}

	public WarehouseOutputBillBean getById(Object id) {
		return warehouseOutputBillDaoImpl.getById(id);
	}

	/**
	 * 事务控制
	 * 
	 * @author why
	 * @date 2018年9月11日-上午11:19:41
	 * @param conn
	 * @param id
	 * @return
	 */
	public WarehouseOutputBillBean getById(Connection conn, Object id) {
		return warehouseOutputBillDaoImpl.getById(conn, id);
	}

	/**
	 * 库存转移时 查询已经入库数量
	 */
	public Long getQuantity(Long removalStockId, Long targetWarehouseId, Long sourceWarehouseId, Long itemId) {
		log.info("<WarehouseOutputBillServiceImpl>------<getQuantity>-----start");
		Long quantity = warehouseOutputBillDaoImpl.getQuantity(removalStockId, targetWarehouseId, sourceWarehouseId,
				itemId);
		log.info("<WarehouseOutputBillServiceImpl>------<getQuantity>-----end");
		return quantity;
	}

	/**
	 * 入库审核
	 */
	@Override
	public synchronized ReturnDataUtil putStock(WarehouseOutputBillBean entity) {
		log.info("<WarehouseOutputBillServiceImpl>----<putStock>------start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		Connection conn = BaseDB.openConnection();
		log.info("connecti0n==========" + conn);
		try {
			conn.setAutoCommit(false);
			// 得到生成的入库单号
			String findChangeId = this.findChangeId();
			entity.setNumber(findChangeId);
			// 审核状态 60201 待审核 60202 已审核 60203 已入库
			entity.setState(60202);
			// 出入库类型 0 入库 1 出库
			entity.setOutput(0);
			entity.setAuditor(UserUtils.getUser().getId().intValue());
			entity.setOperator(UserUtils.getUser().getId().intValue());
			if (entity.getHappenDate() == null) {
				entity.setHappenDate(new Date());
			}
			// 通过仓库id 得到该仓库所属公司 保存到入库单里
			WarehouseInfoBean warehouseInfoBean = warehouseInfoDaoImpl.get(entity.getWarehouseId());
			CompanyBean companyBean = companyDaoImpl.findCompanyById(warehouseInfoBean.getCompanyId().intValue());
			entity.setCompanyId(warehouseInfoBean.getCompanyId().intValue());
			entity.setCompanyName(companyBean.getName());
			WarehouseOutputBillBean bean = warehouseOutputBillDaoImpl.insertTransaction(conn, entity);
			if (bean != null) {
				WarehouseOutputBillBean billBean = warehouseOutputBillDaoImpl.gettransaction(conn, findChangeId);
				WarehouseBillItemBean add = null;
				for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
					// 判断是否为采购入库的方式
					if (StringUtil.isNotBlank(entity.getPurchaseNumber())) {
						//入库的均价计算

						log.info("<WarehouseOutputBillServiceImpl>----------采购入库数量判断 >>>>>>>>");
						// 进行判断入库数量是否足够
						PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemDaoImpl
								.get(warehouseBillItemBean.getPurchaseItemId());
						log.info("已入库数量==="+purchaseBillItemBean.getStorageQuantity()+"---实际采购数量======="+purchaseBillItemBean.getQuantity());
						// 得到采购回来的剩余数量
						Long num = purchaseBillItemBean.getQuantity() - purchaseBillItemBean.getStorageQuantity();
						log.info("本次可以入库最大数量"+num+"--本次入库数量==="+warehouseBillItemBean.getQuantity());
						// 判断入库是 数量是否足够
						if (warehouseBillItemBean.getQuantity() > num) {
							log.info("》》》商品数量不足 无法入库，请重新输入");
							returnDataUtil.setStatus(0);
							returnDataUtil.setMessage(warehouseBillItemBean.getItemName()
									+ "》》》商品数量不足 无法入库，请重新输入！您本次最大可以入库" + num + "件商品");
							return returnDataUtil;
						} else {
							warehouseBillItemBean.setBillId(billBean.getId().intValue());
							warehouseBillItemBean.setCreateTime(new Date());
							PurchaseItemStatisticsBean pisBean=purchaseItemStatisticsDao.getBeanByItemId(warehouseBillItemBean.getItemId());
							warehouseBillItemBean.setMoney( (new BigDecimal(pisBean.getAvgPrice()).multiply(new BigDecimal(warehouseBillItemBean.getQuantity())) ));
							warehouseBillItemBean.setAveragePrice(new BigDecimal(pisBean.getAvgPrice()));
							warehouseBillItemBean.setOutput(0);
							add = warehouseBillItemDaoImpl.insertTransaction(conn, warehouseBillItemBean);
							pisService.addToStatistics2(warehouseBillItemBean);

						}
						// 判断是否为库存转移
					} else if (entity.getType().equals(60206)) {
						log.info("《--------来的库存转移 判断-------》");
						log.info("出库单id===" + entity.getPurchaseId() + "目标仓库id===" + entity.getTargetWarehouseId()
								+ "---来源仓库id" + entity.getSourceWarehouseId());
						Long quantity = warehouseOutputBillDaoImpl.getQuantity(entity.getPurchaseId().longValue(),
								entity.getTargetWarehouseId(), entity.getSourceWarehouseId(),
								warehouseBillItemBean.getItemId());
						// 商品数量减去已入库数量 得到可以入库数量
						log.info("商品数量------" + warehouseBillItemBean.getPurchaseQuantity() + "====已入库数量" + quantity);
						Long num = warehouseBillItemBean.getPurchaseQuantity() - quantity;
						// 判断入库是 数量是否足够
						if (warehouseBillItemBean.getQuantity() > num) {
							returnDataUtil.setStatus(0);
							returnDataUtil.setMessage(warehouseBillItemBean.getItemName() + "》》》商品数量不足 无法入库，请重新输入！");
							return returnDataUtil;
						} else {
							warehouseBillItemBean.setBillId(billBean.getId().intValue());
							warehouseBillItemBean.setCreateTime(new Date());
							PurchaseItemStatisticsBean pisBean=purchaseItemStatisticsDao.getBeanByItemId(warehouseBillItemBean.getItemId());
							warehouseBillItemBean.setMoney( (new BigDecimal(pisBean.getAvgPrice()).multiply(new BigDecimal(warehouseBillItemBean.getQuantity())) ));
							warehouseBillItemBean.setAveragePrice(new BigDecimal(pisBean.getAvgPrice()));
							warehouseBillItemBean.setOutput(0);
							add = warehouseBillItemDaoImpl.insertTransaction(conn, warehouseBillItemBean);

							pisService.addToStatistics2(warehouseBillItemBean);

						}
					} else {
						warehouseBillItemBean.setBillId(billBean.getId().intValue());
						warehouseBillItemBean.setCreateTime(new Date());
						PurchaseItemStatisticsBean pisBean=purchaseItemStatisticsDao.getBeanByItemId(warehouseBillItemBean.getItemId());
						warehouseBillItemBean.setMoney( (new BigDecimal(pisBean.getAvgPrice()).multiply(new BigDecimal(warehouseBillItemBean.getQuantity())) ));
						warehouseBillItemBean.setAveragePrice(new BigDecimal(pisBean.getAvgPrice()));
						warehouseBillItemBean.setOutput(0);
						add = warehouseBillItemDaoImpl.insertTransaction(conn, warehouseBillItemBean);

						pisService.addToStatistics2(warehouseBillItemBean);
					}

				}
				
				try {
					// 改变库存
					this.putStorageTransaction(conn, billBean);
				}catch (Exception e) {
					conn.rollback();
				}
				
				if (add != null) {
					// 判断是否为采购入库的方式
					if (StringUtil.isNotBlank(entity.getPurchaseNumber())) {
						log.info("<WarehouseWarrantInfoController>-----来到采购入库 处理采购单");
						// 标记是否修改成功
						Integer flag = 0;
						// 遍历传入的数据 分别修改对应的改变采购单 中的入库数量鱼入库状态
						for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
							PurchaseBillItemBean purchaseBillItemBean = new PurchaseBillItemBean();
							// 判断入库数量 与实际采购数量 是否相等
							log.info("实际采购数量" + warehouseBillItemBean.getPurchaseQuantity() + "---入库数量"
									+ warehouseBillItemBean.getQuantity().longValue());
							if (warehouseBillItemBean.getPurchaseQuantity()
									.equals(warehouseBillItemBean.getQuantity().longValue())) {
								log.info("《《《《《是否全部商品入库》》》》》");
								// 修改入库状态 0 未入库 1 部分入库 2 全部入库
								purchaseBillItemBean.setStorageState(2);
							} else {
								purchaseBillItemBean.setStorageState(1);
							}
							purchaseBillItemBean.setId(warehouseBillItemBean.getPurchaseItemId().longValue());
							purchaseBillItemBean.setStorageQuantity(warehouseBillItemBean.getQuantity().longValue());
							log.info("=====入库成功后 修改入库商品数量=========");
							boolean update = purchaseBillItemDaoImpl.updateTransaction(conn, purchaseBillItemBean);
							if (update) {
								flag++;
							}
						}
						// 判断是否全部入库成功
						if (flag.equals(entity.getList().size())) {
							log.info("<WarehouseWarrantInfoController>-----第一步 来到 全部入库成功后 修改采购单的状态");
							Integer temp = 0;
							// 遍历采购单商品 进行一个检测 是否已经全部入库 如果全部入库 修改对应状态
							for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
								// 根据id查询商品信息
								log.info("第二步 ===================根据id查询采购商品信息===========");
								PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemDaoImpl.getTransaction(conn,
										warehouseBillItemBean.getPurchaseItemId().longValue());
								log.info("查询出来的商品信息====" + JsonUtils.toJson(purchaseBillItemBean));
								if (purchaseBillItemBean.getQuantity()
										.equals(purchaseBillItemBean.getStorageQuantity())) {
									temp++;
									PurchaseBillItemBean billItemBean = new PurchaseBillItemBean();
									billItemBean.setId(warehouseBillItemBean.getPurchaseItemId().longValue());
									billItemBean.setStorageState(2);
									log.info("<---商品数量---->===" + warehouseBillItemBean.getQuantity().longValue());
									billItemBean.setStorageQuantity(0L);
									log.info("第三步  ====修改采购单商品信息===");
									purchaseBillItemDaoImpl.updateTransaction(conn, billItemBean);
								}
							}
							log.info("第三步 是否全部商品入库成功-------" + temp);
							// 全部商品入库成功后 修改采购单的状态
							PurchaseBillBean purchaseBillBean = new PurchaseBillBean();
							if (temp.equals(entity.getList().size())) {
								purchaseBillBean.setState(2L);
								purchaseBillBean.setStorageState(2);
							} else {
								purchaseBillBean.setStorageState(1);
							}
							purchaseBillBean.setId(entity.getPurchaseId().longValue());
							log.info("====第四步   修改采购单信息===");
							purchaseBillDaoImpl.updateTransaction(conn, purchaseBillBean);

							log.info("===================采购入库完成==========================");
							returnDataUtil.setStatus(1);
							returnDataUtil.setMessage("入库成功，审核通过！");
							log.info("<WarehouseWarrantInfoController>------<insert>-----end");

						} else {
							returnDataUtil.setStatus(0);
							returnDataUtil.setMessage("入库失败,审核未通过！");
							log.info("<WarehouseWarrantInfoController>------<insert>-----end");
						}

					} else {
						log.info("<WarehouseWarrantInfoController>------<insert>-----非采购入库方式");
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("入库成功，审核通过！");
						log.info("<WarehouseWarrantInfoController>------<insert>-----end");
					}
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("入库失败,审核未通过！");
					log.info("<WarehouseWarrantInfoController>------<insert>-----end");
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("入库失败，审核未通过！");
				log.info("<WarehouseWarrantInfoController>------<insert>-----end");
			}

			try {
				conn.commit();
			} catch (Exception e) {
				try {
					conn.rollback();
				} catch (Exception e1) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("入库失败，审核未通过！");
					e1.printStackTrace();
					log.error(e1.getMessage());
				}
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("入库失败，审核未通过！");
				e.printStackTrace();
				log.error(e.getMessage());
			}

		} catch (SQLException e) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("入库失败，审核未通过！");
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			BaseDB.closeConnection(conn);
		}
		return returnDataUtil;
	}

	/**
	 * 入库操作库存以及日志信息
	 * 
	 * @author why
	 * @date 2018年9月11日-上午11:41:31
	 * @param bill
	 */
	public synchronized void putStorageTransaction(Connection conn, WarehouseOutputBillBean bill) {
		log.info("入库操作库存以及日志信息==="+JsonUtils.toJson(bill));
		// 查出该单据下的商品项目
		List<WarehouseBillItemBean> list = warehouseBillItemDaoImpl.getBillItemTransaction(conn,
				bill.getId().intValue());
		Long warehouseId = bill.getWarehouseId();
		log.info("size=====" + list.size());
		for (WarehouseBillItemBean item : list) {
			Long itemId = item.getItemId();
			// 根据商品id查询该商品的库存记录
			WarehouseStockBean stock = warehouseStockDaoImpl.getStockTransaction(conn, warehouseId, itemId);
			long preQuantity = 0;
			long quantity = 0;
			if (stock == null) {// 仓库没存在该商品,需要插入
				stock = new WarehouseStockBean();
				stock.setQuantity(item.getQuantity().longValue());
				stock.setItemId(itemId);
				stock.setWarehouseId(warehouseId);
				stock.setItemName(item.getItemName());
				stock.setCostPrice(item.getPrice().doubleValue());
				stock.setPrice(0.0d);
				quantity = item.getQuantity().longValue();
				stock.setCompanyId(bill.getCompanyId() * 1l);
				stock.setCompanyName(bill.getCompanyName());
				stock.setWarehouseId(bill.getWarehouseId());
				stock.setWarehouseName(bill.getWarehouseName());
				stock.setBarCode(item.getBarCode());
				stock.setUnitName(item.getUnitName());
				warehouseStockDaoImpl.insert(conn, stock);
				item.setAveragePrice(item.getPrice());
			} else {// 已存在该商品库存 更改数量 价格
				preQuantity = stock.getQuantity();
				stock.setQuantity(stock.getQuantity() + item.getQuantity());
				// 动态计算价格
				//double average = warehouseStockDaoImpl.getAveragePrice(bill.getWarehouseId(), item.getItemId(),
					//	item.getQuantity(), item.getPrice());
				//stock.setCostPrice(average);
				//item.setAveragePrice(average);// 记录此时的平均价格
				warehouseStockDaoImpl.update(conn, stock);
			}
			warehouseBillItemDaoImpl.update(conn, item);// 记录此时的平均价格
			// 日志保存
			WarehouseStockLogBean warehouseStockLogBean = new WarehouseStockLogBean();
			warehouseStockLogBean.setBillId(bill.getId());
			warehouseStockLogBean.setBillItemId(itemId);
			warehouseStockLogBean.setItemName(item.getItemName());
			warehouseStockLogBean.setPreQuantity(preQuantity);
			warehouseStockLogBean.setNum(item.getQuantity().longValue());
			warehouseStockLogBean.setOutput(bill.getOutput());
			warehouseStockLogBean.setStockId(stock.getId());
			warehouseStockLogBean.setItemId(itemId);
			warehouseStockLogBean.setWarehouseName(bill.getWarehouseName());
			warehouseStockLogBean.setQuantity(stock.getQuantity());
			warehouseStockLogBean.setWarehouseId(bill.getWarehouseId());
			warehouseStockLogBean.setType(bill.getType());
			warehouseStockLogDaoImpl.insert(conn, warehouseStockLogBean);
		}
		bill.setState(60203);// 已入库
		warehouseOutputBillDaoImpl.update(conn, bill);// 更新单据状态
		log.info("==============采购入库更新完毕======");

	}
}
