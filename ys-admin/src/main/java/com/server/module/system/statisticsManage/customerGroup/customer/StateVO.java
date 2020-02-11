package com.server.module.system.statisticsManage.customerGroup.customer;

import lombok.Data;

@Data
public class StateVO {
    private Integer state;
    private String label;
    public StateVO(Integer state,String label){
        this.state=state;
        this.label=label;
    }
}
