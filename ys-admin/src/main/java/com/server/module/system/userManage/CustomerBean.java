package com.server.module.system.userManage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.server.common.persistence.NotField;

import lombok.Data;
@Data
public class CustomerBean {

	private Long id;  //自增标识
	private String openId; //微信openId
	private Integer type;//账户类型
	private String alipayUserId; //支付宝ID
	private String phone; //用户手机号(支付宝号)
	private String nickName; //昵称
	private String UserName; //用户名
	private Integer sexId; // 性别
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime; //注册时间/创建时间
	private Date updateTime; //注册时间
	private String vmCode;//用户所注册的售货机的编号
    private String city;  //varchar(64) DEFAULT NULL COMMENT '城市',
    private String province;  //varchar(64) DEFAULT NULL COMMENT '省份',
    private String country;  //varchar(64) DEFAULT NULL COMMENT '国家',
    private String headImgUrl;  //varchar(512) DEFAULT NULL COMMENT '头像',
    private Double latitude;  //double(6,2) DEFAULT '0.00' COMMENT '纬度',
    private Double longitude;  //double(6,2) DEFAULT '0.00' COMMENT '经度',
    private String isCertified;  //varchar(1) DEFAULT NULL COMMENT '是否实名认证 T是通过 F是没有实名认证 ',
    private String isStudentCertified;  //varchar(1) DEFAULT NULL COMMENT '否是学生',
    private String userStatus;  //varchar(1) DEFAULT NULL COMMENT '用户状态：Q 代表快速注册用户 T代表已认证用户 B代表被冻结账户 W代表已注册，未激活的账户',
    private String userType;  //varchar(1) DEFAULT NULL COMMENT '1 代表公司账户 2 代表个人账户',
    private String createId;  //varchar(20) DEFAULT '‘’' COMMENT '创建人ID',
    private String lastUpdateId;  //varchar(20) DEFAULT NULL COMMENT '最后更新人',
    private Integer deleteFlag;  //smallint(1) DEFAULT '0' COMMENT '删除标识: 0 正常，1 已删除',
    private String locationName; //用户位置
    private  Integer integral;//积分
    private Long inviterId;//邀请人id
    private BigDecimal userBalance;//用户余额
    @NotField
    private Integer num;//已邀请人数
    @NotField
    private Integer realNum;//已邀请人数(已消费)
    @NotField
    private String state;//用户是否已经消费
    
    public String getLocationName() {
    	return locationName;
    }
    public void setLocationName(String locationName) {
    	this.locationName=locationName;
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getVmCode() {
		return vmCode;
	}
	
	public void setVmCode(String vmCode) {
		this.vmCode=vmCode;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAlipayUserId() {
		return alipayUserId;
	}
	public void setAlipayUserId(String alipayUserId) {
		this.alipayUserId = alipayUserId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public Integer getSexId() {
		return sexId;
	}
	public void setSexId(Integer sexId) {
		this.sexId = sexId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public String getIsCertified() {
		return isCertified;
	}
	public void setIsCertified(String isCertified) {
		this.isCertified = isCertified;
	}
	public String getIsStudentCertified() {
		return isStudentCertified;
	}
	public void setIsStudentCertified(String isStudentCertified) {
		this.isStudentCertified = isStudentCertified;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getLastUpdateId() {
		return lastUpdateId;
	}
	public void setLastUpdateId(String lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
    
}
