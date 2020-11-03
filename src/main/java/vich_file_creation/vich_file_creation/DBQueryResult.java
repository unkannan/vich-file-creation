package vich_file_creation.vich_file_creation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DBQueryResult {

	Statement stmt=null;
	ResultSet rs=null;
	InputStream inp =null;
	Workbook wb =null;
	Sheet sheet=null;
	Connection con=null;
	final String ExcelFilePath = "C:\\Users\\kannanu\\OneDrive\\VICH\\MandatoryRuleFile.xlsx";
	
	
	public void objectCreate() throws IOException {
	inp = new FileInputStream(ExcelFilePath);
	wb = new XSSFWorkbook(inp);
	con=DBconnectionswitch();
	}
	
	public void QueryResults(String Query,String Sheet) throws SQLException, IOException
	{  	  		
			objectCreate();
			sheet = wb.getSheet(Sheet);

		try {
			stmt = con.createStatement();
				} catch (SQLException e3) {
					e3.printStackTrace();
				}
		 
		try {
			rs = stmt.executeQuery(Query);
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
		//Writing db columns into excel
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnsNumber = rsmd.getColumnCount();
        	
        	 Row colheaderrow = sheet.createRow(0);
             for(int col=0 ;col < columnsNumber;col++) {
                 Cell newpath = colheaderrow.createCell(col);
                 newpath.setCellValue(rsmd.getColumnLabel(col+1));
             }
             System.out.println("Column headers are set for excel");
        	
		try {
			while(rs.next()) {
				Row datarow = sheet.createRow(rs.getRow());
                for(int col=0 ;col < columnsNumber;col++) {
                    Cell newpath = datarow.createCell(col);
                   // System.out.println(rs.getString(col));
                    newpath.setCellValue(rs.getString(col+1));  
                }
			}
			System.out.println("Data is loaded successfully in excel sheet "+sheet.getSheetName());
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		 FileOutputStream fileOut = new FileOutputStream(ExcelFilePath);
         wb.write(fileOut);
         fileOut.close();
     
	}
	
	  
	public void queryrun(String query,String Sheet) throws SQLException, IOException {
		QueryResults(query,Sheet);
	}
	
	
	public Connection DBconnectionswitch() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e5) {
			e5.printStackTrace();
		}
		String TestconnString ="jdbc:oracle:thin:@(DESCRIPTION =    (ADDRESS = (PROTOCOL = TCP)(HOST = srv023.emea.eu.int)(PORT = 1571))   (CONNECT_DATA =      (SERVER = DEDICATED)      (SERVICE_NAME = PHV07_DEV.emea.eu.int)     )  )";
		//(DESCRIPTION =    (ADDRESS = (PROTOCOL = TCP)(HOST = srv023.emea.eu.int)(PORT = 1571))   (CONNECT_DATA =      (SERVER = DEDICATED)      (SERVICE_NAME = PHV07_DEV.emea.eu.int)     )  )
		Connection con=null;
		try {
			con = DriverManager.getConnection(TestconnString,"ICSR_EMA", "ICSR_EMA");
		} catch (SQLException e4) {
			e4.printStackTrace();
		}
		return con;
	}
	
	public String LenCheckquery() {
		return "select Flag,section,element,datalen,case,len,xpath ,Filename from (\r\n" + 
				"select 'y' Flag,section,element,datalen,'p' case,'len' len,\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen)||'\"}]}'\r\n" + 
				"xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||datalen||'_'||'p' Filename\r\n" + 
				"from  zz_lencheck \r\n" + 
				"union\r\n" + 
				"select 'y' Flag,section,element,datalen-1,'p' case,'len',\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen-1)||'\"}]}'\r\n" + 
				"xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-1)||'_'||'p'\r\n" + 
				"from  zz_lencheck \r\n" + 
				"union\r\n" + 
				"select 'y',section,element,datalen+1,'n' case,'len',\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen+1)||'\"}]}'\r\n" + 
				"xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen+1)||'_'||'n'\r\n" + 
				"from  zz_lencheck  \r\n" + 
				"union\r\n" + 
				"select 'y' Flag,section,element,datalen-datalen+1,'p' case,'len',\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen-datalen+1)||'\"}]}'\r\n" + 
				"xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen+1)||'_'||'p'\r\n" + 
				"from  zz_lencheck\r\n" + 
				"union\r\n" + 
				"select 'y' Flag,section,element,datalen-datalen,'p' case,'len',\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen-datalen)||'\"}]}'\r\n" + 
				" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen)||'_'||'p'\r\n" + 
				"from  zz_lencheck) \r\n" + 
				" dual";
	}
	
	public String adhocQuery() {
		return " select 'y' Flag,section,element,scenario,case,mandatory,xpath,filename from \r\n" + 
				" ( \r\n" + 
				"  select  sno,section,element,'defaultText' scenario,'p' case, mandatory,\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"defaultText\"}]}'\r\n" + 
				" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||'defaultText'||'_'||'p' filename\r\n" + 
				"from  zz_lencheck\r\n" + 
				"union\r\n" + 
				"select  sno,section,element,'Element_Remove' scenario,'n' case, mandatory,\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||'\",\"value\":\"remove\"}]}'\r\n" + 
				" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||'remove'||'_'||'p' filename\r\n" + 
				"from  zz_lencheck\r\n" + 
				"union\r\n" + 
				" select sno,section,element,'Empty' scenario,'n' case,mandatory,\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"\"}]}'\r\n" + 
				" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||'empty'||'_'||'p' filename\r\n" + 
				"from  zz_lencheck \r\n" + 
				"union\r\n" + 
				"select sno,section,element,'Null' scenario,'n' case, mandatory,\r\n" + 
				"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||'\",\"value\":\"null\"}]}'\r\n" + 
				" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||'null'||'_'||'p' filename\r\n" + 
				"from  zz_lencheck  )\r\n" + 
				"dual where mandatory in ('mandatory','Mandatory') and section in ('A.1.2')";
	}

	public String mandatorycheckquery() {
			return " select 'y' Flag,section,element,scenario,case,mandatory,xpath,filename from \r\n" + 
					" ( \r\n" + 
					"  select  sno,section,element,'defaultText' scenario,'p' case, mandatory,\r\n" + 
					"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"defaultText\"}]}'\r\n" + 
					" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen)||'_'||'p' filename\r\n" + 
					"from  zz_lencheck\r\n" + 
					"union\r\n" + 
					"select  sno,section,element,'Element_Remove' scenario,'n' case, mandatory,\r\n" + 
					"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||'\",\"value\":\"remove\"}]}'\r\n" + 
					" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen)||'_'||'p' filename\r\n" + 
					"from  zz_lencheck\r\n" + 
					"union\r\n" + 
					" select sno,section,element,'Empty' scenario,'n' case,mandatory,\r\n" + 
					"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||xpath1||'\",\"value\":\"\"}]}'\r\n" + 
					" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen)||'_'||'p' filename\r\n" + 
					"from  zz_lencheck \r\n" + 
					"union\r\n" + 
					"select sno,section,element,'Null' scenario,'n' case, mandatory,\r\n" + 
					"'{\"xpath\":[{\"field\":\"'||replace(xpath,'\"','\\\"')||'\",\"value\":\"null\"}]}'\r\n" + 
					" xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen)||'_'||'p' filename\r\n" + 
					"from  zz_lencheck  )\r\n" + 
					"dual where mandatory in ('mandatory','Mandatory')";
	}
}
 
