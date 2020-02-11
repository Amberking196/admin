package com.server.module.app.vminfo;

import com.google.common.collect.Lists;

import java.util.List;

public class VminfoDto {

	/**
	 * 售货机编号
	 */
	private String vmCode;
	/**
	 * 售货机位置
	 */
	private String locationName;
	//公司名称
	private String companyName;
	
	private Integer companyId;

	private int machineVersion;

	private double ratio;//比例

	private double lon;

	private double lat;

	private List<WayDto1> wayList;
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public List<WayDto1> getWayList() {
		if(wayList==null)
			wayList= Lists.newArrayList();
		return wayList;
	}

	public int getMachineVersion() {
		return machineVersion;
	}

	public void setMachineVersion(int machineVersion) {
		this.machineVersion = machineVersion;
	}

	public void setWayList(List<WayDto1> wayList) {
		this.wayList = wayList;
	}
}
