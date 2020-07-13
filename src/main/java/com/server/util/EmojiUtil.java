package com.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * app系统表情操作工具类
 * @author hzh
 *
 */
public class EmojiUtil {

	/**
	 * 将系统表情转化为字符串
	 * @param s
	 * @return
	 */
	public static String getString(String s) {
		if(StringUtil.isBlank(s)){
			return "";
		}
		int length = s.length();
		String context = "";
		//循环遍历字符串，将字符串拆分为一个一个字符
		for (int i = 0; i < length; i++) {
			char codePoint = s.charAt(i);
			//判断字符是否是emoji表情的字符
			if (isEmojiCharacter(codePoint)) {
				//如果是将以大括号括起来
				String emoji = "{" + Integer.toHexString(codePoint) + "}";
				context = context + emoji;
				continue;
			}
			context = context + codePoint;
		}
		return context;
	}
	
	/**
	 * 将表情描述转换成表情
	 *
	 * @param str
	 * @return
	 */
	public static String getEmoji(String str) {
		
		if(StringUtil.isBlank(str)){
			return "";
		}
		
		String string = str;
		String rep = "\\{(.*?)\\}";
		Pattern p = Pattern.compile(rep);
		Matcher m = p.matcher(string);
		while (m.find()) {
			String s1 = m.group().toString();
			String s2 = s1.substring(1, s1.length() - 1);
			String s3;
			try {
				s3 = String.valueOf((char) Integer.parseInt(s2, 16));
				string = string.replace(s1, s3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return string;
	}
	
	/**
	 * 判断是否为表情字符
	 * @param codePoint
	 * @return
	 */
	private static boolean isEmojiCharacter(char codePoint) {
		return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
				|| (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
				|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
	}
}
