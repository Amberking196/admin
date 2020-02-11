package com.server.module.system.warehouseManage.warehouseRemoval;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsBean;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsService;
import com.server.module.system.warehouseManage.stock.WarehouseStockBean;
import com.server.module.system.warehouseManage.stock.WarehouseStockService;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoBean;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoService;
import com.server.module.system.warehouseManage.warehouseWarrant.WarehouseOutputBillBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemService;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ExportExcel;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
@Api(value = "warehouseRemovalController", description = "仓库出库")
@RestController
@RequestMapping("/warehouseRemoval")
public class WarehouseRemovalController {

	private static Logger log = LogManager.getLogger(WarehouseRemovalController.class);

	@Autowired
	private WarehouseRemovalService warehouseRemovalServiceImpl;
	@Autowired
	private WarehouseBillItemService warehouseBillItemServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private CompanyService companyServiceImpl;
	@Autowired
	private WarehouseInfoService warehouseInfoServiceImpl;
	@Autowired
	private WarehouseStockService warehouseStockServiceImpl;
	@Autowired
	private PurchaseItemStatisticsService pisService;
	
	/**
	 * 仓库出库列表
	 * 
	 * @param condition
	 * @return
	 */
	@ApiOperation(value = "仓库出库列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) WarehouseRemovalForm warehouseRemovalForm) {
		log.info("<WarehouseRemovalController>------<listPage>-----start");
		if (warehouseRemovalForm == null) {
			warehouseRemovalForm = new WarehouseRemovalForm();
		}
		returnDataUtil = warehouseRemovalServiceImpl.listPage(warehouseRemovalForm);
		log.info("<WarehouseRemovalController>------<listPage>-----end");
		return returnDataUtil;
	}

	/**
	 * 仓库出库添加 保存
	 * 
	 * @param entity
	 * @param detailBean
	 * @return
	 */
	@ApiOperation(value = "仓库出库添加 保存", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalController>------<add>-----start");
		// 得到生成的出库单号
		String findChangeId = warehouseRemovalServiceImpl.findChangeId();
		entity.setNumber(findChangeId);
		entity.setState(60401);
		entity.setOutput(1);
		//entity.setAuditor(UserUtils.getUser().getId().intValue());
		entity.setOperator(UserUtils.getUser().getId().intValue());
		WarehouseInfoBean warehouseInfoBean = warehouseInfoServiceImpl.get(entity.getWarehouseId());
		CompanyBean companyBean = companyServiceImpl.findCompanyById(warehouseInfoBean.getCompanyId().intValue());
		entity.setCompanyId(warehouseInfoBean.getCompanyId().intValue());
		entity.setCompanyName(companyBean.getName());
		entity.setWarehouseName(warehouseInfoBean.getName());
		WarehouseOutputBillBean bean = warehouseRemovalServiceImpl.add(entity);
		if (bean != null) {
			WarehouseBillItemBean add = null;
			for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
				WarehouseOutputBillBean billBean = warehouseRemovalServiceImpl.get(findChangeId);
				warehouseBillItemBean.setBillId(billBean.getId().intValue());
				add = warehouseBillItemServiceImpl.add(warehouseBillItemBean);
			}
			if (add != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("出库成功，待审核！");
				log.info("<WarehouseRemovalController>------<add>-----end");
				return returnDataUtil;
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("出库失败！");
				log.info("<WarehouseRemovalController>------<add>-----end");
				return returnDataUtil;
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("出库失败！");
			log.info("<WarehouseRemovalController>------<add>-----end");
			return returnDataUtil;
		}
	}

