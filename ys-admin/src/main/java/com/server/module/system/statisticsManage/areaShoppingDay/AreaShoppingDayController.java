package com.server.module.system.statisticsManage.areaShoppingDay;

import com.server.common.utils.excel.ExportExcel;
import com.server.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
@Api(value = "AreaShoppingDayController", description = "按行政区域购买情况统计")
@RestController
@RequestMapping("/areaShoppingDay")
public class AreaShoppingDayController {
    @Autowired
    private AreaShoppingDayService service;

    @ApiOperation(value = "按行政区域购买情况统计list", notes = "list", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public List<AreaShoppingDayDTO> list(AreaShoppingDayForm form) {
        System.out.println("==============");
        return service.list(form);
    }
    @ApiOperation(value = "按行政区域购买情况统计list", notes = "listChar", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listChar", produces = "application/json;charset=UTF-8")
    public MultipleChatData listChar(AreaShoppingDayForm form) {
        List<AreaShoppingDayDTO> list = service.list(form);
        MultipleChatData data = new MultipleChatData();

        String[] x = new String[list.size()-1];


        ChartData chart1=new ChartData();
        chart1.setName("销售额");
        chart1.setUnit("元");
        Double[] y1=new Double[list.size()-1];

        ChartData chart2=new ChartData();
        chart2.setName("机器数");
        chart2.setUnit("台");
        Double[] y2=new Double[list.size()-1];

        ChartData chart3=new ChartData();
        chart3.setName("台均销售额");
        chart3.setUnit("元");
        Double[] y3=new Double[list.size()-1];


        for (int i = 0; i < list.size()-1; i++) {
            AreaShoppingDayDTO obj=list.get(i);
            x[i]=obj.getDay();
            y1[i]=obj.getTotalMoney();
            y2[i]=obj.getMachinesNum()*1d;
            y3[i]=service.divide(obj.getTotalMoney(),obj.getMachinesNum()*1d);
        }
        chart1.setY(y1);
        chart2.setY(y2);
        chart3.setY(y3);
        data.getList().add(chart1);
        data.getList().add(chart2);
        data.getList().add(chart3);

        data.setX(x);

        AreaShoppingDayDTO zong = list.get(list.size()-1);
        data.setAverageOrderPrice(zong.getAverageOrderPrice());
        data.setTotalMoney(zong.getTotalMoney());
        data.setOrderNum(zong.getOrderNum());

        return data;
    }


    @ApiOperation(value = "按行政区域购买情况统计导出", notes = "export", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    public void export(AreaShoppingDayForm form, HttpServletResponse response, HttpServletRequest request){
        List<AreaShoppingDayDTO> list = service.list(form);
        String fileName = "按行政区域购买情况统计列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        try {
            new ExportExcel("按行政区域购买情况统计列表", AreaShoppingDayDTO.class).setDataList(list)
                    .write(response, fileName).dispose();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
