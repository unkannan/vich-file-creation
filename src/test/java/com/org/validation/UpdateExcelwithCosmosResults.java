package com.org.validation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.azure.cosmos.vich.dbresults.validation.SafetyReportCheckInCosmosDB;

import Lib.ExcelReader;

public class UpdateExcelwithCosmosResults{
	final String ExcelFilePath = "Files/Templates/createfilesfromxpath_A.xlsx";
	final String ackFilePath= "Files/acks/";
	static ExcelReader xlreader=null;
	    InputStream inp =null;
		Workbook wb =null;
		Sheet sheet=null;
		final int filename = 10;
		final int flagcheck = 0;
		final int queryResultsCellValue=11;
		SafetyReportCheckInCosmosDB updateTestResults=null;
		
		@Before
		public void pre_requisites() throws IOException {
					inp = new FileInputStream(ExcelFilePath);
					wb = new XSSFWorkbook(inp);
					updateTestResults=new SafetyReportCheckInCosmosDB();
					xlreader=new ExcelReader();
		}
		
		@Test
	    public void vich_dbdatavalidation() throws IOException {
	         
			sheet = wb.getSheetAt(0);
			
			for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath); row++) {
				try {
				if (xlreader.getcellvalue(ExcelFilePath,row,"Flag").equalsIgnoreCase("Y")) {	
					String newfilename =  xlreader.getcellvalue(ExcelFilePath,row,"FILENAME");

					 if(newfilename.contains(".xml")) {
						 newfilename=newfilename.substring(0,newfilename.length()-4);
					 }
					 		System.out.println(newfilename);
					 		//String actualresults=updateTestResults.SafetyReportValidationCheckInDB(newfilename);
					 		String actualresults="dbresults test";
					 		xlreader.setcellvalue(ExcelFilePath, row, "dbresults", actualresults);	
		    				//newpath.setCellValue(results);

		    				 FileOutputStream fileOut = new FileOutputStream(ExcelFilePath);
		    	        wb.write(fileOut);
		    	        fileOut.close();
			            }
				}
		    			catch (Exception e1) {
		    				FileOutputStream fileOut = new FileOutputStream(ExcelFilePath);
			    	        wb.write(fileOut);
			    	        fileOut.close();
		    			}
				}	
			}

		//@Test
	    public void vich_ackValidation() throws IOException {
			String results=null;
			sheet = wb.getSheetAt(0);
			
			for (int j = 1; j < 200 - 1; j++) {
				try {
				if (sheet.getRow(j).getCell(flagcheck).toString().equalsIgnoreCase("y")) {	
					String newfilename = sheet.getRow(j).getCell(filename).getStringCellValue();

					 if(newfilename.contains(".xml")) {
						 newfilename=newfilename.substring(0,newfilename.length()-4);
					 }
					 
					     String ActualFileName=updateTestResults.SearchForFile(ackFilePath,newfilename);
					     
					     ActualFileName=ackFilePath+ActualFileName;
					     if(ActualFileName!=null) 
					    	 results=updateTestResults.ackValidation(ActualFileName);
					    	 
		    				Cell newpath = sheet.getRow(j).createCell(queryResultsCellValue);
		    				newpath.setCellValue(results);

		    				 FileOutputStream fileOut = new FileOutputStream(ExcelFilePath);
		    	        wb.write(fileOut);
		    	        fileOut.close();
			            }
				}
		    			catch (Exception e1) {
		    				FileOutputStream fileOut = new FileOutputStream(ExcelFilePath);
			    	        wb.write(fileOut);
			    	        fileOut.close();
		    			}
				}	
			}
}
