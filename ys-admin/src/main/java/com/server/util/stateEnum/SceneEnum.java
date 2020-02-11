package com.server.util.stateEnum;

import java.util.ArrayList;
import java.util.List;

import com.server.module.commonBean.KeyValueBean;

public enum SceneEnum {


	SCHOOL("SCHOOL", "学校", ""), 
	SCHOOL_001("001", "幼儿园和早教机构", "SCHOOL"), 
	SCHOOL_002("002", "小学", "SCHOOL"), 
	SCHOOL_003("003", "初中", "SCHOOL"), 
	SCHOOL_004("004", "高中&职业技术学院", "SCHOOL"), 
	SCHOOL_005("005", "大学","SCHOOL"), 
	SCHOOL_006("006", "培训机构", "SCHOOL"), 
	SCHOOL_007("007", "其他", "SCHOOL"),

	MALL("MALL", "商业场所", ""), 
	MALL_001("001", "商场", "MALL"), 
	MALL_002("002", "酒店", "MALL"), 
	MALL_003("003", "4S店","MALL"), 
	MALL_004("004", "步行街", "MALL"), 
	MALL_005("005", "其他", "MALL"),

	STATION("STATION", "商业场所", ""), 
	STATION_001("001", "机场", "STATION"), 
	STATION_002("002", "火车站","STATION"), 
	STATION_003("003", "汽车站","STATION"), 
	STATION_004("004", "地铁站", "STATION"), 
	STATION_005("005", "其他", "STATION"),

	FACTORY("FACTORY", "工厂", ""), 
	FACTORY_001("001", "工厂", "FACTORY"),

	COMMUNITY("COMMUNITY", "社区", ""), 
	COMMUNITY_001("001", "社区", "COMMUNITY"),

	OFFICE("OFFICE", "办公楼", ""), 
	OFFICE_001("001", "办公楼", "OFFICE"),

	HOSPITAL("HOSPITAL", "医院", ""), 
	HOSPITAL_001("001", "医院", "HOSPITAL"),

	GOVERNMENT("GOVERNMENT", "政府机构", ""), 
	GOVERNMENT_001("001", "政府机构", "GOVERNMENT"),

	SCENIC_SPOT("SCENIC_SPOT", "旅游景点", ""), 
	SCENIC_SPOT_001("001", "旅游景点", "SCENIC_SPOT"),

	OTHERS("OTHERS", "其他", ""), 
	OTHERS_001("001", "其他", "OTHERS");

	private String state;
	private String name;
	private String scene;

	SceneEnum(String state, String name, String scene) {
		this.state = state;
		this.name = name;
		this.scene = scene;
	}

	public String getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public String getScene() {
		return scene;
	}

	public static List<KeyValueBean> findSonNode(String scene) {
		List<KeyValueBean> sceneList = new ArrayList<KeyValueBean>();
		SceneEnum[] values = SceneEnum.values();
		KeyValueBean keyValue = null;
		for (SceneEnum sceneEnum : values) {
			if(sceneEnum.getScene().equals(scene)){
				keyValue = new KeyValueBean();
				keyValue.setKey(sceneEnum.getState());
				keyValue.setValue(sceneEnum.getName());
				sceneList.add(keyValue);
			}
		}
		return sceneList;
	}

}
