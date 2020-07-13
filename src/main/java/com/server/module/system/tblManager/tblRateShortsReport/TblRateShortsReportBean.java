package com.server.module.system.tblManager.tblRateShortsReport;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.common.persistence.Entity;
/**
 * table name:  tbl_rate_shorts_report
 * author name: yjr
 * create time: 2018-03-28 08:58:56
 */ 
@Entity(tableName="tbl_rate_shorts_report",id="",idGenerate="auto")
public class TblRateShortsReportBean{


@JsonIgnore	public String tableName="tbl_rate_shorts_report";
@JsonIgnore	public String selectSql="select * from tbl_rate_shorts_report where 1=1 ";
@JsonIgnore	public String selectSql1="select id,vmCode,rateShorts,address,name,reportDate,companyName,companyId from tbl_rate_shorts_report where 1=1 ";
	private Long id;
	private String vmCode;
	private Float rateShorts;
	private String address;
	private String name;
	private Date reportDate;
	private String companyName;
	private Long companyId;

	public void setId(Long id){
		this.id=id;
	}
	public Long getId(){
		return id;
	}
	public void setVmCode(String vmCode){
		this.vmCode=vmCode;
	}
	public String getVmCode(){
		return vmCode;
	}
	public void setRateShorts(float rateShorts){
		this.rateShorts=rateShorts;
	}
	public Float getRateShorts(){
		return rateShorts;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setReportDate(Date reportDate){
		this.reportDate=reportDate;
	}
	public Date getReportDate(){
		return reportDate;
	}
	public void setCompanyName(String companyName){
		this.companyName=companyName;
	}
	public String getCompanyName(){
		return companyName;
	}
	public void setCompanyId(Long companyId){
		this.companyId=companyId;
	}
	public Long getCompanyId(){
		return companyId;
	}
}

