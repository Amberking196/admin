package com.server.module.system.adminUser;
 

public class AdminUserBean {
	
	//自增标识	
	private Long id;
	//公司标识	
	private int companyId;
	//登录账号	
	private String loginCode;
	//密码	
	private String password;
	//状态 1=可用 2=禁用	
	private int status;
	//名字	
	private String name;
	//雇用时间	
	private String hireDate;
	//codeNo	
	private String codeNo;
	//部门
	private int departMent;
	//岗位
	private int post;
	//角色 可以拥有多个角色	
	private String role;
	//手机号码	
	private String phone;
	//登录类型	
	private int loginModel;
	//邮箱	
	private String mail;  
	//创建人
	private Long founder;
	//是否为管理员
	private Integer isPrincipal;

	// 员工等级  1 公司，2区域，3线路
	private Integer level;
	// 区域
	private Integer areaId;
	// 线路
	private Integer lineId;
	// 外派人员  默认不是外派人员-->0 ，1表示外派人员
	private Integer isExpatriate;
	// 删除标识 0未删除  1已删除
	private Integer deleteFlag;

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public Integer getIsExpatriate() {
		return isExpatriate;
	}

	public void setIsExpatriate(Integer isExpatriate) {
		this.isExpatriate = isExpatriate;
	}

	private int currentPage;
    private int totalPage;
    
    
	public Integer getIsPrincipal() {
		return isPrincipal;
	}
	public void setIsPrincipal(Integer isPrincipal) {
		this.isPrincipal = isPrincipal;
	}
	public Long getFounder() {
		return founder;
	}
	public void setFounder(Long founder) {
		this.founder = founder;
	}
	public int getPost() {
		return post;
	}
	public void setPost(int post) {
		this.post = post;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHireDate() {
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	public String getCodeNo() {
		return codeNo;
	}
	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}
	public int getDepartMent() {
		return departMent;
	}
	public void setDepartMent(int departMent) {
		this.departMent = departMent;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	} 
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getLoginModel() {
		return loginModel;
	}
	public void setLoginModel(int loginModel) {
		this.loginModel = loginModel;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
    
    
     
}