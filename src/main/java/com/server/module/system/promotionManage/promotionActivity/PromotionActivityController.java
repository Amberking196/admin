package com.server.module.system.promotionManage.promotionActivity;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
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
import com.server.module.system.machineManage.machineList.VendingMachinesInfoBean;
import com.server.module.system.machineManage.machineList.VendingMachinesInfoService;
import com.server.module.system.promotionManage.timeQuantum.TimeQuantumBean;
import com.server.module.system.promotionManage.timeQuantum.TimeQuantumService;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.UUIDUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-08-22 16:51:38
 */
@Api(value = "PromotionActivityController", description = "促销活动管理")
@RestController
@RequestMapping("/promotionActivity")
public class PromotionActivityController {

	private static Logger log = LogManager.getLogger(PromotionActivityController.class);
	@Autowired
	private PromotionActivityService promotionActivityServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoService;
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private TimeQuantumService timeQuantumServiceImpl;

	@ApiOperation(value = "促销活动列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required = false) PromotionActivityForm promotionActivityForm) {
		log.info("<PromotionActivityController>----<listPage>------start");
		if (promotionActivityForm == null) {
			promotionActivityForm = new PromotionActivityForm();
		}
		returnDataUtil = promotionActivityServiceImpl.listPage(promotionActivityForm);
		log.info("<PromotionActivityController>----<listPage>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "促销活动添加", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody PromotionActivityVo vo) {
		log.info("<PromotionActivityController>----<add>------start");
		PromotionActivityBean entity = new PromotionActivityBean();
		//属性复制 将A复制给B
		BeanUtils.copyProperties(vo, entity);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String temp = sf.format(entity.getEndTime());
		DateTime dt1 = new DateTime(entity.getEndTime().getTime());
		dt1 = dt1.withHourOfDay(23);
		dt1 = dt1.withMinuteOfHour(59);
		dt1 = dt1.withSecondOfMinute(59);
		entity.setEndTime(dt1.toDate());

		DateTime dt2 = new DateTime(entity.getStartTime().getTime());
		dt2 = dt2.withHourOfDay(0);
		dt2 = dt2.withMinuteOfHour(0);
		dt2 = dt2.withSecondOfMinute(0);
		entity.setStartTime(dt2.toDate());

		if (entity.getVmCode() != null && StringUtil.isNotBlank(entity.getVmCode())) {
			VendingMachinesInfoBean bean = vendingMachinesInfoService.findVendingMachinesByCode(entity.getVmCode());
			if (bean == null) {
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("不存在的机器编码或售货机被禁用");
				return returnDataUtil;
			}
		}
		Long userId = UserUtils.getUser().getId();
		entity.setCreateUser(userId);
		PromotionActivityBean promotionActivityBean = promotionActivityServiceImpl.add(entity);
		if (promotionActivityBean != null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("活动增加成功！");
			returnDataUtil.setReturnObject(promotionActivityBean);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("活动增加失败！");
			returnDataUtil.setReturnObject(promotionActivityBean);
		}
		log.info("<PromotionActivityController>----<add>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "促销活动修改", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody PromotionActivityBean entity) {
		log.info("<PromotionActivityController>----<update>------start");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String temp = sf.format(entity.getEndTime());
		DateTime dt1 = new DateTime(entity.getEndTime().getTime());
		dt1 = dt1.withHourOfDay(23);
		dt1 = dt1.withMinuteOfHour(59);
		dt1 = dt1.withSecondOfMinute(59);
		entity.setEndTime(dt1.toDate());

		DateTime dt2 = new DateTime(entity.getStartTime().getTime());
		dt2 = dt2.withHourOfDay(0);
		dt2 = dt2.withMinuteOfHour(0);
		dt2 = dt2.withSecondOfMinute(0);
		entity.setStartTime(dt2.toDate());

		if (entity.getVmCode() != null && StringUtil.isNotBlank(entity.getVmCode())) {
			VendingMachinesInfoBean bean = vendingMachinesInfoService.findVendingMachinesByCode(entity.getVmCode());
			if (bean == null) {
				returnDataUtil.setStatus(-99);
				returnDataUtil.setMessage("不存在的机器编码或售货机被禁用");
				return returnDataUtil;
			}
		}
		Long userId = UserUtils.getUser().getId();
		entity.setUpdateUser(userId);
		boolean update = promotionActivityServiceImpl.update(entity);
		if (update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("活动修改成功！");
			returnDataUtil.setReturnObject(update);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("活动修改失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<PromotionActivityController>----<update>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "促销活动删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(Long id) {
		log.info("<PromotionActivityController>----<del>------start");
		boolean flag = promotionActivityServiceImpl.del(id);
		if (flag) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("活动删除成功！");
			returnDataUtil.setReturnObject(flag);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("活动删除失败！");
			returnDataUtil.setReturnObject(flag);
		}
		log.info("<PromotionActivityController>----<del>------end");
		return returnDataUtil;
	}

	

	/**
	 * 上传图片
	 * 
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<PromotionActivityController>--<uploadImage>--start");
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 设置图片的名称
		String uuid = UUIDUtil.getUUID().substring(19, 31);
		// 得到上传图片的后缀
		String substring = filePath.substring(filePath.indexOf("."), filePath.length());
		// 生成图片名称
		String imgName = uuid + substring;
		// 这是t图片文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getActivityImg() + imgName;
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			return imgName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return imgName;
		} finally {
			log.info("<PromotionActivityController>--<uploadImage>--end");

		}
	}
	
	@ApiOperation(value = "促销活动详情", notes = "detail", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil detail(String ids) {
		log.info("<PromotionActivityController>----<detail>------start");
		List<TimeQuantumBean> list = timeQuantumServiceImpl.list(ids);
		if (list!=null && list.size()>0) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("活动详情查看成功！");
			returnDataUtil.setReturnObject(list);
		} else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("活动详情查看失败！");
			returnDataUtil.setReturnObject(list);
		}
		log.info("<PromotionActivityController>----<detail>------end");
		return returnDataUtil;
	}
}
