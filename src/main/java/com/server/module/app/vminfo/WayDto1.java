package com.server.module.app.vminfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WayDto1 {
    @JsonIgnore
    private String itemName;
    private int num;

    private int fullNum;
    @JsonIgnore
    private String vmCode;
    private int wayNumber;
    @JsonIgnore
    private String simpleName;
    private String name;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getFullNum() {
        return fullNum;
    }

    public void setFullNum(int fullNum) {
        this.fullNum = fullNum;
    }

    public String getVmCode() {
        return vmCode;
    }

    public void setVmCode(String vmCode) {
        this.vmCode = vmCode;
    }

    public int getWayNumber() {
        return wayNumber;
    }

    public void setWayNumber(int wayNumber) {
        this.wayNumber = wayNumber;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
