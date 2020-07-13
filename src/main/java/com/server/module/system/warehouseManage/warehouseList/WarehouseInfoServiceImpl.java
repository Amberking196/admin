package com.server.module.system.warehouseManage.warehouseList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;
import java.util.List;

import com.server.module.system.adminUser.AdminUserDao;
import com.server.module.system.adminUser.UserVoForSelect;
import com.server.module.system.companyManage.CompanyDao;
import com.server.module.system.warehouseManage.warehouseAdmin.WarehouseAdminBean;
import com.server.module.system.warehouseManage.warehouseAdmin.WarehouseAdminDao;
import com.server.util.ReturnDataUtil;

import jersey.repackaged.com.google.common.collect.Lists;

/**
 * author name: why create time: 2018-05-14 22:06:48
 */
@Service
public class WarehouseInfoServiceImpl implements WarehouseInfoService {

	public static Logger log = LogManager.getLogger(WarehouseInfoServiceImpl.class); 
	
	@Autowired
	private WarehouseInfoDao warehouseInfoDaoImpl;
	
	@Autowired
	private WarehouseAdminDao warehouseAdminDao;
	
	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private AdminUserDao adminUserDao;
	@Override
	
	/**
	 * 查询仓库列表
	 */
	public ReturnDataUtil listPage(WarehouseInfoForm condition) {
		// TODO Auto-generated method stub
		return warehouseInfoDaoImpl.listPage(condition);
	}
	
	/**
	 * 校验仓库名
	 */
	@Override
	public boolean checkName(String name,Long companyId) {
		// TODO Auto-generated method stub
		return warehouseInfoDaoImpl.checkName(name,companyId);
	}
	
	/**
	 * 修改仓库
	 */
	@Override
	public boolean update(WarehouseInfoBean entity) {
		// TODO Auto-generated method stub
		return warehouseInfoDaoImpl.update(entity);
	}
	
	/**
	 * 删除仓库
	 */
	@Override
	public boolean delete(Object id) {
		// TODO Auto-generated method stub
		return warehouseInfoDaoImpl.delete(id);
	}
	
	/**
	 * 查询单个仓库
	 */
	@Override
	public WarehouseInfoBean get(Object id) {
		// TODO Auto-generated method stub
		return warehouseInfoDaoImpl.get(id);
	}
	
	/**
	 * 增加仓库
	 */
	@Override
	public WarehouseInfoBean insert(WarehouseInfoBean entity) {
		// TODO Auto-generated method stub
		return warehouseInfoDaoImpl.insert(entity);
	}
	

	/**
	 * 查询所有状态为启用的仓库
	 */
	public List<WarehouseInfoBean> findWarehouseInfoBean(){
		return warehouseInfoDaoImpl.findWarehouseInfoBean();
	}
	
	/**
	 * 判断当前用户是否是仓库的负责人
	 */
	public WarehouseInfoBean checkPrincipal(WarehouseInfoForm warehouseInfoForm) {
		return warehouseInfoDaoImpl.checkPrincipal(warehouseInfoForm);
	}

	public List<WarehouseInfoBean> findAllWarehouseInfoBean(){
		return warehouseInfoDaoImpl.findAllWarehouseInfoBean();
	}

    @Override
    public ReturnDataUtil listPageByCompanyId(WarehouseInfoForm warehouseInfoForm) {
		return warehouseInfoDaoImpl.listPageByCompanyId(warehouseInfoForm);
    }
    
    public List<WarehouseVo> listCurrUserWarehouse(){
    	List<WarehouseAdminBean> listAdmin=warehouseAdminDao.listCurrUserWarehouse(); 
    	List<WarehouseVo> list=Lists.newArrayList();
    	for (WarehouseAdminBean admin : listAdmin) {
    		WarehouseVo vo=new WarehouseVo();
    		vo.setId(admin.getWarehouseInfoId());
    		vo.setWarehouseName(admin.getWarehouseName());
    		list.add(vo);
		}
       return list;
    }


	@Override
	public List<UserVoForSelect> listUsersForSelect(Long warehouseId) {
		// TODO Auto-generated method stub
		WarehouseInfoBean warehouse=warehouseInfoDaoImpl.get(warehouseId);
		
		Long companyId=warehouse.getCompanyId();
		
		String sqlIn=companyDao.findAllSonCompanyIdForInSql(companyId.intValue());
		
		List<UserVoForSelect> list=adminUserDao.findUserByCompanyIsSql(sqlIn);
		
		return list;
	}

	@Override
	public ReturnDataUtil findListByUserId(WarehouseInfoForm warehouseInfoForm) {
		return warehouseInfoDaoImpl.findListByForm(warehouseInfoForm);
	}



}
