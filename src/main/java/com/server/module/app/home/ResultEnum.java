package com.server.module.app.home;
public enum ResultEnum{
	UNKOWN_ERROR(-2,"未知错误"),
	NOT_LOGIN(-1,"登录失败或登录超时"),
	ERROR(0,"失败"),
	SUCCESS(1,"成功"),
	ILLEGAL_PARAMS (2, "非法参数"),
	ADD_FAILED (3, "添加失败"),
	UPDATE_FAILED (5, "更新失败"),
	DELETE_FAILED (7, "删除失败"),
	ILLEGAL_STATE(9, "非法状态"),
	RECORD_LIMIT(10, "记录数量达到最大值"),
	COMPANY_NOT_EXIST(11, "找不到所属公司"),

	DAO_DUPLICATE(101, "该记录已存在"),
	
	CUSTOMER_NOT_EXIST(11, "账户不存在"),
	MACHINE_NOT_EXIST(12, "机器不存在"),
	WAY_NOT_EXIST(13, "货道不存在"),
	
	USER_NOT_CORRECT(14,"账号或密码错误"),
	USER_INVALID(15,"用户已禁用"),
	USER_HASBIND(16,"用户已绑定支付宝账号,请联系管理员"),
	NOT_AUTHORIZED(17,"抱歉,你没有操作权限"),
	NOT_REALNAME(18,"请实名验证后，再购物"),
	ITEM_NOT_EXIST(21, "商品不存在"),
	
	ORDER_NOT_EXIST(31, "订单不存在"),
	ORDER_NOT_ALLOWED_PICKUP(32, "该订单已出货或已取消"),
	ORDER_CUSTOMER_NOT_MATCH(33, "订单与客户不匹配"),
	ORDER_NUM_EXCEPTION(34, "订单的商品数异常"),
	ORDER_WAY_NUM_NOT_ENOUGH(35, "货道库存不足"),
	ORDER_PAY_TIME_OUT(36,"订单超时未支付"),
	ORDER_MAINTENANCE_REFUND(37,"进入维护"),
	ORDER_WAIT_PAY(38,"等待支付"),
	ORDER_WECHAT_WAIT_PAY(39,"微信反扫码等待支付"),
	ORDER_ALIPAY_WAIT_PAY(40,"支付宝反扫码等待支付"),
	
	WECHAT_MP_ERROR(41, "微信公众号错误"),
	ALIPAY_ERROR(42, "支付宝生活号错误"),
	ALIPAY_CUTPAYMENT_FAIL(43,"支付失败,请确认账户余额是否充足"),
	ALIPAY_AGREEMENT_FAIL(43,"支付失败,请确认是否与本公司签订免密协议"),

	WXPAY_NOTIFY_MONEY_VERIFY_ERROR(51, "微信支付异步通知金额校验失败"),
	WXPAY_REFUND_ERROR(52, "微信支付异步通知金额校验失败"),
	ALIPAY_NOTIFY_MONEY_VERIFY_ERROR(53, "支付宝支付异步通知金额校验失败"),
	ALIPAY_REFUND_ERROR(54, "支付宝退款支付异步通知金额校验失败"),
	PAY_NOTIFY_MONEY_VERIFY_ERROR(55, "支付异步通知金额校验失败"),
	
	VERIFYCODE_UNCORRECT(61,"手机验证码不正确"),
	VERIFYCODE_TIMEOUT(62,"验证超时,请重新获取验证码"),
	
	WECHAT_CLOSE_ORDER_FAIL(71,"微信关闭订单失败"),
	ALIPAY_CLOSE_ORDER_FAIL(72,"支付宝关闭订单失败"),
	WECHAT_REFUND_ORDER_FAIL(73,"微信关闭订单失败"),
	ALIPAY_REFUND_ORDER_FAIL(74,"支付宝关闭订单失败"),
	MICROPAY_WECHAT_ERROR(75,"微信反扫码支付失败"),
	MICROPAY_ALIPAY_ERROR(76,"支付宝反扫码支付失败"),
	
	GAME_TIMES_NOT_ENOUGH(81,"亲，购物完成后才能参与一次抽奖哟！"),
	GAME_SUCCESS(82,"亲，恭喜你获得一次抽奖机会，请前往抽奖吧！"),
	
	NOT_SUPPORT(1001, "暂时不支持该操作"),
	AUTH_FAILED(1002, "认证失败"),
	AUTH_TIMEOUT(1003, "认证超时"),
	
	MACHINES_PROGRAM_PVERSION_NOT_EXISTS(1101,"机器父级主控版本不存在"),
	MACHINES_PROGRAM_VERSION_EXISTS(1102,"机器主控版本已存在，请勿重复!"),
	MACHINES_PROGRAM_VERSION_CANT_BLANK(1103,"机器主控版本不能为空!"),
	CACHE_ERROR(2001, "缓存异常"),
	CACHE_GET_ERROR(2002, "获取缓存失败"),
	

	TCP_COOKIES_NOT_EXIST(3001, "tcp.cookies不存在"),
	TCP_TASK_TIME_OUT(3002, "tcp任务超时"),
	
	MECHINE_NOT_FREE(4001,"温馨提示:门已开，请稍后！！！"),
	PHONE_NOT_VERIFY(4002,"温馨提示:支付宝未进行手机认证,请先手机验证!!!"),
	ORDER_NOT_PAY(4003,"您还有尚未付款的订单,请付清订单后再使用,谢谢合作!!!"),
	ITEM_SOLD_OUT(4004,"温馨提示:该商品已售罄,请尝试选择其他门!!!"),
	SERVER_INNER_ERROR(4005,"网络故障,请稍候再试!!!"),
	// 运维人员的提示
	OPEN_DOOR_ERROR(4006,"开门失败,检查系统是否正常!!!"),
	FORWARD_SIGN_PAGE(4007,"跳转签约页面"),
	DOOR_NOT_AVAILABLE(4008,"温馨提示:当前选择门号不可用,请选择其他门号!!!"),
	
	REFUND_SUCCESS(5001,"退款操作成功"),
	REFUND_NOT_AUTHORIZED(5002,"无权进行退款操作"),
	REFUND_ERROR(5003,"退款失败"),
	REFUND_PROCESS_ERROR(5004,"退款程序异常"),
	REFUND_ORDER_NOT_EXISTS(5005,"订单不存在，退款失败"),
	REFUND_ORDER_ERROR(5006,"退款订单异常，无法退款"),
	REFUND_PRICE_MISMATCH(5007,"退款金额不匹配"),
	REFUND_APPLICATION_FAIL(5008,"退款申请失败"),
	
	BARGAIN_FAIL(6002,"砍价失败"),
	BARGAIN_TIMES_NOT_ENOUGH(6003,"每天3次已用完，砍价次数不足"),
	BARGAINED(6004,"该订单已砍价成功"),
	BARGAIN_END(6005,"该订单已砍价已结束"),
	BARGAIN_NOT_FOUNT(6006,"砍价订单不存在")
	;
	
	private Integer code;
	private String message;
	/**
	 * @param code
	 * @param message
	 */
	private ResultEnum(Integer code, String message) {
		this.code = code;
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	public static String getMessage(Integer code) {
		for (ResultEnum c : ResultEnum.values()) {
			if (c.getCode() == code) {
				return c.getMessage();
			}
		}
		return null;
	}
}
