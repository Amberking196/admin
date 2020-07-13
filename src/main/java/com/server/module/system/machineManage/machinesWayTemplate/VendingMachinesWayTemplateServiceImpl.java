package com.server.module.system.machineManage.machinesWayTemplate;

import com.server.util.ReturnDataUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:19:25
 */
@Service
public class VendingMachinesWayTemplateServiceImpl implements VendingMachinesWayTemplateService {

    private static Log log = LogFactory.getLog(VendingMachinesWayTemplateServiceImpl.class);
    @Autowired
    private VendingMachinesWayTemplateDao vendingMachinesWayTemplateDaoImpl;

    public ReturnDataUtil listTemplateName(VendingMachinesWayTemplateCondition condition) {
        return vendingMachinesWayTemplateDaoImpl.listTemplateName(condition);
    }

    public VendingMachinesWayTemplateBean add(VendingMachinesWayTemplateBean entity) {
        return vendingMachinesWayTemplateDaoImpl.insert(entity);
    }

    @Override
    public ReturnDataUtil listDetailsById(Long templateId) {
        return vendingMachinesWayTemplateDaoImpl.listDetailsById(templateId);
    }

    @Override
    public boolean checkTemplateName(String templateName) {
        return vendingMachinesWayTemplateDaoImpl.checkTemplateName( templateName);
    }

    @Override
    public ReturnDataUtil listPage(VendingMachinesWayTemplateCondition condition) {
        return vendingMachinesWayTemplateDaoImpl.listPage(condition);
    }

    @Override
    public ReturnDataUtil listOwnTemplate(VendingMachinesWayTemplateCondition condition) {
        return vendingMachinesWayTemplateDaoImpl.listOwnTemplate(condition);
    }



    public boolean update(VendingMachinesWayTemplateBean entity) {
    	//设置更新时间
    	entity.setUpdateTime(new Date());
        return vendingMachinesWayTemplateDaoImpl.update(entity);
    }

    public boolean del(Object id) {
        return vendingMachinesWayTemplateDaoImpl.delete(id);
    }

    public List<VendingMachinesWayTemplateBean> list(VendingMachinesWayTemplateCondition condition) {
        return null;
    }

    public VendingMachinesWayTemplateBean get(Object id) {
        return vendingMachinesWayTemplateDaoImpl.get(id);
    }
}

