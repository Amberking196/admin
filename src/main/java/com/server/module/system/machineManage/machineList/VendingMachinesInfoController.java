package com.server.module.system.machineManage.machineList;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.server.common.persistence.BaseDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.analysis.function.Abs;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.discovery.converters.Auto;
import com.server.common.paramconfig.AlipayConfig;
import com.server.common.utils.excel.ExportExcel;
import com.server.module.customer.order.OrderDto;
import com.server.module.sys.utils.UserUtils;
import com.server.module.system.adminUser.AdminConstant;
import com.server.module.system.adminUser.AdminUserBean;
import com.server.module.system.adminUser.AdminUserService;
import com.server.module.system.companyManage.CompanyBean;
import com.server.module.system.companyManage.CompanyService;
import com.server.module.system.logsManager.exportLog.ExportLogBean;
import com.server.module.system.logsManager.operationLog.OperationLogService;
import com.server.module.system.logsManager.operationLog.OperationsManagementLogBean;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseBean;
import com.server.module.system.machineManage.machineBase.VendingMachinesBaseService;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayBean;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayService;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemBean;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemCondition;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemDao;
import com.server.module.system.userManage.CustomerService;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.DateUtils;
import com.server.util.HttpUtil;
import com.server.util.JsonUtils;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import com.server.util.qRCodeDownLoad.QRCodeDownLoad;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jersey.repackaged.com.google.common.collect.Lists;

@Api(value = "VendingMachinesInfoController", description = "售货机列表")
@RestController
@RequestMapping("/vendingMachinesInfo")
public class VendingMachinesInfoController {

	public static Logger log = LogManager.getLogger(VendingMachinesInfoController.class);

	@Autowired
	private VendingMachinesInfoService vendingMachinesInfoServiceImpl;
	@Autowired
	private CompanyService companyServiceImpl;
	@Autowired
	private VendingMachinesBaseService vendingMachinesBaseServiceImpl;
	@Autowired
	private IDownLoadService iDownLoadServiceImpl;
	@Autowired
	private VendingMachinesWayService vendingMachinesWayServiceImpl;
	@Autowired
	private AdminUserService adminUserServiceImpl;
	@Autowired
	private OperationLogService operationLogServiceImpl;
	@Autowired
	private AlipayConfig alipayConfig;
	@Autowired
	private QRCodeDownLoad qrCodeDownLoad;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private CustomerService customerServiceImpl;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private VendingMachinesWayItemDao vmwiDao;
    @Autowired
    private VendingMachinesInfoDao vmiDao;
	@Autowired
	AdminUserService adminUserService;
    public static double pi = 3.1415926535897932384626;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;
    public static double DEF_PI = 3.14159265359; // PI
    public static double DEF_2PI= 6.28318530712; // 2*PI
    public static double DEF_PI180= 0.01745329252; // PI/180.0
    public static double DEF_R =6370693.5; // radius of earth
    
	@ApiOperation(value = "售货机列表", notes = "售货机列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/listPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil listPage(VendingMachinesInfoCondition condition,
			HttpServletRequest request) {
		if(condition==null) {
			condition=new VendingMachinesInfoCondition();
		}
		Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
		AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
		condition.setAreaId(user.getAreaId());
		return vendingMachinesInfoServiceImpl.listPage(condition);
	}

