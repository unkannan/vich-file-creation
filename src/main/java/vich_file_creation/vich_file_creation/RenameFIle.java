package vich_file_creation.vich_file_creation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class RenameFIle {
	
	InputStream inp =null;
	Workbook wb =null;
	Sheet sheet=null;
	int newfilenameCol=0;
	//final String bizRuleFolder ="C:\\Project\\VICH_ProjFolder\\testdata\\BizRules\\";
	static XPath xPath = null;
	static Document doc = null;
	static DocumentBuilder b = null;
	static DocumentBuilderFactory f = null;
	public RenameFIle(){
		
	}
	
	public void CreateObjects(String ExcelFilePath) throws IOException {
		inp = new FileInputStream(ExcelFilePath);
		wb = new XSSFWorkbook(inp);
	}

	public int ColumnIndexInExcelUsingHeaderValue(String ExcelHeader) {
		for (int col = 1; col <= 20; col++) {
			System.out.println(sheet.getRow(0).getCell(col).getStringCellValue());
			if(sheet.getRow(0).getCell(col).getStringCellValue().equalsIgnoreCase(ExcelHeader))
				return col;
		}
		return 0;
	}
	
	
	public void renameExistingFilesinGivenFolder(int Sheetnumber,String ExcelFilePath,String bizRuleFolder) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException, TransformerException {
		boolean flag=false;
		CreateObjects(ExcelFilePath);
		sheet=wb.getSheetAt(Sheetnumber);
		int FileNameCol=ColumnIndexInExcelUsingHeaderValue("filename");
		int newFileNameCol=ColumnIndexInExcelUsingHeaderValue("NewFileName");
		
		
		for (int j = 1; j <= sheet.getLastRowNum()+1; j++) {
			String filename = sheet.getRow(j).getCell(FileNameCol).getStringCellValue();
			String newfilename = sheet.getRow(j).getCell(newFileNameCol).getStringCellValue();
			File newfile=new File(bizRuleFolder+"//"+newfilename+".xml");
			File file=new File(bizRuleFolder+"//"+filename+".xml");
			System.out.println("Checking for file exists: "+file.getName());
			try {
			if(file.exists()) {
				System.out.println(filename);
				file.renameTo(newfile);
				System.out.println("file rename process completed is file: "+file.getName());
				System.out.println("new Batch and Message Identifiers getting updated now for file: "+newfile.getName());
				flag=settingBatchCaseidentifiers(newfile);
				if (flag)
					System.out.println("Update Batch and Messge Identifiers is Sucesss for file: "+newfile.getName());
				else
					System.out.println("Update Batch and Messge Identifiers Failed for file: "+newfile.getName());
			}
			else
				System.out.println("file Does not Exists : "+file.getName());
			}catch(Exception e)
			{
					e.printStackTrace(); 
			   }
		}
	}

	boolean settingBatchCaseidentifiers(File file) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, TransformerException {
		xPath = XPathFactory.newInstance().newXPath();
		f = DocumentBuilderFactory.newInstance();
		b = f.newDocumentBuilder();
		doc = b.parse(file.getAbsoluteFile());
		//File file = new File(newfilename.replaceAll(".xml", ""));
		//Batch identifier
		Node startDateNode = (Node) xPath.compile("/MCCI_IN200100UV01/id/@extension").evaluate(doc, XPathConstants.NODE);
		try{startDateNode.setTextContent("Batch_"+file.getName().replaceAll(".xml", ""));}
			catch(Exception e) {System.out.println("Batch ID is not updated");}
		//message number
		startDateNode = (Node) xPath.compile("/MCCI_IN200100UV01/PORR_IN049006UV/id/@extension").evaluate(doc, XPathConstants.NODE);
		try{startDateNode.setTextContent("Message_"+file.getName().replaceAll(".xml", ""));}
		catch(Exception e) {System.out.println("Message ID is not updated");}
		//Case identifier
		startDateNode = (Node) xPath.compile("/MCCI_IN200100UV01/PORR_IN049006UV/controlActProcess/subject/investigationEvent/id/@extension").evaluate(doc, XPathConstants.NODE);
		try{startDateNode.setTextContent("USA-USFDACVM"+"-"+file.getName().replaceAll(".xml","").substring(file.getName().length()-12));}
		catch(Exception e) {System.out.println("Case ID is not updated");}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);

		
		System.out.println("File created " + file.getName());
		System.out.println("****************************************************************************************************");
		return true;
		
	}
}
	
