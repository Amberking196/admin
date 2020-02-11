package com.server.module.system.machineManage.machinesWayTemplate;

import com.server.common.persistence.Entity;
import com.server.module.system.machineManage.machinesWayTemplateDetail.VendingMachinesWayTemplateDelBean;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * table name:  vending_machines_way_template
 * author name: zfc
 * create time: 2018-08-03 10:19:25
 */
@Data
@Entity(tableName = "vending_machines_way_template", id = "id", idGenerate = "auto")
public class VendingMachinesWayTemplateBean {


/*    @JsonIgnore
    public String tableName = "vending_machines_way_template";
    @JsonIgnore
    public String selectSql = "select * from vending_machines_way_template where 1=1 ";
    @JsonIgnore
    public String selectSql1 = "select id,templateName,companyId,areaID,userId,updateTime,createTime from vending_machines_way_template where 1=1 ";

    */

    // 模板Id
    private Long id;

    // 模板名称
    private String  templateName;

    // 公司
    private Integer companyId;

    // 区域
    private Integer areaID;

    // 用户
    private Long userId;

    // 创建和更新时间
    private Date updateTime;
    private Date createTime;

    // 一个模板下有多个模板详情
    private List<VendingMachinesWayTemplateDelBean> details;


}

