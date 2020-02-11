package com.server.module.system.machineManage.machinesPic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.google.common.collect.Lists;
import com.server.common.paramconfig.AlipayConfig;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesCondition;
import com.server.module.system.machineManage.machinesAdvertising.VendingMachinesAdvertisingBean;
import com.server.util.ReturnDataUtil;
import com.server.util.richText.Result;
import com.server.util.richText.ResultRichTextUtil;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-11-02 10:38:21
 */
@Api(value = "VendingMachinesPicController", description = "售货机促销图片")
@RestController
@RequestMapping("/vendingPic")
public class VendingMachinesPicController {

	private static Logger log=LogManager.getLogger(VendingMachinesPicController.class);
	@Autowired
	private VendingMachinesPicService vendingPicServiceImpl;
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private VendingMachinesPicDao vendingPicDaoImpl;

	@ApiOperation(value = "售货机图片列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) VendingMachinesPic vendingMachinesPic) {
		log.info("<VendingPicController>------<listPage>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		if(vendingMachinesPic==null) {
			vendingMachinesPic=new VendingMachinesPic();
		}
		returnDataUtil=vendingPicServiceImpl.listPage(vendingMachinesPic);
		log.info("<VendingPicController>------<listPage>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "售货机图片添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody VendingMachinesPicBean entity) {
		log.info("<VendingPicController>------<add>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean flag = false;
//		VendingMachinesPicDto  vmpd  = vendingPicServiceImpl.checkRepeat(entity.getBasicItemId());
//		if(entity.getTarget()==1) {
//			flag = vendingPicServiceImpl.findPicMachinesBeanByCompanyIdAndItemId(entity.getCompanyId(),entity.getBasicItemId());
//			Set<Integer> vmiCompanyIds=vmpd.getVmiCompanyIds();
//	        List<String> list = Arrays.asList(vendingPicDaoImpl.getChildList(entity.getCompanyId()).split(","));
//	        for(Integer vmiCompanyId:vmiCompanyIds) {
//	        	if(list.contains(vmiCompanyId)) {
//	        		flag=true;
//	        		break;
//	        	}
//	        }
//		}
		
		if(flag) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该公司或子公司下已经有该商品促销信息");
			returnDataUtil.setReturnObject(flag);
		}else {
			entity.setCreateUser(UserUtils.getUser().getId()); 
			VendingMachinesPicBean add = vendingPicServiceImpl.add(entity);
			if(add!=null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("售货机图片设置成功！"); 
				returnDataUtil.setReturnObject(add);
			}else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("售货机图片设置失败！");
				returnDataUtil.setReturnObject(add);
			}
		}
		log.info("<VendingPicController>------<add>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "售货机促销图片修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody VendingMachinesPicBean entity) {
		log.info("<VendingPicController>------<update>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
//			VendingMachinesPicBean bean = vendingPicServiceImpl.get(entity.getId());
//			if(!(bean.getCompanyId().equals(entity.getCompanyId()))) {
//				boolean flag = vendingPicServiceImpl.findPicMachinesBeanByCompanyIdAndItemId(entity.getCompanyId(),entity.getBasicItemId());
//				if(flag) {
//					returnDataUtil.setStatus(0);
//					returnDataUtil.setMessage("该公司或子公司下已经添加了图片,您可以编辑为其他公司！");
//					returnDataUtil.setReturnObject(flag);
//				}else {
//					entity.setCreateUser(UserUtils.getUser().getId());
//					boolean update2 = vendingPicServiceImpl.update(entity);
//					if(update2) {
//						returnDataUtil.setStatus(1);
//						returnDataUtil.setMessage("售货机促销图片修改成功！"); 
//						returnDataUtil.setReturnObject(update2);
//					}else {
//						returnDataUtil.setStatus(0);
//						returnDataUtil.setMessage("售货机促销图片修改失败！");
//						returnDataUtil.setReturnObject(update2);
//					}
//				}
//			}else {
				entity.setUpdateUser(UserUtils.getUser().getId());
				boolean update = vendingPicServiceImpl.update(entity);
				if(update) {
					returnDataUtil.setStatus(1);
					returnDataUtil.setMessage("售货机促销图片修改成功！"); 
					returnDataUtil.setReturnObject(update);
				}else {
					returnDataUtil.setStatus(0);
					returnDataUtil.setMessage("售货机促销图片修改失败！");
					returnDataUtil.setReturnObject(update);
				}
//			}
		log.info("<VendingPicController>------<update>-------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "售货机促销图片删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<VendingPicController>------<del>-------start");
		ReturnDataUtil returnDataUtil=new ReturnDataUtil();
		boolean del = vendingPicServiceImpl.del(id);
		if(del) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("售货机促销图片删除成功！"); 
			returnDataUtil.setReturnObject(del);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("售货机促销图片删除失败！");
			returnDataUtil.setReturnObject(del);
		}
		log.info("<VendingPicController>------<del>-------end");
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
		log.info("<VendingPicController>--<uploadImage>--start");
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
			log.info("<VendingPicController>--<uploadImage>--end");
			return imgName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("<VendingPicController>--<uploadImage>--end");
			return null;
		} finally {
			log.info("<VendingPicController>--<uploadImage>--end");

		}
	}
	
	
    @ApiOperation(value = "促销图片绑定机器列表", notes = "list", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public List<VendingPicMachinesBean> list(VendingPicMachinesCondition condition) {

        return vendingPicServiceImpl.list(condition);
    }

    @ApiOperation(value = "促销图片绑定机器添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/bindVmCodeAdd", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(Long picId,String[] codes) {
        ReturnDataUtil rd=new ReturnDataUtil();
        VendingPicMachinesCondition condition=new VendingPicMachinesCondition();
        condition.setPicId(picId);
        List<VendingPicMachinesBean> list=vendingPicServiceImpl.list(condition);
        List<String> codeList= Lists.newArrayList();
        VendingMachinesPicBean machinesAdvertisingBean=vendingPicServiceImpl.get(picId);
        boolean canAdd=true;
        for (String code : codes) {
            boolean flag=false;
            for (VendingPicMachinesBean obj : list) {
                if(code.equals(obj.getVmCode())){
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
		VendingMachinesPicDto  vmpd  = vendingPicServiceImpl.checkRepeat(machinesAdvertisingBean.getBasicItemId());
		Set<String> vmCodes=vmpd.getVmCodes();
		for(String code:codeList) {//机器与机器比较
			if(vmCodes.contains(code)) {
				canAdd=false;
				break;
			}
		}
		if(!canAdd) {
			rd.setMessage("机器与机器范围重叠");
			return rd;
		}
		Set<String> set =new HashSet<>();
		//codes companyIds
		for(Integer companyId:vmpd.getCompanyIds()) {
			Collections.addAll(set, vendingPicDaoImpl.getChildList(companyId.longValue()).split(","));
		}
		String vmCodeStrs = StringUtils.join(codeList);
		VendingMachinesPicDto  vmpd2 = vendingPicDaoImpl.vmCodeTranCompanyId(vmCodeStrs);
		for(Integer i:vmpd2.getCompanyIds()) {
			 if(set.contains(String.valueOf(i))) {
				 canAdd=false;
				 break;
			 }
		}
		if(!canAdd) {
			rd.setMessage("机器与公司范围重叠");
			return rd;
		}
        System.out.println("codeList="+codeList.size());
        vendingPicServiceImpl.addAll(picId,codeList);
        machinesAdvertisingBean.setUpdateTime(new Date());
        User user=UserUtils.getUser();
        machinesAdvertisingBean.setUpdateUser(user.getId());
        vendingPicServiceImpl.update(machinesAdvertisingBean);
        return rd;
    }
    
    @ApiOperation(value = "促销图片绑定机器解除", notes = "delAll", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/delAll", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil delAll(Long[] ids) {
        ReturnDataUtil rd=new ReturnDataUtil();
        if(ids.length==0)
            return rd;
        Long id=ids[0];
        Long advertisingId=vendingPicServiceImpl.getPicVmCode(id).getPicId();
        vendingPicServiceImpl.deleteAll(ids);
        VendingMachinesPicBean machinesAdvertisingBean=vendingPicServiceImpl.get(advertisingId);
        machinesAdvertisingBean.setUpdateTime(new Date());
        User user=UserUtils.getUser();
        machinesAdvertisingBean.setUpdateUser(user.getId());
        vendingPicServiceImpl.update(machinesAdvertisingBean);
        return rd;
    }
}
