package com.server.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	@SuppressWarnings("static-access")
	public static <T> void exportExcel(String title,String[] headers,String[] columnName,
			HttpServletResponse res,List<T> data,String date) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException{
		res.setContentType("application/x-download");
		res.addHeader("Content-Disposition",
				"attachment;filename=" + new String((title).getBytes("GB2312"), "iso8859-1") + ".xls");
		OutputStream out=null;
		try {
			out = res.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		HSSFCellStyle style = workbook.createCellStyle();
		
		style.setAlignment((short)2);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontHeightInPoints((short)10);
		style.setFont(font);
	
		//标题
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnName.length-1));
		cell0.setCellValue(title+" ： "+date);
		HSSFCellStyle titleStyle = workbook.createCellStyle();        //标题样式
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont ztFont = workbook.createFont();   
		ztFont.setItalic(false);                     // 设置字体为斜体字   
		ztFont.setColor(HSSFFont.COLOR_NORMAL);            // 将字体设置为“红色”   
		ztFont.setFontHeightInPoints((short)16);    // 将字体大小设置为18px   
		ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上  
		ztFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);    //加粗
//		ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）   
//		ztFont.setStrikeout(true);                  // 是否添加删除线   
		titleStyle.setFont(ztFont); 
		cell0.setCellStyle(titleStyle);
		
		//列名
		HSSFRow row1 = sheet.createRow(1);
		for (int i = 0; i < columnName.length; i++) {
			HSSFCell cell = row1.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(columnName[i]);
		}
		for (int i = 0; i < data.size(); i++) {
			HSSFRow rows = sheet.createRow(i+2);
			for (int j = 0; j < headers.length; j++) {
				HSSFCell cell = rows.createCell(j);
			
				cell.setCellStyle(style);
				Field field = data.get(i).getClass().getDeclaredField(headers[j]);
				field.setAccessible(true);
				if(field.get(data.get(i))!=null){
					//判断是否为数值类型
					if(field.get(data.get(i)).toString().matches("^(-?\\d{1,12})(\\.\\d+)?$")) {
						cell.setCellValue(Double.parseDouble(field.get(data.get(i)).toString()));
					}else {
						cell.setCellValue(field.get(data.get(i)).toString());
					}
				}else{
					cell.setCellValue("");
				}
			}
		}
		for (int i = 0; i < columnName.length; i++) {
//			sheet.autoSizeColumn(i,true);
			sheet.setColumnWidth(i, columnName.toString().length() * 188);
		}
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ;
	}
	
	public static <T> void exportExcel2007(String title,String[] headers,String[] columnName,
			HttpServletResponse res,List<T> data,String date) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException{
		res.setContentType("application/x-download");
		res.addHeader("Content-Disposition",
				"attachment;filename=" + new String((title).getBytes("GB2312"), "iso8859-1") + ".xls");
		OutputStream out=null;
		try {
			out = res.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(title);
		XSSFCellStyle style = workbook.createCellStyle();
		
		style.setAlignment((short)2);
		XSSFFont font = workbook.createFont();
		font.setColor(XSSFFont.COLOR_NORMAL);
		font.setFontHeightInPoints((short)10);
		style.setFont(font);
	
		//标题
		XSSFRow row0 = sheet.createRow(0);
		XSSFCell cell0 = row0.createCell(0);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnName.length-1));
		cell0.setCellValue(title+" ： "+date);
		XSSFCellStyle titleStyle = workbook.createCellStyle();        //标题样式
		titleStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont ztFont = workbook.createFont();   
		ztFont.setItalic(false);                     // 设置字体为斜体字   
		ztFont.setColor(XSSFFont.COLOR_NORMAL);            // 将字体设置为“红色”   
		ztFont.setFontHeightInPoints((short)16);    // 将字体大小设置为18px   
		ztFont.setFontName("宋体");             // 将“宋体”字体应用到当前单元格上  
		ztFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);    //加粗
//		ztFont.setUnderline(Font.U_DOUBLE);         // 添加（Font.U_SINGLE单条下划线/Font.U_DOUBLE双条下划线）   
//		ztFont.setStrikeout(true);                  // 是否添加删除线   
		titleStyle.setFont(ztFont); 
		cell0.setCellStyle(titleStyle);
		
		//列名
		XSSFRow row1 = sheet.createRow(1);
		for (int i = 0; i < columnName.length; i++) {
			XSSFCell cell = row1.createCell(i);
			cell.setCellStyle(style);
			cell.setCellValue(columnName[i]);
		}
		for (int i = 0; i < data.size(); i++) {
			XSSFRow rows = sheet.createRow(i+2);
			for (int j = 0; j < headers.length; j++) {
				XSSFCell cell = rows.createCell(j);
			
				cell.setCellStyle(style);
				Field field = data.get(i).getClass().getDeclaredField(headers[j]);
				field.setAccessible(true);
				if(field.get(data.get(i))!=null){
					//判断是否为数值类型
					if(field.get(data.get(i)).toString().matches("^(-?\\d{1,12})(\\.\\d+)?$")) {
						cell.setCellValue(Double.parseDouble(field.get(data.get(i)).toString()));
					}else {
						cell.setCellValue(field.get(data.get(i)).toString());
					}
				}else{
					cell.setCellValue("");
				}
			}
		}
		for (int i = 0; i < columnName.length; i++) {
//			sheet.autoSizeColumn(i,true);
			sheet.setColumnWidth(i, columnName.toString().length() * 188);
		}
		try {
			workbook.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return ;
	}
}
