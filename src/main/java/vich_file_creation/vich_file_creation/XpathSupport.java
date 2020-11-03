package vich_file_creation.vich_file_creation;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.util.Random;

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

	static JSONParser parser = null;
	static JSONObject parentObject = null;
	InputStream inp = null;
	Workbook wb = null;
	Sheet sheet = null;
	final int flagcheck = 0;
	final int xmlField = 9;
	final int filename = 10;
	static int rand_int = 0;
	String ProcessedFilesArchieveFolderPath = "C:\\Users\\kannanu\\eclipse-workspace\\adr-messaging-tests\\src\\main\\resources\\VETFunctionalTestData\\ProcessedFilesArchieve";
	String DirectoryToCreateFiles = "Files/createvichtestfiles";
	File dirProcessedFilesArchieve;
	String dirVetFunctionaltestdataDirPath = "C:\\Users\\kannanu\\eclipse-workspace\\adr-messaging-tests\\src\\main\\resources\\VETFunctionalTestData";
	File dirdirVetFunctionaltestdataDir;

	public XpathSupport() throws IOException {
		f = DocumentBuilderFactory.newInstance();
		try {
			b = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		parser = new JSONParser();
		xPath = XPathFactory.newInstance().newXPath();
	}

	private Document parseXML(String templatefileXML) {
		try {
			doc = b.parse(templatefileXML);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public void CreateVICHFilesFromInputSheet(String templatefileXML, String NullFlavorsTemplate,
			String InputExcelxpath, String newfilename) {
		File destDir = new File(DirectoryToCreateFiles);
		doc = parseXML(templatefileXML);// parsing template xml

		try {
			parentObject = (JSONObject) parser.parse(InputExcelxpath);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONArray parentArray = (JSONArray) parentObject.get("xpath");
		int XpathElement = 0;
		while (XpathElement < parentArray.size()) {
			JSONObject finalObject = (JSONObject) parentArray.get(XpathElement++);
			String xmlxpathstr = finalObject.get("field").toString();
			setBatchCaseidentifiers(newfilename);
			updatingXpathValueinXML(finalObject.get("value").toString(), xmlxpathstr, NullFlavorsTemplate);
		}
		newfilename = writingfinalFileFromdoc(newfilename);
		// File dirVetFunctionaltestdataDir=new File(dirVetFunctionaltestdataDirPath);
		// CopyFilesToVetFoldertoRun(destDir,dirVetFunctionaltestdataDir);
	}

	void updatingXpathValueinXML(String value, String xmlxpathstr, String NullFlavorsTemplate) {
		Node startDateNode = null;
		try {
			startDateNode = (Node) xPath.compile(xmlxpathstr).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		if (value.equals("remove")) {
			startDateNode.getParentNode().removeChild(startDateNode);
		} else if (value.equals("null")) {
			AppendNullFlavorNode(startDateNode, xmlxpathstr, NullFlavorsTemplate);
		} else {
			setNodeValueinDoc(xmlxpathstr, value);
		}
	}

	void setBatchCaseidentifiers(String newfilename) {
		File file = new File(newfilename.replaceAll(".xml", ""));
		// Batch identifier
		setNodeValueinDoc("/MCCI_IN200100UV01/id/@extension", "Batch_" + file.getName());
		// message number
		setNodeValueinDoc("/MCCI_IN200100UV01/PORR_IN049006UV/id/@extension", "Message_" + file.getName());
		// Case identifier
		Random rand = new Random();
		rand_int = rand.nextInt(100000);
		String caseidxpath = "/MCCI_IN200100UV01/PORR_IN049006UV/controlActProcess/subject/investigationEvent/id/@extension";
		setNodeValueinDoc(caseidxpath, "USA-GAPINDSY-" + rand_int);
	}

	void setNodeValueinDoc(String xmlElementXpath, String value) {
		Node node = null;
		try {
			node = (Node) xPath.compile(xmlElementXpath).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		node.setTextContent(value);
	}

	void AppendNullFlavorNode(Node startDateNode, String xmlxpathstr, String NullFlavorsTemplate) {
		DocumentBuilderFactory.newInstance();
		DocumentBuilder b1 = null;
		try {
			b1 = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc1 = null;
		try {
			doc1 = b1.parse(NullFlavorsTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String MainNodeXpath = xmlxpathstr.substring(0,
				(xmlxpathstr.length() - startDateNode.getNodeName().length() - 1));
		Node MainNode = null;
		try {
			MainNode = (Node) xPath.compile(MainNodeXpath).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println(MainNode.getNodeName());
		Node NullNode = null;
		try {
			NullNode = (Node) xPath.compile("/MCCI_IN200100UV01/" + startDateNode.getNodeName()).evaluate(doc1,
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println(NullNode.getNodeName());
		NullNode = doc.importNode(NullNode, true);
		MainNode.replaceChild(NullNode, startDateNode);
	}

	private String writingfinalFileFromdoc(String newfilename) {
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
			e.printStackTrace();
		}
		return newfilename;
	}

	public void CopyFilesToVetFoldertoRun(File srcDir, File dstDir) throws IOException {
		dirProcessedFilesArchieve = new File(ProcessedFilesArchieveFolderPath);
		copyfiles(dstDir, dirProcessedFilesArchieve);
		System.out.println("existing test files are archieved and placed in " + dirProcessedFilesArchieve);
		purgeDirectoryButKeepSubDirectories(dstDir);
		System.out.println("files purged in  " + dstDir);
		copyfiles(srcDir, dstDir);
		System.out.println("Final vich test files copied to " + dstDir);
	}

	void copyfiles(File srcDir, File dstDir) throws IOException {
		for (File srcFile : srcDir.listFiles()) {
			if (!srcFile.isDirectory()) {
				FileUtils.copyFileToDirectory(srcFile, dstDir);
			}
		}
	}

	void purgeDirectoryButKeepSubDirectories(File dir) {
		for (File file : dir.listFiles()) {
			if (!file.isDirectory()) {
				// System.out.println(file.getName());
				file.delete();
			}
		}
	}
}