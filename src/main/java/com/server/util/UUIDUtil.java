package com.server.util;

import java.util.UUID;

/**
 * @author lixiaoying 2017年9月13日 下午4:12:42
 */
public class UUIDUtil {
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] retArray = new String[number];
		for (int i = 0; i < number; i++) {
			retArray[i] = getUUID();
		}
		return retArray;
	}

	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.replaceAll("-", "");
	}
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}
