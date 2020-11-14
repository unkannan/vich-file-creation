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
	//RenameFile file;
	File destDir = null;
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
		destDir=new File(DirectoryToCreateFiles);
	}
	//@Test
	public void CreateTestDataFilesFromExcel() {
		//File destDir = new File(DirectoryToCreateFiles);
		
		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath); row++) {
			if (xlreader.getcellvalue(ExcelFilePath,row,"Flag").equalsIgnoreCase("Y")) {
				String newfilename = xlreader.getcellvalue(ExcelFilePath,row,"FILENAME");
				System.out.println(newfilename+": File creation process started ");
				try {
					createFile.CreateVICHFilesFromInputSheet(VICH_TestFile_AllFields, NullFlavorsTemplate, xlreader.getcellvalue(ExcelFilePath,row,"XPATH"),destDir+ "/" + newfilename+".xml");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("File cannot be created " + newfilename);
				}
				System.out.println("File created " + newfilename);
				System.out.println("********************************************************");
			}
		}
	}
	
	@Test
	public void renameFile(){
		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath); row++) {
			if (xlreader.getcellvalue(ExcelFilePath,row,"Flag").equalsIgnoreCase("Y")) {
				String filename = xlreader.getcellvalue(ExcelFilePath,row,"FILENAME");
				String newfilename = xlreader.getcellvalue(ExcelFilePath,row,"NewFileName");
				File file=new File(destDir+"//"+filename+".xml");
				File newfile=new File(destDir+"//"+newfilename+".xml");
				System.out.println("Checking for file exists: "+file.getName());
				}
			}
		}
}
