package com.server.module.system.machineManage.machinesWayTemplateDetail;

import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.module.system.machineManage.machineItem.VendingMachinesItemService;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayBean;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayService;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayServiceImpl;
import com.server.module.system.machineManage.machinesWayTemplate.VendingMachinesWayTemplateService;
import com.server.module.system.machineManage.machinesWayTemplate.WayItemDto;
import com.server.util.ReturnDataUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:35:22
 */
@Api(value = "VendingMachinesWayTemplateDelController", description = "售货机模板详情")
@RestController
@RequestMapping("/vendingMachinesWayTemplateDel")
public class VendingMachinesWayTemplateDelController {


    @Autowired
    private VendingMachinesWayTemplateDelService vendingMachinesWayTemplateDelServiceImpl;
    @Autowired
    private AdminUserService adminUserServiceImpl;
    @Autowired
    private VendingMachinesItemService vendingMachinesItemService;
    @Autowired
    private VendingMachinesWayService vendingMachinesWayService;
    @Autowired
    private VendingMachinesWayTemplateService vendingMachinesWayTemplateServiceImpl;


    @ApiOperation(value = "售货机模板详情添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody VendingMachinesWayTemplateDelBean entity) {
        ReturnDataUtil dataUtil = vendingMachinesWayTemplateDelServiceImpl.checkWayNum(entity);

        // 如果已经有数据了，直接返回
        if (dataUtil.getStatus() == 0) {
            return dataUtil;
        }

        return new ReturnDataUtil(vendingMachinesWayTemplateDelServiceImpl.add(entity));
    }


    @ApiOperation(value = "商品信息绑定到货道上", notes = "addItemToWay", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addItemToWay", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addItemToWay(@RequestBody ItemToWayTemplateDetailDto dto, HttpServletRequest request) {
    	//判断该售货机是否有货道，如果有就添加，没有的话直接返回
    	ReturnDataUtil dataUtil = vendingMachinesWayTemplateServiceImpl.listDetailsById(dto.getTemplateId());
    	if(dataUtil.getTotal()<1) {//利用总数进行判断
    		dataUtil.setMessage("货道为空，不能添加商品");
    		dataUtil.setStatus(-1);
    		return dataUtil;
    	}
    	//判断该模板是否有该货道
    	  dataUtil = vendingMachinesWayTemplateDelServiceImpl.checkWayNum(dto);

         //该模板存在该货道，可以添加商品
          if (dataUtil.getStatus() == 0) {
        	  ReturnDataUtil data = vendingMachinesWayTemplateDelServiceImpl.addItemToWay(dto);
              if((boolean)data.getReturnObject()) {
             	 data.setMessage("添加成功");
              }else {
             	 data.setMessage("添加失败");
              }
              return data;
          }else { // 该货道不存在，添加失败
        	  dataUtil.setStatus(-1);
        	  dataUtil.setMessage("该货道不存在，请选择其他货道");
              return dataUtil;
          }
        
    }


    // 根据模板的详情与售货机编号关联起来，将数据插入到vending_machine_way表中。
    @ApiOperation(value = "模板与售货机关联起来", notes = "templateBindVM", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/templateBindMachine", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil templateBindMachine(@RequestBody VendingMachinesWayTemplateDelCondition condition , HttpServletRequest request) {

        ReturnDataUtil dataUtil = new ReturnDataUtil();

        // 拿到用户的数据
        Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
        ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
        AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
        if (userBean.getAreaId() == 0) {
            userBean.setAreaId(null);
        }

        // 根据模板Id当前模板下的模板详情。
        List<VendingMachinesWayTemplateDelBean> DetailBeans = vendingMachinesWayTemplateDelServiceImpl.findDetailsByTemplateId(condition.getTemplateId());

        // 查一下该售货机有多少个货道
        int size = vendingMachinesWayService.findwayNumberByVmCode(condition.getVmCode());

        if (DetailBeans.size() != size) {
            dataUtil.setStatus(0);
            dataUtil.setMessage("模板的通道和售货机的通道不匹配！");
            return dataUtil;
        }

        // 售货机货道和模板货道是否一一匹配
        boolean isMatch = true;
        for (VendingMachinesWayTemplateDelBean bean : DetailBeans) {
            Integer wayNumber = bean.getWayNumber();
            VendingMachinesWayBean wayBean = vendingMachinesWayService.findByVmCodeAndWayNumber(condition.getVmCode(), wayNumber);

            // 如果售货机货道没有数据，那就是模板和售货机的货道不匹配
            if (wayBean == null) {
                isMatch = false;
            }
        }


        // 如果是一一匹配的，那才保存
        if (isMatch) {
            // 保存价格库实体(vm_item表)数据
            for (VendingMachinesWayTemplateDelBean bean : DetailBeans) {

                // 得到对应的售货机货道
                Integer wayNumber = bean.getWayNumber();
                VendingMachinesWayBean wayBean = vendingMachinesWayService.findByVmCodeAndWayNumber(condition.getVmCode(), wayNumber);

                VendingMachinesItemBean newItem = new VendingMachinesItemBean();
                newItem.setBasicItemId(bean.getItemId());
                newItem.setCompanyId(Long.valueOf(userBean.getCompanyId()));
                newItem.setCostPrice(new BigDecimal(bean.getCostPrice()));
                newItem.setPrice(new BigDecimal(bean.getPrice()));
                newItem.setCreateTime(new Date());
                //newItem.setEndTime(dto.getEndTime());
                newItem.setHot(0);
                newItem.setUpdateTime(new Date());

                // 将数据插入到价格库中
                VendingMachinesItemBean alreadyInsertBean = vendingMachinesItemService.add(newItem);

                // 如果是有数据的，则更新
                if (wayBean != null) {
                    wayBean.setFullNum(Math.toIntExact(bean.getMaxCapacity()));
                    wayBean.setNum(Math.toIntExact(bean.getCurCapacity()));
                    wayBean.setUpdateTime(new Date());
                    wayBean.setItemId(alreadyInsertBean.getId());
                    wayBean.setState(30001);
                    vendingMachinesWayService.update(wayBean);
                }

            }
        } else {
            dataUtil.setStatus(0);
            dataUtil.setMessage("模板的通道和售货机的通道不匹配！");
            return dataUtil;
        }

        return dataUtil;
    }


    @ApiOperation(value = "售货机模板详情列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(VendingMachinesWayTemplateDelCondition condition) {
        return vendingMachinesWayTemplateDelServiceImpl.listPage(condition);
    }


    @ApiOperation(value = "修改模板和商品的数据(容量/价钱)", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/updateDetailAndItem", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil updateDetailAndItem(@RequestBody WayItemDto entity) {

        return vendingMachinesWayTemplateDelServiceImpl.updateDetailAndItem(entity);

    }


    @ApiOperation(value = "售货机模板详情修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody VendingMachinesWayTemplateDelBean entity) {


        return new ReturnDataUtil(vendingMachinesWayTemplateDelServiceImpl.update(entity));
    }

    @ApiOperation(value = "售货机模板详情删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(@RequestBody VendingMachinesWayTemplateDelBean entity) {
        return new ReturnDataUtil(vendingMachinesWayTemplateDelServiceImpl.del(entity));
    }

}

