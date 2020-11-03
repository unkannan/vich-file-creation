package vich_file_creation.vich_file_creation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
 
import java.io.InputStream;
import java.util.Random;
 
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.xml.namespace.QName;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.commons.io.FileUtils;

public class XpathSupport {
 
	static DocumentBuilderFactory f = null;
	static DocumentBuilder b = null;
	static Document doc = null;
	static XPath xPath = null;
	
	static JSONParser parser=null;
	static JSONObject parentObject = null;
	InputStream inp =null;
	Workbook wb =null;
	Sheet sheet=null;
	XpathSupport createFile=null;
	final int flagcheck = 0;
	final int xmlField = 9;
	final int filename = 10;
	static int  rand_int =0;
	String ProcessedFilesArchieveFolderPath="C:\\Users\\kannanu\\eclipse-workspace\\adr-messaging-tests\\src\\main\\resources\\VETFunctionalTestData\\ProcessedFilesArchieve";
	String DirectoryToCreateFiles="Files/createvichtestfiles";
	File dirProcessedFilesArchieve;
	String dirVetFunctionaltestdataDirPath="C:\\Users\\kannanu\\eclipse-workspace\\adr-messaging-tests\\src\\main\\resources\\VETFunctionalTestData";
	File dirdirVetFunctionaltestdataDir;

	public void CreateObjects(String ExcelFilePath) throws IOException {
		inp = new FileInputStream(ExcelFilePath);
		wb = new XSSFWorkbook(inp);
		createFile = new XpathSupport();
	}

