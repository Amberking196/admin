package com.server.module.customer.product.shoppingGoodsSpellGroup;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-10-16 16:41:04
 */
@Api(value = "ShoppingGoodsSpellGroupController", description = "商品拼团设置")
@RestController
@RequestMapping("/shoppingGoodsSpellGroup")
public class ShoppingGoodsSpellGroupController {

	private static Logger log=LogManager.getLogger(ShoppingGoodsSpellGroupController.class);
	@Autowired
	private ShoppingGoodsSpellGroupService shoppingGoodsSpellGroupServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;

	@ApiOperation(value = "商品拼团设置列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) ShoppingGoodsSpellGroupForm ShoppingGoodsSpellGroupForm) {
		log.info("<ShoppingGoodsSpellGroupController>-------<listPage>-------start");
		if(ShoppingGoodsSpellGroupForm==null) {
			ShoppingGoodsSpellGroupForm=new ShoppingGoodsSpellGroupForm();
		}
		returnDataUtil = shoppingGoodsSpellGroupServiceImpl.listPage(ShoppingGoodsSpellGroupForm);
		log.info("<ShoppingGoodsSpellGroupController>-------<listPage>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "商品拼团设置添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody ShoppingGoodsSpellGroupVo vo) {
		log.info("<ShoppingGoodsSpellGroupController>------<add>-------start");
		ShoppingGoodsSpellGroupBean entity = new ShoppingGoodsSpellGroupBean();
        BeanUtils.copyProperties(vo, entity);
        DateTime dt1 = new DateTime(entity.getEndTime().getTime());
        dt1=dt1.withHourOfDay(23);
        dt1=dt1.withMinuteOfHour(59);
        dt1=dt1.withSecondOfMinute(59);
        entity.setEndTime(dt1.toDate());

        DateTime dt2 = new DateTime(entity.getStartTime().getTime());
        dt2=dt2.withHourOfDay(0);
        dt2=dt2.withMinuteOfHour(0);
        dt2=dt2.withSecondOfMinute(0);
        entity.setStartTime(dt2.toDate());
        entity.setCreateUser(UserUtils.getUser().getId());
        //判断是否已经设置团购
        //ShoppingGoodsSpellGroupBean bean = shoppingGoodsSpellGroupServiceImpl.isConglomerateCommodity(entity.getGoodsId());
		//if(bean==null) {
			ShoppingGoodsSpellGroupBean bean2 = shoppingGoodsSpellGroupServiceImpl.add(entity);
			if(bean2!=null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("商品团购设置成功");
				returnDataUtil.setReturnObject(bean2);
			}else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("商品团购设置失败");
				returnDataUtil.setReturnObject(bean2);
			}
		//}else {
		//	returnDataUtil.setStatus(0);
		//	returnDataUtil.setMessage("该商品已经设置团购了，不能重复设置！");
		//}
        
		log.info("<ShoppingGoodsSpellGroupController>------<add>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "商品拼团设置修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody ShoppingGoodsSpellGroupVo vo) {
		ShoppingGoodsSpellGroupBean entity = new ShoppingGoodsSpellGroupBean();
        BeanUtils.copyProperties(vo, entity);
        DateTime dt1 = new DateTime(entity.getEndTime().getTime());
        dt1=dt1.withHourOfDay(23);
        dt1=dt1.withMinuteOfHour(59);
        dt1=dt1.withSecondOfMinute(59);
        entity.setEndTime(dt1.toDate());

        DateTime dt2 = new DateTime(entity.getStartTime().getTime());
        dt2=dt2.withHourOfDay(0);
        dt2=dt2.withMinuteOfHour(0);
        dt2=dt2.withSecondOfMinute(0);
        entity.setStartTime(dt2.toDate());
        entity.setUpdateUser(UserUtils.getUser().getId());
        Boolean flag=shoppingGoodsSpellGroupServiceImpl.update(entity);
        ReturnDataUtil returnDataUtil=new ReturnDataUtil(flag);
		if(flag) {
			returnDataUtil.setStatus(1);
	        returnDataUtil.setMessage("拼团设置修改成功");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("拼团设置修改失败");
		}
        return returnDataUtil;
	}

	@ApiOperation(value = "商品拼团设置删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody List<Integer> id) {
		String ids = StringUtils.join(id, ",");
		Boolean flag=shoppingGoodsSpellGroupServiceImpl.del(ids);
        ReturnDataUtil returnDataUtil=new ReturnDataUtil(flag);
		if(flag) {
			returnDataUtil.setStatus(1);
	        returnDataUtil.setMessage("拼团设置删除成功");
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("拼团设置删除失败");
		}
		return returnDataUtil;
	}
	
	@ApiOperation(value = "判断是否是拼团商品", notes = "isConglomerateCommodity", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/isConglomerateCommodity", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil isConglomerateCommodity(Long shoppingGoodsId) {
		log.info("<ShoppingGoodsSpellGroupController>-----------<isConglomerateCommodity>----------start");
		ShoppingGoodsSpellGroupBean bean = shoppingGoodsSpellGroupServiceImpl.isConglomerateCommodity(shoppingGoodsId);
		if(bean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("拼团商品");
		}else {
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("非拼团商品");
		}
		log.info("<ShoppingGoodsSpellGroupController>-----------<isConglomerateCommodity>----------end");
		return returnDataUtil;
	}
	

}
