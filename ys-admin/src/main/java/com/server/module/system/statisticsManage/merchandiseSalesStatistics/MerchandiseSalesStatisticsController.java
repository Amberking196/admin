package com.server.module.system.statisticsManage.merchandiseSalesStatistics;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.replenishManage.machinesReplenishStatement.ReplenishmentReportBean;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-05-09 21:15:27
 */
@Api(value = "MerchandiseSalesStatisticsController", description = "商品销售统计")
@RestController
@RequestMapping("/merchandiseSalesStatistics")
public class MerchandiseSalesStatisticsController {

	public static Logger log = LogManager.getLogger(MerchandiseSalesStatisticsController.class);
	@Autowired
	private MerchandiseSalesStatisticsService merchandiseSalesStatisticsImpl;
	@Autowired
	AdminUserService adminUserService;

	@ApiOperation(value = "商品销售统计列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(MerchandiseSalesStatisticsCondition condition,HttpServletRequest request) {
		log.info("<MerchandiseSalesStatisticsController>----<listPage>----start");
		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		condition.setAreaId(user.getAreaId());
		ReturnDataUtil listPage = merchandiseSalesStatisticsImpl.listPage(condition);
		log.info("<MerchandiseSalesStatisticsController>----<listPage>----end");
		return listPage;
	}
	
	@ApiOperation(value = "导出", notes = "导出")
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/exportAll")
    public void exportFileAll(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes,MerchandiseSalesStatisticsCondition condition) {
		ReturnDataUtil listPage = merchandiseSalesStatisticsImpl.listPage(condition);
		condition.setPageSize(listPage.getPageSize() * listPage.getTotalPage());
		ReturnDataUtil returnData = merchandiseSalesStatisticsImpl.listPage(condition);
		List<ReplenishmentReportBean> data = (List<ReplenishmentReportBean>)returnData.getReturnObject();
		String title ="商品销售列表详情";
		String[] headers = new String[]{"id","itemName","barCode","vmCode","salesQuantity","avgPrice","allPrice"};
		String[] column = new String[]{"序号","商品名称","条形码","售货机编码","销售数量","平均售价","总价"};
		try {
			ExcelUtil.exportExcel(title, headers,column, response, data, "");
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
