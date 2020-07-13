package com.server.module.customer.complain;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.common.paramconfig.AlipayConfig;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.machineManage.machineList.VmbaseInfoDto;
import com.server.module.system.machineManage.machinesBadOpenLog.BadOpenLogBean;
import com.server.module.system.machineManage.machinesBadOpenLog.MachinesBadOpenService;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.ExcelUtil;
import com.server.util.PhotoCompression;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-11-22 17:23:16
 */
@Api(value = "TblCustomerComplainController", description = "用户投诉")
@RestController
@RequestMapping("/tblCustomerComplain")
public class TblCustomerComplainController {

	private static Logger log = LogManager.getLogger(TblCustomerComplainController.class);
	@Autowired
	private TblCustomerComplainService tblCustomerComplainServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private VendingMachinesInfoService vmInfoService;
	@Autowired
	private MachinesBadOpenService machinesBadOpenService;

	@ApiOperation(value = "用户投诉列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) TblCustomerComplainForm tblCustomerComplainForm) {
		log.info("<TblCustomerComplainController>----<listPage>------start");
		if (tblCustomerComplainForm == null) {
			tblCustomerComplainForm = new TblCustomerComplainForm();
		}
		returnDataUtil = tblCustomerComplainServiceImpl.listPage(tblCustomerComplainForm);
		log.info("<TblCustomerComplainController>----<listPage>------end");
		return returnDataUtil;
	}

