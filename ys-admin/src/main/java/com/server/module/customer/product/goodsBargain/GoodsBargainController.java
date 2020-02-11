package com.server.module.customer.product.goodsBargain;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.customer.product.ShoppingGoodsBean;
import com.server.module.customer.product.ShoppingGoodsService;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserDao;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: yjr
 * create time: 2018-12-21 10:31:41
 */
@Api(value = "GoodsBargainController", description = "商品砍价")
@RestController
@RequestMapping("/goodsBargain")
public class GoodsBargainController {


    @Autowired
    private GoodsBargainService goodsBargainServiceImpl;
    @Autowired
    private ShoppingGoodsService shoppingGoodsService;
    @Autowired
    private AdminUserDao adminUserDao;
    @ApiOperation(value = "商品砍价列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil listPage(GoodsBargainCondition condition) {
        return goodsBargainServiceImpl.listPage(condition);
    }

    @ApiOperation(value = "商品砍价添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(@RequestBody GoodsBargainBean entity) {
        ReturnDataUtil rd = new ReturnDataUtil();
        if (check(entity, rd)) return rd;
        User user= UserUtils.getUser();
        AdminUserBean userBean=adminUserDao.findUserById(user.getId());
        entity.setCreateUser(user.getId());
        entity.setCreateUserName(userBean.getName());
        entity.setCreateTime(new Date());
        entity.setState(1);//默认停用
        Calendar cal = Calendar.getInstance();
        cal.setTime(entity.getEndTime());
        // 设置时分秒
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        entity.setEndTime(cal.getTime());
        return new ReturnDataUtil(goodsBargainServiceImpl.add(entity));
    }

    private boolean check(@RequestBody GoodsBargainBean entity, ReturnDataUtil rd) {
        Long goodsId=entity.getGoodsId();
        if(goodsId==null){
             rd.setStatus(0);
             rd.setMessage("没有商品id信息");
            return true;
        }
        ShoppingGoodsBean shoppingGoodsBean = shoppingGoodsService.get(goodsId);

        if(shoppingGoodsBean==null){
            System.out.println("goods is not exist "+goodsId);
            rd.setStatus(0);
            rd.setMessage("不存在该商品");
            return true;
        }

        if(shoppingGoodsBean.getSalesPrice().doubleValue()<entity.getLowestPrice().doubleValue()){
            rd.setStatus(0);
            rd.setMessage("砍到的最低价格不能大于商品销售价");
            return true;
        }
        double bargainMoney=entity.getBargainCount()*entity.getOneBargainPrice().doubleValue();
        if(shoppingGoodsBean.getSalesPrice().doubleValue()<bargainMoney){
            rd.setStatus(0);
            rd.setMessage("每刀价格*总刀数不能大于商品销售价");
            return true;
        }
        return false;
    }

    @ApiOperation(value = "商品砍价修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil update(@RequestBody GoodsBargainBean entity) {

        ReturnDataUtil rd = new ReturnDataUtil();
        if (check(entity, rd)) return rd;
        User user= UserUtils.getUser();
        AdminUserBean userBean=adminUserDao.findUserById(user.getId());
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(userBean.getId());

        return new ReturnDataUtil(goodsBargainServiceImpl.update(entity));
    }

    @ApiOperation(value = "停用砍价活动", notes = "disable", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/disable", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil disable(Long id) {
        GoodsBargainBean bean = goodsBargainServiceImpl.get(id);
        bean.setState(1);
        Long userId=UserUtils.getUser().getId();
        bean.setUpdateUser(userId);
        bean.setUpdateTime(new Date());
        return new ReturnDataUtil(goodsBargainServiceImpl.update(bean));
    }

    @ApiOperation(value = "启用砍价活动", notes = "enable", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/enable", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil enable(Long id) {
        ReturnDataUtil returnDataUtil=new ReturnDataUtil();
        GoodsBargainBean bean = goodsBargainServiceImpl.get(id);
        Long goodsId=bean.getGoodsId();
        GoodsBargainCondition condition=new GoodsBargainCondition();
        condition.setGoodsId(goodsId);
        condition.setPageSize(100);
        ReturnDataUtil rd=goodsBargainServiceImpl.listPage(condition);
        List<GoodsBargainBean> list=(List<GoodsBargainBean>)rd.getReturnObject();
        for (GoodsBargainBean goodsBargainBean : list) {
            if(goodsBargainBean.getState()==0){
                returnDataUtil.setStatus(0);
                returnDataUtil.setMessage("只能有一个启用的砍价活动");
                return returnDataUtil;
            }
        }
        bean.setState(0);
        Long userId=UserUtils.getUser().getId();
        bean.setUpdateUser(userId);
        bean.setUpdateTime(new Date());
        return new ReturnDataUtil(goodsBargainServiceImpl.update(bean));
    }

    /*@ApiOperation(value = "商品砍价删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil del(Object id) {
        return new ReturnDataUtil(goodsBargainServiceImpl.del(id));
    }
*/



}