	public void CreatingXMLFilesFromGivenSheet(int Sheetnumber,String ExcelFilePath,String ValidVICHtemplate,String NullFlavorsTemplate) throws IOException{
		CreateObjects(ExcelFilePath);
		sheet=wb.getSheetAt(Sheetnumber);
		
		//Cleans the directory files of Files/Sheet1 
		File destDir = new File(DirectoryToCreateFiles);
		purgeDirectoryButKeepSubDirectories(destDir);
		
		//Creating files
		for (int j = 1; j <= sheet.getLastRowNum()+1; j++) {
			try {
				if (sheet.getRow(j).getCell(flagcheck).toString().equalsIgnoreCase("y")) {
					System.out.println("****************************************************************************************************");
					String jasonString = sheet.getRow(j).getCell(xmlField).getStringCellValue();
					String newfilename = sheet.getRow(j).getCell(filename).getStringCellValue();
					System.out.println(sheet.getRow(j).getCell(filename).getStringCellValue());
					System.out.println(sheet.getRow(j).getCell(xmlField).getStringCellValue());
					try {
						createFile.FileContentModifyFromVICHTemplate(ValidVICHtemplate, NullFlavorsTemplate, jasonString,destDir+ "/" + newfilename+".xml");
					} catch (Exception e) {
						System.out.println("***********************Red  ALERT********************************");
						System.out.println("***********************Red  ALERT********************************");
						System.out.println("***********************Red  ALERT********************************");
						System.out.println("Error in creating file > " + newfilename + ".xml");
						System.out.println("***********************Red  ALERT********************************");
						System.out.println("***********************Red  ALERT********************************");
						System.out.println("***********************Red  ALERT********************************");
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
			}
			
		}
		File dirVetFunctionaltestdataDir=new File(dirVetFunctionaltestdataDirPath);
		CopyFilesToVetFoldertoRun(destDir,dirVetFunctionaltestdataDir);
	}
	
	public void FileContentModifyFromVICHTemplate(String templatefileXML, String NullFlavorsTemplate, String jasonxpath,
			String newfilename) throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException, TransformerException, ParseException {
		
		f = DocumentBuilderFactory.newInstance();
		b = f.newDocumentBuilder();
		doc = b.parse(templatefileXML);
		xPath = XPathFactory.newInstance().newXPath();

		parser = new JSONParser();
		parentObject = (JSONObject) parser.parse(jasonxpath);
 
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = null;
		try {
			b = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = b.parse(templatefileXML);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONParser parser = new JSONParser();

		JSONObject parentObject = null;
		try {
			parentObject = (JSONObject) parser.parse(jasonxpath);
		} catch (ParseException e) {
			e.printStackTrace();
		}
 
		JSONArray parentArray = (JSONArray) parentObject.get("xpath");
		int i = 0;
		while (i < parentArray.size()) {
 
			Random rand = new Random(); 
			rand_int = rand.nextInt(1000);
			
			JSONObject finalObject = (JSONObject) parentArray.get(i++);
			//System.out.println(jasonxpath);
			String xmlxpathstr = finalObject.get("field").toString();
			//Set the Batch identifier and cases with new id
			settingBatchCaseidentifiers(newfilename);
			Node startDateNode = (Node) xPath.compile(xmlxpathstr).evaluate(doc, XPathConstants.NODE);
			System.out.println(startDateNode.getNodeName());
			ModifyXpathInFileContent(finalObject.get("value").toString(),startDateNode,xmlxpathstr,NullFlavorsTemplate);
		}
		
		createfinalfile(newfilename);
	}
 
	void ModifyXpathInFileContent(String value,Node startDateNode,String xmlxpathstr,String NullFlavorsTemplate) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		if (value.equals("remove")) {
			RemoNode(startDateNode);
		} else if (value.equals("null")) {
			AppendNullFlavorNode(startDateNode, xmlxpathstr, NullFlavorsTemplate);
		} else {
			SetXMLText(startDateNode, value);
		}
	}
	void settingBatchCaseidentifiers(String newfilename) throws XPathExpressionException {
		
		File file = new File(newfilename.replaceAll(".xml", ""));
		//System.out.println(file.getName());
		//Batch identifier
		Node startDateNode = (Node) xPath.compile("/MCCI_IN200100UV01/id/@extension").evaluate(doc, XPathConstants.NODE);
		startDateNode.setTextContent("Batch_"+file.getName());
		//message number
		startDateNode = (Node) xPath.compile("/MCCI_IN200100UV01/PORR_IN049006UV/id/@extension").evaluate(doc, XPathConstants.NODE);
		startDateNode.setTextContent("Message_"+file.getName());
		//Case identifier
		startDateNode = (Node) xPath.compile("/MCCI_IN200100UV01/PORR_IN049006UV/controlActProcess/subject/investigationEvent/id/@extension").evaluate(doc, XPathConstants.NODE);
		startDateNode.setTextContent("Case_"+file.getName());
		
	}
 	void SetXMLText(Node startDateNode,String value) throws XPathExpressionException{
				//System.out.println(startDateNode.getNodeName());
					startDateNode.setTextContent(value);
	}

	void RemoNode(Node startDateNode) throws XPathExpressionException {
			//	System.out.println(startDateNode.getNodeName());
					startDateNode.getParentNode().removeChild(startDateNode);
	}
	
	void AppendNullFlavorNode(Node startDateNode,String xmlxpathstr, String NullFlavorsTemplate)
			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory.newInstance();
				DocumentBuilder b1 = f.newDocumentBuilder();
					Document doc1 = b1.parse(NullFlavorsTemplate);

		String MainNodeXpath = xmlxpathstr.substring(0,
				(xmlxpathstr.length() - startDateNode.getNodeName().length() - 1));
			Node MainNode = (Node) xPath.compile(MainNodeXpath).evaluate(doc, XPathConstants.NODE);
System.out.println(MainNode.getNodeName());
		Node NullNode = (Node) xPath.compile("/MCCI_IN200100UV01/" + startDateNode.getNodeName()).evaluate(doc1, XPathConstants.NODE);
		System.out.println(NullNode.getNodeName());
			NullNode = doc.importNode(NullNode, true);
	    		MainNode.replaceChild(NullNode,startDateNode);
		}

	
	void createfinalfile(String newfilename) throws TransformerException {
		// write the content into xml file
 /*
				JSONObject finalObject = (JSONObject) parentArray.get(i++);
					System.out.println(finalObject.get("field"));
				String xpath = finalObject.get("field").toString();
					System.out.println(finalObject.get("value"));

			UpdateElementValueInXMLContent(doc,xpath,finalObject.get("value").toString());
			UpdateBatchMessageIdentifiers(doc,newfilename);
		}*/
		// write the content into xml file
		newfilename=writeFinalContentInXMLFile(doc,newfilename);
		
		System.out.println("File created " + newfilename);
		System.out.println("********************************************************");
	}
	
/*	private void UpdateElementValueInXMLContent(Document doc,String xpath,String value){
		XPath xPath = XPathFactory.newInstance().newXPath();
		Node startDateNode = null;
		try {
			startDateNode = (Node) xPath.compile(xpath).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		startDateNode.setTextContent(value);
	}*/
	
	/*
	private void UpdateBatchMessageIdentifiers(Document doc,String newfilename) {
		File f=new File(newfilename);
		//Updating Batch ID
		UpdateElementValueInXMLContent(doc,"/MCCI_IN200100UV01/id/@extension",f.getName().substring(0,f.getName().length()-4));
		
		//Updating Message id
		UpdateElementValueInXMLContent(doc,"/MCCI_IN200100UV01/PORR_IN049006UV/id/@extension",f.getName().substring(0,f.getName().length()-4));
		
		//updating case id with random number in last chars
		int n = 10000 + new Random().nextInt(90000); //generating random no.
		UpdateElementValueInXMLContent(doc,"/MCCI_IN200100UV01/PORR_IN049006UV/controlActProcess/subject/investigationEvent/id/@extension",
				"USA-GAPINDSY-"+n);

	}*/
	
	private String writeFinalContentInXMLFile(Document doc,String newfilename){
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(newfilename));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newfilename;
	//	System.out.println("File created " + newfilename);
	//	System.out.println("****************************************************************************************************");
		
	}


	
	public void CopyFilesToVetFoldertoRun(File srcDir,File dstDir) throws IOException {
		dirProcessedFilesArchieve=new File(ProcessedFilesArchieveFolderPath);
		copyfiles(dstDir,dirProcessedFilesArchieve);
		System.out.println("existing test files are archieved and placed in "+dirProcessedFilesArchieve);
		purgeDirectoryButKeepSubDirectories(dstDir);
		System.out.println("files purged in  "+dstDir);
		copyfiles(srcDir,dstDir);
		System.out.println("Final vich test files copied to "+ dstDir);
	}
	void copyfiles(File srcDir,File dstDir) throws IOException {
		for (File srcFile: srcDir.listFiles()) {
		    if (!srcFile.isDirectory()) {
		        FileUtils.copyFileToDirectory(srcFile, dstDir);
		    }
		}
	}

	void purgeDirectoryButKeepSubDirectories(File dir) {
	    for (File file: dir.listFiles()) {
	        if (!file.isDirectory()) {
	        	//System.out.println(file.getName());
	            file.delete();
	        }
	    }
	}
}