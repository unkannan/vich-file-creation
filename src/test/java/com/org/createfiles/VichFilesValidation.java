package com.org.createfiles;

import Lib.ExcelReader;
import vich_file_creation.vich_file_creation.XpathSupport;
import java.io.File;
import java.io.IOException;

import org.junit.*;

public class VichFilesValidation {
	static String ExcelFilePath = "Files/Templates/createfilesfromxpath_A.xlsx";
	final String VICH_TestFile_AllFields = "Files/Templates/VICH_File_With_All_Elements.xml";
	ExcelReader xlreader = null;
	XpathSupport createFile = null;
	final String NullFlavorsTemplate = "Files/Templates/NullFlavorsTemplate.xml";
	//String DirectoryToCreateFiles = "Files/createvichtestfiles";
	String DirectoryToCreateFiles = "Files/SectionA/Mand";
	// RenameFile file;
	File destDir = null;
	final int dataSheet=0;

	@Before
	public void PrerequisitesBeforecreation() {
		try {
			createFile = new XpathSupport();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// renamefileObj=new RenameFIle();
		xlreader = new ExcelReader();
		destDir = new File(DirectoryToCreateFiles);
	}

	// @Test
	public void CreateTestDataFilesFromExcel() {
		// File destDir = new File(DirectoryToCreateFiles);

		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath,dataSheet); row++) {
			if (xlreader.getcellvalue(ExcelFilePath,dataSheet, row, "Flag").equalsIgnoreCase("Y")) {
				String newfilename = xlreader.getcellvalue(ExcelFilePath,dataSheet, row, "FILENAME");
				System.out.println(newfilename + ": File creation process started ");
				try {
					createFile.CreateVICHFilesFromInputSheet(VICH_TestFile_AllFields, NullFlavorsTemplate,
							xlreader.getcellvalue(ExcelFilePath,dataSheet, row, "XPATH"), destDir + "/" + newfilename + ".xml");
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
	public void renameFile() {
		for (int row = 1; row <= xlreader.getDataRowCount(ExcelFilePath,dataSheet); row++) {
			if (xlreader.getcellvalue(ExcelFilePath,dataSheet, row, "Flag").equalsIgnoreCase("Y")) {
				String filename = xlreader.getcellvalue(ExcelFilePath,dataSheet, row, "FILENAME");
				String newfilename = xlreader.getcellvalue(ExcelFilePath,dataSheet,row, "NewFileName");
				File file = new File(destDir + "//" + filename + ".xml");
				File newfile = new File(destDir + "//" + newfilename + ".xml");
				System.out.println("Checking for file exists: " + file.getName());

				try {
					if (file.exists()) {
						System.out.println(filename);
						file.renameTo(newfile);
						System.out.println("file rename process completed is file: " + newfile.getName());
					} else
						System.out.println("file Does not Exists : " + file.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
