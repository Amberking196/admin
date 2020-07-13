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
public interface VendingMachinesWayTemplateDelDao {

    public ReturnDataUtil listPage(VendingMachinesWayTemplateDelCondition condition);

    public List<VendingMachinesWayTemplateDelBean> list(VendingMachinesWayTemplateDelCondition condition);

    public boolean update(VendingMachinesWayTemplateDelBean entity);

    public boolean delete(Object id);

    public VendingMachinesWayTemplateDelBean get(Object id);

    public VendingMachinesWayTemplateDelBean insert(VendingMachinesWayTemplateDelBean entity);


    /**
     * 将商品绑定到货道上(不生成价格库记录)
     *
     * @param dto
     * @return
     */
    public boolean addItemToWay(ItemToWayTemplateDetailDto dto);


    /**
     * 查询要添加的货道号是否已存在
     * @param entity
     * @return
     */
    ReturnDataUtil checkWayNum(VendingMachinesWayTemplateDelBean entity);

    /**
     * 修改具体模板的容量/价钱
     *
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
     * 	验证添加商品到货道时是否存在该货道
    * @param dto
    * @return
    */
	public ReturnDataUtil checkWayNum(ItemToWayTemplateDetailDto dto);

}

