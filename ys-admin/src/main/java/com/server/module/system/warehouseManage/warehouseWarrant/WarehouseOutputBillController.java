package com.server.module.system.warehouseManage.warehouseWarrant;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.converters.Auto;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.purchase.purchaseBill.PurchaseBillBean;
import com.server.module.system.purchase.purchaseBill.PurchaseBillService;
import com.server.module.system.purchase.purchaseBill.PurchaseBillServiceImpl;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemService;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsBean;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsDao;
import com.server.module.system.statisticsManage.purchaseItemStatistics.PurchaseItemStatisticsService;
import com.server.module.system.warehouseManage.stock.WarehouseStockService;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoBean;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoForm;
import com.server.module.system.warehouseManage.warehouseList.WarehouseInfoService;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemService;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ExportExcel;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-05-16 00:05:25
 */
@Api(value = "WarehouseOutputBillController", description = "仓库入库")
@RestController
@RequestMapping("/warehouseOutputBill")
public class WarehouseOutputBillController {

	private static Logger log = LogManager.getLogger(WarehouseOutputBillController.class);

	@Autowired
	private WarehouseOutputBillService warehouseOutputBillServiceImpl;
	@Autowired
	private WarehouseStockService warehouseStockServiceImpl;
	@Autowired
	private WarehouseBillItemService warehouseBillItemServiceImpl;
	@Autowired
	private CompanyService companyServiceImpl;
	@Autowired
	private WarehouseInfoService warehouseInfoServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private PurchaseBillService purchaseBillServiceImpl;
	@Autowired
	private PurchaseBillItemService purchaseBillItemServiceImpl;
	@Autowired
	private PurchaseItemStatisticsService pisService;
	@Autowired
	private PurchaseItemStatisticsDao purchaseItemStatisticsDao;
	/**
	 * 仓库入库列表
	 * 
	 * @param condition
	 * @return
	 */
	@ApiOperation(value = "仓库入库列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) WarehouseOutputBillForm warehouseOutputBillForm) {
		log.info("<WarehouseWarrantInfoController>------<listPage>-----start");
		if (warehouseOutputBillForm == null) {
			warehouseOutputBillForm = new WarehouseOutputBillForm();
		}
		returnDataUtil = warehouseOutputBillServiceImpl.listPage(warehouseOutputBillForm);
		log.info("<WarehouseWarrantInfoController>------<listPage>-----end");
		return returnDataUtil;
	}

