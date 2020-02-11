package com.server.util;

import java.math.BigDecimal;

public class ArithmeticUtil {

	
	/**
	 * 计算本次能够退款最大金额
	 * @author why
	 * @date 2019年3月19日 下午3:25:14 
	 * @param orderMoney  订单金额
	 * @param balance 用户余额
	 * @return
	 */
	public static BigDecimal result(BigDecimal orderMoney, BigDecimal balance) {
		BigDecimal result = new BigDecimal(0);
		BigDecimal calculate = calculate(orderMoney);
		for (int i = 0; i < 5; i++) {
			if (i == 0) {
				result = (balance.subtract(calculate).add(orderMoney.multiply(BigDecimal.valueOf(0))))
						.divide(BigDecimal.valueOf(1),0,BigDecimal.ROUND_DOWN);
				if (probability(result).compareTo(BigDecimal.valueOf(0)) == 0) {
					return result;
				}
			}
			if (i == 1) {
				result = (balance.subtract(calculate).add(orderMoney.multiply(BigDecimal.valueOf(0.04))))
						.divide(BigDecimal.valueOf(1.04),0,BigDecimal.ROUND_DOWN);
				if (probability(result).compareTo(BigDecimal.valueOf(0.04)) == 0) {
					return result;
				}
			}
			if (i == 2) {
				result = (balance.subtract(calculate).add(orderMoney.multiply(BigDecimal.valueOf(0.06))))
						.divide(BigDecimal.valueOf(1.06),0,BigDecimal.ROUND_DOWN);
				if (probability(result).compareTo(BigDecimal.valueOf(0.06)) == 0) {
					return result;
				}
			}
			if (i == 3) {
				result = (balance.subtract(calculate).add(orderMoney.multiply(BigDecimal.valueOf(0.08))))
						.divide(BigDecimal.valueOf(1.08),0,BigDecimal.ROUND_DOWN);
				if (probability(result).compareTo(BigDecimal.valueOf(0.08)) == 0) {
					return result;
				}
			}
			if (i == 4) {
				result = (balance.subtract(calculate).add(orderMoney.multiply(BigDecimal.valueOf(0.1))))
						.divide(BigDecimal.valueOf(1.1),0,BigDecimal.ROUND_DOWN);
				if (probability(result).compareTo(BigDecimal.valueOf(0.1)) == 0) {
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * 计算赠送金额
	 * 
	 * @author why
	 * @date 2019年3月18日 下午5:45:45
	 * @param price
	 * @return
	 */
	public static BigDecimal calculate(BigDecimal price) {
		BigDecimal multiply = new BigDecimal(0);
		if (price.compareTo(BigDecimal.valueOf(50)) >= 0 && price.compareTo(BigDecimal.valueOf(100)) < 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.04));
			// 金额单次 100<=price<200
		} else if (price.compareTo(BigDecimal.valueOf(100)) >= 0 && price.compareTo(BigDecimal.valueOf(200)) < 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.06));
			// 金额单次 200<=price<500
		} else if (price.compareTo(BigDecimal.valueOf(200)) >= 0 && price.compareTo(BigDecimal.valueOf(500)) < 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.08));
			// 金额单次 price>=500
		} else if (price.compareTo(BigDecimal.valueOf(500)) >= 0) {
			multiply = price.multiply(BigDecimal.valueOf(0.1));
		}
		return multiply;
	}

	/**
	 * 计算金额的概率
	 * @author why
	 * @date 2019年3月19日 下午3:21:21 
	 * @param all
	 * @return
	 */
	public static BigDecimal probability(BigDecimal all) {
		BigDecimal multiply = new BigDecimal(0);
		if (all.compareTo(BigDecimal.valueOf(50)) >= 0 && all.compareTo(BigDecimal.valueOf(100)) < 0) {
			multiply = BigDecimal.valueOf(0.04);
			// 金额单次 100<=price<200
		} else if (all.compareTo(BigDecimal.valueOf(100)) >= 0 && all.compareTo(BigDecimal.valueOf(200)) < 0) {
			multiply = BigDecimal.valueOf(0.06);
			// 金额单次 200<=price<500
		} else if (all.compareTo(BigDecimal.valueOf(200)) >= 0 && all.compareTo(BigDecimal.valueOf(500)) < 0) {
			multiply = BigDecimal.valueOf(0.08);
			// 金额单次 price>=500
		} else if (all.compareTo(BigDecimal.valueOf(500)) >= 0) {
			multiply = BigDecimal.valueOf(0.1);
		}
		return multiply;
	}
}
