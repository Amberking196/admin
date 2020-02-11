package com.server.util;

import com.server.module.app.home.ResultEnum;

public class ResultUtil {

	public static ReturnDataUtil success(){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(1);
		returnData.setMessage("成功");
		return returnData;
	}
	
	public static ReturnDataUtil success(Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(1);
		returnData.setMessage("成功");
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil success(ResultEnum resultEnum){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(resultEnum.getCode());
		returnData.setMessage(resultEnum.getMessage());
		return returnData;
	}
	
	public static ReturnDataUtil success(ResultEnum resultEnum,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(resultEnum.getCode());
		returnData.setMessage(resultEnum.getMessage());
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil success(Integer status,String message,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(status);
		returnData.setMessage(message);
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil success(Integer status,String message,Object returnObject,
			Integer currentPage,Long total){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(status);
		returnData.setMessage(message);
		returnData.setReturnObject(returnObject);
		returnData.setCurrentPage(currentPage);
		returnData.setTotal(total);
		returnData.setTotalPage(returnData.getTotalPage());
		return returnData;
	}
	
	public static ReturnDataUtil success(Object returnObject,Integer currentPage,Long total){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(1);
		returnData.setMessage("成功");
		returnData.setReturnObject(returnObject);
		returnData.setCurrentPage(currentPage);
		returnData.setTotal(total);
		returnData.setTotalPage(returnData.getTotalPage());
		return returnData;
	}
	
	public static ReturnDataUtil error(){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(0);
		returnData.setMessage("失败");
		return returnData;
	}
	public static ReturnDataUtil selectError(){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(0);
		returnData.setMessage("查询无数据");
		return returnData;
	}
	
	public static ReturnDataUtil selectError(Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(0);
		returnData.setMessage("查询无数据");
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil error(Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(0);
		returnData.setMessage("失败");
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil error(Integer status,String message,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(status);
		returnData.setMessage(message);
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil error(Object returnObject,Integer currentPage,Long total){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(0);
		returnData.setMessage("失败");
		returnData.setReturnObject(returnObject);
		returnData.setCurrentPage(currentPage);
		returnData.setTotal(total);
		returnData.setTotalPage(returnData.getTotalPage());
		return returnData;
	}
	
	public static ReturnDataUtil error(ResultEnum resultEnum){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(resultEnum.getCode());
		returnData.setMessage(resultEnum.getMessage());
		return returnData;
	}
	
	public static ReturnDataUtil error(ResultEnum resultEnum,Object returnObject){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(resultEnum.getCode());
		returnData.setMessage(resultEnum.getMessage());
		returnData.setReturnObject(returnObject);
		return returnData;
	}
	
	public static ReturnDataUtil error(Integer status,String message,Object returnObject,
			Integer currentPage,Long total){
		ReturnDataUtil returnData = new ReturnDataUtil();
		returnData.setStatus(status);
		returnData.setMessage(message);
		returnData.setReturnObject(returnObject);
		returnData.setCurrentPage(currentPage);
		returnData.setTotal(total);
		returnData.setTotalPage(returnData.getTotalPage());
		return returnData;
	}
	
	
	
	
}
