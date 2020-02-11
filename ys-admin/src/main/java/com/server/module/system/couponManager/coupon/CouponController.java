package com.server.module.system.couponManager.coupon;

import com.server.common.paramconfig.AlipayConfig;
import com.server.module.customer.product.ShoppingGoodsController;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.couponManager.couponCustomer.CouponCustomerDao;
import com.server.module.system.couponManager.couponCustomer.CustomerCouponVo;
import com.server.module.system.itemManage.itemBasic.ItemBasicService;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.GET;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * author name: yjr
 * create time: 2018-06-28 09:01:06
 */
@Api(value = "CouponController", description = "优惠劵")
@RestController
@RequestMapping("/coupon")
public class CouponController {

    private static Logger log = LogManager.getLogger(ShoppingGoodsController.class);
    @Autowired
    private ItemBasicService itemBasicServiceImpl;
    @Autowired
    private AlipayConfig alipayConfig;


    @Autowired
    private CouponService couponServiceImpl;
    @Autowired
    private CouponCustomerDao couponCustomerDao;

    @Autowired
    private VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	
    @ApiOperation(value = "优惠劵列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(CouponForm condition) {
    	log.info("<CouponController>----<listPage>------start");
    	ReturnDataUtil listPage = couponServiceImpl.listPage(condition);
    	log.info("<CouponController>----<listPage>------end");
        return listPage;
    }

    @ApiOperation(value = "优惠劵添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody CouponAddVo vo) {
    	log.info("<CouponController>----<add>------start");
    	returnDataUtil=couponServiceImpl.add(vo);
        log.info("<CouponController>----<add>------end");
    	return returnDataUtil;
    }

    @ApiOperation(value = "优惠劵修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody CouponBean entity) {
    	log.info("<CouponController>----<update>------start");
        returnDataUtil = couponServiceImpl.update(entity);
        log.info("<CouponController>----<update>------end");
        return returnDataUtil;
    }

    @ApiOperation(value = "优惠劵删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Integer id)
    {
    	log.info("<CouponController>----<del>------start");
        ReturnDataUtil re=new ReturnDataUtil();
        List<CustomerCouponVo> list= couponCustomerDao.listByCouponId(id);

        if(list.size()>0){
            re.setMessage("该优惠劵已经下发给用户，不能删除");
            re.setStatus(-99);
            return re;
        }
        couponServiceImpl.del(id);
        log.info("<CouponController>----<del>------end");
        return re;
    }
    @ApiOperation(value = "优惠劵获取", notes = "get", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/get", produces = "application/json;charset=UTF-8")
    public CouponBean get(Integer id)
    {
    	log.info("<CouponController>----<get>------start");
        CouponBean coupon= couponServiceImpl.get(id);
        log.info("<CouponController>----<get>------end");
        return coupon;
    }



    @ApiOperation(value = "绑定优惠劵到客户", notes = "addMachine", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addCustomer", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addCustomer(Long[] customerIds, Long couponId) {
        return couponServiceImpl.addCustomer(customerIds, couponId);
    }

    @ApiOperation(value = "绑定优惠劵到所有", notes = "addAllMachine", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addAllCustomer", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addAllCustomer(AddAllCustomerForm condition) {
    	log.info("<CouponController>----<addAllCustomer>------start");
    	ReturnDataUtil returnDataUtil= couponServiceImpl.addAllCustomer(condition);
    	log.info("<CouponController>----<addAllCustomer>------end");
        return returnDataUtil;
    }

/*
    @ApiOperation(value = "我的优惠劵列表", notes = "myListPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/myListPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myListPage(CouponForm condition) {
        condition.setCustomerId(1);
        return couponServiceImpl.listPage(condition);
    }*/


    /**
     * 后台操作上传图片
     *
     * @param file
     * @return 上传成功信息
     */
    @ApiOperation(value = "图片上传", notes = "图片上传")
    @PostMapping(value = "/uploadImage")
    public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
        log.info("<CouponController>--<uploadImage>--start");
        String filePath = file.getOriginalFilename(); // 获取文件的名称
        String imgName = itemBasicServiceImpl.findImgName(filePath);
        // filePath = "/home/ysNew/ys-admin/itemImg/" + imgName; //
        // 这是文件的保存路径，如果不设置就会保存到项目的根目录
        filePath = alipayConfig.getCouponImgPath() + imgName;
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
            System.out.println("file==="+imgName);
            return imgName;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            log.info("<CouponController>--<uploadImage>--end");
        }
    }
    
    @ApiOperation(value = "判断优惠券是否下发给用户", notes = "whetherIssued", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/whetherIssued", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil whetherIssued(Integer id) {
    	log.info("<CouponController>----<whetherIssued>------start");
    	ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	boolean b=couponCustomerDao.isHaveCustomer(id.longValue());
        if(b){
        	returnDataUtil.setStatus(0);
        	returnDataUtil.setMessage("该优惠劵已下发用户，不能修改");
        	returnDataUtil.setReturnObject(b);
        }else {
        	returnDataUtil.setStatus(1);
        	returnDataUtil.setMessage("该优惠劵未下发用户，可以修改");
        	returnDataUtil.setReturnObject(b);
        }
        log.info("<CouponController>----<whetherIssued>------end");
        return returnDataUtil;
    }
    
    
    @ApiOperation(value = "亚运城下发优惠券", notes = "addAsianCustomer", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/addAsianCustomer", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil addAsianCustomer() {
    	log.info("<CouponController>----<addAsianCustomer>------start");
    	ReturnDataUtil returnDataUtil=couponServiceImpl.addAsianCustomer();
        log.info("<CouponController>----<addAsianCustomer>------end");
        return returnDataUtil;
    }
}

