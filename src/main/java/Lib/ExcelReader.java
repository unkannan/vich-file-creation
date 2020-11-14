package Lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
		System.out.println(colname+": Column does not exist in excel");
		throw new NullPointerException();
	}

	public String getcellvalue(String xlFIle,int row,String colname) {
		OpenExcelToAccess(xlFIle);
		sheet = wb.getSheetAt(0);
		String cellValue=null;
		try {
		 cellValue=sheet.getRow(row).getCell(getcolumnindex(xlFIle, colname)).getStringCellValue(); 
				closeExcel();
		}catch(Exception e) {}
		return cellValue;		 
	}
	public void setcellvalue(String xlFIle,int row,String colname,String results) throws IOException {
		try{
			File excel = new File("D:\\vich-file-creation\\Files\\Templates\\createfilesfromxpath_A.xlsx");
			FileInputStream inp1 = new FileInputStream(excel);
		
			XSSFWorkbook wb1 = new XSSFWorkbook(inp1);
			XSSFSheet  sheet1 = wb1.getSheetAt(0);
		Cell newpath = sheet1.getRow(row).createCell(2);
		newpath.setCellValue("kkkkkk");
		System.out.println(newpath.getStringCellValue());
		FileOutputStream fileOut = new FileOutputStream(excel);
        wb1.write(fileOut);
        
     
        fileOut.close();
        wb1.close();
        inp1.close();
		}catch (Exception e1) {
			FileOutputStream fileOut = new FileOutputStream(xlFIle);
	        wb.write(fileOut);
	        fileOut.close();
	        e1.printStackTrace();
		}
	}
}


