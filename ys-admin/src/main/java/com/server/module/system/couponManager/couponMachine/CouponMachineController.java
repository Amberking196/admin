package com.server.module.system.couponManager.couponMachine;

import com.server.module.system.couponManager.couponCustomer.CouponCustomerDao;
import com.server.module.system.couponManager.couponCustomer.CustomerCouponVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * author name: yjr
 * create time: 2018-06-28 09:11:41
 */
@Api(value = "CouponMachineController", description = "机器优惠劵")
@RestController
@RequestMapping("/couponMachine")
public class CouponMachineController {


    @Autowired
    private CouponMachineService couponMachineServiceImpl;
    @Autowired
    private CouponCustomerDao couponCustomerDao;

    /*@ApiOperation(value = "机器优惠劵列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(CouponMachineForm condition) {
        return couponMachineServiceImpl.listPage(condition);
    }
*/
    @ApiOperation(value = "机器优惠劵列表for 添加机器页面", notes = "listPageForAddMachine", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPageForAddMachine", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPageForAddMachine(CouponMachineForm condition) {
        return couponMachineServiceImpl.listPageForAddMachine(condition);
    }

    /*@ApiOperation(value = "机器优惠劵添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody CouponMachineBean entity) {
        return new ReturnDataUtil(couponMachineServiceImpl.add(entity));
    }*/

    /*@ApiOperation(value = "机器优惠劵修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody CouponMachineBean entity) {
        return new ReturnDataUtil(couponMachineServiceImpl.update(entity));
    }*/

    @ApiOperation(value = "机器优惠劵删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Integer id)
    {
        ReturnDataUtil re=new ReturnDataUtil();
        CouponMachineBean obj=couponMachineServiceImpl.get(id);
        String vmCode=obj.getVmCode();
        List<CustomerCouponVo> list= couponCustomerDao.list(vmCode);

        if(list.size()>0){
            re.setMessage("该机器的优惠劵已经下发给用户，不能删除");
            re.setStatus(-1);
            return re;
        }
        couponMachineServiceImpl.del(id);
        return re;
    }



}



