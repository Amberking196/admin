package com.server.module.system.statisticsManage.purchaseItemStatistics;

import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemForm;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * author name: yjr
 * create time: 2018-08-24 11:02:40
 */
@Api(value = "purchaseItemStatisticsController", description = "采购记录")
@RestController
@RequestMapping("/purchaseItemStatistics")
public class PurchaseItemStatisticsController {

    private static Logger log = LogManager.getLogger(PurchaseItemStatisticsController.class);
 
    @Autowired
    private PurchaseItemStatisticsService purchaseItemStatisticsServiceImpl;
    @Autowired
    private PurchaseBillItemService purchaseBillItemServiceImpl;
    

    @ApiOperation(value = "采购成本统计列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(@RequestBody(required=false) PurchaseItemStatisticsCondition condition) {
        if(condition==null) {
        	condition=new PurchaseItemStatisticsCondition();
        }
    	return purchaseItemStatisticsServiceImpl.listPage(condition);
    }

    @ApiOperation(value = "采购成本统计列表详情", notes = "successListPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/successListPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil successListPage(@RequestBody PurchaseBillItemForm purchaseBillItemForm) {
    	return purchaseBillItemServiceImpl.successListPage(purchaseBillItemForm);
    }

    @ApiOperation(value = "全部导出", notes = "全部导出")
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/exportAll")
    public void exportFileAll( HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, PurchaseItemStatisticsCondition condition) {
        log.info("<PurchaseItemStatisticsController>----<exportAll>----start");
        ReturnDataUtil data1 = purchaseItemStatisticsServiceImpl.listPage(condition);
        condition.setPageSize(data1.getPageSize() * data1.getTotalPage());
        ReturnDataUtil returnData = purchaseItemStatisticsServiceImpl.listPage(condition);
        List<PurchaseItemStatisticsBean> data = (List<PurchaseItemStatisticsBean>)returnData.getReturnObject();
        String title ="采购成本统计";
        String[] headers = new String[]{"itemName","barCode","unitName","avgPrice","sumQuantity"};
        String[] column = new String[]{"商品名称","商品条形码","商品单位","均价","商品数量"};
        try {
            //添加导出日志的内容
            ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
            bean.setContent("用户: "+bean.getOperatorName()+" 导出采购成本统计列表--全部数据");
            ExcelUtil.exportExcel(title, headers,column, response, data, "");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("<PurchaseItemStatisticsController>----<exportAll>----start");
    }


    @ApiOperation(value = "导出详情列表", notes = "导出详情列表")
    @SuppressWarnings("unchecked")
    @GetMapping(value = "/exportDetails")
    public void exportFileAll( HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, PurchaseBillItemForm purchaseBillItemForm) {
        log.info("<PurchaseItemStatisticsController>----<exportDetails>----start");
        ReturnDataUtil data1 = purchaseBillItemServiceImpl.successListPage(purchaseBillItemForm);
        purchaseBillItemForm.setPageSize(data1.getPageSize() * data1.getTotalPage());
        ReturnDataUtil returnData = purchaseBillItemServiceImpl.successListPage(purchaseBillItemForm);
        List<PurchaseBillItemBean> data = (List<PurchaseBillItemBean>)returnData.getReturnObject();
        String title ="采购成本统计列表详情";
        String[] headers = new String[]{"supplierName","itemName","barCode","applyNumber","unitName","price","quantity","createTime"};
        String[] column = new String[]{"供应商","商品名称","商品条形码","采购单号","商品单位","进货价","商品数量","创建日期"};
        try {
            //添加导出日志的内容
            ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
            if(purchaseBillItemForm.getStartTime()!=null&&purchaseBillItemForm.getEndTime()!=null) {
                bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(purchaseBillItemForm.getStartTime())+"--"+ DateUtil.formatYYYYMMDD(purchaseBillItemForm.getEndTime())+"采购成本统计列表详情的数据");

            }else {
                bean.setContent("用户: "+bean.getOperatorName()+" 导出采购成本统计列表详情--全部数据");
            }
            ExcelUtil.exportExcel(title, headers,column, response, data, "");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("<PurchaseItemStatisticsController>----<exportDetails>----start");
    }
}	