	/**
	 * 故障申报图片上传
	 * 
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<CustomerMessageController>--<uploadImage>--start");
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 得到图片的后缀名
		String fileNameExtension = filePath.substring(filePath.indexOf("."), filePath.length());
		// 生成实际存储的真实文件名
		String imgName = UUID.randomUUID().toString() + fileNameExtension;
		// 得到配置文件中的图片上传 保存地址
		filePath = alipayConfig.getComplainImg() + imgName;
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			log.info("imgName======" + imgName);
			// 设置图片压缩后的图片名称
			String newIngName = imgName.substring(0, 12) + fileNameExtension;
			// 设置图片压缩后的保存地址
			String newFilePath = alipayConfig.getComplainImg() + newIngName;
			PhotoCompression.reduceImg(filePath, newFilePath, 500, 500, 0.7F);
			// 返回压缩后的图片名称
			return newIngName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			log.info("<CustomerMessageController>--<uploadImage>--end");
		}

	}

	@ApiOperation(value = "我的故障申报", notes = "myDeclaration", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/myDeclaration", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil myDeclaration(@RequestBody(required = false) TblCustomerComplainForm tblCustomerComplainForm)  {
        log.info("<TblCustomerController>-----<myDeclaration>-----start");
        if(tblCustomerComplainForm==null){
            tblCustomerComplainForm=new TblCustomerComplainForm();
        }
        List<TblCustomerComplainBean> list = tblCustomerComplainServiceImpl.myDeclaration(tblCustomerComplainForm);
        if(list.size()>0 && list!=null){
            returnDataUtil.setStatus(1);
            returnDataUtil.setMessage("我的故障申报查看成功");
            returnDataUtil.setReturnObject(list);
        }else{
            returnDataUtil.setStatus(0);
            returnDataUtil.setMessage("我的故障申报查看失败");
            returnDataUtil.setReturnObject(list);
        }
        log.info("<TblCustomerController>-----<myDeclaration>-----end");
        return returnDataUtil;
    }

	@ApiOperation(value = "用户投诉列表导出", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil export(TblCustomerComplainForm tblCustomerComplainForm,HttpServletResponse res,HttpServletRequest request) {
		log.info("<TblCustomerComplainController>----<export>------start");
		if (tblCustomerComplainForm == null) {
			tblCustomerComplainForm = new TblCustomerComplainForm();
		}
		tblCustomerComplainForm.setIsShowAll(1);
		returnDataUtil = tblCustomerComplainServiceImpl.listPage(tblCustomerComplainForm);
		List<TblCustomerComplainBean> data=(List<TblCustomerComplainBean>) returnDataUtil.getReturnObject();

		String title ="故障申报列表";
		StringBuffer date = new StringBuffer("起始--至今");
		if(tblCustomerComplainForm.getStartTime()!=null){
			date.replace(0,2,DateUtil.formatYYYYMMDD(tblCustomerComplainForm.getStartTime()));
		}
		if(tblCustomerComplainForm.getEndTime()!=null){
			date.replace(date.length()-2,date.length(),DateUtil.formatYYYYMMDD(tblCustomerComplainForm.getEndTime()));
		}
		String[] headers = new String[]{"vmCode","content","typeLabel","phone","createTimes","stateLabel"};
		String[] column = new String[]{"售货机编号","故障内容","故障类型","用户手机","发送时间","回复状态"};
		//添加导出日志的内容
		ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
		if(tblCustomerComplainForm.getStartTime()!=null&&tblCustomerComplainForm.getEndTime()!=null) {
			//导出日志内容按时间格式输出
			bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(tblCustomerComplainForm.getStartTime())+"--"+DateUtil.formatYYYYMMDD(tblCustomerComplainForm.getEndTime())+"的故障申报列表的数据");
		}else {
			bean.setContent("用户: "+bean.getOperatorName()+" 导出故障申报列表--全部数据");
		}
		try {
			ExcelUtil.exportExcel(title, headers,column, res, data, date.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException
				| IOException e) {
			e.printStackTrace();
		}
		log.info("<TblCustomerComplainController>----<export>------end");
		return returnDataUtil;
	}
	/**
	 * 处理机器门已关，但是心跳显示门未关时不发短信给用户，禁止向该机器的用户发送短信
	 * @author hebiting
	 * @date 2019年3月20日上午8:51:19
	 * @param form
	 * @return
	 */
	@PostMapping("/resolveMsg")
	public ReturnDataUtil resolveMsg(@RequestBody WayInductionForm form){
		boolean result = false;
		if(form != null){
			if(StringUtils.isNotBlank(form.getPhone())){
				result = true;
				redisClient.set("sendMsgInterval"+form.getPhone(), "1", 2592000);
				if(("1").equals(form.getInductionBad())){
					BadOpenLogBean byPhone = machinesBadOpenService.getByPhone(form.getPhone());
					if(byPhone!=null){
						redisClient.set("doorInduction"+byPhone.getVmCode(), "1");
					}
				}
			}
			if(result)
				return ResultUtil.success();
		}
		return ResultUtil.error();
	}
	@GetMapping("/getBadInducVMByPhone")
	public ReturnDataUtil getBadInductionVmCode(String phone){
		BadOpenLogBean byPhone = machinesBadOpenService.getByPhone(phone);
		if(byPhone == null){
			return ResultUtil.error();
		}
		return ResultUtil.success(byPhone.getVmCode());
	}
	/**
	 * 获取心跳中货道是否开启异常的机器编码
	 * @author hebiting
	 * @date 2019年3月20日上午8:54:22
	 * @return
	 */
	@GetMapping("/getBadInduction")
	public ReturnDataUtil getBadInductionVmCode(){
		Set<String> keys = redisClient.keys("doorInduction*");
		Set<String> vmCodes = new HashSet<String>();
		for(String key : keys){
			vmCodes.add(key.substring(13, key.length()));
		}
		return ResultUtil.success(vmCodes);
	}
	/**
	 * 若已修复该机器货道开启关闭显示异常，则删除
	 * @author hebiting
	 * @date 2019年3月20日上午8:54:57
	 * @param vmCode
	 * @return
	 */
	@GetMapping("/removeBadInduction")
	public ReturnDataUtil removeBadInduction(String vmCode){
		redisClient.del("doorInduction"+vmCode);
		return ResultUtil.success();
	}
	@ApiOperation(value = "用户投诉添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody TblCustomerComplainBean entity) {
		log.info("<CustomerMessageController>----<add>------start");
		Integer count = tblCustomerComplainServiceImpl.findComplaintsNumberById();
		if(count<=10){
			TblCustomerComplainBean tblCustomerComplainBean = tblCustomerComplainServiceImpl.add(entity);
			if (tblCustomerComplainBean != null) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("故障申报成功！");
			} else {
				returnDataUtil.setStatus(0);
				returnDataUtil.setMessage("故障申报失败！");
			}
		}else{
			returnDataUtil.setStatus(-99);
			returnDataUtil.setMessage("亲，您已经提问10次了，不可以继续提问了哟！如果还有问题，请电话联系客服，谢谢！");
		}
		log.info("<CustomerMessageController>----<add>------end");
		return returnDataUtil;
	}
}