	/**
	 * 仓库入库添加 保存
	 * 
	 * @param entity
	 * @param detailBean
	 * @return
	 */
	@ApiOperation(value = "仓库入库添加 保存", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseWarrantInfoController>------<add>-----start");
		// 得到生成的入库单号
		String findChangeId = warehouseOutputBillServiceImpl.findChangeId();
		entity.setNumber(findChangeId);
		entity.setState(60201);
		entity.setOutput(0);
		// entity.setAuditor(UserUtils.getUser().getId().intValue());
		entity.setOperator(UserUtils.getUser().getId().intValue());
		entity.setCreateTime(new Date());
		if(entity.getHappenDate()==null) {
			entity.setHappenDate(new Date());
		}
		WarehouseInfoBean warehouseInfoBean = warehouseInfoServiceImpl.get(entity.getWarehouseId());
		CompanyBean companyBean = companyServiceImpl.findCompanyById(warehouseInfoBean.getCompanyId().intValue());
		entity.setCompanyId(warehouseInfoBean.getCompanyId().intValue());
		entity.setCompanyName(companyBean.getName());
		WarehouseOutputBillBean bean = warehouseOutputBillServiceImpl.add(entity);
		if (bean != null) {
			WarehouseOutputBillBean billBean = warehouseOutputBillServiceImpl.get(findChangeId);
			WarehouseBillItemBean add = null;
			for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
				// 判断是否为采购入库的方式
				if (StringUtil.isNotBlank(entity.getPurchaseNumber())) {
					// 进行判断入库数量是否足够
					PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemServiceImpl
							.get(warehouseBillItemBean.getPurchaseItemId());
					// 得到采购回来的剩余数量
					Long num = purchaseBillItemBean.getQuantity() - purchaseBillItemBean.getStorageQuantity();
					// 判断入库是 数量是否足够
					if (warehouseBillItemBean.getQuantity() > num) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage(
								warehouseBillItemBean.getItemName() + "》》》商品数量不足 无法入库，请重新输入！您本次最大可以入库" + num + "件商品");
						return returnDataUtil;
					} else {
						warehouseBillItemBean.setBillId(billBean.getId().intValue());
						warehouseBillItemBean.setCreateTime(new Date());
						add = warehouseBillItemServiceImpl.add(warehouseBillItemBean);
					}
				} else {
					warehouseBillItemBean.setBillId(billBean.getId().intValue());
					warehouseBillItemBean.setCreateTime(new Date());
					add = warehouseBillItemServiceImpl.add(warehouseBillItemBean);
				}
			}
			if (add != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("入库成功，待审核！");
				log.info("<WarehouseWarrantInfoController>------<add>-----end");
				return returnDataUtil;
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("入库失败！");
				log.info("<WarehouseWarrantInfoController>------<add>-----end");
				return returnDataUtil;
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("入库失败！");
			log.info("<WarehouseWarrantInfoController>------<add>-----end");
			return returnDataUtil;
		}
	}

	/**
	 * 仓库入库 审核 ?
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库入库 审核", notes = "insert", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/insert", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil insert(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseWarrantInfoController>------<insert>-----start");
		//entity.setCreateTime(new Date());
		returnDataUtil = warehouseOutputBillServiceImpl.putStock(entity);
		log.info("<WarehouseWarrantInfoController>------<insert>-----end");
		return returnDataUtil;
	}

	/**
	 * 仓库入库修改 保存
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库入库修改 保存", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseWarrantInfoController>------<update>-----start");
		if(entity.getHappenDate()==null) {
			entity.setHappenDate(new Date());
		}
		boolean update = warehouseOutputBillServiceImpl.update(entity);
		if (update) {
			if (entity.getType() == 60204) {
				for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
					// 进行判断入库数量是否足够
					PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemServiceImpl
							.get(warehouseBillItemBean.getPurchaseItemId());
					// 得到采购回来的剩余数量
					Long num = purchaseBillItemBean.getQuantity() - purchaseBillItemBean.getStorageQuantity();
					// 判断入库是 数量是否足够
					if (warehouseBillItemBean.getQuantity() > num) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage(
								warehouseBillItemBean.getItemName() + "》》》商品数量不足 无法入库，请重新输入！您本次最大可以入库" + num + "件商品");
						return returnDataUtil;
					} else {
						warehouseBillItemServiceImpl.update(warehouseBillItemBean);
					}
				}
			} else {
				warehouseBillItemServiceImpl.modification(entity);
			}

			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("入库单修改成功！");
			log.info("<WarehouseWarrantInfoController>------<update>-----end");
			return returnDataUtil;
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("入库单修改失败！");
			log.info("<WarehouseWarrantInfoController>------<update>-----end");
			return returnDataUtil;
		}
	}

	/**
	 * 仓库入库修改 审核
	 * 
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "仓库入库修改 审核", notes = "audit", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/audit", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil audit(@RequestBody WarehouseOutputBillBean entity) {
		log.info("<WarehouseWarrantInfoController>------<update>-----start");
		entity.setAuditor(UserUtils.getUser().getId().intValue());
		
		entity.setState(60202);
		boolean update = warehouseOutputBillServiceImpl.update(entity);
		if (update) {
			// 判断是否为采购入库的方式
			if (entity.getType() == 60204) {
				for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
					// 进行判断入库数量是否足够
					PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemServiceImpl
							.get(warehouseBillItemBean.getPurchaseItemId());
					// 得到采购回来的剩余数量
					Long num = purchaseBillItemBean.getQuantity() - purchaseBillItemBean.getStorageQuantity();
					// 判断入库是 数量是否足够
					if (warehouseBillItemBean.getQuantity() > num) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage(
								warehouseBillItemBean.getItemName() + "》》》商品数量不足 无法入库，请重新输入！您本次最大可以入库" + num + "件商品");
						return returnDataUtil;
					} else {
						log.info("--------------------------------------");
						warehouseBillItemBean.setMoney( (new BigDecimal(purchaseBillItemBean.getPrice()).multiply(new BigDecimal(warehouseBillItemBean.getQuantity())) ));
						warehouseBillItemBean.setAveragePrice(new BigDecimal(purchaseBillItemBean.getPrice()));
						warehouseBillItemBean.setPrice(new BigDecimal(purchaseBillItemBean.getPrice()));
						warehouseBillItemBean.setOutput(0);
						pisService.addToStatistics2(warehouseBillItemBean);
						
						warehouseBillItemServiceImpl.update(warehouseBillItemBean);
					}
				}
			} else if(entity.getType().equals(60206)){
				log.info("《--------来的库存转移 判断-------》");
				for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
					log.info("目标仓库id==="+entity.getTargetWarehouseId()+"---来源仓库id"+entity.getSourceWarehouseId());
					Long quantity = warehouseOutputBillServiceImpl.getQuantity(entity.getPurchaseId().longValue(),entity.getTargetWarehouseId(), entity.getSourceWarehouseId(), warehouseBillItemBean.getItemId());
					//商品数量减去已入库数量  得到可以入库数量
					log.info("商品数量------"+warehouseBillItemBean.getPurchaseQuantity()+"====已入库数量"+quantity);
					Long num=warehouseBillItemBean.getPurchaseQuantity()-quantity;
					// 判断入库是 数量是否足够
					if (warehouseBillItemBean.getQuantity() > num) {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage(
								warehouseBillItemBean.getItemName() + "》》》商品数量不足 无法入库，请重新输入！");
						return returnDataUtil;
					} else {
						warehouseBillItemBean.setMoney(warehouseBillItemBean.getPrice().multiply(new BigDecimal(warehouseBillItemBean.getQuantity())) );
						warehouseBillItemBean.setAveragePrice(warehouseBillItemBean.getPrice());
						warehouseBillItemBean.setPrice(warehouseBillItemBean.getPrice());
						warehouseBillItemBean.setOutput(0);
						pisService.addToStatistics2(warehouseBillItemBean);
						warehouseBillItemServiceImpl.update(warehouseBillItemBean);
					}
				}
				
			}else {
				warehouseBillItemServiceImpl.modification(entity);
			}
			// 改变库存
			warehouseStockServiceImpl.putStorage(entity.getId());

			// 判断是否为采购入库的方式
			if (entity.getType() == 60204) {
				log.info("<WarehouseWarrantInfoController>-----《update》----来到采购入库 处理采购单");
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
						log.info("《update》----《《《《《是否全部商品入库》》》》》");
						// 修改入库状态 0 未入库 1 部分入库 2 全部入库
						purchaseBillItemBean.setStorageState(2);
					} else {
						purchaseBillItemBean.setStorageState(1);
					}
					purchaseBillItemBean.setId(warehouseBillItemBean.getPurchaseItemId().longValue());
					purchaseBillItemBean.setStorageQuantity(warehouseBillItemBean.getQuantity().longValue());
					log.info("《update》----=====入库成功后 修改商品数量=========");
					boolean update2 = purchaseBillItemServiceImpl.update(purchaseBillItemBean);
					if (update2) {
						flag++;
					}
				}
				// 判断是否全部入库成功
				if (flag.equals(entity.getList().size())) {
					log.info("<WarehouseWarrantInfoController>-----《update》----第一步 来到 全部入库成功后 修改采购单的状态");
					Integer temp = 0;
					// 遍历采购单商品 进行一个检测 是否已经全部入库 如果全部入库 修改对应状态
					for (WarehouseBillItemBean warehouseBillItemBean : entity.getList()) {
						// 根据id查询商品信息
						log.info("第二步 《update》----===================根据id查询采购商品信息===========");
						PurchaseBillItemBean purchaseBillItemBean = purchaseBillItemServiceImpl
								.get(warehouseBillItemBean.getPurchaseItemId().longValue());
						log.info("查询出来的商品信息 《update》----====" + JsonUtils.toJson(purchaseBillItemBean));
						if (purchaseBillItemBean.getQuantity().equals(purchaseBillItemBean.getStorageQuantity())) {
							temp++;
							PurchaseBillItemBean billItemBean = new PurchaseBillItemBean();
							billItemBean.setId(warehouseBillItemBean.getPurchaseItemId().longValue());
							billItemBean.setStorageState(2);
							log.info("<---商品数量---->===《update》----" + warehouseBillItemBean.getQuantity().longValue());
							billItemBean.setStorageQuantity(0L);
							log.info("第三步  《update》----====修改采购单商品信息===");
							purchaseBillItemServiceImpl.update(billItemBean);
						}
					}
					log.info("第三步 《update》----是否全部商品入库成功-------" + temp);
					// 全部商品入库成功后 修改采购单的状态
					PurchaseBillBean purchaseBillBean = new PurchaseBillBean();
					if (temp.equals(entity.getList().size())) {
						purchaseBillBean.setState(2L);
						purchaseBillBean.setStorageState(2);
					} else {
						purchaseBillBean.setStorageState(1);
					}
					purchaseBillBean.setId(entity.getPurchaseId().longValue());
					log.info("====第四步   《update》----修改采购单信息===");
					purchaseBillServiceImpl.update(purchaseBillBean);

					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("入库单修改成功，审核通过！");
					log.info("<WarehouseWarrantInfoController>------<insert>-----end");
					return returnDataUtil;
				} else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("入库单修改失败,审核未通过！");
					log.info("<WarehouseWarrantInfoController>------<insert>-----end");
					return returnDataUtil;
				}
			} else { // 非采购方式入库

				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("入库单修改成功,通过审核！");
				log.info("<WarehouseWarrantInfoController>------<update>-----end");
				return returnDataUtil;
			}
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("入库单修改失败,未通过审核！");
			log.info("<WarehouseWarrantInfoController>------<update>-----end");
			return returnDataUtil;
		}
	}

	/**
	 * 仓库入库详情
	 * 
	 * @param map
	 * @return
	 */
	@ApiOperation(value = "仓库入库详情", notes = "getBean", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getBean(@RequestBody Map<String, String> map) {
		log.info("<WarehouseWarrantInfoController>------<getBean>-----start");
		WarehouseOutputBillBean bean = warehouseOutputBillServiceImpl.get(map.get("changeId"));
		int allNum = 0;
		double allMoney = 0;
		List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
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
		log.info("<WarehouseWarrantInfoController>------<getBean>-----end");
		return returnDataUtil;
	}

	/**
	 * 全部导出
	 * 
	 * @param response
	 * @param condition
	 */
	@ApiOperation(value = "全部导出", notes = "全部导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAll")
	public void exportFileAll(HttpServletResponse response,HttpServletRequest request, WarehouseOutputBillForm warehouseOutputBillForm) {
		log.info("<WarehouseWarrantInfoController>------<exportFileAll>-----start");
		ReturnDataUtil data1 = warehouseOutputBillServiceImpl.listPage(warehouseOutputBillForm);
		warehouseOutputBillForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
		ReturnDataUtil returnData = warehouseOutputBillServiceImpl.listPage(warehouseOutputBillForm);
		List<WarehouseOutputBillBean> data = (List<WarehouseOutputBillBean>) returnData.getReturnObject();
		log.info("=================11111111111111111" + JsonUtils.toJson(data));
		String title = "入库单列表";
		String[] headers = new String[] { "num", "number", "warehouseName", "stateName", "typeLabel", "time",
				"operatorName", "auditorName" };
		String[] column = new String[] { "序号", "入库编号", "仓库", "入库状态", "入库类型", "制单时间", "操作人", "审核人" };
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(warehouseOutputBillForm.getStartTime()!=null&&warehouseOutputBillForm.getEndTime()!=null) {
				//导出日志内容按时间格式
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(warehouseOutputBillForm.getStartTime())+"--"+DateUtil.formatYYYYMMDD(warehouseOutputBillForm.getEndTime())+"的入库单列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出入库单列表--全部数据");
			}
			ExcelUtil.exportExcel(title, headers, column, response, data, "");
			log.info("<WarehouseWarrantInfoController>------<exportFileAll>-----end");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 入库单导出
	 * 
	 * @param response
	 * @param warehouseOutputBillForm
	 */
	@ApiOperation(value = "入库单导出", notes = "入库单导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/export")
	public void exportFile(HttpServletResponse response, WarehouseOutputBillForm warehouseOutputBillForm) {
		log.info("<WarehouseWarrantInfoController>------<exportFile>-----start-----入库单导出");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		ExportExcel exportExcel = new ExportExcel(wb, sheet);
		WarehouseOutputBillBean bean = warehouseOutputBillServiceImpl.get(warehouseOutputBillForm.number);
		List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
		if (list.size() > 0) {
			int allNum = 0;
			for (WarehouseBillItemBean detailBean : list) {
				allNum += detailBean.getQuantity();
			}
			bean.setAllNum(allNum);
			bean.setList(list);
		}
		// 标题
		String title = "商品入库信息";
		// 判断是否为归还入库
		log.info("===入库类型====" + bean.getType() + "--------类型名：" + bean.getTypeLabel());
		String[] headers = null;
		String[] column = null;
		if (bean.getType().equals(60205)) {
			log.info("来到归还入库导出列名");
			// 列名
			headers = new String[] { "num", "barCode", "itemName", "unitName", "quantity", "remark" };
			column = new String[] { "序号", "商品条形码", "商品名称", "商品单位", "商品数量", "备注" };
		} else {
			// 列名
			headers = new String[] { "num", "barCode", "itemName", "unitName", "purchaseQuantity", "quantity",
					"remark" };
			column = new String[] { "序号", "商品条形码", "商品名称", "商品单位", "商品数量", "入库数量", "备注" };
		}
		// 创建报表头部
		exportExcel.createNormalHead(title, column.length);
		// 设置第二行
		String[] paramt = new String[] { "入库单号", "供货单位", "仓库", "状态" };
		String supplierName = bean.getSupplierName();
		if(supplierName==null) {
			supplierName="";
		}
		String[] params = new String[] { bean.getNumber(), supplierName, bean.getWarehouseName(),
				bean.getStateName() };
		exportExcel.createNormalTwoRow(paramt, params, column.length);
		// 设置第三行
		String[] paramt1 = new String[] { "制单时间", "操作人", "审核人", "" };
		String auditorName = bean.getAuditorName();
		if (auditorName == null) {
			auditorName = "";
		}
		String[] params1 = new String[] { bean.getTime(), bean.getOperatorName(), auditorName, "" };
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
		log.info("<WarehouseWarrantInfoController>------<exportFile>-----end");
	}

	/**
	 * 仓库入库 进行删除
	 * 
	 * @param map
	 * @return
	 */
	@ApiOperation(value = "仓库入库删除", notes = "delete", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil delete(@RequestBody List<Integer> ids) {
		log.info("<WarehouseWarrantInfoController>------<delete>-----start");
		for (Integer id : ids) {
			WarehouseOutputBillBean bean = warehouseOutputBillServiceImpl.getById(id);
			bean.setDeleteFlag(1);
			boolean update = warehouseOutputBillServiceImpl.update(bean);
			if (update) {
				List<WarehouseBillItemBean> list = warehouseBillItemServiceImpl.get(bean.getId().intValue());
				for (WarehouseBillItemBean warehouseBillItemBean : list) {
					warehouseBillItemBean.setDeleteFlag(1);
					boolean update2 = warehouseBillItemServiceImpl.update(warehouseBillItemBean);
					if (update2) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("仓库入库单删除成功！");
						returnDataUtil.setReturnObject(update2);
					} else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("仓库入库单删除失败！");
						returnDataUtil.setReturnObject(update2);
					}
				}
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("仓库入库单删除失败！");
				returnDataUtil.setReturnObject(update);
			}
		}
		log.info("<WarehouseWarrantInfoController>------<delete>-----end");
		return returnDataUtil;
	}
}
