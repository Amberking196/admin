package com.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviseUtil {

	private final static String RESTART_COMMAND="r:1&1$";
	
	public static Integer checkCodeSum(String command){
		if(StringUtil.isNotBlank(command)){
			int sum = 0;
			char[] ss = command.toCharArray();
			for (char c : ss) {
				if(Character.isDigit(c)){
					Integer unitChar = Integer.valueOf(String.valueOf(c));
					sum+=unitChar;
				}
			}
			return sum;
		}
		return null;
	}
	
	//获取重启的校验码
	public static String genRebootCommandStr(String factoryNumber, int way) {
		//   n:111380310016;d:0,1,0,0;v:99&44$
		//   n:000000000000;d:8;v:7;l:10&21$
		//   fn:open;n:****;order:******;door:*******
	    //   fn:check;n:****;order:******;door:*******
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmss");
		String orderId = sf.format(new Date());
		StringBuilder sb = new StringBuilder();
		sb.append("fn:"+"reboot");
		sb.append(";n:"+factoryNumber+";order:"+orderId);
		sb.append(";door:"+way);
		sb.append("\n");
		//String doorstr="0,0,0,0";
		//int len = sumString(factoryNumber) + way + 18 + 9;
		//sb.append(";v:9;l:99;&" + len + "$");
		//sb.append("o:"+id);
	
		return sb.toString();
	}
	public static String restartCommand(){
		return RESTART_COMMAND;
	}
	
//	public static void main(String[] args) {
//		Integer checkCodeSum = checkCodeSum("w:3,1,23,4700&$");
//		System.out.println(checkCodeSum);
//	}
}
