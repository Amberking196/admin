package com.server.module.system.itemManage.itemBasic;
 
 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.server.common.paramconfig.AlipayConfig;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.logsManager.operationLog.HeadquartersConfigLogBean;
import com.server.module.system.logsManager.operationLog.HeadquartersConfigService;
import com.server.module.system.logsManager.operationLog.OperationLogService;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesBean;
import com.server.module.system.machineManage.advertisingBindMachine.VendingAdvertisingMachinesCondition;
import com.server.module.system.machineManage.machinesAdvertising.VendingMachinesAdvertisingBean;
import com.server.util.DateUtil;
import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * author name: why create time: 2018-04-10 14:22:54
 */
@Api(value = "ItemBasicController", description = "商品基本信息")
@RestController
@RequestMapping("/itemBasic")
public class ItemBasicController {
 
	public static Logger log = LogManager.getLogger(ItemBasicController.class); 	    
	@Autowired
	private ItemBasicService itemBasicServiceImpl;
	@Autowired
	private HeadquartersConfigService headquartersConfigServiceImpl;
	@Autowired
	private AdminUserService adminUserServiceImpl;

    @Autowired
    private ReturnDataUtil returnDataUtil;
    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
	private CompanyService companyService;

	/**
	 * 
	 * @param condition
	 * @return
	 */
	@ApiOperation(value = "商品基础信息列表", notes = "商品基础信息列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil listPage(ItemBasicCondition condition) {
		log.info("<ItemBasicController>--<listPage>--start");  
		returnDataUtil =itemBasicServiceImpl.listPage(condition);
		log.info("<ItemBasicController>--<listPage>--end");  
		return returnDataUtil;
	}

	@ApiOperation(value = "添加商品", notes = "添加商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil add(@RequestBody(required=false) ItemBasicBean entity, HttpServletRequest request) throws IOException {
		log.info("<ItemBasicController>--<add>--start");  
		if(entity==null) {
			entity=new ItemBasicBean();
		}
		//添加判断，看商品类型是否合法
//		if(entity.getTypeId()==0) {//非法参数
//			returnDataUtil.setMessage("添加失败，请选择合理的商品类型");
//			returnDataUtil.setStatus(-1);
//			return returnDataUtil;
//		}
		entity.setCompanyId(entity.getCompanyId());
		CompanyBean companyBean=companyService.findCompanyById(entity.getCompanyId().intValue());
		entity.setCompanyName(companyBean.getName());

		ItemBasicBean bean = itemBasicServiceImpl.checkBarcode(entity.getBarCode());
		if (bean.getBarCode() != null) {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该条形码已近存在！"); 
		} else {
			Long id = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			entity.setCreateTime(new Date());
			entity.setLoginInfoId(id);
			ItemBasicBean insert = itemBasicServiceImpl.insert(entity);
			if(insert != null) {
				// 得到当前用户Id 查询出当前用户信息
				Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
				ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
				AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
				HeadquartersConfigLogBean logBean = new HeadquartersConfigLogBean();
				logBean.setCompanyId(userBean.getCompanyId());
				logBean.setUserName(userBean.getName());
				logBean.setContent("增加商品信息");
				logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
				// 增加总部操作日志
				headquartersConfigServiceImpl.insert(logBean);
				returnDataUtil= new ReturnDataUtil(insert);
			}
		} 
		log.info("<ItemBasicController>--<add>--end");  
		return returnDataUtil; 
 
	}

	@ApiOperation(value = "查询单个商品", notes = "查询单个商品", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getItemBasic", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getItemBasic(Long id) {
		log.info("<ItemBasicController>--<getItemBasic>--start");  
		returnDataUtil=new ReturnDataUtil(itemBasicServiceImpl.getItemBasic(id));
		log.info("<ItemBasicController>--<getItemBasic>--end");  
		return returnDataUtil;
	}

