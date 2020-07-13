package com.server.module.customer.userInfo.address;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.module.system.adminUser.AdminConstant;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@RestController
@RequestMapping("/address")
public class AddressController {

	private static Logger log=LogManager.getLogger(AddressController.class);
	@Autowired
	private AddressService addressService;
	/**
	 * 新增地址
	 * @author hebiting
	 * @date 2018年7月6日上午10:11:53
	 * @return
	 */
	@PostMapping("/add")
	public ReturnDataUtil addAddress(@RequestBody AddressBean address,HttpServletRequest request){
		log.info("<AddressController>------<addAddress>------start");
		if(address.getCustomerId()==null){
			address.setCustomerId(Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString()));
		}
		if(address.getDefaultFlag()==1){
			addressService.updateDefaultFlag(address.getCustomerId());
		}
		AddressBean insert = addressService.insert(address);
		if(insert != null){
			log.info("<AddressController>------<addAddress>------end");
			return ResultUtil.success();
		}
		log.info("<AddressController>------<addAddress>------end");
		return ResultUtil.error(0,"新增失败",null);
	}
	
	/**
	 * 修改地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午10:15:58
	 * @param address
	 * @return
	 */
	@PostMapping("/update")
	public ReturnDataUtil updateAddress(@RequestBody AddressBean address,HttpServletRequest request){
		log.info("<AddressController>------<updateAddress>------start");
		address.setUpdateTime(new Date());
		if(address.getCustomerId()==null){
			address.setCustomerId(Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString()));
		}
		if(address.getDefaultFlag()==1 && addressService.updateDefaultFlag(address.getCustomerId())){
			boolean update = addressService.update(address);
			if(update){
				log.info("<AddressController>------<updateAddress>------end");
				return ResultUtil.success();
			}
		}else{
			boolean update = addressService.update(address);
			if(update){
				log.info("<AddressController>------<updateAddress>------end");
				return ResultUtil.success();
			}
		}
		log.info("<AddressController>------<updateAddress>------end");
		return ResultUtil.error(0,"更新失败",null);
	}
	
	/**
	 * 根据用户id查询地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午10:20:12
	 * @param customerId
	 * @return
	 */
	@PostMapping("/select")
	public ReturnDataUtil selectAddress(@RequestParam(required=false)Long customerId,HttpServletRequest request){
		log.info("<AddressController>------<select>------start");
		if(customerId==null){
			customerId = Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		}
		List<AddressBean> addressList = addressService.select(customerId);
		if(addressList!=null && addressList.size()>0){
			log.info("<AddressController>------<select>------end");
			return ResultUtil.success(addressList);
		}
		log.info("<AddressController>------<select>------end");
		return ResultUtil.selectError();
	}
	
	/**
	 * 根据用户id查询地址信息
	 * @author hebiting
	 * @date 2018年7月6日上午10:20:12
	 * @param customerId
	 * @return
	 */
	@PostMapping("/selectById")
	public ReturnDataUtil selectAddressById(@RequestParam(required=true)Integer id,HttpServletRequest request){
		log.info("<AddressController>------<selectAddressById>------start");
		Long customerId = Long.valueOf(request.getAttribute(AdminConstant.LOGIN_USER_ID).toString());
		AddressBean address = addressService.selectById(customerId,id);
		if(address!=null){
			log.info("<AddressController>------<selectAddressById>------end");
			return ResultUtil.success(address);
		}
		log.info("<AddressController>------<selectAddressById>------end");
		return ResultUtil.selectError();
	}
	
	/**
	 * 真正删除用户地址
	 * @author hebiting
	 * @date 2018年7月6日上午11:18:30
	 * @param addressId
	 * @return
	 */
	@PostMapping("/delete")
	public ReturnDataUtil deleteAddress(Long addressId){
		log.info("<AddressController>------<deleteAddress>------start");
		boolean delete = addressService.delete(addressId);
		if(delete){
			log.info("<AddressController>------<deleteAddress>------end");
			return ResultUtil.success();
		}
		log.info("<AddressController>------<deleteAddress>------end");
		return ResultUtil.error(0,"删除失败",null);
	}
}
