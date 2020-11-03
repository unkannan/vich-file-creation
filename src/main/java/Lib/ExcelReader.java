package Lib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
	static InputStream inp = null;
	static Sheet sheet = null;
	static Workbook wb = null;
	 
	private void OpenExcelToAccess(String xlFIle) {
		try {
			inp = new FileInputStream(xlFIle);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb = WorkbookFactory.create(inp);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getDataRowCount(String xlFIle) {
		OpenExcelToAccess(xlFIle);
		sheet = wb.getSheetAt(0);
		int totalrowstoread = sheet.getLastRowNum();
		closeExcel();
		return totalrowstoread;
	}

	private void closeExcel() {
		try {
			inp.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			wb.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getcolumnindex(String xlFIle,String colname) {
		OpenExcelToAccess(xlFIle);
		sheet = wb.getSheetAt(0);
		int firstRow=0;
		
		for(int columnIndex=0;columnIndex<sheet.getRow(0).getLastCellNum();columnIndex++) {
			if(sheet.getRow(firstRow).getCell(columnIndex).toString().equalsIgnoreCase(colname)) {
				closeExcel();
				return columnIndex;
			}
		}
		System.out.println("Column does not exist in excel");
		throw new NullPointerException();
	}

	public String getcellvalue(String xlFIle,int row,String colname) {
		OpenExcelToAccess(xlFIle);
		sheet = wb.getSheetAt(0);
		
		String cellValue=sheet.getRow(row).getCell(getcolumnindex(xlFIle, colname)).toString(); 
				closeExcel();
		return cellValue;		 
	}

}


