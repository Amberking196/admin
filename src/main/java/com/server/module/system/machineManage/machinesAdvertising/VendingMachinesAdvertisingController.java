package com.server.module.system.machineManage.machinesAdvertising;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.common.paramconfig.AlipayConfig;
import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
@Api(value = "VendingMachinesAdvertisingController", description = "售货机广告")
@RestController
@RequestMapping("/vendingMachinesAdvertising")
public class VendingMachinesAdvertisingController {

	private static Logger log=LogManager.getLogger(VendingMachinesAdvertisingController.class);
	@Autowired
	private VendingMachinesAdvertisingService vendingMachinesAdvertisingServiceImpl;
	@Autowired
	private AlipayConfig alipayConfig;

	@ApiOperation(value = "售货机广告列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm) {
		log.info("<VendingMachinesAdvertisingController>------<listPage>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(vendingMachinesAdvertisingForm==null) {
			vendingMachinesAdvertisingForm=new VendingMachinesAdvertisingForm();
		}
		returnDataUtil=vendingMachinesAdvertisingServiceImpl.listPage(vendingMachinesAdvertisingForm);
		log.info("<VendingMachinesAdvertisingController>------<listPage>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "售货机广告添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody VendingMachinesAdvertisingBean entity) {
		log.info("<VendingMachinesAdvertisingController>------<add>-------start"+entity);
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(1==entity.getAdvType()) { //公司弹窗
			boolean flag = vendingMachinesAdvertisingServiceImpl.findAdvertisingMachinesBeanByCompanyId(entity.getCompanyId());
			if(flag) {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("该公司或子公司下已经添加了弹窗，请勿重复添加，您可以编辑弹窗信息！");
				returnDataUtil.setReturnObject(flag);
			}else {
				entity.setCreateUser(UserUtils.getUser().getId());
				VendingMachinesAdvertisingBean add = vendingMachinesAdvertisingServiceImpl.add(entity);
				if(add!=null) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("售货机广告设置成功！"); 
					returnDataUtil.setReturnObject(add);
				}else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("售货机广告设置失败！");
					returnDataUtil.setReturnObject(add);
				}
			}
		}else {//
			entity.setCreateUser(UserUtils.getUser().getId());
			VendingMachinesAdvertisingBean add = vendingMachinesAdvertisingServiceImpl.add(entity);
			if(add!=null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("售货机广告设置成功！"); 
				returnDataUtil.setReturnObject(add);
			}else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("售货机广告设置失败！");
				returnDataUtil.setReturnObject(add);
			}
		}
		log.info("<VendingMachinesAdvertisingController>------<add>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "售货机广告修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody VendingMachinesAdvertisingBean entity) {
		log.info("<VendingMachinesAdvertisingController>------<update>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(1==entity.getAdvType()) { //公司弹窗
			VendingMachinesAdvertisingBean bean = vendingMachinesAdvertisingServiceImpl.get(entity.getId());
			if(!(bean.getCompanyId().equals(entity.getCompanyId()))) {
				boolean flag = vendingMachinesAdvertisingServiceImpl.findAdvertisingMachinesBeanByCompanyId(entity.getCompanyId());
				if(flag) {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("该公司或子公司下已经添加了弹窗,您可以编辑为其他公司！");
					returnDataUtil.setReturnObject(flag);
				}else {
					entity.setCreateUser(UserUtils.getUser().getId());
					boolean update2 = vendingMachinesAdvertisingServiceImpl.update(entity);
					if(update2) {
						returnDataUtil.setStatus(1);
						returnDataUtil.setMessage("售货机广告修改成功！"); 
						returnDataUtil.setReturnObject(update2);
					}else {
						returnDataUtil.setStatus(0);
						returnDataUtil.setMessage("售货机广告修改失败！");
						returnDataUtil.setReturnObject(update2);
					}
				}
			}else {
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean update = vendingMachinesAdvertisingServiceImpl.update(entity);
				if(update) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("售货机广告修改成功！"); 
					returnDataUtil.setReturnObject(update);
				}else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("售货机广告修改失败！");
					returnDataUtil.setReturnObject(update);
				}
			}
		}else {
			entity.setUpdateUser(UserUtils.getUser().getId());
			boolean update = vendingMachinesAdvertisingServiceImpl.update(entity);
			if(update) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("售货机广告修改成功！"); 
				returnDataUtil.setReturnObject(update);
			}else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("售货机广告修改失败！");
				returnDataUtil.setReturnObject(update);
			}
		}
		log.info("<VendingMachinesAdvertisingController>------<update>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "售货机广告删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<VendingMachinesAdvertisingController>------<del>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean del = vendingMachinesAdvertisingServiceImpl.del(id);
		if(del) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("售货机广告删除成功！"); 
			returnDataUtil.setReturnObject(del);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("售货机广告删除失败！");
			returnDataUtil.setReturnObject(del);
		}
		log.info("<VendingMachinesAdvertisingController>------<del>-------end");
		return returnDataUtil;
	}
	
	
	/**
	 * 后台操作上传 商品图片
	 * @param file
	 * @return 上传成功信息
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<VendingMachinesAdvertisingController>--<uploadImage>--start");
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 得到图片的后缀名
		String fileNameExtension = filePath.substring(filePath.indexOf("."), filePath.length());
		// 生成实际存储的真实文件名
		String imgName  = UUID.randomUUID().toString() + fileNameExtension;
		// 这是文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getVmAdvertisingImg() + imgName;
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			log.info("<VendingMachinesAdvertisingController>--<uploadImage>--end");
			return imgName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("<VendingMachinesAdvertisingController>--<uploadImage>--end");
			return null;
		} finally {
			log.info("<VendingMachinesAdvertisingController>--<uploadImage>--end");

		}
	}
	
	@ApiOperation(value = "手机端售货机广告页查询", notes = "list", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil list(@RequestBody VendingMachinesAdvertisingForm vendingMachinesAdvertisingForm) {
		log.info("<VendingMachinesAdvertisingController>------<list>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		List<VendingMachinesAdvertisingBean> list = vendingMachinesAdvertisingServiceImpl.list(vendingMachinesAdvertisingForm);
		if(list!=null && list.size()>0) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("手机端售货机广告页查询成功！"); 
			returnDataUtil.setReturnObject(list);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("手机端售货机广告页查询失败！");
			returnDataUtil.setReturnObject(list);
		}
		log.info("<VendingMachinesAdvertisingController>------<list>-------end");
		return returnDataUtil;
	}
	
	

	@ApiOperation(value = "查询机器首页的充值图", notes = "查询机器首页的充值图", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findVendingSlideshow", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findVendingSlideshow(String vmCode) {
		log.info("<VendingMachinesAdvertisingController>------<list>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		String homeImg = vendingMachinesAdvertisingServiceImpl.findVendingSlideshow(vmCode);
		if(homeImg!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setReturnObject(homeImg);
		}else{
			returnDataUtil.setStatus(0);
			returnDataUtil.setReturnObject(homeImg);
		}
		
		log.info("<VendingMachinesAdvertisingController>------<list>-------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "机器广告显示", notes = "showMachinesAdvertising", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/showMachinesAdvertising", produces = "application/json;charset=UTF-8")
    public  ReturnDataUtil showMachinesAdvertising(String vmCode) {
    	log.info("<VendingMachinesAdvertisingController>------<showMachinesAdvertising>------start");
        ReturnDataUtil returnDataUtil=new ReturnDataUtil();
    	returnDataUtil.setReturnObject(vendingMachinesAdvertisingServiceImpl.findVendingAdvertisingMachinesBean(vmCode));
    	log.info("<VendingMachinesAdvertisingController>------<showMachinesAdvertising>------end");
    	return returnDataUtil;
    }
}
