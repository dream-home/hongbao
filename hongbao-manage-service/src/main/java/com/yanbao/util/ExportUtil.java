package com.yanbao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportUtil {
	public static void exportExcel(List<Map<String, String>> info,String fileName,String title,HttpServletResponse response){
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO_8859_1");
		    response.setContentType("application/CSV");
		    response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		    response.addHeader("Cache-Control", "must-revalidate");
		    response.addHeader("Pragma", "must-revalidate");
		    OutputStream outputStream = response.getOutputStream();
		    String[] alltitle=title.split(",");
		    if(fileName.contains(".xls")||fileName.contains(".xlsx")){
		    	title=title.replace(",", "\t");
		    }
            outputStream.write((title+"\r\n").getBytes());
		    for (Map<String, String> map : info) {
		    	String contentString="";
		    	for (String string : alltitle) {
		    		if(null!=map.get(string)){
		    			contentString+=map.get(string)+",";
		    		}
				}
		    	if(!"".equals(contentString)){
		    		contentString=contentString.substring(0, contentString.length()-1);
		    		outputStream.write(contentString.getBytes());
		    		outputStream.flush();
		    	}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
			File file=new File("C:\\Users\\Administrator\\Downloads\\PartnerBill.xls");
			List<List<Map<String, String>>> iList=readExcelWithTitle(file);
			for (List<Map<String, String>> list : iList) {
				for (Map<String, String> map : list) {
					for (String key : map.keySet()) {
						System.out.println(key+"---"+map.get(key));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<List<Map<String, String>>> readExcelWithTitle(File file) throws Exception{
		 String[] nameandtype=file.getName().split("\\.");
	     String fileType = nameandtype[nameandtype.length-1];
	     InputStream is = new FileInputStream(file);
	     Workbook wb = null;
	     try {
	         if (fileType.equals("xls")) {
	             wb = new HSSFWorkbook(is);
	         } else if (fileType.equals("xlsx")) {
	             wb = new XSSFWorkbook(is);
	         } else {
	             throw new Exception("读取的不是excel文件");
	         }
	         List<List<Map<String, String>>> result = new ArrayList<List<Map<String,String>>>();//对应excel文件
	         int sheetSize = wb.getNumberOfSheets();
	         for (int i = 0; i < sheetSize; i++) {//遍历sheet页
	             Sheet sheet = wb.getSheetAt(i);
	             List<Map<String, String>> sheetList = new ArrayList<Map<String, String>>();//对应sheet页
	             List<String> titles = new ArrayList<String>();//放置所有的标题
	             int rowSize = sheet.getLastRowNum() + 1;
	             for (int j = 0; j < rowSize; j++) {//遍历行
	                 Row row = sheet.getRow(j);
	                 if (row == null) {//略过空行
	                     continue;
	                 }
	                 int cellSize = row.getLastCellNum();//行中有多少个单元格，也就是有多少列
	                 if (j == 0) {//第一行是标题行
	                     for (int k = 0; k < cellSize; k++) {
	                         Cell cell = row.getCell(k);
	                         titles.add(cell.toString());
	                     }
	                 } else {//其他行是数据行
	                     Map<String, String> rowMap = new HashMap<String, String>();//对应一个数据行
	                     for (int k = 0; k < titles.size(); k++) {
	                         Cell cell = row.getCell(k);
	                         String key = titles.get(k);
	                         String value = null;
	                         if (cell != null) {
	                             value = cell.toString();
	                         }
	                         rowMap.put(key, value);
	                     }
	                     sheetList.add(rowMap);
	                 }
	             }
	             result.add(sheetList);
	         }
	         return result;
	     } catch (Exception e) {
	         e.printStackTrace();
	         return null;
	     } finally {
	         if (wb != null) {
	             wb.close();
	         }
	         if (is != null) {
	             is.close();
	         }
	     }
	 } 
}
