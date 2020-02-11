package com.server.module.system.machineManage.machineList;

import com.google.common.collect.Lists;
import com.server.dbpool.BaseDB;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.companyManage.companyMachines.ReplenishCompanyMachinesBean;
import com.server.module.system.companyManage.companyMachines.ReplenishCompanyMachinesDao;
import com.server.module.system.machineManage.machineType.MachinesTypeBean;
import com.server.module.system.machineManage.machineType.MachinesTypeServiceImpl;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayCondition;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayDao;
import com.server.module.system.machineManage.machinesWay.WayDto1;
import com.server.module.system.machineManage.machinesWay.WayItem;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemBean;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemCondition;
import com.server.module.system.machineManage.machinesWayItem.VendingMachinesWayItemDao;
import com.server.module.system.purchase.purchaseApplyItem.PurchaseApplyBillItemBean;
import com.server.module.system.purchase.purchaseBill.PurchaseBillBean;
import com.server.module.system.purchase.purchaseBillItem.PurchaseBillItemBean;
import com.server.module.system.statisticsManage.payRecord.ListSaleNumCondition;
import com.server.module.system.statisticsManage.payRecord.ListSaleNumDTO;
import com.server.module.system.statisticsManage.payRecord.PayRecordDao;
import com.server.redis.RedisClient;
import com.server.util.DateUtil;
import com.server.util.ReturnDataUtil;
import com.server.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class VendingMachinesInfoServiceImpl implements VendingMachinesInfoService {

	public static Logger log = LogManager.getLogger(VendingMachinesInfoServiceImpl.class);
	@Autowired
	private VendingMachinesInfoDao vendingMachinesInfoDaoImpl;
	@Autowired
	private MachinesTypeServiceImpl machinesTypeServiceImpl;
	@Autowired
	private RedisClient redisClient;
	@Autowired
	private CompanyDao companyManageDao;
	@Autowired
	private VendingMachinesWayDao vendingMachinesWayDao;
	@Autowired
	private VendingMachinesWayItemDao wayItemDao;
	@Autowired
	private ReplenishCompanyMachinesDao replenishCompanyMachinesDaoImpl;
	@Autowired
	private VendingMachinesWayDao vendingMachinesWayDaoImpl;
	@Autowired
	private PayRecordDao payRecordDaoImpl;

	/**
	 * 售货机列表 查询
	 */
	public ReturnDataUtil listPage(VendingMachinesInfoCondition condition) {
		ReturnDataUtil data = vendingMachinesInfoDaoImpl.listPage(condition);
		@SuppressWarnings("unchecked")
		List<MachinesInfoAndBaseDto> list = (List<MachinesInfoAndBaseDto>) data.getReturnObject();
		// 逐个查询机器类型名称 通常走缓存速度快
		for (MachinesInfoAndBaseDto machinesInfoAndBaseDto : list) {
			Integer typeId = machinesInfoAndBaseDto.getMachinesTypeId();
			MachinesTypeBean typeBean = machinesTypeServiceImpl.get(typeId);
			if (typeBean != null) {
				machinesInfoAndBaseDto.setMachinesTypeName(typeBean.getName());
				if (StringUtil.isNotBlank(redisClient.get("enterAli-" + machinesInfoAndBaseDto.getCode()))) {
					machinesInfoAndBaseDto.setCanEnter(false);
				} else {
					machinesInfoAndBaseDto.setCanEnter(true);
				}
			}

		}
		return data;
	}

	/***
	 * 根据公司id查询公司当下的售卖机信息
	 */
	@Override
	public List<String> findVmcodeByCompanyId(String companyIds) {
		if (StringUtils.isNotBlank(companyIds))
			return vendingMachinesInfoDaoImpl.findVmcodeByCompanyId(companyIds);
		else
			return null;
	}


    /**
     * 最近五台售货机列表 查询
     */
    public ReturnDataUtil nearbyListPage(VendingMachinesInfoCondition condition) {
        ReturnDataUtil data = vendingMachinesInfoDaoImpl.nearbyListPage(condition);
        return data;
    }

	/**
	 * 根据售卖机code查询售卖机信息
	 */
	@Override
	public VendingMachinesInfoBean findVendingMachinesByCode(String code) {
		return vendingMachinesInfoDaoImpl.findVendingMachinesByCode(code);
	}

	@Override
	public List<VendingMachinesInfoBean> findVendingMachinesByCodes( List<String> codes ) {
		return vendingMachinesInfoDaoImpl.findVendingMachinesByCodes(codes);
	}


	/**
	 * 根据售卖机code查询地图售卖机信息
	 */
	@Override
	public VendingMachinesInfoBean findVendingMachinesByCodeForMap(String code) {
		return vendingMachinesInfoDaoImpl.findVendingMachinesByCodeForMap(code);
	}
	
	/**
	 * 根据公司id查询公司当下的售卖机信息
	 */
	@Override
	public List<VendingMachinesInfoBean> findVmByCompanyId(Integer companyId) {
		return vendingMachinesInfoDaoImpl.findVmByCompanyId(companyId);
	}

	/**
	 * 添加售货机
	 */
	@Override
	public VendingMachinesInfoBean addMachines(VendingMachinesInfoBean bean) {
		// TODO Auto-generated method stub
		// 更新时间
		//bean.setCreateTime(new Date());
		bean.setUpdateTime(new Date());
		// 判断添加售货机是否添加了补货公司
		if (companyManageDao.checkIsReplenishCompany(bean.getReplenishCompanyId())) {// 需要添加补货公司
			Connection conn = null;
			try {
				conn = BaseDB.openConnection();// 取得同一个连接对象
				// 开启事物，获取连接对象
				// 对多表进行操作
				conn.setAutoCommit(false);
				// 添加售货机信息
				VendingMachinesInfoBean entity = vendingMachinesInfoDaoImpl.addMachines(bean, conn);
				ReplenishCompanyMachinesBean machinesBean = new ReplenishCompanyMachinesBean();
				machinesBean.setCode(entity.getCode());
				machinesBean.setCompanyId(entity.getReplenishCompanyId().longValue());
				// 执行添加操作
				replenishCompanyMachinesDaoImpl.insert(machinesBean, conn);
				log.info("<VendingMachinesInfoServiceImpl>------<addMachines>----end");
				conn.commit();
				return entity;
			} catch (SQLException e) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				return null;

			} finally {
				BaseDB.closeConnection(conn);
			}
		} else {
			return vendingMachinesInfoDaoImpl.addMachines(bean);
		}
	}

	/**
	 * 生成 售货机编号
	 */
	@Override
	public String getVendingMachines(String areadNumber, Long typeId) {
		// TODO Auto-generated method stub
		return vendingMachinesInfoDaoImpl.getVendingMachines(areadNumber, typeId);
	}

	/**
	 * 根据收货机编号 查询出相关信息
	 */
	@Override
	public MachinesInfoAndBaseDto getMachinesInfoAndBase(String code) {
		// TODO Auto-generated method stub
		return vendingMachinesInfoDaoImpl.getMachinesInfoAndBase(code);
	}

	/**
	 * 修改公司售货机信息
	 */
	@Override
	public boolean updateEntity(VendingMachinesInfoBean entity) {
		// TODO Auto-generated method stub
		log.info("<VendingMachinesInfoServiceImpl>------<updateEntity>----start");
		// 修改更新时间
		//entity.setUpdateTime(new Date());
		Connection conn = null;
		try {
			conn = BaseDB.openConnection();// 取得同一个连接对象
			// 开启事物，获取连接对象
			// 对多表进行操作
			conn.setAutoCommit(false);
			// 更新售货机信息
			boolean falg = vendingMachinesInfoDaoImpl.updateEntity(entity, conn);
			// 确认entity中的补货公司id是否为补货公司
			if (companyManageDao.checkIsReplenishCompany(entity.getReplenishCompanyId())&&entity.getCompanyId().intValue()!=entity.getReplenishCompanyId()) {// 是补货公司
				ReplenishCompanyMachinesBean bean = new ReplenishCompanyMachinesBean();
				bean.setCode(entity.getCode());
				bean.setCompanyId(entity.getReplenishCompanyId().longValue());
				// 判断该售货机是否已添加过补货公司
				Integer id = replenishCompanyMachinesDaoImpl.check(entity.getCode());
				if (id != null) {
					// 已添加有补货公司，进行更新
					bean.setId(id.longValue());
					falg = replenishCompanyMachinesDaoImpl.update(bean, conn);
				} else {
					// 没有过添加补货公司，进行添加操作
					falg = replenishCompanyMachinesDaoImpl.insert(bean, conn);
				}
			} else if (entity.getReplenishCompanyId() == entity.getCompanyId().intValue()) {// 没有添加补货公司
				// 没有添加补货公司或该售货机取消补货公司
				Integer id = replenishCompanyMachinesDaoImpl.check(entity.getCode());
				if (id != null) {// 有补货公司，取消绑定
					falg = replenishCompanyMachinesDaoImpl.delete(id.longValue(), conn);
				}
			}
			conn.commit();
			log.info("<VendingMachinesInfoServiceImpl>------<updateEntity>----end");
			return falg;
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;

		} finally {
			BaseDB.closeConnection(conn);
		}
	}

	/**
	 * 根据路线lineId查询所有机器信息
	 */
	@Override
	public List<VendingMachinesInfoBean> findMachinesByLineId(Integer lineId) {
		return vendingMachinesInfoDaoImpl.findMachinesByLineId(lineId);
	}

	/**
	 * 根据售货机编号 查询二维码路径
	 */
	@Override
	public String getVendingMachinesInfoBean(String code) {
		// TODO Auto-generated method stub
		return vendingMachinesInfoDaoImpl.getVendingMachinesInfoBean(code);
	}

	/**
	 * 查询所有售货机编号
	 */
	@Override
	public List<String> getCode() {
		// TODO Auto-generated method stub
		return vendingMachinesInfoDaoImpl.getCode();
	}

	/**
	 * 售货机列表 编辑
	 */
	@Override
	public boolean updateInfoAndBase(MachinesInfoAndBaseDto dto) {
		// TODO Auto-generated method stub
		return vendingMachinesInfoDaoImpl.updateInfoAndBase(dto);
	}

	/**
	 * 判断是否该机器模板下 已经存在一台售货机
	 */
	@Override
	public boolean getMachinesBaseId(Integer machinesId) {
		// TODO Auto-generated method stub
		return vendingMachinesInfoDaoImpl.getMachinesBaseId(machinesId);
	}

	/**
	 * 查询当前登录用户所属公司 所有的售货机编号
	 */
	public List<String> findCode(Integer state, Integer companyId,Integer areaId) {
		return vendingMachinesInfoDaoImpl.findCode(state, companyId, areaId);
	}

	/**
	 * 查询新二维码路径
	 */
	@Override
	public String getQRCode(String code) {
		return vendingMachinesInfoDaoImpl.getQRCode(code);
	}

	/**
	 * 根据当前用户 查询出本公司以及子公司的售货机信息
	 */
	public List<VendingMachinesInfoBean> getMachinesInfo(String code, Long companyId) {
		return vendingMachinesInfoDaoImpl.getMachinesInfo(code, companyId);
	}

	/**
	 * 根据线路Id查询所有的售货机
	 *
	 * @param lineId
	 * @return
	 */
	public ReturnDataUtil findMachinesListPage(VendingMachinesInfoCondition condition) {
		return vendingMachinesInfoDaoImpl.findMachinesListPage(condition);
	}

	@Override
	public ReturnDataUtil untieVendingMachine(UntieBean untieBean) {
		return vendingMachinesInfoDaoImpl.untieVendingMachine(untieBean);
	}

	@Override
	public List<WayDto1> findAllWayAndItem(String vmCode) {
		//查询机器的所有货道
		List<WayDto1> list = vendingMachinesWayDao.listAll1(vmCode);
		//查询该机器商品种类
		int count = vendingMachinesWayDaoImpl.findItemCoutItem(vmCode,2);
		log.info("商品种类=="+count);
		//查询售货机一个月所有商品的销量总和
		ListSaleNumCondition condition=new ListSaleNumCondition();
		condition.setVmCode(vmCode);
		Date date = new Date();
		 Calendar rightNow = Calendar.getInstance();  
        rightNow.setTime(date);  
        rightNow.add(Calendar.MONTH, -1);  
	    Date dt1 = rightNow.getTime();  
	    String startTime = DateUtil.formatYYYYMMDD(dt1); 
		condition.setStartTime(startTime);
		condition.setEndTime(DateUtil.formatYYYYMMDD(date));
		ReturnDataUtil returnData = payRecordDaoImpl.listSaleNum(condition);
        List<ListSaleNumDTO> list1 = (List<ListSaleNumDTO>)returnData.getReturnObject();
        //商品的销量总和
        int coutSalesVolume=0;
       for (ListSaleNumDTO listSaleNumDTO : list1) {
    	   coutSalesVolume+=listSaleNumDTO.getNum();
       }
       log.info("商品的销量总和=="+coutSalesVolume);
		for (WayDto1 wayDto1 : list) {
			Long wayId = wayDto1.getWayId();
			List<WayItem> itemList = wayItemDao.listWayItem(wayId);
			if(itemList!=null&&itemList.size()>0) {
				 for (ListSaleNumDTO listSaleNumDTO : list1) {
					 for (WayItem wayItem : itemList) {
							if(listSaleNumDTO.getWayNumber()==wayDto1.getWayNumber()&&listSaleNumDTO.getBasicItemId().equals(wayItem.getBasicItemId())) {
								//计算最优货道容量推荐值
								BigDecimal a = new BigDecimal((listSaleNumDTO.getNum()*wayItem.getFullNum().intValue()*count));
								BigDecimal b = new BigDecimal(coutSalesVolume);
								BigDecimal newNumber=a.divide(b,0,BigDecimal.ROUND_HALF_UP);
								wayItem.setRecommendCapacity(newNumber.intValue());
							}
						}
			       }
			}
			wayDto1.setItemList(itemList);
		}
		return list;
	}
	
	@Override
	public List<WayDto1> findAllWayAndItemForExport(String vmCode) {
		List<WayDto1> list = vendingMachinesWayDao.listAll1(vmCode);
		for (WayDto1 wayDto1 : list) {
			Long wayId = wayDto1.getWayId();
			List<WayItem> itemList = wayItemDao.listWayItem(wayId);
			wayDto1.setItemList(itemList);
		}
		return list;
	}

	@Override
	public VmbaseInfoDto getBaseInfo(String vmCode) {
		return vendingMachinesInfoDaoImpl.getBaseInfo(vmCode);
	}
	
	@Override
	public MachinesLAC getMachinesLAC(String vmCode) {
		log.info("<VendingMachinesInfoServiceImpl--getMachinesLAC--start>");
		MachinesLAC machinesLAC = vendingMachinesInfoDaoImpl.getMachinesLAC(vmCode);
		log.info("<VendingMachinesInfoServiceImpl--getMachinesLAC--end>");
		return machinesLAC;
	}
}