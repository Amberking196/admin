package com.server.module.system.machineManage.machinesWayTemplateDetail;

import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayBean;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayDao;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayDaoImpl;
import com.server.module.system.machineManage.machinesWayTemplate.WayItemDto;
import com.server.util.ReturnDataUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EmptyStackException;
import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:35:22
 */
@Service
public class VendingMachinesWayTemplateDelServiceImpl implements VendingMachinesWayTemplateDelService {

    private static Log log = LogFactory.getLog(VendingMachinesWayTemplateDelServiceImpl.class);
    @Autowired
    private VendingMachinesWayTemplateDelDao vendingMachinesWayTemplateDelDaoImpl;
    @Autowired
    private VendingMachinesWayDao vendingMachinesWayDaoImpl;



    public ReturnDataUtil listPage(VendingMachinesWayTemplateDelCondition condition) {
        return vendingMachinesWayTemplateDelDaoImpl.listPage(condition);
    }

    public VendingMachinesWayTemplateDelBean add(VendingMachinesWayTemplateDelBean entity) {
        return vendingMachinesWayTemplateDelDaoImpl.insert(entity);
    }

    @Override
    public ReturnDataUtil addItemToWay(ItemToWayTemplateDetailDto dto) {

        boolean b = vendingMachinesWayTemplateDelDaoImpl.addItemToWay(dto);
        return new ReturnDataUtil(b);
    }

    @Override
    public VendingMachinesWayBean addBeanToMachineWay(VendingMachinesWayBean entity) {
        return vendingMachinesWayDaoImpl.insert(entity);
    }

    @Override
    public ReturnDataUtil checkWayNum(VendingMachinesWayTemplateDelBean entity) {

        return vendingMachinesWayTemplateDelDaoImpl.checkWayNum(entity);
    }

    @Override
    public ReturnDataUtil updateDetailAndItem(WayItemDto entity) {
        return vendingMachinesWayTemplateDelDaoImpl.updateDetailAndItem(entity);
    }

    @Override
    public List<VendingMachinesWayTemplateDelBean> findDetailsByTemplateId(Long templateId) {
        return vendingMachinesWayTemplateDelDaoImpl.findDetailsByTemplateId(templateId);
    }


    public boolean update(VendingMachinesWayTemplateDelBean entity) {
        return vendingMachinesWayTemplateDelDaoImpl.update(entity);
    }

    public boolean del(Object entity) {
        return vendingMachinesWayTemplateDelDaoImpl.delete(entity);
    }

    public List<VendingMachinesWayTemplateDelBean> list(VendingMachinesWayTemplateDelCondition condition) {
        return null;
    }

    public VendingMachinesWayTemplateDelBean get(Object id) {
        return vendingMachinesWayTemplateDelDaoImpl.get(id);
    }

	@Override
	public ReturnDataUtil checkWayNum(ItemToWayTemplateDetailDto dto) {
		// TODO Auto-generated method stub
		return vendingMachinesWayTemplateDelDaoImpl.checkWayNum(dto);
	}
}