	@ApiOperation(value = "编辑商品", notes = "编辑商品之后的保存按钮", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateItemBasic", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateItemBasic(@RequestBody ItemBasicBean entity,HttpServletRequest request) {
		log.info("<ItemBasicController>--<updateItemBasic>--start");  
		//添加判断，看商品类型是否合法
//		if(entity.getTypeId()==0) {//非法参数
//			returnDataUtil.setMessage("添加失败，请选择合理的商品类型");
//			returnDataUtil.setStatus(-1);
//			return returnDataUtil;
//		}

		boolean updateEntity = itemBasicServiceImpl.updateEntity(entity);
		if(updateEntity) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			HeadquartersConfigLogBean logBean = new HeadquartersConfigLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setContent("修改商品信息");
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加总部操作日志
			headquartersConfigServiceImpl.insert(logBean);
			returnDataUtil= new ReturnDataUtil(updateEntity);
		}
		log.info("<ItemBasicController>--<updateItemBasic>--end");  
		return returnDataUtil;
	}

	@ApiOperation(value = "商品导出", notes = "商品导出")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String exportFile(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, ItemBasicCondition condition) { 
		log.info("<ItemBasicController>--<exportFile>--start");  
		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(condition.getStartDate()!=null&&condition.getEndDate()!=null) {
				//导出日志内容时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(condition.getStartDate())+"--"+DateUtil.formatYYYYMMDD(condition.getEndDate())+"的商品基础信息列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出商品基础信息列表--当前页数据");
				
			}
			String fileName = "商品基础信息列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			ReturnDataUtil data = itemBasicServiceImpl.listPage(condition);
			new ExportExcel("商品基础信息列表", ItemBasicDto.class).setDataList((List) data.getReturnObject())
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			log.info("<ItemBasicController>--<exportFile>--end");  
		}
		return null;
	}

	@ApiOperation(value = "全部商品导出", notes = "全部商品导出")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportAll", method = RequestMethod.GET)
	public String exportFileAll(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, ItemBasicCondition condition) {
		log.info("<ItemBasicController>--<exportFileAll>--start");  
		try {
			condition.setPageSize(100000);
			String fileName = "全部商品基础信息列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			ReturnDataUtil data = itemBasicServiceImpl.listPage(condition);
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: "+bean.getOperatorName()+" 导出商品基础信息列表--全部数据");
			new ExportExcel("全部商品基础信息列表", ItemBasicDto.class).setDataList((List) data.getReturnObject())
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			log.info("<ItemBasicController>--<exportFileAll>--end");  
		}
		return null;
	}

	@ApiOperation(value = "我的商品库查询", notes = "商品库查询（我的，基础库，全部）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listItem", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listItem(MyItemCondition condition) {
		log.info("<ItemBasicController>--<listItem>--start");  
		returnDataUtil=new ReturnDataUtil(itemBasicServiceImpl.myItemList(condition));
		log.info("<ItemBasicController>--<listItem>--end");  
		return returnDataUtil;
	}

	/**
	 * 后台操作上传图片
	 * 
	 * @param file
	 * @return 上传成功信息
	 *
	 */
	@ApiOperation(value = "图片上传", notes = "图片上传")
	@PostMapping(value = "/uploadImage")
	public String uploadImage(@RequestParam("uploadedfile") MultipartFile file) {
		log.info("<ItemBasicController>--<uploadImage>--start");   
		String filePath = file.getOriginalFilename(); // 获取文件的名称 
		String imgName = itemBasicServiceImpl.findImgName(filePath);
		// 获取图片保存文件夹地址
		filePath =alipayConfig.getItemImg().trim() + imgName; 
		log.info("<filePath>----"+filePath);
		try {
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
			outputStream.write(file.getBytes());
			outputStream.flush();
			outputStream.close();
			return  imgName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			log.info("<ItemBasicController>--<uploadImage>--end");  
		}
	}

	// 文件下载相关代码
	@ApiOperation(value = "图片下载", notes = "图片下载")
	@PostMapping(value = "/downloadImage")
	public String downloadImage(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		List<String> pic = itemBasicServiceImpl.getPic();
		for (String string : pic) {

			String urlString = "http://lenvar-resource-products.oss-cn-shenzhen.aliyuncs.com/" + string;
			// 构造URL
			URL url = new URL(urlString);
			// 打开连接
			URLConnection con = url.openConnection();
			// 设置请求超时为5s
			con.setConnectTimeout(5 * 1000);
			// 输入流
			InputStream is = con.getInputStream();

			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			File sf = new File("C:\\files\\ys");
			if (!sf.exists()) {
				sf.mkdirs();
			}
			OutputStream os = new FileOutputStream(sf.getPath() + "\\" + string);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
		}
		return null;
	}

	/**
	 * 根据条形码 模糊查询
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "条形码模糊查询", notes = "条形码模糊查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findItemBasic", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findItemBasic(@RequestBody ItemBasicCondition condition) {
		log.info("<ItemBasicController>--<findItemBasic>--start");  
		ReturnDataUtil data=new ReturnDataUtil();
		List<ItemBasicBean> findItemBasic = itemBasicServiceImpl.findItemBasic(condition);
		if(findItemBasic.size()>0) {
			data.setReturnObject(findItemBasic);
		}else{
			data.setReturnObject(null);
			data.setStatus(0);
			data.setMessage("该仓库不存在该商品");
		}
		log.info("<ItemBasicController>--<findItemBasic>--end");  
		return data;
	}
	/**
	 * 根据条形码 从商品基础信息中模糊查询
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "条形码模糊查询", notes = "条形码模糊查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findItemBasicByCode", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil findItemBasicByCode(@RequestBody(required=false) ItemBasicCondition condition) {
		log.info("<ItemBasicController>--<findItemBasic>--start"); 
		ReturnDataUtil data=new ReturnDataUtil();
		List<ItemBasicBean> findItemBasic = itemBasicServiceImpl.findItemBasicByCode(condition);
		if(findItemBasic.size()>0) {
			data.setReturnObject(findItemBasic);
		}else{
			data.setReturnObject(null);
			data.setStatus(0);
			data.setMessage("该商品条形码不存在，请输入合法的条形码");
		}
		log.info("<ItemBasicController>--<findItemBasic>--end");  
		return data;
	}

    /**
     * 商品列表 下拉框用  id,name
     * @return
     */
	@GetMapping(value = "/listAllItem", produces = "application/json;charset=UTF-8")
	public List<Map<String,Object>> listAllItemForSelect(){
		return itemBasicServiceImpl.listAllItem();
	}
	/**
	 * 根据条形码 模糊查询
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "商品名称模糊查询", notes = "商品名称模糊查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getItemBasic", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getItemBasic(@RequestBody ItemBasicCondition itemcondition) {
		log.info("<ItemBasicController>--<getItemBasic>--start");  
		ReturnDataUtil data=new ReturnDataUtil();
		List<ItemBasicBean> findItemBasic = itemBasicServiceImpl.getItemBasic(itemcondition);
		if(findItemBasic.size()>0) {
			data.setReturnObject(findItemBasic);
		}else{
			data.setReturnObject(null);
			data.setStatus(0);
			data.setMessage("该仓库不存在该商品");
		}
		log.info("<ItemBasicController>--<getItemBasic>--end");  
		return data;
	}
	
	@ApiOperation(value = "近似商品查询", notes = "近似商品查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/sameItemList", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil sameItemList(@RequestBody ItemBasicCondition itemcondition) {
		log.info("<ItemBasicController>--<sameItemList>--start");  
		ReturnDataUtil data=new ReturnDataUtil();
		List<ItemBasicBean> findItemBasic = itemBasicServiceImpl.getItemConnect(itemcondition);
		if(findItemBasic.size()>0) {
			data.setReturnObject(findItemBasic);
		}else{
			data.setReturnObject(null);
			data.setStatus(0);
			data.setMessage("该仓库不存在该商品");
		}
		log.info("<ItemBasicController>--<sameItemList>--end");  
		return data;
	}
	
	@ApiOperation(value = "绑定近似商品", notes = "add", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/addSameItem", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil add(Integer id,Integer[] ids) {
        ReturnDataUtil rd=new ReturnDataUtil();
        ItemBasicCondition condition=new ItemBasicCondition();
        condition.setIsBind(0);
        condition.setId(id);
        
        //List<VendingAdvertisingMachinesBean> list=vendingAdvertisingMachinesServiceImpl.list(condition);
		List<ItemBasicBean> list = itemBasicServiceImpl.getItemConnect(condition);

        List<Integer> codeList= Lists.newArrayList();
        System.out.println("list=="+list.size());
        for (Integer code : ids) {
            boolean flag=false;
            for (ItemBasicBean ib : list) {
                if(code.equals(ib.getId().intValue())){
                    flag=true;
                    break;
                }
            }
            if(flag){
                codeList.add(code);
            }
        }
        System.out.println("codeList="+codeList.size());
        itemBasicServiceImpl.addAll(id,codeList);
       
        return rd;
    }
	
    @ApiOperation(value = "相似商品绑定解除", notes = "delAll", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping(value = "/delSameItem", produces = "application/json;charset=UTF-8")
    public ReturnDataUtil delAll(Integer id,Integer[] ids) {
        ReturnDataUtil rd=new ReturnDataUtil();
        if(ids.length==0)
            return rd;
        //Long id=ids[0];
        //Long advertisingId=itemBasicServiceImpl.get(id).getAdvertisingId();
        List<Integer> codeList= Lists.newArrayList(ids);
        itemBasicServiceImpl.delAll(id,codeList);
        return rd;
    }
}
