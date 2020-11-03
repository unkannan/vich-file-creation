package com.org.createfiles;

import Lib.ExcelReader;
import vich_file_creation.vich_file_creation.XpathSupport;
import java.io.File;
import java.io.IOException;

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
		try {
			createFile = new XpathSupport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//renamefileObj=new RenameFIle();
		xlreader=new ExcelReader();
		
	}
	@Test
	public void CreateTestDataFilesFromExcel() {
		File destDir = new File(DirectoryToCreateFiles);
		System.out.println("kannan");
		
		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath); row++) {
			if (xlreader.getcellvalue(ExcelFilePath,row,"Flag").equalsIgnoreCase("Y")) {
				String newfilename = xlreader.getcellvalue(ExcelFilePath,row,"FILENAME");
				System.out.println(newfilename+": File creation process started ");
				try {
					createFile.CreateVICHFilesFromInputSheet(VICH_TestFile_AllFields, NullFlavorsTemplate, xlreader.getcellvalue(ExcelFilePath,row,"XPATH"),destDir+ "/" + newfilename+".xml");
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("File created " + newfilename);
				System.out.println("********************************************************");
			}
		}
	}
}	