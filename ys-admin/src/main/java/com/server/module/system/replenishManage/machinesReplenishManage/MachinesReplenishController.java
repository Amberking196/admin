package com.server.module.system.replenishManage.machinesReplenishManage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.lly835.bestpay.utils.JsonUtil;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.sys.model.User;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.machineManage.machineReplenish.ReplenishDetailVo1;
import com.server.module.system.machineManage.machinesWay.StatisticsWayNumVo;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaDto;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaForm;
import com.server.module.system.synthesizeManage.vendingAreaManage.VendingAreaService;
import com.server.module.system.synthesizeManage.vendingLineManage.CompanyAreaUserDto;
import com.server.module.system.synthesizeManage.vendingLineManage.VendingLineBean;
import com.server.module.system.synthesizeManage.vendingLineManage.VendingLineService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.server.util.DateUtils;
import com.server.util.ReturnDataUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * author name: why create time: 2018-04-23 16:19:47
 */
@Api(value = "MachinesReplenishController", description = "补货管理")
@RestController
@RequestMapping("/machinesReplenish")
public class MachinesReplenishController {

	public static Logger log = LogManager.getLogger(MachinesReplenishController.class);
	
	@Autowired
	private MachinesReplenishService machinesReplenishServiceImpl;

	@Autowired
	private AdminUserService adminUserService; 
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private VendingAreaService vendingAreaService;
	@Autowired
	private VendingLineService vendingLineService;

