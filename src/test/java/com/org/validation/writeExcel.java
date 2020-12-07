package com.org.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class writeExcel {
	@Test
	public void setcellvalue() throws IOException {
		final String ExcelFilePath = "Files/Templates/createfilesfromxpath_A.xlsx";
		try{
			File excel = new File(ExcelFilePath);
			FileInputStream inp1 = new FileInputStream(excel);
			XSSFWorkbook wb1 = new XSSFWorkbook(inp1);
			XSSFSheet  sheet1 = wb1.getSheetAt(0);
			
	//	Cell newpath = sheet1.getRow(2).createCell(11);
		System.out.println(sheet1.getRow(1).getCell(2).getStringCellValue());
		System.out.println(sheet1.getSheetName());
		sheet1.getRow(1).getCell(11).setCellValue("kalifa");
				//newpath.getStringCellValue());
		//newpath.setCellValue("kkkkkk");
		//System.out.println(newpath.getStringCellValue());
		inp1.close();
		
		
		FileOutputStream fileOut = new FileOutputStream(excel);
		
        wb1.write(fileOut);
        wb1.close();
        
     
        fileOut.close();
        
		}catch (Exception e1) {
	        e1.printStackTrace();
		}
	}
}
