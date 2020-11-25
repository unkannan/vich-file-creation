package com.azure.cosmos.vich.dbresults.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class SafetyReportCheckInCosmosDB {
	
	static DocumentBuilderFactory f = null;
	static DocumentBuilder b = null;
	static Document doc = null;
	static XPath xPath = null;
	static JSONParser parser=null;
	static JSONObject parentObject = null;
	
	/*
	
	
	InputStream inp =null;
	Workbook wb =null;
	Sheet sheet=null;
	XpathSupport createFile=null;
	final int flagcheck = 0;
	final int xmlField = 8;
	final int filename = 9;
	static int  rand_int =0;
	String ProcessedFilesArchieveFolderPath="C:\\Users\\kannanu\\eclipse-workspace\\adr-messaging-tests\\src\\main\\resources\\VETFunctionalTestData\\ProcessedFilesArchieve";
	String DirectoryToCreateFiles="Files/createvichtestfiles";
	File dirProcessedFilesArchieve;
	String dirVetFunctionaltestdataDirPath="C:\\Users\\kannanu\\eclipse-workspace\\adr-messaging-tests\\src\\main\\resources\\VETFunctionalTestData";
	File dirdirVetFunctionaltestdataDir;
	*/
	
	ConnectMongoDB connectingmongo=null;
	DB db=null;
	public SafetyReportCheckInCosmosDB() {
		connectingmongo=new ConnectMongoDB();
		db=connectingmongo.connectdb();
	}

	public String SafetyReportValidationCheckInDB(String newfilename) {
		//DBObject query = new BasicDBObject("batchNumber",newfilename).append("officialReceiveDate", 1);
		DBObject query = new BasicDBObject("reports.reportIdentifier","Message_"+newfilename);
		BasicDBObject field1 = new BasicDBObject("_id", 0).append("reports.detailText", 1);
		//BasicDBObject field2 = new BasicDBObject("officialReceiveDate", 1);
		

//		query.put("_id", 0);
        DBCollection col = db.getCollection("reported.AerMessageAcknowledgement"); 
        DBCursor c1 = col.find(query,field1).sort(new BasicDBObject("_id", -1)).limit(1);
        
        while (c1.hasNext()) {
			String ActualSafetyReportResultsStr=c1.next().toString();
			System.out.println("ActualSafetyReportResults In DB ="+ActualSafetyReportResultsStr);
			//Const jsonString = JSON.stringify(ActualSafetyReportResultsStr);
			return ActualSafetyReportResultsStr;
		}
		c1.close();
		return null;
	}
	
	public String ReadJason(String newfilename) throws ParserConfigurationException, SAXException, IOException, ParseException, XPathExpressionException {
		String jasonxpath="{\r\n" + 
				"    \"_id\" : NumberLong(20000000410),\r\n" + 
				"    \"batchNumber\" : \"EU-EC-M-20000000410-ACK\",\r\n" + 
				"    \"batchSenderIdentifier\" : \"USFDACVM\",\r\n" + 
				"    \"batchReceiverIdentifier\" : \"SubmittingOrganizationID\",\r\n" + 
				"    \"localMessageNumber\" : \"EU-EC-M-20000000410\",\r\n" + 
				"    \"batchTransmissionTime\" : ISODate(\"2020-08-20T10:01:05.890Z\"),\r\n" + 
				"    \"documentStoreIdentifier\" : \"bd3def37-af9b-4c5c-b8e3-bbb567af0148\",\r\n" + 
				"    \"reports\" : [ \r\n" + 
				"        {\r\n" + 
				"            \"reportId\" : NumberLong(20000000410),\r\n" + 
				"            \"reportIdentifier\" : \"Message_B7_vich_worddoc_attached_p\",\r\n" + 
				"            \"localReportNumber\" : \"EU-EC-20000000410\",\r\n" + 
				"            \"createdOn\" : ISODate(\"2020-08-20T10:01:05.890Z\"),\r\n" + 
				"            \"reportValidity\" : \"correct\",\r\n" + 
				"            \"detailText\" : \"safety report loaded; Validated against 3.00 business rules;\\n                    Comments: Parsing process: Correct report;\\n                    Classification: case number MESSAGE_B7_VICH_WORDDOC_ATTACHED_P, Replaced report\\n\"\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
				//SafetyReportValidation(newfilename);
		 try {	
	   if(jasonxpath==null) return null;
		 DocumentBuilderFactory f = null;
		 DocumentBuilder b = null;
		 Document doc = null;
		 XPath xPath = null;
		
		 JSONParser parser=null;
		 JSONObject parentObject = null;
	
		f = DocumentBuilderFactory.newInstance();
		b = f.newDocumentBuilder();
		doc = b.parse(jasonxpath);
		xPath = XPathFactory.newInstance().newXPath();

		parser = new JSONParser();
		parentObject = (JSONObject) parser.parse(jasonxpath);
		JSONArray parentArray = (JSONArray) parentObject.get("xpath");

		int i = 0;
		Node startDateNode = (Node) xPath.compile(jasonxpath).evaluate(doc, XPathConstants.NODE);
		System.out.println(startDateNode.getNodeValue());
		 }catch(Exception e) {e.printStackTrace();}
		return null;
		
	}

	public String ackValidation(String newfilename) throws FileNotFoundException {
	//	String text = extractElement("/MCCI_IN200101UV01/MCCI_IN000002UV01/id/@extension", newfilename);
		//String text = extractElement("/MCCI_IN200101UV01/MCCI_IN000002UV01//text", newfilename);
		 String xmlStr = new Scanner( new File(newfilename) ).useDelimiter("\\Z").next();
		 return extractElement1("text",xmlStr);
		}
	
	private static String extractElement(String elementName,String xml) {
		System.out.println(xml);
		XPath xPath = null;
		    Document doc = null;
		  DocumentBuilder b = null;
		  DocumentBuilderFactory f = null;
		f = DocumentBuilderFactory.newInstance();
		try {
		b = f.newDocumentBuilder();
		doc = b.parse(xml);
				}catch(Exception e) {}
	
		xPath = XPathFactory.newInstance().newXPath();

		Node startDateNode = null;
		try {
			startDateNode = (Node) xPath.compile(elementName).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			System.out.println("StartNodeValue="+startDateNode.getNodeValue());
			e.printStackTrace();
		}
		System.out.println(startDateNode.getNodeValue());
		return startDateNode.getNodeValue();
	}
	
	private static String extractElement1(String elementName, String xml) {
		String start = "<" + elementName + ">";
		String end = "</" + elementName + ">";
		int pos = xml.indexOf(start);
		if (pos < 0) {
			return null;
		}
		int from = pos + start.length();
		int to = xml.indexOf(end, from);
		if (to < 0) {
			return null;
		}
		return xml.substring(from, to);
	}

	public String SearchForFile(String dir,String file) {
		System.out.println("file searching="+file);
		File[] files = new File(dir).listFiles();
		for(File f:files) {
			String localfile=(f.getName()).substring(0,(f.getName()).length()-11);
			System.out.println("ack file = "+f.getName());
			System.out.println("substring= "+f.getName().substring(4,file.length()+4));
			
			if((file).equalsIgnoreCase(f.getName().substring(4,file.length()+4)))
	            return f.getName();
		}
		return null;
	 } 
	}