	@ApiOperation(value = "附近五台售货机列表", notes = "售货机列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/nearbyListPage", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil nearbyListPage(Double lon,Double lat) {
		//百度坐标转GCJ02坐标转WGS84坐标 √
		log.info("<vendingMachinesInfo>--<nearbyListPage>--start"); 
		Map<String, Object> map = new HashMap<String, Object>();
		VendingMachinesInfoCondition condition=new VendingMachinesInfoCondition();
		double r = 6371;//地球半径千米
		double dis = 1;//1千米距离
		double dlng =  2*Math.asin(Math.sin(dis/(2*r))/Math.cos(lat*Math.PI/180));
		dlng = dlng*180/Math.PI;//角度转为弧度
		double dlat = dis/r;
		dlat = dlat*180/Math.PI;
		
		URIBuilder url1 = null;
		try {
			url1 = new URIBuilder("http://api.map.baidu.com/geoconv/v1/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		url1.addParameter("coords", lon+","+lat);
		url1.addParameter("from", "5");//5：百度地图采用的经纬度坐标;
		url1.addParameter("to","3");//3：国测局（GCJ02）坐标;
		url1.addParameter("ak", "Z0BjyssAiR8xKSLrn21uk7cnNxzVw2kY");//百度开发者密钥
		log.info("百度坐标转换为GCJ02坐标url"+url1);
		String jsonResult1 = HttpUtil.get(url1);
		if (StringUtil.isNotBlank(jsonResult1)) {
			map = JsonUtils.toObject(jsonResult1, new TypeReference<Map<String, Object>>() {
			});
		}
		log.info(map);
		List<Map<String,Double>> bdMap1= (List<Map<String, Double>>) map.get("result");
		lon=bdMap1.get(0).get("x");
		lat=bdMap1.get(0).get("y");
		//GCJ02坐标转84坐标
		Map<String,Double> m=transform(lat,lon);
		lon = lon * 2 - m.get("lon");
        lat = lat * 2 - m.get("lat");
        double lon84=lon;
        double lat84=lat;
		condition.setMinlat(lat-dlat);
		condition.setMaxlat(lat+dlat);
		condition.setMinlon(lon-dlng);
		condition.setMaxlon(lon+dlng);
		//condition.setCompanyId(CompanyEnum.YOUSHUIDAOJIA.getIndex());        
		condition.setIsShowAll(1);
		
		ReturnDataUtil returnDataUtil=vendingMachinesInfoServiceImpl.nearbyListPage(condition);
		List<MachinesInfoAndBaseDto> vendingMachinesInfoList=(List<MachinesInfoAndBaseDto>) returnDataUtil.getReturnObject();
		List<MachinesInfoAndBaseDto> bdMachinesInfoAndBaseDto = new ArrayList<MachinesInfoAndBaseDto>();

		log.info("附近1km估略售货机数量"+vendingMachinesInfoList.size());
		Iterator i=vendingMachinesInfoList.iterator();
		while(i.hasNext()) {
			MachinesInfoAndBaseDto miabd=(MachinesInfoAndBaseDto) i.next();
			miabd.setDistance((double) Math.round(GetShortDistance(miabd.getLon(),miabd.getLat(),lon84,lat84) * 100) / 100);
			if(miabd.getDistance()>1000) {
				i.remove();
			}
		}

        //List<MachinesInfoAndBaseDto> filterList = vendingMachinesInfoList.stream().filter(v -> GetShortDistance(v.getLon(),v.getLat(),lon84,lat84) < 1000).collect(Collectors.toList());
		log.info("附近1km准确售货机数量"+vendingMachinesInfoList.size());

        Collections.sort(vendingMachinesInfoList, new Comparator<MachinesInfoAndBaseDto>(){
			public int compare(MachinesInfoAndBaseDto m1, MachinesInfoAndBaseDto m2) {
				return new Double(m1.getDistance()).compareTo(m2.getDistance());
			}
		});
        
        //取最近的5台售货机
//        if(filterList.size()>5) {
//            filterList = filterList.subList(0,5);
//        }else if(filterList.size()>0){
//    		log.info("---"+filterList.size());
//            filterList = filterList.subList(0, filterList.size());
//    		log.info("+++"+filterList.size());
//        }

        //84坐标转百度坐标
        bdMachinesInfoAndBaseDto=from84toBaidu(vendingMachinesInfoList);
        
        
//		URIBuilder url = null;
//		for(MachinesInfoAndBaseDto miabd:vendingMachinesInfoList) {
//			try {
//				url = new URIBuilder("http://api.map.baidu.com/geoconv/v1/");
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//			url.addParameter("coords", miabd.getLon()+","+miabd.getLat());
//			url.addParameter("from", "1");//1：GPS设备获取的角度坐标，WGS84坐标;
//			url.addParameter("to","5");//5：百度地图采用的经纬度坐标;
//			url.addParameter("ak", "Z0BjyssAiR8xKSLrn21uk7cnNxzVw2kY");//百度开发者密钥
//			log.info("84坐标转换百度坐标url"+url);
//			String jsonResult = HttpUtil.get(url);
//			if (StringUtil.isNotBlank(jsonResult)) {
//				map = JsonUtils.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
//				});
//			}
//			log.info(map);
//			List<Map<String,Double>> bdMap= (List<Map<String, Double>>) map.get("result");
//			miabd.setLon(bdMap.get(0).get("x"));
//			miabd.setLat(bdMap.get(0).get("y"));
//			bdMachinesInfoAndBaseDto.add(miabd);
//		}

		log.info("附近1km实际售货机数量"+bdMachinesInfoAndBaseDto.size());
		returnDataUtil.setReturnObject(bdMachinesInfoAndBaseDto);
		returnDataUtil.setTotal((long)bdMachinesInfoAndBaseDto.size());
		log.info("<vendingMachinesInfo>--<nearbyListPage>--end"); 
		return returnDataUtil;
	}
       //适用于近距离
       public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
       {
           double ew1, ns1, ew2, ns2;
           double dx, dy, dew;
           double distance;
           // 角度转换为弧度
           ew1 = lon1 * DEF_PI180;
           ns1 = lat1 * DEF_PI180;
           ew2 = lon2 * DEF_PI180;
           ns2 = lat2 * DEF_PI180;
           // 经度差
           dew = ew1 - ew2;
           // 若跨东经和西经180 度，进行调整
           if (dew > DEF_PI)
           	dew = DEF_2PI - dew;
           else if (dew < -DEF_PI)
           	dew = DEF_2PI + dew;
           dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
           dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
           // 勾股定理求斜边长
           distance = Math.sqrt(dx * dx + dy * dy);
           return distance;
       }
       
       //适用于远距离
       public static double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
       {
           double ew1, ns1, ew2, ns2;
           double distance;
           // 角度转换为弧度
           ew1 = lon1 * DEF_PI180;
           ns1 = lat1 * DEF_PI180;
           ew2 = lon2 * DEF_PI180;
           ns2 = lat2 * DEF_PI180;
           // 求大圆劣弧与球心所夹的角(弧度)
           distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
           // 调整到[-1..1]范围内，避免溢出
           if (distance > 1.0)
                distance = 1.0;
           else if (distance < -1.0)
                 distance = -1.0;
           // 求大圆劣弧长度
           distance = DEF_R * Math.acos(distance);
           return distance;
       }
        
   	@ApiOperation(value = "附近所有售货机列表", notes = "售货机列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
   	@GetMapping(value = "/allNearbyListPage", produces = "application/json;chbbbbbbarset=UTF-8")
   	public ReturnDataUtil allNearbyListPage() {
		log.info("<vendingMachinesInfo>--<allNearbyListPage>--start");
		Map<String, Object> map = new HashMap<String, Object>();
		VendingMachinesInfoCondition condition=new VendingMachinesInfoCondition();
		condition.setIsShowAll(1);
		ReturnDataUtil returnDataUtil=vendingMachinesInfoServiceImpl.nearbyListPage(condition);
		List<MachinesInfoAndBaseDto> vendingMachinesInfoList=(List<MachinesInfoAndBaseDto>) returnDataUtil.getReturnObject();
		vendingMachinesInfoList=from84toBaidu(vendingMachinesInfoList);
		returnDataUtil.setTotal((long)vendingMachinesInfoList.size());
		log.info("<vendingMachinesInfo>--<allNearbyListPage>--end"); 
		return returnDataUtil;
   	}
   	
    public static Map<String,Double> transform(double lat, double lon) {
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        Map<String,Double> m=new HashMap<String,Double>();
        m.put("lat", mgLat);
        m.put("lon", mgLon);
        return m;
    }
    
    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }
    
    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
   	//批量坐标84转百度坐标
   	public List<MachinesInfoAndBaseDto> from84toBaidu(List<MachinesInfoAndBaseDto> vendingMachinesInfoList){
		Map<String, Object> map = new HashMap<String, Object>();
   		int time= vendingMachinesInfoList.size()%100==0?vendingMachinesInfoList.size()/100:vendingMachinesInfoList.size()/100+1;
		URIBuilder url = null;
		try {
			url = new URIBuilder("http://api.map.baidu.com/geoconv/v1/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}			
		url.addParameter("from", "1");//1：GPS设备获取的角度坐标，WGS84坐标;
		url.addParameter("to","5");//5：百度地图采用的经纬度坐标;
		url.addParameter("ak", "Z0BjyssAiR8xKSLrn21uk7cnNxzVw2kY");//百度开发者密钥
		url.addParameter("coords", "0,0");

		int t=1;String position="";
		for(int i=1;i<=vendingMachinesInfoList.size();i++) {
			//批量84坐标转百度坐标 
			if(position.equals("")) {
				position=vendingMachinesInfoList.get(i-1).getLon()+","+vendingMachinesInfoList.get(i-1).getLat();
			}else {
				position=position+";"+vendingMachinesInfoList.get(i-1).getLon()+","+vendingMachinesInfoList.get(i-1).getLat();
			}
			if(i%100==0 || (t==time && i==vendingMachinesInfoList.size()))  {
				url.setParameter("coords", position);
				String jsonResult = HttpUtil.get(url);
				if (StringUtil.isNotBlank(jsonResult)) {
					map = JsonUtils.toObject(jsonResult, new TypeReference<Map<String, Object>>() {
					});
				}
				log.info("批量84坐标转百度坐标返回结果map"+map);
				List<Map<String,Double>> bdMap= (List<Map<String, Double>>) map.get("result");
				for(int j=0;j<=bdMap.size()-1;j++) {
					vendingMachinesInfoList.get(100*(t-1)+j).setLon(bdMap.get(j).get("x"));
					vendingMachinesInfoList.get(100*(t-1)+j).setLat(bdMap.get(j).get("y"));
				}
				t=t+1;
				position="";
			}
		}
		return vendingMachinesInfoList;
   		
   	}
    
    
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/export")
	public String exportFile(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, VendingMachinesInfoCondition condition) {
		try {
			String fileName = "售货机列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
			condition.setAreaId(user.getAreaId());
			ReturnDataUtil data = vendingMachinesInfoServiceImpl.listPage(condition);
			//导出日志内容添加
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(condition.getStartDate()!=null&&condition.getEndDate()!=null) {
				//导出日志内容按时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(condition.getStartDate())+"--"+DateUtil.formatYYYYMMDD(condition.getEndDate())+"的售货机列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机列表--当前页数据");
			}
			new ExportExcel("售货机列表", MachinesInfoAndBaseDto.class).setDataList((List) data.getReturnObject())
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportAll", method = RequestMethod.GET)
	public String exportAll(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes, VendingMachinesInfoCondition condition) {
		try {
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			AdminUserBean user = (AdminUserBean) adminUserService.findUserById(userId).getReturnObject();
			condition.setAreaId(user.getAreaId());
			ReturnDataUtil data = vendingMachinesInfoServiceImpl.listPage(condition);
			condition.setPageSize(data.getPageSize() * data.getTotalPage());
			ReturnDataUtil listPage = vendingMachinesInfoServiceImpl.listPage(condition);
			//导出日志内容添加
			ExportLogBean bean=(ExportLogBean) request.getAttribute("exportBean");
			if(condition.getStartDate()!=null&&condition.getEndDate()!=null) {
				//导出日志内容按时间格式输出
				bean.setContent("用户: "+bean.getOperatorName()+" 导出"+DateUtil.formatYYYYMMDD(condition.getStartDate())+"--"+DateUtil.formatYYYYMMDD(condition.getEndDate())+"的售货机列表的数据");
			}else {
				bean.setContent("用户: "+bean.getOperatorName()+" 导出售货机列表--全部数据");
			}
			String fileName = "全部售货机列表" + DateUtils.getDateStr(new Date(), "yyyyMMddHHmmss") + ".xlsx";
			new ExportExcel("全部售货机列表", MachinesInfoAndBaseDto.class).setDataList((List) listPage.getReturnObject())
					.write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(value = "添加售货机", notes = "添加售货机", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/addMachines", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil addMachines(@RequestBody VendingMachinesInfoBean bean, HttpServletRequest request) {
		// 得到该用户的公司信息
		CompanyBean company = companyServiceImpl.getCompany(UserUtils.getUser().getId().intValue());
		log.info("公司地区Id>>>>>>>>>>>>" + company.getAreaId());
		// 得到 售货机类型编号 如要传入一个 VendingMachinesBaseBean 的 ID
		VendingMachinesBaseBean base = vendingMachinesBaseServiceImpl.get(bean.getMachinesBaseId());
		Long typeId = Integer.valueOf(base.getMachinesTypeId()).longValue();
		log.info("售货机类型ID>>>>>>>>>>>>>>>>" + typeId);
		/*
		 * String areaNumber=null; if(bean.getAreaId()<10) {
		 * areaNumber="0"+bean.getAreaId(); }else { areaNumber=bean.getAreaId()+""; }
		 */
		// 通过 地区编号 和 售货机类型编号 生成得到售货机编号
		String vmCode = vendingMachinesInfoServiceImpl.getVendingMachines(company.getAreaId() + "", typeId);
		// 生成二维码
		Assert.hasText(vmCode, "售货机编号不能为空");
		// 得到配置文件的保存支付宝二维码的地址
		String aLiPath = alipayConfig.getAliLocation().trim();
		String url=qrCodeDownLoad.downLoadALiCode(vmCode,aLiPath);
		
		// 得到配置文件的保存文件地址  [二码合一的二维码]
		String path = alipayConfig.getLocation().trim();
		String picture = null;
		if(bean.getMachineType()==2) {
			 picture = qrCodeDownLoad.dowanLoadCodeVision(vmCode, path);
		}else {
			 picture = qrCodeDownLoad.dowanLoadCode(vmCode, path);
		}
		bean.setCode(vmCode); // 售货机编号
		bean.setUrl(url);
		bean.setQrCode(picture);
		bean.setLocatoinDate(new Date());
		// 判断是否已经存在
		boolean flag = vendingMachinesInfoServiceImpl.getMachinesBaseId(bean.getMachinesBaseId());
		if (flag) {
			ReturnDataUtil datautil = new ReturnDataUtil();
			datautil.setStatus(0);
			datautil.setMessage("该机器模板已经存在一台售货机了，请重新输入售货机出厂编号");
			return datautil;
		} else {
			VendingMachinesInfoBean addMachines = vendingMachinesInfoServiceImpl.addMachines(bean);
			if (addMachines != null) {
				// 给售货机增加货道
				for (int i = 1; i <= bean.getWayCount(); i++) {
					VendingMachinesWayBean wayBean = new VendingMachinesWayBean();
					wayBean.setVendingMachinesCode(vmCode);
					wayBean.setWayNumber(i);
					vendingMachinesWayServiceImpl.add(wayBean);
				}
				return new ReturnDataUtil(addMachines);
			}
			return null;
		}
	}

	@ApiOperation(value = "查询单个售货机", notes = "查询单个售货机", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/getMachinesInfoAndBase", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ReturnDataUtil getMachinesInfoAndBase(String code) {
		return new ReturnDataUtil(vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(code));
	}

	@ApiOperation(value = "修改单个售货机", notes = "修改单个售货机", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateVendingMachinesInfoBean", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateVendingMachinesInfoBean(@RequestBody VendingMachinesInfoBean entity,
			HttpServletRequest request) {
		// 得到修改之前的信息
		MachinesInfoAndBaseDto machinesInfoAndBase = vendingMachinesInfoServiceImpl
				.getMachinesInfoAndBase(entity.getCode());
		String stateName = machinesInfoAndBase.getStateName();
		/*System.out.println("===========================0.0=============================");
		System.out.println(machinesInfoAndBase.getState());
		System.out.println(entity.getState());*/
		if (machinesInfoAndBase.getState().equals(entity.getState())==false){
			entity.setEditTime(new Date());
			//System.out.println("========================为啥要乱来=============================");
		}
		boolean falg = vendingMachinesInfoServiceImpl.updateEntity(entity);
		if (falg) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			OperationsManagementLogBean logBean = new OperationsManagementLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setVmCode(entity.getCode());
			// 得到修改之后的信息
			MachinesInfoAndBaseDto dto = vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(entity.getCode());
			String newStateName = dto.getStateName();
			//判断售货机的公司是否发生改变
			if(machinesInfoAndBase.getCompanyId().intValue()!=entity.getCompanyId()) {//售货机公司更改
				//根据公司id查询共公司名称
				String oldName=companyServiceImpl.findCompanyById(machinesInfoAndBase.getCompanyId()).getName();
				String newName=companyServiceImpl.findCompanyById(entity.getCompanyId()).getName();
				logBean.setContent("更改编号为" + entity.getCode() + "的售货机信息,该售货机由  “" + oldName + "” 变更为 “" + newName+"”");
			}else {
				logBean.setContent("更改编号为" + entity.getCode() + "的售货机信息,状态" + stateName + "改为" + newStateName);
				
			}
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加操作日志
			operationLogServiceImpl.add(logBean);
			return new ReturnDataUtil(falg);
		}
		return null;
	}

	@ApiOperation(value = "支付宝二维码", notes = "支付宝二维码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getVendingMachinesInfoBean")
	public ReturnDataUtil getVendingMachinesInfoBean(@RequestBody Map<String, String> code) {
		log.info("<VendingMachinesInfoController>----<getVendingMachinesInfoBean>----start");
		String url = vendingMachinesInfoServiceImpl.getVendingMachinesInfoBean(code.get("code"));
		log.info("<VendingMachinesInfoController>----<getVendingMachinesInfoBean>----end");
		return new ReturnDataUtil(url);
	}

	@ApiOperation(value = "批量生成二维码", notes = "批量生成二维码", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/createQRCode")
	public ReturnDataUtil createQRCode() {
		List<String> code2 = vendingMachinesInfoServiceImpl.getCode();
		// 得到配置文件的保存文件地址
		String path = alipayConfig.getLocation();
		for (String code : code2) {
			String loadCode = qrCodeDownLoad.dowanLoadCode(code, path);
			VendingMachinesInfoBean info = new VendingMachinesInfoBean();
			info.setCode(code);
			info.setQrCode(loadCode);
			vendingMachinesInfoServiceImpl.updateEntity(info);
		}
		return new ReturnDataUtil();
	}

	@ApiOperation(value = "售货机列表 修改售货机", notes = "修改售货机", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/updateInfoAndBase", produces = "application/json;charset=UTF-8")
	public ReturnDataUtil updateInfoAndBase(@RequestBody MachinesInfoAndBaseDto entity, HttpServletRequest request) {
		// 得到之前的信息
		MachinesInfoAndBaseDto dto = vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(entity.getCode());
		String stateName = dto.getStateName();
		if (dto.getState().equals(entity.getState())==false){
			entity.setEditTime(new Date());
		}
		boolean falg = vendingMachinesInfoServiceImpl.updateInfoAndBase(entity);
		if (falg) {
			// 得到当前用户Id 查询出当前用户信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			ReturnDataUtil data = adminUserServiceImpl.findUserById(userId);
			AdminUserBean userBean = (AdminUserBean) data.getReturnObject();
			OperationsManagementLogBean logBean = new OperationsManagementLogBean();
			logBean.setCompanyId(userBean.getCompanyId());
			logBean.setUserName(userBean.getName());
			logBean.setVmCode(entity.getCode());
			// 得到修改后的信息
			MachinesInfoAndBaseDto baseDto = vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(entity.getCode());
			String newStateName = baseDto.getStateName();
			logBean.setContent("更改编号为" + entity.getCode() + "的售货机信息,状态" + stateName + "改为" + newStateName);
			logBean.setOperationTime(DateUtil.formatYYYYMMDDHHMMSS(new Date()));
			// 增加操作日志
			operationLogServiceImpl.add(logBean);
			return new ReturnDataUtil(falg);
		}
		return null;
	}

	@ApiOperation(value = "新二维码批量下载", notes = "新二维码批量下载", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@GetMapping(value = "/downloadNewQRCode", produces = "application/json;charset=UTF-8")
	public void downloadNewQRCode(VendingMachinesInfoCondition condition, HttpServletResponse response) {
		log.info("<VendingMachinesInfoController>------<downloadNewQRCode>----start");
		ReturnDataUtil data = vendingMachinesInfoServiceImpl.listPage(condition);
		condition.setPageSize(data.getPageSize() * data.getTotalPage());
		List<MachinesInfoAndBaseDto> list = (List<MachinesInfoAndBaseDto>) vendingMachinesInfoServiceImpl
				.listPage(condition).getReturnObject();
		iDownLoadServiceImpl.downLoadQRCode(list, response);
		log.info("<VendingMachinesInfoController>------<downloadNewQRCode>----end");
	}

	@ApiOperation(value = "新二维码", notes = "新二维码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/getQRCode")
	public ReturnDataUtil getQRCode(@RequestBody Map<String, String> code) {
		return new ReturnDataUtil(vendingMachinesInfoServiceImpl.getQRCode(code.get("code")));
	}

	@ApiOperation(value = "查询线路下的所有售货机", notes = "查询线路下的所有售货机", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/findMachinesInfo")
	public ReturnDataUtil findMachinesInfo(@RequestBody VendingMachinesInfoCondition condition) {
		log.info("<VendingMachinesInfoController>------<findMachinesInfo>----start");
		ReturnDataUtil findMachinesListPage = vendingMachinesInfoServiceImpl.findMachinesListPage(condition);
		log.info("<VendingMachinesInfoController>------<findMachinesInfo>----start");
		return findMachinesListPage;
	}

	/**
	 * 校验新增加的售货机编号是否是本公司的
	 * 
	 * @param code
	 * @return
	 */
	@SuppressWarnings("unused")
	@ApiOperation(value = "增加售货机", notes = "增加售货机", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/lineAddMachinesInfo")
	public ReturnDataUtil lineAddMachinesInfo(@RequestBody VendingMachinesInfoBean entity) {
		ReturnDataUtil dataUtil = new ReturnDataUtil();
		log.info("<VendingMachinesInfoController>------<lineAddMachinesInfo>----start");
		List<VendingMachinesInfoBean> machinesInfo = vendingMachinesInfoServiceImpl.getMachinesInfo(entity.getCode(),
				entity.getCompanyId().longValue());
		if (machinesInfo.size() > 0) {
			List<VendingMachinesInfoBean> list = vendingMachinesInfoServiceImpl
					.findMachinesByLineId(entity.getLineId());
			if (list.size()==0) {
				boolean updateEntity = vendingMachinesInfoServiceImpl.updateEntity(entity);
				if (updateEntity) {
					dataUtil.setStatus(1);
					dataUtil.setMessage("机器添加成功！");
				} else {
					dataUtil.setStatus(2);
					dataUtil.setMessage("机器添加失败！");
				}
			} else {
				boolean falg=false;
				for (VendingMachinesInfoBean vendingMachinesInfoBean : list) {
					if (vendingMachinesInfoBean.getCode().equals(entity.getCode())) {
						falg=true;
					} 
				}
				if(!falg) {
					boolean updateEntity = vendingMachinesInfoServiceImpl.updateEntity(entity);
					if (updateEntity) {
						dataUtil.setStatus(1);
						dataUtil.setMessage("机器添加成功！");
					} else {
						dataUtil.setStatus(2);
						dataUtil.setMessage("机器添加失败！");
					}
				}else {
					dataUtil.setStatus(0);
					dataUtil.setMessage("该售货机已经存在这条线路上了，请重新输入！");
				}
			}
		} else {
			dataUtil.setStatus(0);
			dataUtil.setMessage("输入有误，该售货机不在本公司！");
		}
		log.info("<VendingMachinesInfoController>------<lineAddMachinesInfo>----end");
		return dataUtil;
	}

	@ApiOperation(value = "生成支付宝二维码", notes = "downQRCode", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/downQRCode")
	public String downQRCode() {
		log.info("<VendingMachinesInfoController>------<downQRCode>----start");
		List<String> code2 = vendingMachinesInfoServiceImpl.getCode();
		// 得到配置文件的保存支付宝二维码的地址
		String path = alipayConfig.getAliLocation();
		for (String code : code2) {
			//生成支付宝二维码
			String loadCode = qrCodeDownLoad.downLoadALiCode(code, path);
			//修改售货机二维码
			VendingMachinesInfoBean info = new VendingMachinesInfoBean();
			info.setCode(code);
			info.setUrl(loadCode);
			vendingMachinesInfoServiceImpl.updateEntity(info);
		}
		log.info("<VendingMachinesInfoController>------<downQRCode>----end");
		return "success";
	}


	@ApiOperation(value = "解除物理售货机和售货机绑定", notes = "untie", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/untie")
	public ReturnDataUtil untie(@RequestBody UntieBean untieBean,HttpServletRequest request) {

		log.info("<VendingMachinesInfoController>------<untie>----start");

		ReturnDataUtil util = new ReturnDataUtil();
		if (untieBean == null) {
			util.setStatus(0);
			util.setMessage("没有解绑的信息传递过来");
			return util;
		} else {

			// 得到用户的信息
			Long userId = (Long) request.getAttribute(AdminConstant.LOGIN_USER_ID);
			untieBean.setUserId(userId);
			log.info("<VendingMachinesInfoController>------<untie>----end");
			return vendingMachinesInfoServiceImpl.untieVendingMachine(untieBean);
		}

	}
	
	@ApiOperation(value = "生成临时二维码", notes = "createUrl", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/createUrl")
	public ReturnDataUtil createUrl() {
		log.info("<VendingMachinesInfoController>------<createUrl>----start");
		String vmCode=null;
		Long customerId = userUtils.getSmsUser().getId();
		if(customerId!=null) {
			String code = customerServiceImpl.findCustomerById(customerId).getVmCode();
			if(StringUtil.isNotBlank(code)) {
				vmCode=code;
			}
		}
		//得到图片保存地址
		String path = alipayConfig.getTemporary();
		String picture = qrCodeDownLoad.dowanLoadCode2(customerId,vmCode, path);
		log.info("<VendingMachinesInfoController>------<createUrl>----end");
		return new ReturnDataUtil(picture);
	}
	
	
	@ApiOperation(value = "质量比对", notes = "checkQuantify", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/checkQuantify")
	public ReturnDataUtil checkQuantify() {
		List<MachinesInfo> list = new ArrayList<MachinesInfo>();
		List<MachinesInfo> list2 = new ArrayList<MachinesInfo>();
		List<HeartBeatDto> hBeatList=Lists.newArrayList();
		Set<String> set = redisClient.keys("HM-*");
		for(String key:set) {
			String hbString=redisClient.get("HM-"+key.split("-")[1]);
			MachinesInfo m=CommandParser.parseVer1(hbString);
			list.add(m);
		}
		try {
			Thread.sleep(5000);
			Set<String> set2 = redisClient.keys("HM-*");
			for(String key:set2) {
				String hbString=redisClient.get("HM-"+key.split("-")[1]);
				MachinesInfo m=CommandParser.parseVer1(hbString);
				list2.add(m);
			}
			for(MachinesInfo a:list) {
				for(MachinesInfo b:list2) {

					if(a!=null && b!=null && a.getFactoryNumber().equals(b.getFactoryNumber())) {
						try {
							//log.info("机器出厂编码"+a.getFactoryNumber());
							for(int i = 0;i<b.getItemChangeList().size();i++) {
								int diff = Math.abs(a.getItemChangeList().get(i).getWayWeight()-b.getItemChangeList().get(i).getWayWeight());
								if(diff>100) {
									HeartBeatDto hb=new HeartBeatDto();
									hb.setFactoryNumber(a.getFactoryNumber());
									String vmCode=redisClient.get("MachinesKey:numToCode"+a.getFactoryNumber());
									hb.setVmCode(vmCode);
									hb.setRemark(i+1+"门质量差"+diff+"g");
									hb.setBeforeHeartBeat(a.getParams());
									hb.setAfterHeartBeat(b.getParams());
                                    VendingMachinesInfoBean machinesInfoAndBaseDto = vmiDao.findVendingMachinesByCodeNew(vmCode);
                                    if(machinesInfoAndBaseDto!=null){
                                        hBeatList.add(hb);
                                    }
									log.info(i+1+"门质量差"+diff+"g--"+a.getParams()+"-"+b.getParams());
								};
							}
						} catch (Exception e) {
							log.info("未知错误"+a.getParams()+"-"+b.getParams());
						}
						break;
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ReturnDataUtil(hBeatList);

	}

	@ApiOperation(value = "质量库存比对", notes = "checkCompareQuantify", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/checkCompareQuantify")
	public ReturnDataUtil checkCompareQuantify() {
		List<MachinesInfo> list = new ArrayList<MachinesInfo>();
		List<MachinesInfo> returnDataUtil = new ArrayList<MachinesInfo>();

		//List<HeartBeatDto> hBeatList=Lists.newArrayList();
		VendingMachinesWayItemCondition vendingMachinesWayItemCondition=new VendingMachinesWayItemCondition();
		List<VendingMachinesWayItemBean> vmwiList=vmwiDao.list(vendingMachinesWayItemCondition);
		Set<String> set = redisClient.keys("HM-*");
		for(String key:set) {
			String hbString=redisClient.get("HM-"+key.split("-")[1]);
			MachinesInfo m=CommandParser.parseVer1(hbString);
			list.add(m);
		}
		for(MachinesInfo a:list) {
			if(a!=null) {
				//log.info("机器出厂编码"+a.getFactoryNumber());
				a.setVmCode(redisClient.get("MachinesKey:numToCode"+a.getFactoryNumber()));
				for(ItemChangeDto i:a.getItemChangeList()) {
					Iterator<VendingMachinesWayItemBean> listIt = vmwiList.iterator();
					while(listIt.hasNext()){
						VendingMachinesWayItemBean vmwi=listIt.next();
						if(vmwi.getVmCode().equals(a.getVmCode()) && vmwi.getWayNumber().intValue()==i.getWayNum().intValue()) {
							//log.info(vmwi.getNum()+"--"+Math.round(i.getWayWeight()/(double)vmwi.getWeight()));
							Long num=(Math.round(i.getWayWeight()/(double)vmwi.getWeight()));
							if(vmwi.getNum().longValue()!=num.longValue()) {
								MachinesInfo b=new MachinesInfo();
								b.setCurrWayNum(vmwi.getWayNumber().intValue());
								b.setTotalItemNum(vmwi.getNum().intValue());
								b.setTotalWayNum(num.intValue());
								b.setVmCode(a.getVmCode());
								b.setFactoryNumber(a.getFactoryNumber());
								b.setAddress(vmwi.getAddress());
								returnDataUtil.add(b);
							}
						}
						if(vmwi.getWayNumber()==4) {
							listIt.remove();
						}
					}
				}
			}
		}
		Collections.sort(returnDataUtil, new Comparator<MachinesInfo>() {
			@Override
			public int compare(MachinesInfo dt1,  MachinesInfo dt2) {
				if (Integer.parseInt(dt1.getVmCode()) > Integer.parseInt(dt2.getVmCode())) {
					return 1;
				} else if (Integer.parseInt(dt1.getVmCode())  < Integer.parseInt(dt2.getVmCode())) {
					return -1;
				} else {// 相等
					return 0;
				}
			}
		});
		return new ReturnDataUtil(returnDataUtil);
	}

	@ApiOperation(value = "检测非正常开门", notes = "checkCompareQuantify", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/checkOpenDoor")
	public ReturnDataUtil checkOpenDoor() {
		List<MachinesInfo> list = new ArrayList<MachinesInfo>();
		//List<HeartBeatDto> hBeatList=Lists.newArrayList();
		//VendingMachinesWayItemCondition vendingMachinesWayItemCondition=new VendingMachinesWayItemCondition();
		//List<VendingMachinesWayItemBean> vmwiList=vmwiDao.list(vendingMachinesWayItemCondition);
		//检测未开门的收获
		Set<String> set = redisClient.keys("HM-*");
		for(String key:set) {
			String hbString=redisClient.get("HM-"+key.split("-")[1]);
			MachinesInfo m=CommandParser.parseVer1(hbString);
			if(StringUtils.isNotBlank(m.getState()) ) {
				for(ItemChangeDto i:m.getItemChangeList()) {
					if(i.getState().equals("1")) {
						m.setVmCode(redisClient.get("MachinesKey:numToCode"+m.getFactoryNumber()));
						m.setCurrWayNum(i.getWayNum());
                        VendingMachinesInfoBean machinesInfoAndBaseDto = vmiDao.findVendingMachinesByCodeNew(m.getVmCode());
						if(machinesInfoAndBaseDto!=null){
							m.setAddress(machinesInfoAndBaseDto.getLocatoinName());
							list.add(m);
						}

						break;
					}
				}
			}
		}
		return new ReturnDataUtil(list);
	}

	@ApiOperation(value = "心跳查询", notes = "queryHeartBeat", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@PostMapping(value = "/queryHeartBeat")
	public ReturnDataUtil queryHeartBeat(@RequestParam String key) {
        List<HeartBeatQueryDto> list = new ArrayList<HeartBeatQueryDto>();

        if(	UserUtils.getUser().getId().equals(1L)){
            log.info("queryHeartBeat"+key);
            //List<MachinesInfo> list = new ArrayList<MachinesInfo>();
            //List<HeartBeatDto> hBeatList=Lists.newArrayList();
            //VendingMachinesWayItemCondition vendingMachinesWayItemCondition=new VendingMachinesWayItemCondition();
            //List<VendingMachinesWayItemBean> vmwiList=vmwiDao.list(vendingMachinesWayItemCondition);
            Set<String> set = redisClient.keys("*"+key+"*");
            Map<String,String> map=new HashMap<String,String>();

            for(String redisKey:set) {
                String hbString=redisClient.get(redisKey);
                log.info("value"+hbString);
                HeartBeatQueryDto heartBeatQueryDto=new HeartBeatQueryDto();
                heartBeatQueryDto.setKey(redisKey);
                heartBeatQueryDto.setValue(hbString);
                list.add(heartBeatQueryDto);
            }
        }else{
            String hbString=redisClient.get("HM-"+key);
            HeartBeatQueryDto heartBeatQueryDto=new HeartBeatQueryDto();
            heartBeatQueryDto.setKey("HM-"+key);
            heartBeatQueryDto.setValue(hbString);
            list.add(heartBeatQueryDto);
        }
		return new ReturnDataUtil(list);
	}

	//获取所有售货机编号
	@GetMapping("/getCodes")
	public ReturnDataUtil getCodes(){
           List<String> code = vendingMachinesInfoServiceImpl.getCode();
           return new ReturnDataUtil(code);
	}

	//根据收货机编号查找货道配置
	@GetMapping("/findAisleConfigurationByCode")
	public ReturnDataUtil findAisleConfigurationByCode(String code) {
		return new ReturnDataUtil(vendingMachinesInfoServiceImpl.getMachinesInfoAndBase(code).getAisleConfiguration());
	}

}
