package com.server.module.customer.product.tblCustomerSpellGroup;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.server.module.customer.order.OrderService;
import com.server.module.customer.product.shoppingGoodsSpellGroup.ShoppingGoodsSpellGroupService;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-10-17 11:06:09
 */
@Api(value = "TblCustomerSpellGroupController", description = "用户拼团")
@RestController
@RequestMapping("/tblCustomerSpellGroup")
public class TblCustomerSpellGroupController {
	
	private static Logger log=LogManager.getLogger(TblCustomerSpellGroupController.class);

	@Autowired
	private TblCustomerSpellGroupService tblCustomerSpellGroupServiceImpl;

	@Autowired
	private ShoppingGoodsSpellGroupService shoppingGoodsSpellGroupService;
    @Autowired
	private OrderService orderService;

	@Autowired
	private ReturnDataUtil  returnDataUtil;

	@ApiOperation(value = "用户拼团列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) TblCustomerSpellGroupForm tblCustomerSpellGroupForm) {
		log.info("<TblCustomerSpellGroupController>------<listPage>------start");
		if(tblCustomerSpellGroupForm==null) {
			tblCustomerSpellGroupForm=new TblCustomerSpellGroupForm();
		}
		returnDataUtil=tblCustomerSpellGroupServiceImpl.listPage(tblCustomerSpellGroupForm);
		log.info("<TblCustomerSpellGroupController>------<listPage>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "发起拼团", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody TblCustomerSpellGroupBean entity) {
		log.info("<TblCustomerSpellGroupController>------<add>-----start");
		TblCustomerSpellGroupBean add = tblCustomerSpellGroupServiceImpl.add(entity);
		if(add!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("发起拼团成功！");
			returnDataUtil.setReturnObject(add);
		}else {
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("发起拼团失败！");
			returnDataUtil.setReturnObject(add);
		}
		log.info("<TblCustomerSpellGroupController>------<add>-----end");
		return returnDataUtil;
	}

	@ApiOperation(value = "用户拼团修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody TblCustomerSpellGroupBean entity) {
		return new ReturnDataUtil(tblCustomerSpellGroupServiceImpl.update(entity));
	}

	@ApiOperation(value = "用户拼团删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Object id) {
		return new ReturnDataUtil(tblCustomerSpellGroupServiceImpl.del(id));
	}
	
	@ApiOperation(value = "查询商品下的用户拼团信息", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody TblCustomerSpellGroupForm  tblCustomerSpellGroupForm) {
		log.info("<TblCustomerSpellGroupController>-----<list>------start");
		List<TblCustomerSpellGroupBean> list = tblCustomerSpellGroupServiceImpl.list(tblCustomerSpellGroupForm);
		returnDataUtil.setReturnObject(list);
		log.info("<TblCustomerSpellGroupController>-----<list>------end");
		return returnDataUtil;
	}
	@ApiOperation(value = "根据参加拼团的id查询用户信息", notes = "findCustomerByIds", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findCustomerByIds", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findCustomerByIds(String participationCustomerId) {
		log.info("<TblCustomerSpellGroupController>-----<list>------start");
		ReturnDataUtil returnData=tblCustomerSpellGroupServiceImpl.findCustomerByIds(participationCustomerId);
		log.info("<TblCustomerSpellGroupController>-----<list>------end");
		return returnData;
	}
	
	@ApiOperation(value = "判断当前用户是否重复参与拼团", notes = "isRepeatedSpellGroup", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/isRepeatedSpellGroup", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil isRepeatedSpellGroup(Long  id) {
		log.info("<TblCustomerSpellGroupController>-----<isRepeatedSpellGroup>------start");
		ReturnDataUtil returnData=tblCustomerSpellGroupServiceImpl.isRepeatedSpellGroup(id);
		log.info("<TblCustomerSpellGroupController>-----<isRepeatedSpellGroup>------end");
		return returnData;
	}

	
	@ApiOperation(value = "判断当前用户是否可以发起拼团", notes = "isStartSpellGroup", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/isStartSpellGroup", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil isStartSpellGroup(Long  spellGroupId) {
		log.info("<TblCustomerSpellGroupController>-----<isStartSpellGroup>------start");
		ReturnDataUtil returnData=tblCustomerSpellGroupServiceImpl.isStartSpellGroup(spellGroupId);
		log.info("<TblCustomerSpellGroupController>-----<isStartSpellGroup>------end");
		return returnData;
	}

	@ApiOperation(value = "拼团订单列表", notes = "listSpellOrders", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listSpellOrders", produces = "application/json;charset=UTF-8")
	public  ReturnDataUtil listSpellOrders(SpellOrderCondition condition){

		ReturnDataUtil data=tblCustomerSpellGroupServiceImpl.listSpellOrders(condition);
		return data;
	}
	@ApiOperation(value = "拼团订单详情", notes = "listSpellOrdersDetail", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listSpellOrdersDetail", produces = "application/json;charset=UTF-8")
	public Map<String,Object> listSpellOrdersDetail(Integer spellId,Integer orderId,String ptCode){
		List<Map<String,Object>> baseSpell=tblCustomerSpellGroupServiceImpl.listBaseSpellInfo(orderId);
		List<Map<String,Object>> spellList=tblCustomerSpellGroupServiceImpl.listSpell(spellId);
		List<Map<String,Object>> orderList=tblCustomerSpellGroupServiceImpl.listSpellGoodsInfo(orderId);
		List<Map<String,Object>> refundInfo=tblCustomerSpellGroupServiceImpl.listRefundInfo(ptCode);
		Map<String,Object> map= Maps.newHashMap();
		map.put("baseSpell",baseSpell);
        map.put("goodsInfo",orderList);
        map.put("spellInfo",spellList);
        map.put("refundInfo",refundInfo);

        return map;
	}

	@ApiOperation(value = "提货劵详情", notes = "listVouchersInfo", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listVouchersInfo", produces = "application/json;charset=UTF-8")
	public Map<String,Object> listVouchersInfo(Integer orderId){
			List<Map<String,Object>> vouchersInfo=tblCustomerSpellGroupServiceImpl.listVouchersInfo(orderId);
		Map<String,Object> map= Maps.newHashMap();
		map.put("vouchersInfo",vouchersInfo);
		return map;
	}

}
