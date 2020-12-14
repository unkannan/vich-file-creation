package Lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

	public int getDataRowCount(String xlFIle, int dataSheet) {
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

	public int getcolumnindex(String xlFIle, int dataSheet, String colname) {
		OpenExcelToAccess(xlFIle);
		sheet = wb.getSheetAt(0);
		int firstRow = 0;

		for (int columnIndex = 0; columnIndex < sheet.getRow(0).getLastCellNum(); columnIndex++) {
			if (sheet.getRow(firstRow).getCell(columnIndex).toString().equalsIgnoreCase(colname)) {
				closeExcel();
				return columnIndex;
			}
		}
		System.out.println(colname + ": Column does not exist in excel");
		throw new NullPointerException();
	}

	public String getcellvalue(String xlFIle, int dataSheet, int row, String colname) {
		OpenExcelToAccess(xlFIle);
		sheet = wb.getSheetAt(dataSheet);
		String cellValue = "N";
		try {
			cellValue = sheet.getRow(row).getCell(getcolumnindex(xlFIle, dataSheet, colname)).getStringCellValue();
			closeExcel();
		} catch (Exception e) {
		}
		return cellValue;
	}

	public void setcellvalue(String xlFIle, int dataSheet, int row, String colname, String results)  {
		FileInputStream inp1=null;
		File excel =null;
		FileOutputStream fileOut =null;
		try {
			  excel = new File(xlFIle);
			  inp1 = new FileInputStream(excel);

			XSSFWorkbook wb = new XSSFWorkbook(inp1);
			XSSFSheet sheet = wb.getSheetAt(dataSheet);
			sheet.getRow(row).getCell(getcolumnindex(xlFIle, dataSheet, colname)).setCellValue(results);
			inp1.close();

			 fileOut = new FileOutputStream(excel);

			wb.write(fileOut);
			wb.close();

			fileOut.close();

		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				inp1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			 try {
				fileOut = new FileOutputStream(excel);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				wb.write(fileOut);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				wb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//sheet.getRow(row).getCell(getcolumnindex(xlFIle, dataSheet, colname)).setCellValue(results);
		}
	}
}
