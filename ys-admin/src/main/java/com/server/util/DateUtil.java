package com.server.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;
@Component
public class DateUtil {
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdftime = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat sdfdetail = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getAgeByYear(int age){
		GregorianCalendar ca = new GregorianCalendar();
		ca.setTime(new Date());
		ca.add(GregorianCalendar.YEAR, -age);
		Date date = ca.getTime();
		return sdf.format(date);
	}
	
	/**
	 * **********************************************
	 * �� �� �� ��
	 * @param age
	 * @return
	 ***********************************************
	 */
	public static String getDateTime(long time){
		return sdf.format(new Date(time*1000));
	}
	
	
	/**
	 * **********************************************
	 * �� �� ʱ �� ��  �� �� ��
	 * @param date
	 * @return
	 ***********************************************
	 */
	public static int getYearByAge(Date date){
		Date nowDate = new Date();
		long time = nowDate.getTime() - date.getTime();
		return (int)(time/31536000000l);
	}
	
 
	/**
	 * **********************************************
	 * obtains current time
	 * @param args
	 * **********************************************
	 */
	public static long getCurrentDatetime(){
		long time = System.currentTimeMillis()/1000;
		return time;
	}
	
	public static long getLastDatetime(int day){
		long time = getCurrentDatetime();
		time = time+(day*60*60*24);
		return time;
	}
	
	public static String getCurrentTime(){
		return sdfdetail.format(new Date());
	}
	
	
	public static String formatYYYYMMDD(Date date){
		if(date == null){
			return null;
		}
		return sdf.format(date);
	}
	
	public static String formatYYYYMMDDHHMMSS(Date date){
		if(date == null){
			return null;
		}
		return sdfdetail.format(date);
	}
	/**
	 * 获取今天还剩余多少秒
	 * @author hebiting
	 * @date 2018年12月21日下午6:42:50
	 * @return
	 */
	public static int getTodayRemainSecond(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return (int)((calendar.getTimeInMillis()-System.currentTimeMillis())/1000);
	}
	
	public static void main(String[] args) {
		System.out.println(getTodayRemainSecond());
	}
	
	/**
	 * 将date转换为 yyyy-MM-dd HH:mm:ss,days是增加的天数
	 * @author hebiting
	 * @date 2018年4月21日上午9:53:10
	 * @param date
	 * @param days
	 * @return
	 */
	public static String formatLocalYYYYMMDDHHMMSS(Date date,int days){
	    Instant instant = date.toInstant();
	    ZoneId zoneId = ZoneId.systemDefault();
	    LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
	    localDate = localDate.plusDays(days);
	    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return localDate.format(format);
	}
	
	public static Date getTodayStart(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return  calendar.getTime();
	}
	
	public static Date getSomeDayStart(Date someDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(someDay);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return  calendar.getTime();
	}
	
	public static Date getTodayEnd(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return  calendar.getTime();
	}
	
	public static Date getSomeDayEnd(Date someDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(someDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return  calendar.getTime();
	}
}



