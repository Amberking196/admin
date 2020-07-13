package com.server.module.system.machineManage.advertisingBindMachine;

import com.google.common.collect.Lists;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.machinesAdvertising.VendingMachinesAdvertisingBean;
import com.server.module.system.machineManage.machinesAdvertising.VendingMachinesAdvertisingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

/**
 * author name: yjr
 * create time: 2018-12-24 15:45:31
 */
@Api(value = "VendingAdvertisingMachinesController", description = "广告绑定机器")
@RestController
@RequestMapping("/vendingAdvertisingMachines")
public class VendingAdvertisingMachinesController {


    @Autowired
    private VendingAdvertisingMachinesService vendingAdvertisingMachinesServiceImpl;
    @Autowired
    private VendingMachinesAdvertisingService machinesAdvertisingService;
    /*@ApiOperation(value = "广告绑定机器列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(VendingAdvertisingMachinesCondition condition) {
        return vendingAdvertisingMachinesServiceImpl.listPage(condition);
    }*/

    @ApiOperation(value = "广告绑定机器列表", notes = "list", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public List<VendingAdvertisingMachinesBean> list(VendingAdvertisingMachinesCondition condition) {

        return vendingAdvertisingMachinesServiceImpl.list(condition);
    }

    @ApiOperation(value = "广告绑定机器添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(Long advertisingId,String[] codes) {
        ReturnDataUtil rd=new ReturnDataUtil();
        VendingAdvertisingMachinesCondition condition=new VendingAdvertisingMachinesCondition();
        condition.setAdvertisingId(advertisingId);
        List<VendingAdvertisingMachinesBean> list=vendingAdvertisingMachinesServiceImpl.list(condition);
        List<String> codeList= Lists.newArrayList();
        System.out.println("list=="+list.size());
        for (String code : codes) {
            boolean flag=false;
            for (VendingAdvertisingMachinesBean obj : list) {
                if(code.equals(obj.getVmCode())){
                    System.out.println(obj.getId()+"   id");
                    if(obj.getId()==0){
                        flag=true;
                        break;
                    }
                }
            }
            if(flag){
                codeList.add(code);
            }
        }
        System.out.println("codeList="+codeList.size());
        vendingAdvertisingMachinesServiceImpl.addAll(advertisingId,codeList);
        VendingMachinesAdvertisingBean machinesAdvertisingBean=machinesAdvertisingService.get(advertisingId);
        machinesAdvertisingBean.setUpdateTime(new Date());
        User user=UserUtils.getUser();
        machinesAdvertisingBean.setUpdateUser(user.getId());
        machinesAdvertisingService.update(machinesAdvertisingBean);
        return rd;
    }
    @ApiOperation(value = "广告绑定机器解除", notes = "delAll", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/delAll", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil delAll(Long[] ids) {
        ReturnDataUtil rd=new ReturnDataUtil();
        if(ids.length==0)
            return rd;
        Long id=ids[0];
        Long advertisingId=vendingAdvertisingMachinesServiceImpl.get(id).getAdvertisingId();
        vendingAdvertisingMachinesServiceImpl.deleteAll(ids);
        VendingMachinesAdvertisingBean machinesAdvertisingBean=machinesAdvertisingService.get(advertisingId);
        machinesAdvertisingBean.setUpdateTime(new Date());
        User user=UserUtils.getUser();
        machinesAdvertisingBean.setUpdateUser(user.getId());
        machinesAdvertisingService.update(machinesAdvertisingBean);
        return rd;
    }

   /* @ApiOperation(value = "广告绑定机器修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody VendingAdvertisingMachinesBean entity) {
        return new ReturnDataUtil(vendingAdvertisingMachinesServiceImpl.update(entity));
    }*/

   // @ApiOperation(value = "广告绑定机器删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
   // public ReturnDataUtil del(Object id) {
       // return new ReturnDataUtil(vendingAdvertisingMachinesServiceImpl.del(id));
    //}
}

