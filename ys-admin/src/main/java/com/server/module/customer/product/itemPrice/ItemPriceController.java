package com.server.module.customer.product.itemPrice;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/itemPrice")
public class ItemPriceController {

	@Autowired
	private ItemPriceService itemPriceService;
	
	@PostMapping("/add")
	public ReturnDataUtil insert(@RequestBody ItemPriceBean itemPrice,HttpServletRequest request){
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		itemPrice.setCreateUser(userId);
		itemPrice.setCreateTime(new Date());
		ItemPriceBean price = itemPriceService.insert(itemPrice);
		if(price != null){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	@PostMapping("/update")
	public ReturnDataUtil update(@RequestBody ItemPriceBean itemPrice,HttpServletRequest request){
		Long userId = (Long)request.getAttribute(AdminConstant.LOGIN_USER_ID);
		itemPrice.setUpdateUser(userId);
		itemPrice.setUpdateTime(new Date());
		if(itemPriceService.update(itemPrice)){
			return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	
	@PostMapping("/get")
	public ReturnDataUtil getByBasicItemId(Long basicItemId){
		List<ItemPriceDto> priceList = itemPriceService.getPriceByBasicItemId(basicItemId);
		if(priceList!=null && priceList.size()>0){
			return ResultUtil.success(priceList);
		}
		return ResultUtil.error();
	}
	
}
