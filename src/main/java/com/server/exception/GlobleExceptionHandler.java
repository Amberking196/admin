package com.server.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.module.app.home.ResultEnum;
import com.server.module.customer.CustomerUtil;
import com.server.util.ResultUtil;
import com.server.util.ReturnDataUtil;

@ControllerAdvice
public class GlobleExceptionHandler {

	public static Logger log = LogManager.getLogger(GlobleExceptionHandler.class); 
	
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	public ReturnDataUtil handle(IllegalArgumentException e) {
		log.error("IllegalArgumentException",e);
		e.printStackTrace();
		return ResultUtil.error(ResultEnum.ILLEGAL_PARAMS,e.getMessage());
	}
	
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public ReturnDataUtil handle(RuntimeException e) {
		log.info("某用户奇怪的异常操作"+CustomerUtil.getCustomerId());
		log.error("RuntimeException",e);
		e.printStackTrace();
		return ResultUtil.error(ResultEnum.ERROR,e.getMessage());
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ReturnDataUtil handle(Exception e) {
		log.error("Exception",e);
		e.printStackTrace();
		return ResultUtil.error(ResultEnum.UNKOWN_ERROR,e.getMessage());
	}
}
