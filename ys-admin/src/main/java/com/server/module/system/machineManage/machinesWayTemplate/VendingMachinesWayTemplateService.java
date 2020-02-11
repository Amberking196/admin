package com.server.module.system.machineManage.machinesWayTemplate;

import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:19:25
 */
public interface VendingMachinesWayTemplateService {


    public ReturnDataUtil listTemplateName(VendingMachinesWayTemplateCondition condition);

    public List<VendingMachinesWayTemplateBean> list(VendingMachinesWayTemplateCondition condition);

    public boolean update(VendingMachinesWayTemplateBean entity);

    public boolean del(Object id);

    public VendingMachinesWayTemplateBean get(Object id);

    public VendingMachinesWayTemplateBean add(VendingMachinesWayTemplateBean entity);

    /**
     * 根据id查询出详情
     * @param templateId
     * @return
     */
    public ReturnDataUtil listDetailsById(Long templateId);


    /**
     * 检查是否有重名的模板了
     * true表示已经有了
     * false表示还没有
     *
     * @param templateName
     * @return
     */
    boolean checkTemplateName(String templateName);

    /**
     * 模板列表
     * @param condition
     * @return
     */
    public ReturnDataUtil listPage(VendingMachinesWayTemplateCondition condition);



    /**
     * 根据用户Id查询出个人的模板信息
     * @param condition
     * @return
     */
    ReturnDataUtil listOwnTemplate(VendingMachinesWayTemplateCondition condition);
}

