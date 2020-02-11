package com.server.module.system.machineManage.machinesWayTemplateDetail;

import com.server.module.system.machineManage.machineItem.VendingMachinesItemBean;
import com.server.module.system.machineManage.machinesWay.BindItemDto;
import com.server.module.system.machineManage.machinesWay.VendingMachinesWayBean;
import com.server.module.system.machineManage.machinesWayTemplate.WayItemDto;
import com.server.util.ReturnDataUtil;

import java.util.List;

/**
 * author name: zfc
 * create time: 2018-08-03 10:35:22
 */
public interface VendingMachinesWayTemplateDelService {


    public ReturnDataUtil listPage(VendingMachinesWayTemplateDelCondition condition);

    public List<VendingMachinesWayTemplateDelBean> list(VendingMachinesWayTemplateDelCondition condition);

    public boolean update(VendingMachinesWayTemplateDelBean entity);

    public boolean del(Object id);

    public VendingMachinesWayTemplateDelBean get(Object id);

    public VendingMachinesWayTemplateDelBean add(VendingMachinesWayTemplateDelBean entity);


    /**
     * 将商品绑定到货道上
     *
     * @param dto
     * @return
     */
    public ReturnDataUtil addItemToWay(ItemToWayTemplateDetailDto dto);

    /**
     * 将商品详情的数据添加到售货机货道上(插入到售货机货道表上)
     *
     * @param entity
     * @return
     */
    public VendingMachinesWayBean addBeanToMachineWay(VendingMachinesWayBean entity);


    /**
     * 查询添加的货道号是否已存在
     * @param entity
     * @return
     */
    ReturnDataUtil checkWayNum(VendingMachinesWayTemplateDelBean entity);

    /**
     * 修改具体模板的容量/价钱
     * @param entity
     * @return
     */
    ReturnDataUtil updateDetailAndItem(WayItemDto entity);

    /**
     * 根据模板Id查询出所有的模板详情
     * @param templateId
     * @return
     */
    List<VendingMachinesWayTemplateDelBean> findDetailsByTemplateId(Long templateId);
    /**
      * 验证添加商品到货道时是否存在该货道
     * @param dto
     * @return
     */
	public ReturnDataUtil checkWayNum(ItemToWayTemplateDetailDto dto);
}