	@ApiOperation(value = "补货管理列表", notes = "listPage", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(MachinesReplenishCondition condition,HttpServletRequest request) {
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}
		
		return machinesReplenishServiceImpl.listPage(condition);
	}
	@ApiOperation(value = "补货管理列表", notes = "export", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
	public void export(MachinesReplenishCondition condition,HttpServletResponse response,HttpServletRequest request) {
		condition.setPageSize(100);
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}		
		ReturnDataUtil data= machinesReplenishServiceImpl.listPage(condition);
		List<MachinesReplenishBean> list=(List<MachinesReplenishBean>)data.getReturnObject();
		List<String> listCode=Lists.newArrayList();
		
		for (MachinesReplenishBean bean : list) {
			listCode.add(bean.getCode());
		}
		
		List<ItemNumVo> listItem=machinesReplenishServiceImpl.getItemsByCode(listCode,condition.getVersion());
		System.out.println(listItem.size()+"    size=======");
		List<MachinesReplenishBean> listExport=Lists.newArrayList();
		
		for(int i=0;i<listItem.size();i++){
			MachinesReplenishBean vo=new MachinesReplenishBean();
			ItemNumVo numVo=listItem.get(i);
			MachinesReplenishBean last=null;
			System.out.println(" i="+i);
			if(listExport.size()>0){
				 //last=listExport.get(listExport.size()-1);
				 boolean notIn=true;
				 for(int k=listExport.size()-1;k>=0;k--){
					 System.out.println(" k="+k);
					 last=listExport.get(k);
					 if(last.getCode()!=null && last.getCode().equals(numVo.getVmCode())){
						 //找到了
						 //BeanUtils.copyProperties(last, vo);
						 //vo.setItemNums(numVo.getItemName()+" "+numVo.getNum()+"/"+numVo.getFullNum()+" "+numVo.getRate()+"%");
						 vo.setFullNum(numVo.getFullNum());
						vo.setNum(numVo.getNum());
						vo.setBuNum(vo.getFullNum()-vo.getNum());
						//vo.setRate(numVo.getRate()+"%");
						 
						 listExport.add(vo);
						 notIn=false;
						 break;
					 }
				 }
				 if(notIn){
					 for(int j=0;j<list.size();j++){
						 System.out.println("j="+j);
						if(numVo.getVmCode().equals(list.get(j).getCode())){
							MachinesReplenishBean temp=list.get(j);
							BeanUtils.copyProperties(temp, vo);

							vo.setFullNum(numVo.getFullNum());
							vo.setNum(numVo.getNum());
							vo.setBuNum(vo.getFullNum()-vo.getNum());

							//vo.setRate(numVo.getRate()+"%");
							//vo.setTempCode(temp.getCode());
							//vo.setItemNums(numVo.getItemName()+" "+numVo.getNum()+"/"+numVo.getFullNum()+" "+numVo.getRate()+"%");
						}
					}
					listExport.add(vo);
				 }
				 
			} else {
				
				for(int j=0;j<list.size();j++){
					 System.out.println("j="+j);
					if(numVo.getVmCode().equals(list.get(j).getCode())){
						MachinesReplenishBean temp=list.get(j);
						BeanUtils.copyProperties(temp, vo);

						vo.setFullNum(numVo.getFullNum());
						vo.setNum(numVo.getNum());
						vo.setBuNum(vo.getFullNum()-vo.getNum());

						//vo.setRate(numVo.getRate()+"%");
						//vo.setTempCode(temp.getCode());
						//vo.setItemNums(numVo.getItemName()+" "+numVo.getNum()+"/"+numVo.getFullNum()+" "+numVo.getRate()+"%");
					}
				}
				listExport.add(vo);
			}
			
			
			
		}
		/*for (MachinesReplenishBean bean : list) {
			
			String code=bean.getCode();
			String itemNums="";
			for(int i=0;i<listItem.size();i++){
				ItemNumVo vo=listItem.get(i);
				if(code.equals(vo.getVmCode())){
					itemNums=itemNums+vo.getItemName()+" "+vo.getNum()+"/"+vo.getFullNum()+" "+vo.getRate()+"% ||";
				}
				
			}
			bean.setItemNums(itemNums);
		}*/
		
		try {
			String fileName = "补货统计统计" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			new ExportExcel("补货统计统计", MachinesReplenishBean.class).setDataList(listExport)
					.write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@ApiOperation(value = "补货管理报表导出", notes = "export1", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/export1", produces = "application/json;charset=UTF-8")
	public void export1(MachinesReplenishCondition condition,HttpServletResponse response,HttpServletRequest request) {
		condition.setPageSize(1000);

		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}
		ReturnDataUtil data= machinesReplenishServiceImpl.listPage(condition);
		System.out.println("other=="+condition.getOtherCompanyId()+"   version="+condition.getVersion());
		List<MachinesReplenishBean> list=(List<MachinesReplenishBean>)data.getReturnObject();
		List<String> listCode=Lists.newArrayList();
		
		for (MachinesReplenishBean bean : list) {
			listCode.add(bean.getCode());
		}
		
		List<ItemNumVo> listItem=machinesReplenishServiceImpl.getItemsByCode(listCode,condition.getVersion());
		for (MachinesReplenishBean bean : list) {
			for(int i=0;i<listItem.size();i++){
				ItemNumVo vo=listItem.get(i);
				if(bean.getCode().equals(vo.getVmCode())){
					bean.getListVo().add(vo);
				}
				
			}
		}

		try {
			//添加导出日志的内容
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			bean.setContent("用户: "+bean.getOperatorName()+" 导出补货管理--全部数据");
			String fileName = "需补货统计" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			new ExportExcel("需补货统计", MachinesReplenishBean.class).setDataList1(list)
					.write(response, fileName).dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@ApiOperation(value = "补货管理打印", notes = "print", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/print", produces = "application/json;charset=UTF-8")
	public List<ReplenishDetailVo1> print(MachinesReplenishCondition condition,HttpServletRequest request) {
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}
		ReturnDataUtil returnData = machinesReplenishServiceImpl.listPage(condition);

		List<MachinesReplenishBean> list = (List<MachinesReplenishBean>) returnData.getReturnObject();
        
		List<ReplenishDetailVo1> listVo = machinesReplenishServiceImpl.genReplenishDetail(list,condition.getVersion());
		// returnData.setReturnObject(listVo);

		return listVo;
	}

	@ApiOperation(value = "勾选某个售货机打印", notes = "printVM", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/printDetailVm", produces = "application/json;charset=UTF-8")
	public List<ReplenishDetailVo1> printDetailVm(String[] codes,int version ) {
		List<MachinesReplenishBean> returnData = machinesReplenishServiceImpl.listDetailVm(codes,version);

		//List<MachinesReplenishBean> list = (List<MachinesReplenishBean>) returnData.getReturnObject();
		List<ReplenishDetailVo1> listVo = machinesReplenishServiceImpl.genReplenishDetail(returnData,version);
		// returnData.setReturnObject(listVo);

		return listVo;
	}

	@ApiOperation(value = "补货管理打印All", notes = "printAll", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/printAll",produces = "application/json;charset=UTF-8")
	public List<ReplenishDetailVo1> printAll(MachinesReplenishCondition condition,HttpServletRequest request) {
		condition.setPageSize(1000);		
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		if(user!=null && user.getAreaId()>0) {
			condition.setAreaId(user.getAreaId());
		}
		ReturnDataUtil returnData = machinesReplenishServiceImpl.listPage(condition);

		List<MachinesReplenishBean> list = (List<MachinesReplenishBean>) returnData.getReturnObject();

		List<ReplenishDetailVo1> listVo = machinesReplenishServiceImpl.genReplenishDetail(list,condition.getVersion());
		// returnData.setReturnObject(listVo);

		return listVo;
	}

	@ApiOperation(value = "详情查询", notes = "getDetails", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getDetails", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getDetails(String code,int version) {

		return new ReturnDataUtil(machinesReplenishServiceImpl.getDetails(code,version));
	}

	@ApiOperation(value = "补货预览", notes = "getPreview", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getPreview", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil getPreview(String lineId, String code,int version) {
		return new ReturnDataUtil(machinesReplenishServiceImpl.getPreview(lineId, code,version));
	}

	@ApiOperation(value = "判断用户是否是总部或者线路负责人", notes = "judgeAuthority", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/judgeAuthority", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil judgeAuthority() {
		log.info("<machinesReplenish>----<judgeAuthority>-----start");
		ReturnDataUtil returnDataUtil = new ReturnDataUtil();
		//拿到登录用户信息
		User user = UserUtils.getUser();
		ReturnDataUtil dataUtil = adminUserService.findUserById(user.getId());
		AdminUserBean adminUserBean = (AdminUserBean) dataUtil.getReturnObject();
		//log.info("123324----"+JsonUtil.toJson(adminUserBean));
		if(adminUserBean.getLevel()!=null&&adminUserBean.getLevel()>0) {
			
			//log.info("1232132312222222324----"+adminUserBean.getLevel());
			//公司级
			if(adminUserBean.getLevel()==1) {
				returnDataUtil.setStatus(1);
				returnDataUtil.setMessage("公司等级 ");
				returnDataUtil.setReturnObject(adminUserBean);
			}else if(adminUserBean.getLevel()==2) {
				returnDataUtil.setStatus(2);
				returnDataUtil.setMessage("区域等级");
				returnDataUtil.setReturnObject(adminUserBean);
			}else if(adminUserBean.getLevel()==3) {
				returnDataUtil.setStatus(3);
				returnDataUtil.setMessage("线路等级");
				returnDataUtil.setReturnObject(adminUserBean);
			}
		}else {
			returnDataUtil.setStatus(0);
			returnDataUtil.setMessage("该用户没有设置权限等级，请先设置！");
			returnDataUtil.setReturnObject(adminUserBean);
		}
		log.info("<machinesReplenish>----<judgeAuthority>-----end");
		return returnDataUtil;
	}

}
