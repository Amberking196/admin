package com.server.module.system.machineManage.machineReplenish;


import java.util.List;
import java.util.Map;

public class MachineReplenishBean {

    //机器名
    private String vmCode;

    //货道1总量
    private Integer door1Num;
    //货道2总量
    private Integer door2Num;
    //货道3总量
    private Integer door3Num;
    //货道4总量
    private Integer door4Num;

    // 货道商品名称
    Map<Object,Object> itemNames;
    public Map<Object, Object> getMap() {
        return itemNames;
    }

    public void setMap(Map<Object, Object> map) {
        this.itemNames = map;
    }


    public String getVmCode() {
        return vmCode;
    }

    public void setVmCode(String vmCode) {
        this.vmCode = vmCode;
    }

    public Integer getDoor1Num() {
        return door1Num;
    }

    public void setDoor1Num(Integer door1Num) {
        this.door1Num = door1Num;
    }

    public Integer getDoor2Num() {
        return door2Num;
    }

    public void setDoor2Num(Integer door2Num) {
        this.door2Num = door2Num;
    }

    public Integer getDoor3Num() {
        return door3Num;
    }

    public void setDoor3Num(Integer door3Num) {
        this.door3Num = door3Num;
    }

    public Integer getDoor4Num() {
        return door4Num;
    }

    public void setDoor4Num(Integer door4Num) {
        this.door4Num = door4Num;
    }
}