	/**
	 * 仓库出库 审核
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库出库 审核", notes = "insert", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil insert(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalController>------<insert>-----start");
		entity.setAuditor(UserUtils.getUser().getId().intValue());
		Map<String, String> map = new HashMap<String, String>();
		List<Long> itemIdList = Lists.newArrayList();
		for (WarehouseBillItemBean billItemBean : entity.getList()) {
			itemIdList.add(billItemBean.getItemId());
		}
		String itemIds = StringUtils.join(itemIdList, ",");

		// 判断库存是否足够
		List<WarehouseStockBean> list = warehouseRemovalServiceImpl.checkQuantity(entity.getWarehouseId(), itemIds);
		log.info("list.size()===" + list.size());
		if (list.size() > 0) {
			for (WarehouseBillItemBean billItemBean : entity.getList()) {
				boolean falg = false;
				for (WarehouseStockBean warehouseStockBean : list) {
					if (billItemBean.getItemId().equals(warehouseStockBean.getItemId())) {
						//if (billItemBean.getPrice().equals(warehouseStockBean.getCostPrice())) {
							falg = true;
							if (billItemBean.getQuantity() > warehouseStockBean.getQuantity()) {
								map.put(billItemBean.getItemName(), "库存不足,当前库存是"+warehouseStockBean.getQuantity());
							}
						//}
					}
				}
				if (!falg) {
					map.put(billItemBean.getItemName(), "仓库没有该商品！");
				}
			}
			if (map.size() > 0) {
				returnDataUtil.setStatus(3);
				returnDataUtil.setReturnObject(map);
			} else {
				// 得到生成的入库单号
				String findChangeId = warehouseRemovalServiceImpl.findChangeId();
				entity.setNumber(findChangeId);
				entity.setState(60402);
				entity.setOutput(1);
				entity.setAuditor(UserUtils.getUser().getId().intValue());
				entity.setOperator(UserUtils.getUser().getId().intValue());
				entity.setCreateTime(new Date());
				WarehouseInfoBean warehouseInfoBean = warehouseInfoServiceImpl.get(entity.getWarehouseId());
				CompanyBean companyBean = companyServiceImpl
						.findCompanyById(warehouseInfoBean.getCompanyId().intValue());
				entity.setCompanyId(warehouseInfoBean.getCompanyId().intValue());
				entity.setCompanyName(companyBean.getName());
				entity.setWarehouseName(warehouseInfoBean.getName());
				WarehouseOutputBillBean bean = warehouseRemovalServiceImpl.add(entity);
				if (bean != null) {
					WarehouseBillItemBean add = null;
					WarehouseOutputBillBean billBean = warehouseRemovalServiceImpl.get(findChangeId);
					for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
						warehouseBillItemBean.setBillId(billBean.getId().intValue());
						warehouseBillItemBean.setCreateTime(new Date());
						
						PurchaseItemStatisticsBean pisBean=pisService.getBeanByItemId(warehouseBillItemBean.getItemId());
						warehouseBillItemBean.setMoney( (new BigDecimal(pisBean.getAvgPrice()).multiply(new BigDecimal(warehouseBillItemBean.getQuantity())) ));
						warehouseBillItemBean.setAveragePrice(new BigDecimal(pisBean.getAvgPrice()));
						warehouseBillItemBean.setPrice(new BigDecimal(pisBean.getAvgPrice()));
						warehouseBillItemBean.setOutput(1);
						pisService.addToStatistics2(warehouseBillItemBean);
						add = warehouseBillItemServiceImpl.add(warehouseBillItemBean);
					}
					// 改变库存
					warehouseStockServiceImpl.putStorage(billBean.getId());
					if (add != null) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("出库成功，审核通过！");
						log.info("<WarehouseRemovalController>------<insert>-----end");
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("出库失败！");
						log.info("<WarehouseRemovalController>------<insert>-----end");

					}
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("审核未通过，出库失败！");
					log.info("<WarehouseRemovalController>------<insert>-----end");
				}
			}
			log.info("<WarehouseRemovalController>------<insert>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage(entity.getWarehouseName() + "的仓库没有您输入的所有商品,无法出库!");
			log.info("<WarehouseRemovalController>------<insert>-----end");
			return returnDataUtil;
		}

	}

	/**
	 * 仓库出库修改 保存
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库出库修改 保存", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalController>------<update>-----start");
		boolean update = warehouseRemovalServiceImpl.update(entity);
		if (update) {
			warehouseBillItemServiceImpl.modification(entity);
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("出库单修改成功！");
			log.info("<WarehouseRemovalController>------<update>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("出库单修改失败！");
			log.info("<WarehouseRemovalController>------<update>-----end");
			return returnDataUtil;
		}
	}

	/**
	 * 仓库出库修改 审核
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库出库修改 审核", notes = "audit", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/audit", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil audit(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalController>------<update>-----start");
		entity.setAuditor(UserUtils.getUser().getId().intValue());
		Map<String, String> map = new HashMap<String, String>();
		List<Long> itemIdList = Lists.newArrayList();
		for (WarehouseBillItemBean billItemBean : entity.getList()) {
			itemIdList.add(billItemBean.getItemId());
		}
		String itemIds = StringUtils.join(itemIdList, ",");
		// 判断库存是否足够
		List<WarehouseStockBean> list = warehouseRemovalServiceImpl.checkQuantity(entity.getWarehouseId(), itemIds);
		if (list.size() > 0) {
			for (WarehouseBillItemBean billItemBean : entity.getList()) {
				boolean falg = false;
				for (WarehouseStockBean warehouseStockBean : list) {
					if (billItemBean.getItemId().equals(warehouseStockBean.getItemId())) {
						//if (billItemBean.getPrice().equals(warehouseStockBean.getCostPrice())) {
							falg = true;
							if (billItemBean.getQuantity() > warehouseStockBean.getQuantity()) {
								map.put(billItemBean.getItemName(), "库存不足,当前库存是"+warehouseStockBean.getQuantity());
								break;
							}
						//}
					}
				}
				if (!falg) {//库存没有该商品
					map.put(billItemBean.getItemName(), " 仓库不存在该商品！");
				}
				PurchaseItemStatisticsBean pisBean=pisService.getBeanByItemId(billItemBean.getItemId());
				billItemBean.setMoney((new BigDecimal(pisBean.getAvgPrice()).multiply(new BigDecimal(billItemBean.getQuantity())) ));
				billItemBean.setAveragePrice(new BigDecimal(pisBean.getAvgPrice()));
				billItemBean.setPrice(new BigDecimal(pisBean.getAvgPrice()));
				billItemBean.setOutput(1);
				if (map.size() > 0) {
					
				}else {
					pisService.addToStatistics2(billItemBean);
				}
			}
			if (map.size() > 0) {
				returnDataUtil.setStatus(3);
				returnDataUtil.setReturnObject(map);
			} else {
				entity.setState(60402);
				boolean update = warehouseRemovalServiceImpl.update(entity);
				if (update) {
					warehouseBillItemServiceImpl.modification(entity);
					// 改变库存
					warehouseStockServiceImpl.putStorage(entity.getId());

					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("出库单审核通过！");
					log.info("<WarehouseRemovalController>------<update>-----end");
					return returnDataUtil;
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("出库单审核未通过！");
					log.info("<WarehouseRemovalController>------<update>-----end");
					return returnDataUtil;
				}
			}
			log.info("<WarehouseRemovalController>------<insert>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(2);
			returnDataUtil.setMessage(entity.getWarehouseName() + "的仓库没有任何商品,无法出库!");
			log.info("<WarehouseRemovalController>------<insert>-----end");
			return returnDataUtil;
		}

	}

	/**
	 * 仓库出库详情
	 * 
	 * @param map
	 * @return
	 */
	@ApiOperation(value = "仓库出库详情", notes = "getBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getBean(@RequestBody Map<String, String> map) {
		log.info("<WarehouseRemovalController>------<getBean>-----start");
		WarehouseOutputBillBean bean = warehouseRemovalServiceImpl.get(map.get("changeId"));
		List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
		Long warehouseId=bean.getWarehouseId();
		//List<Long> itemList=Lists.newArrayList();
		for(WarehouseBillItemBean obj : list){
			WarehouseStockBean warehouseStockBean=warehouseStockServiceImpl.getWarehouseItem(warehouseId, obj.getItemId());
			obj.setInventory(warehouseStockBean.getQuantity().intValue());
		}
		if (list.size() > 0) {
			int allNum = 0;
			double allMoney = 0;
			for (WarehouseBillItemBean detailBean : list) {
				allNum += detailBean.getQuantity();
				//allMoney += detailBean.getMoney();
			}
			bean.setAllNum(allNum);
			BigDecimal bg = new BigDecimal(allMoney);
			double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			bean.setAllMoney(f1);
			bean.setList(list);
			returnDataUtil.setReturnObject(bean);
			log.info("<WarehouseRemovalController>------<getBean>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setReturnObject(bean);
			return returnDataUtil;
		}
	}

	/**
	 * 全部导出
	 * 
	 * @param request
	 * @param response
	 * @param condition
	 */
	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAll")
	public void exportFileAll(HttpServletResponse response,HttpServletRequest request, WarehouseRemovalForm warehouseRemovalForm) {
		log.info("<WarehouseRemovalController>------<exportFileAll>-----start");
		ReturnDataUtil data1 = warehouseRemovalServiceImpl.listPage(warehouseRemovalForm);
		warehouseRemovalForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = warehouseRemovalServiceImpl.listPage(warehouseRemovalForm);
		List<WarehouseOutputBillBean> data = (List<WarehouseOutputBillBean>) returnData.getReturnObject();
		String title = "出库单列表";
		String[] headers = new String[] { "num", "number", "warehouseName", "stateName", "time", "typeName",
				"consigneeName", "operatorName", "auditorName" };
		String[] column = new String[] { "序号", "出库编号", "仓库", "出库状态", "制单时间", "出库类型", "提货人", "操作人", "审核人" };
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(warehouseRemovalForm.getStartTime()!=null&&warehouseRemovalForm.getEndTime()!=null) {
				//导出日志内容时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(warehouseRemovalForm.getStartTime())+"--"+DateUtil.formatYYYYMMDD(warehouseRemovalForm.getEndTime())+"的出库单列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出出库单列表--全部数据");
				
			}
			ExcelUtil.exportExcel(title, headers, column, response, data, "");
			log.info("<WarehouseRemovalController>------<exportFileAll>-----end");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 仓库出库撤销
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库出库撤销", notes = "repeal", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/repeal", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil repeal(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalController>------<repeal>-----start");
		if (entity.getState() != null) {
			if (entity.getState() == 60402 || entity.getState() == 60403) {
				entity.setState(60404);
				boolean update = warehouseRemovalServiceImpl.update(entity);
				if (update) {
					// 改变库存
					warehouseRemovalServiceImpl.changeStock(entity);
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("仓库出库单撤销成功！");
					log.info("<WarehouseRemovalController>------<repeal>-----end");
					return returnDataUtil;
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("仓库出库单撤销失败！");
					log.info("<WarehouseRemovalController>------<repeal>-----end");
					return returnDataUtil;
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("仓库出库单撤销失败！");
				log.info("<WarehouseRemovalController>------<repeal>-----end");
				return returnDataUtil;
				// }
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("仓库出库单撤销失败！");
			log.info("<WarehouseRemovalController>------<repeal>-----end");
			return returnDataUtil;
		}
	}

	/**
	 * 出库单导出
	 * 
	 * @param response
	 * @param warehouseRemovalForm
	 */
	@ApiOperation(value = "出库单导出", notes = "出库单导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/export")
	public void exportFile(HttpServletResponse response, WarehouseRemovalForm warehouseRemovalForm) {
		log.info("<WarehouseRemovalController>------<exportFile>-----start");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		ExportExcel exportExcel = new ExportExcel(wb, sheet);
		WarehouseOutputBillBean bean = warehouseRemovalServiceImpl.get(warehouseRemovalForm.number);
		List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
		if (list.size() > 0) {
			int allNum = 0;
			double allMoney = 0;
			for (WarehouseBillItemBean detailBean : list) {
				allNum += detailBean.getQuantity();
				//allMoney += detailBean.getMoney();
			}
			bean.setAllNum(allNum);
			bean.setAllMoney(allMoney);
			bean.setList(list);
		}
		// 标题
		String title = "商品出库信息";
		// 列名
		String[] headers = new String[] { "num", "barCode", "itemName", "unitName", "quantity", "remark" };
		String[] column = new String[] { "序号", "商品条形码", "商品名称", "商品单位", "商品数量", "备注" };
		// 创建报表头部
		exportExcel.createNormalHead(title, column.length);
		// 设置第二行
		String[] paramt = new String[] { "出库单号", "出库类型", "仓库", "状态" };
		String[] params = new String[] { bean.getNumber(), bean.getTypeName(), bean.getWarehouseName(),
				bean.getStateName() };
		exportExcel.createNormalTwoRow(paramt, params, column.length);
		// 设置第三行
		String[] paramt1 = new String[] { "制单时间", "操作人", "审核人", "提货人：" };
		String[] params1 = new String[] { bean.getTime(), bean.getAuditorName(), bean.getOperatorName(),
				bean.getConsigneeName() };
		exportExcel.createNormalThreeRow(paramt1, params1, column.length);
		// 设置第四行
		String[] params2 = new String[] { bean.getAllNum() + "" };
		exportExcel.createNormalFourRow(params2, column.length);
		try {
			exportExcel.createNormalFiveRow(bean.getList(), column, headers);
			exportExcel.outputExcel(title, response);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("<WarehouseRemovalController>------<exportFile>-----end");
	}

	/**
	 * 仓库出库进行删除
	 * 
	 * @param map
	 * @return
	 */
	@ApiOperation(value = "仓库出库删除", notes = "delete", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(@RequestBody List<Integer> ids) {
		log.info("<WarehouseRemovalController>------<delete>-----start");
		for (Integer id : ids) {
			WarehouseOutputBillBean bean = warehouseRemovalServiceImpl.getBean(id);
			bean.setDeleteFlag(1);
			boolean update = warehouseRemovalServiceImpl.update(bean);
			if (update) {
				List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
				for (WarehouseBillItemBean warehouseBillItemBean : list) {
					warehouseBillItemBean.setDeleteFlag(1);
					boolean update2 = warehouseBillItemServiceImpl.update(warehouseBillItemBean);
					if (update2) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("仓库出库单删除成功！");
						returnDataUtil.setReturnObject(update2);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("仓库出库单删除失败！");
						returnDataUtil.setReturnObject(update2);
					}
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("仓库出库单删除失败！");
				returnDataUtil.setReturnObject(update);
			}
		}
		log.info("<WarehouseRemovalController>------<delete>-----end");
		return returnDataUtil;
	}

	/**
	 * 入库 库存转移 查询
	 * 
	 * @param bean
	 * @return
	 */
	@ApiOperation(value = "入库 库存转移 查询", notes = "getBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getBillItem", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getBillItem(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseRemovalController>------<getBillItem>-----start");
		WarehouseOutputBillBean bean = warehouseRemovalServiceImpl.get(entity.getNumber());
		if (bean != null) {
			// 判断出库的状态 是否出库 以及是转移他库的出库单
			if (bean.getState().equals(60403)) {
				if (bean.getType().equals(60411)) {
					List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
					if (list.size() > 0) {
						bean.setList(list);
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("出库单信息查询成功!");
						returnDataUtil.setReturnObject(bean);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("出库单信息查询失败！");
						returnDataUtil.setReturnObject(bean);
					}
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("输入的出库单号 不是转移他库类型，不能进行库存转移！");
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("出库单未成功出库！");
				returnDataUtil.setReturnObject(bean);
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("出库单信息查询失败！");
			returnDataUtil.setReturnObject(bean);
		}
		log.info("<WarehouseRemovalController>------<getBillItem>-----end");
		return returnDataUtil;
	}
}
