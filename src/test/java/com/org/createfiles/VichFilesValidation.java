package com.org.createfiles;

import Lib.ExcelReader;
import vich_file_creation.vich_file_creation.XpathSupport;
import java.io.File;
import org.junit.*;

public class VichFilesValidation {
	static String ExcelFilePath = "Files/Templates/createfilesfromxpath_A.xlsx";
	final String VICH_TestFile_AllFields = "Files/Templates/VICH_File_With_All_Elements.xml";
	ExcelReader xlreader=null;
	XpathSupport createFile=null;
	final String NullFlavorsTemplate = "Files/Templates/NullFlavorsTemplate.xml";
	String DirectoryToCreateFiles="Files/createvichtestfiles";
	@Before
	public void GetReadyBeforecreation()   {
		createFile = new XpathSupport();
		//renamefileObj=new RenameFIle();
		xlreader=new ExcelReader();
		
	}
	@Test
	public void CreateTestDataFilesFromExcel() {
		File destDir = new File(DirectoryToCreateFiles);
		int sheetnumber=0;
		System.out.println("kannan");
		
			//createFile.CreatingXMLFilesFromGivenSheet(sheetnumber,ExcelFilePath,VICH_TestFile_AllFields,NullFlavorsTemplate);
		for (int j = 1; j <= xlreader.getDataRowCount(ExcelFilePath); j++) {
			if (xlreader.getcellvalue(ExcelFilePath,j,"flag").equalsIgnoreCase("Y")
					|| xlreader.getcellvalue(ExcelFilePath,j,"flag") != null) {
				String newfilename = xlreader.getcellvalue(ExcelFilePath,j,"FILENAME");
				try {
					createFile.FileContentModifyFromVICHTemplate(VICH_TestFile_AllFields, NullFlavorsTemplate, xlreader.getcellvalue(ExcelFilePath,j,"XPATH"),destDir+ "/" + newfilename+".xml");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}	


/*	public static void main(String args[]) {

final String NullFlavorsTemplate = "Files/Templates/NullFlavorsTemplate.xml";
System.out.println(xlreader.getcolumnindex(ExcelFilePath,"FILENAME"));
//XpathSupport createFile = new XpathSupport();




System.out.println("datarowcount="+xlreader.getDataRowCount(ExcelFilePath));
 
	for (int j = 1; j <= xlreader.getDataRowCount(ExcelFilePath); j++) {
		System.out.println("flag="+xlreader.getcellvalue(ExcelFilePath,j,"flag"));
		if (xlreader.getcellvalue(ExcelFilePath,j,"flag").equalsIgnoreCase("Y")
				|| xlreader.getcellvalue(ExcelFilePath,j,"flag") != null) {
			System.out.println("jasonString="+xlreader.getcellvalue(ExcelFilePath,j,"XPATH"));
			String jasonString = xlreader.getcellvalue(ExcelFilePath,j,"XPATH"); 
			System.out.println("newfilename="+xlreader.getcellvalue(ExcelFilePath,j,"FILENAME").toString());
			String newfilename = xlreader.getcellvalue(ExcelFilePath,j,"FILENAME");
			try {
				//createFile.createfilefromtemplate1(validtemplatefile, jasonString,
					//	"Files/bizrulefilesfldr/" + newfilename + ".xml");
			} catch (Exception e) {
				System.out.println("Error in creating file");
				e.printStackTrace();
			}
		}
	}
}*/