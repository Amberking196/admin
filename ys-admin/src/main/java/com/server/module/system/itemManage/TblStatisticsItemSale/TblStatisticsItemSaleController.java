package com.server.module.system.itemManage.TblStatisticsItemSale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * author name: why create time: 2018-05-02 09:36:44
 */
@Api(value = "TblStatisticsItemSaleController", description = "商品上架数")
@RestController
@RequestMapping("/tblStatisticsItemSale")
public class TblStatisticsItemSaleController {

	@Autowired
	private TblStatisticsItemSaleService tblStatisticsItemSaleServiceImpl;

	@ApiOperation(value = "商品上架数列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(TblStatisticsItemSaleCondition condition) {
		return tblStatisticsItemSaleServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "增加上架数", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public TblStatisticsItemSaleBean add( ) {
		return tblStatisticsItemSaleServiceImpl.add();
	}
	
}
