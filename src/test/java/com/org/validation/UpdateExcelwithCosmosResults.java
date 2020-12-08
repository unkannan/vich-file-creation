package com.org.validation;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.azure.cosmos.vich.dbresults.validation.SafetyReportCheckInCosmosDB;
import com.google.common.io.Files;

import Lib.ExcelReader;

public class UpdateExcelwithCosmosResults {
	final String ExcelFilePath = "Files/Templates/createfilesfromxpath_A.xlsx";
	final String ackFilePath = "Files/acks/";
	ExcelReader xlreader = null;
	// InputStream inp =null;
	// Workbook wb =null;
	// Sheet sheet=null;
	final int filename = 10;
	final int flagcheck = 0;
	final int queryResultsCellValue = 11;
	final int dataSheet = 0;
	SafetyReportCheckInCosmosDB updateTestResults = null;

	@Before
	public void pre_requisites() throws IOException {
		// inp = new FileInputStream(ExcelFilePath);
		// wb = new XSSFWorkbook(inp);
		updateTestResults = new SafetyReportCheckInCosmosDB();
		xlreader = new ExcelReader();
	}

	//@Test
	public void vich_dbdatavalidation() throws IOException {
		String results = null;
		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath, dataSheet); row++) {
			if (xlreader.getcellvalue(ExcelFilePath, dataSheet, row, "Flag").equalsIgnoreCase("Y")) {
				String newfilename = xlreader.getcellvalue(ExcelFilePath, dataSheet, row, "FILENAME");

				if (newfilename.contains(".xml")) {
					newfilename = newfilename.substring(0, newfilename.length() - 4);
				}
				System.out.println(newfilename);
				results=updateTestResults.SafetyReportValidationCheckInDB(newfilename);
				xlreader.setcellvalue(ExcelFilePath, dataSheet, row, "dbresults", results);
			}
		}
	}

	@Test
	public void vich_ackValidation() throws IOException {
		String results = null;

		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath, dataSheet); row++) {
			System.out.println("************************************************************");
			System.out.println("************************************************************");
			try{
				if (xlreader.getcellvalue(ExcelFilePath, dataSheet, row, "Flag").equalsIgnoreCase("Y")) {
					String newfilename = xlreader.getcellvalue(ExcelFilePath, dataSheet, row, "FILENAME");
						System.out.println("validtion started for File:"+newfilename);

				if (newfilename.contains(".xml")) {
					newfilename = newfilename.replace(".xml", "");
				}

				String ActualFileName = updateTestResults.SearchForFile(ackFilePath, newfilename);
				System.out.println("Acks for validtion:"+ActualFileName);
				ActualFileName = ackFilePath + ActualFileName;
				if (ActualFileName != null)
					System.out.println("Found ack in folder:"+ActualFileName);
					results = updateTestResults.ackValidation(ActualFileName);
					System.out.println("Validation completed:"+ActualFileName);
				xlreader.setcellvalue(ExcelFilePath, dataSheet, row, "ackresults", results);
				System.out.println("updated the excel");
				File srcFile=new File(ActualFileName);
				File destFile=new File("Files/acks/archive");
				FileUtils.moveFileToDirectory(srcFile, destFile,true);
				System.out.println("ack archived"+destFile.getName());
			}
			}catch(Exception e) {}
		}
	}
}
