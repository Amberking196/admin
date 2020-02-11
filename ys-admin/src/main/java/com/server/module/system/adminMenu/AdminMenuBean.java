package com.server.module.system.adminMenu;

import java.util.List;

public class AdminMenuBean {

	private Integer id;
	private Integer pid;
	private String name;
	private String url;
	private Integer isShow;
	private List<AdminMenuBean> sonMenu;
	private String wordName;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public List<AdminMenuBean> getSonMenu() {
		return sonMenu;
	}
	public void setSonMenu(List<AdminMenuBean> sonMenu) {
		this.sonMenu = sonMenu;
	}
	public String getWordName() {
		return wordName;
	}
	public void setWordName(String wordName) {
		this.wordName = wordName;
	}
	
	
	
}
