package com.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;

import com.server.module.system.shoppingManager.customerOrder.CustomerOrderBean;
import com.server.module.system.shoppingManager.customerOrder.ShoppingBean;
import com.server.module.system.warehouseManage.warehouseWarrantDetail.WarehouseBillItemBean;

/**
 * EXCEL报表工具类
 * 
 */
public class ExportExcel {

	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;

	public HSSFWorkbook getWb() {
		return wb;
	}

	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	public HSSFSheet getSheet() {
		return sheet;
	}

	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	public ExportExcel(HSSFWorkbook wb, HSSFSheet sheet) {
		super();
		this.wb = wb;
		this.sheet = sheet;
	}

	/**
	 * 创建通用EXCEL头部
	 * 
	 * @param headString
	 *            头部显示的字符
	 * @param colSum
	 *            该报表的列数
	 */
	public void createNormalHead(String headString, int colSum) {
		HSSFRow row = sheet.createRow(0);
		// 设置第一行
		HSSFCell cell = row.createCell(0);
		row.setHeight((short) 400);
		// 定义单元格为字符串类型
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(headString));
		// 指定合并区域
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) colSum));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 300);
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		cell.setCellStyle(cellStyle);
	}

	/**
	 * 创建通用报表第二行
	 * 
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 */
	public void createNormalTwoRow(String[] paramt, String[] params, int colSum) {
		HSSFRow row1 = sheet.createRow(1);
		row1.setHeight((short) 300);
		HSSFCell cell2 = row1.createCell(0);
		cell2.setCellType(HSSFCell.ENCODING_UTF_16);
		cell2.setCellValue(new HSSFRichTextString(paramt[0] + "：" + params[0] + "            " + paramt[1] + "："
				+ params[1] + "          " + paramt[2] + "：" + params[2] + "       " + paramt[3] + "：" + params[3]));
		// 指定合并区域
		sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) colSum));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		cell2.setCellStyle(cellStyle);
	}

	/**
	 * 创建通用报表第三行
	 * 
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 */
	public void createNormalThreeRow(String[] paramt, String[] params, int colSum) {
		HSSFRow row1 = sheet.createRow(2);
		row1.setHeight((short) 300);
		HSSFCell cell2 = row1.createCell(0);
		cell2.setCellType(HSSFCell.ENCODING_UTF_16);
		cell2.setCellValue(new HSSFRichTextString(paramt[0] + "：" + params[0] + "            " + paramt[1] + "："
				+ params[1] + "          " + paramt[2] + "：" + params[2] + "       " + paramt[3] + params[3]));
		// 指定合并区域
		sheet.addMergedRegion(new Region(2, (short) 0, 2, (short) colSum));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);
		cell2.setCellStyle(cellStyle);
	}

	/**
	 * 创建通用报表第四行
	 * 
	 * @param params
	 *            统计条件数组
	 * @param colSum
	 *            需要合并到的列索引
	 */
	public void createNormalFourRow(String[] params, int colSum) {
		HSSFRow row1 = sheet.createRow(3);
		row1.setHeight((short) 600);

		HSSFCell cell2 = row1.createCell(0);

		cell2.setCellType(HSSFCell.ENCODING_UTF_16);
		cell2.setCellValue(new HSSFRichTextString("商品总入库数量：" + params[0]));

		// 指定合并区域
		sheet.addMergedRegion(new Region(3, (short) 0, 3, (short) colSum));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		cellStyle.setFont(font);
		cell2.setCellStyle(cellStyle);
	}

	/**
	 * 设置报表标题
	 * 
	 * @param columHeader
	 *            标题字符串数组
	 */
	public void createColumHeader(String[] columHeader) {

		// 设置列头
		HSSFRow row2 = sheet.createRow(2);

		// 指定行高
		row2.setHeight((short) 600);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		cellStyle.setWrapText(true);// 指定单元格自动换行

		// 单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 250);
		cellStyle.setFont(font);

		// 设置单元格背景色
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFCell cell3 = null;

		for (int i = 0; i < columHeader.length; i++) {
			cell3 = row2.createCell(i);
			cell3.setCellType(HSSFCell.ENCODING_UTF_16);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(new HSSFRichTextString(columHeader[i]));
		}

	}

	public void createNormalFiveRow(List<WarehouseBillItemBean> fialList, String[] column, String[] headers)
			throws NoSuchFieldException, SecurityException, NumberFormatException, IllegalArgumentException,
			IllegalAccessException {
		// 给工作表列定义列宽(实际应用自己更改列数)
		for (int i = 0; i < column.length; i++) {
			sheet.setColumnWidth(i, 6000);
		}
		// 设置第五行
		HSSFRow row4 = sheet.createRow(4);
		// 设置行高
		row4.setHeight((short) 300);
		HSSFCell row4Cell = null;
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();

		// 指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		int m = 0;
		// 创建不同的LIST的列标题
		for (int i = 0; i < column.length; i++) {
			row4Cell = row4.createCell(i);
			row4Cell.setCellStyle(cellStyle);
			row4Cell.setCellValue(new HSSFRichTextString(column[m].toString()));
			m++;
		}
		// 循环创建中间的单元格的各项的值
		for (int i = 0; i < fialList.size(); i++) {
			HSSFRow rows = sheet.createRow(i + 5);
			for (int j = 0; j < headers.length; j++) {
				HSSFCell cell = rows.createCell(j);

				cell.setCellStyle(cellStyle);
				Field field = fialList.get(i).getClass().getDeclaredField(headers[j]);
				field.setAccessible(true);
				if (field.get(fialList.get(i)) != null) {
					// 判断是否为数值类型
					if (field.get(fialList.get(i)).toString().matches("^(-?\\d{1,10})(\\.\\d+)?$")) {
						cell.setCellValue(Double.parseDouble(field.get(fialList.get(i)).toString()));
					} else {
						cell.setCellValue(field.get(fialList.get(i)).toString());
					}
				} else {
					cell.setCellValue("");
				}
			}
		}
	}

	/**
	 * 输入EXCEL文件
	 * 
	 * @param fileName
	 *            文件名
	 * @throws UnsupportedEncodingException
	 */
	public void outputExcel(String fileName, HttpServletResponse res) throws UnsupportedEncodingException {
		res.setContentType("application/x-download");
		res.addHeader("Content-Disposition",
				"attachment;filename=" + new String((fileName).getBytes("GB2312"), "iso8859-1") + ".xls");
		OutputStream fos = null;
		try {
			fos = res.getOutputStream();
			wb.write(fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 商城订单列表导出标题
	 * 
	 * @author why
	 * @date 2018年11月12日 下午5:44:03
	 * @param headString
	 *            头部显示的字符
	 * @param colSum
	 *            该报表的列数
	 */
	public void createNormalShoppingGoodsHead(String headString, int colSum) {
		HSSFRow row = sheet.createRow(0);
		// 设置第一行
		HSSFCell cell = row.createCell(0);
		row.setHeight((short) 400);
		// 定义单元格为字符串类型
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(headString));
		// 指定合并区域
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) colSum));
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("楷体");
		font.setFontHeight((short) 300);
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 商城订单导出
	 * 
	 * @author why
	 * @date 2018年11月13日 上午9:52:25
	 * @param fialList
	 * @param column
	 * @param headers
	 * @param column2
	 * @param headers2
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public void createNormalShoppingGoodsFiveRow(List<CustomerOrderBean> fialList, String[] column, String[] headers,
			String[] column2, String[] headers2) throws NoSuchFieldException, SecurityException, NumberFormatException,
			IllegalArgumentException, IllegalAccessException {
		// 行名
		String[] col = new String[column.length + column2.length];
		// 循环添加数组内容
		for (int i = 0; i < col.length; i++) {

			if (i < column.length) {
				col[i] = column[i];
			} else {
				col[i] = column2[i - column.length];
			}
		}

		// 给工作表列定义列宽(实际应用自己更改列数)
		for (int i = 1; i < col.length; i++) {
			sheet.setColumnWidth(i, 7000);
		}
		// 设置行
		HSSFRow row4 = sheet.createRow(1);
		// 设置行高
		row4.setHeight((short) 500);
		HSSFCell row4Cell = null;
		// 创建单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();

		// 指定单元格居中对齐
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 指定单元格垂直居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 指定当单元格内容显示不下时自动换行
		cellStyle.setWrapText(true);

		// 设置单元格字体
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 200);
		cellStyle.setFont(font);
		int m = 0;
		// 创建不同的LIST的列标题
		for (int i = 0; i < col.length; i++) {
			row4Cell = row4.createCell(i);
			row4Cell.setCellStyle(cellStyle);
			row4Cell.setCellValue(new HSSFRichTextString(col[m].toString()));
			m++;
		}
		String[] header = new String[headers.length + headers2.length];
		int row = 2; // 设置初始行
		// 循环创建中间的单元格的各项的值
		int e = 0;
		HSSFRow rows=null;
		for (int i = 0; i < fialList.size(); i++) {
			List<ShoppingBean> list = fialList.get(i).getList();
			//设置起始行 和结束行
			int start=row;
			int end=row+(list.size())-1;
			if(list.size()>0) {
				for (int j = 0; j < list.size(); j++) {
					 rows = sheet.createRow(j + e + 2);
					for (int k = 0; k < header.length; k++) {
						HSSFCell cell = rows.createCell(k);
						cell.setCellStyle(cellStyle);
						if (k < headers.length) { //给前边进行赋值
							// 进行 行合并
							sheet.addMergedRegion(new CellRangeAddress(start,end, k, k));
							Field field = fialList.get(i).getClass().getDeclaredField(headers[k]);
							field.setAccessible(true);
							if (field.get(fialList.get(i)) != null) {
								// 判断是否为数值类型
								if (field.get(fialList.get(i)).toString().matches("^(-?\\d{1,10})(\\.\\d+)?$")) {
									cell.setCellValue(Double.parseDouble(field.get(fialList.get(i)).toString()));
								} else {
									cell.setCellValue(field.get(fialList.get(i)).toString());
								}
							} else {
								cell.setCellValue("");
							}
						}else {
							Field field = list.get(j).getClass().getDeclaredField(headers2[k - headers.length]);
							field.setAccessible(true);
							if (field.get(list.get(j)) != null) {
								// 判断是否为数值类型
								if (field.get(list.get(j)).toString().matches("^(-?\\d{1,10})(\\.\\d+)?$")) {
									cell.setCellValue(Double.parseDouble(field.get(list.get(j)).toString()));
								} else {
									cell.setCellValue(field.get(list.get(j)).toString());
								}
							} else {
								cell.setCellValue("");
							}
							

						}
					}
					row=end+1;
				}
				e += list.size();
			}else {
				rows= sheet.createRow(i);
				for (int k = 0; k < headers.length; k++) {
					HSSFCell cell = rows.createCell(k);
					cell.setCellStyle(cellStyle);
					Field field = fialList.get(i).getClass().getDeclaredField(headers[k]);
					field.setAccessible(true);
					if (field.get(fialList.get(i)) != null) {
						// 判断是否为数值类型
						if (field.get(fialList.get(i)).toString().matches("^(-?\\d{1,10})(\\.\\d+)?$")) {
							cell.setCellValue(Double.parseDouble(field.get(fialList.get(i)).toString()));
						} else {
							cell.setCellValue(field.get(fialList.get(i)).toString());
						}
					} else {
						cell.setCellValue("");
					}
				}
			}
		}

	}
}