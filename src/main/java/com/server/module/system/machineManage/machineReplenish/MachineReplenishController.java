package com.server.module.system.machineManage.machineReplenish;

import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/machineManage")
@Api(value = "machineReplenishController", description = "机器补货盘点")
public class MachineReplenishController {
    public static Logger log = LogManager.getLogger(MachineReplenishController.class);

    @Autowired
    MachineReplenishService machineReplenishService;
    @Autowired
    CompanyService companyService;

    /**
     * 机器补货盘点
     *
     * @param machineReplenishForm
     * @return
     */
    @ApiOperation(value = "根据条件查询机器补货信息", notes = "根据条件查询机器补货信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/machineReplenishSum")
    @ResponseBody
    public ReturnDataUtil findMachineReplenishSum(@RequestBody(required = false) MachineReplenishForm machineReplenishForm, HttpServletRequest request) {
        if (machineReplenishForm == null) {
            machineReplenishForm = new MachineReplenishForm();
        }

        Integer companyId = UserUtils.getUser().getCompanyId();
        List<Integer> list = companyService.findAllSonCompanyId(companyId);
        String s = "";
        for (int a = 0; a < list.size(); a++) {
            if (a == list.size() - 1) {
                s = s + list.get(a);
            } else {
                s = s + list.get(a) + ",";
            }
        }
        machineReplenishForm.setCompany(s);
       // machineReplenishForm.setCompanyId(companyId);
        return machineReplenishService.findMachineReplenishSum(machineReplenishForm);

    }


    /**
     * 机器补货详情查询
     *
     * @param machineReplenishForm
     * @return
     */
    @ApiOperation(value = "根据条件查询机器补货详情", notes = "根据条件查询机器补货详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("/machineReplenishDetile")
    @ResponseBody
    public ReturnDataUtil findMachineReplenishDetile(@RequestBody(required = false) MachineReplenishForm machineReplenishForm, HttpServletRequest request) {

        return machineReplenishService.findMachineReplenishDetile(machineReplenishForm);

    }

    /**
     * 机器补货数据全部导出到Excel中
     *
     * @param response             response 对象
     * @param machineReplenishForm
     */
    @ApiOperation(value = "机器补货数据全部导出到Excel中", notes = "机器补货数据全部导出到Excel中")
    @SuppressWarnings("unchecked")
    @PostMapping(value = "/exportAll")
    public void exportFileAll(HttpServletResponse response,HttpServletRequest request,
                              @RequestBody(required = false) MachineReplenishForm machineReplenishForm) {
        log.info("<MachineReplenishController>------<exportFileAll>-----start");

        if (machineReplenishForm == null) {
            machineReplenishForm = new MachineReplenishForm();
        }

        Integer companyId = UserUtils.getUser().getCompanyId();
        List<Integer> list = companyService.findAllSonCompanyId(companyId);
        String s = "";
        for (int a = 0; a < list.size(); a++) {
            if (a == list.size() - 1) {
                s = s + list.get(a);
            } else {
                s = s + list.get(a) + ",";
            }
        }
        machineReplenishForm.setCompany(s);
     //   machineReplenishForm.setCompanyId(companyId);


        ReturnDataUtil dataUtil = machineReplenishService.exportExcel(machineReplenishForm);
        List<MachineReplenish2ExcelBean> returnObject = (List<MachineReplenish2ExcelBean>) dataUtil.getReturnObject();
        String[] headers = new String[]{"vmCode", "wayNumber", "itemName", "replenishTime", "replenishCount"};
        String[] columnName = new String[]{"机器编号", "货道编号", "商品名称", "补货时间", "补货数量"};

        try {
        	 ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
             //设置导出日志的内容
        	 if(machineReplenishForm.getStartDate()!=null&&machineReplenishForm.getEndDate()!=null) {//导出内容按日期输出
        		 bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(machineReplenishForm.getStartDate())+"--"+DateUtil.formatYYYYMMDD(machineReplenishForm.getEndDate())+"的售货机补货盘点的数据");
        	 }else {
        		 bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机补货盘点--全部的数据");
        	 }
            ExcelUtil.exportExcel2007("机器补货数据", headers, columnName, response, returnObject, "");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
                | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.info("<MachineReplenishController>------<exportFileAll>-----end");
    }
}
