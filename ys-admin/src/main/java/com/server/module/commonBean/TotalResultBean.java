package com.server.module.commonBean;

public class TotalResultBean<T> {

	public final static String TOTAL_SQL = " SELECT FOUND_ROWS() AS total";
	
	private Long total;
	private T result;
	
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	
	public TotalResultBean(){}
	
	public TotalResultBean(Long total,T result){
		this.total = total;
		this.result = result;
	}
	
}
