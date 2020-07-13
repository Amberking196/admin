package com.server.module.system.officialManage.officialArticle;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.common.paramconfig.AlipayConfig;
import com.server.module.customer.product.ShoppingGoodsServiceImpl;
import com.server.module.sys.utils.UserUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.UUIDUtil;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-08-01 15:07:48
 */
@Api(value = "OfficialArticleController", description = "文章列表")
@RestController
@RequestMapping("/officialArticle")
public class OfficialArticleController {

	private static Logger log=LogManager.getLogger(OfficialArticleController.class);
	
	@Autowired
	private OfficialArticleService officialArticleServiceImpl;
	@Autowired
	private ReturnDataUtil returnDataUtil;
	@Autowired
	private AlipayConfig alipayConfig;

	/**
	 * 文章列表查询
	 * @param officialArticleForm
	 * @return
	 */
	@ApiOperation(value = "文章列表", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(@RequestBody(required=false) OfficialArticleForm officialArticleForm) {
		log.info("<OfficialArticleController>----<listPage>------start");
		if(officialArticleForm==null) {
			officialArticleForm=new OfficialArticleForm();
		}
		returnDataUtil=officialArticleServiceImpl.listPage(officialArticleForm);
		log.info("<OfficialArticleController>----<listPage>------end");
		return returnDataUtil;
	}

	/**
	 * 机器信息列表查询
	 * @param officialArticleForm
	 * @return
	 */
	@ApiOperation(value = "机器信息列表", notes = "machinelistPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/machineListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil machinelistPage(@RequestBody(required=false) OfficialArticleForm officialArticleForm) {
		log.info("<OfficialArticleController>----<machinelistPage>------start");
		if(officialArticleForm==null) {
			officialArticleForm=new OfficialArticleForm();
		}
		returnDataUtil=officialArticleServiceImpl.machineListPage(officialArticleForm);
		log.info("<OfficialArticleController>----<machinelistPage>------end");
		return returnDataUtil;
	}

	/**
	 * 文章发布
	 * @param entity
	 * @return
	 */
	@ApiOperation(value = "发布文章", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody OfficialArticleBean entity) {
		log.info("<OfficialArticleController>----<add>------start");
		entity.setCreateUser(UserUtils.getOffUser().getId());
		OfficialArticleBean officialArticleBean = officialArticleServiceImpl.add(entity);
		if(officialArticleBean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("文章发布成功！");
			returnDataUtil.setReturnObject(officialArticleBean);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("文章发布失败！");
			returnDataUtil.setReturnObject(officialArticleBean);
		}
		log.info("<OfficialArticleController>----<add>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "文章编辑", notes = "update", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil update(@RequestBody OfficialArticleBean entity) {
		log.info("<OfficialArticleController>----<update>------start");
		entity.setUpdateUser(UserUtils.getOffUser().getId());
		boolean update = officialArticleServiceImpl.update(entity);
		if(update) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("文章编辑成功！");
			returnDataUtil.setReturnObject(update);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("文章编辑失败！");
			returnDataUtil.setReturnObject(update);
		}
		log.info("<OfficialArticleController>----<update>------end");
		return returnDataUtil;
	}

	@ApiOperation(value = "文章删除", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/del", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil del(@RequestBody Map<String,Integer> ids) {
		log.info("<OfficialArticleController>----<del>------start");
		boolean del = officialArticleServiceImpl.del(ids.get("id"));
		if(del) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("文章删除成功！");
			returnDataUtil.setReturnObject(del);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("文章删除失败！");
			returnDataUtil.setReturnObject(del);
		}
		log.info("<OfficialArticleController>----<del>------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "文章预览", notes = "del", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/preview", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil preview(@RequestBody Map<String,Integer> ids) {
		log.info("<OfficialArticleController>----<preview>------start");
		 OfficialArticleBean officialArticleBean = officialArticleServiceImpl.get(ids.get("id"));
		if(officialArticleBean!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("文章预览成功！");
			returnDataUtil.setReturnObject(officialArticleBean);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("文章预览失败！");
			returnDataUtil.setReturnObject(officialArticleBean);
		}
		log.info("<OfficialArticleController>----<preview>------end");
		return returnDataUtil;
	}
	
	/**
	 * 上传图片
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<OfficialArticleController>--<uploadImage>--start");
		// 获取文件的名称
		String filePath = file.getOriginalFilename();
		// 设置图片的名称
		String uuid = UUIDUtil.getUUID().substring(19, 31);
		//得到上传图片的后缀
		String substring = filePath.substring(filePath.length()-4, filePath.length());
		//生成图片名称
		String imgName=uuid+substring;
		// 这是文件的保存路径，如果不设置就会保存到项目的根目录
		filePath = alipayConfig.getArticleImg() + imgName;
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
			log.info("<OfficialArticleController>--<uploadImage>--end");

		}
	}
	
	/**
	 * 官网文章查询
	 * @param officialArticleForm
	 * @return
	 */
	@ApiOperation(value = "官网文章查询", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/officialList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil officialList(@RequestBody(required=false) OfficialArticleForm officialArticleForm) {
		log.info("<OfficialArticleController>----<officialList>------start");
		if(officialArticleForm==null) {
			officialArticleForm=new OfficialArticleForm();
		}
		returnDataUtil=officialArticleServiceImpl.listPage(officialArticleForm);
		log.info("<OfficialArticleController>----<officialList>------end");
		return returnDataUtil;
	}
	
	/**
	 * 官网首页文章查询
	 * @param officialArticleForm
	 * @return
	 */
	@ApiOperation(value = "官网首页文章查询", notes = "listPage", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/officialHomeList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil officialHomeList(@RequestBody Map<String, Integer> ids) {
		log.info("<OfficialArticleController>----<officialHomeList>------start");
		OfficialArticleForm officialArticleForm=new OfficialArticleForm();
		officialArticleForm.setSid(ids.get("sid"));
		returnDataUtil=officialArticleServiceImpl.listPage(officialArticleForm);
		log.info("<OfficialArticleController>----<officialHomeList>------end");
		return returnDataUtil;
	}
	
	@ApiOperation(value = "官网首页文章查询", notes = "findDto", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findDto", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findDto(Long id) {
		log.info("<OfficialArticleController>----<findDto>------start");
		OfficialArticleDto officialArticleDto = officialArticleServiceImpl.findDto(id);
		if(officialArticleDto!=null) {
			returnDataUtil.setStatus(1);
			returnDataUtil.setMessage("文章详情查看成功！");
			returnDataUtil.setReturnObject(officialArticleDto);
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("文章详情查看失败！");
			returnDataUtil.setReturnObject(officialArticleDto);
		}
		log.info("<OfficialArticleController>----<findDto>------end");
		return returnDataUtil;
	}
}
