package vich_file_creation.vich_file_creation;

import java.io.File;
import java.io.IOException;
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

public class BatchIDUpdates {

	public static void setBatchCaseidentifiersforRenamedFiles(String xmlfile) {
		DocumentBuilder b = null;
		Document doc = null;
		File file = new File(xmlfile);
		System.out.println(xmlfile);
		System.out.println(file.getName());
		//XPath xPath = XPathFactory.newInstance().newXPath();
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		try {
			b = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			doc = b.parse(xmlfile);
			System.out.println("xml parsed successfully");
			System.out.println(xmlfile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		doc = setNodeValueinDoc("/MCCI_IN200100UV01/id/@extension", "Batch_" + file.getName(), doc);
		doc = setNodeValueinDoc("/MCCI_IN200100UV01/PORR_IN049006UV/id/@extension", "Message_" + file.getName(), doc);
		// Case identifier
		Random rand = new Random();
		int rand_int = rand.nextInt(100000);
		doc = setNodeValueinDoc(
				"/MCCI_IN200100UV01/PORR_IN049006UV/controlActProcess/subject/investigationEvent/id/@extension",
				"USA-GAPINDSY-" + rand_int, doc);
		writingfinaltestxmlFileFromdoc(xmlfile, doc);
	}

	private static Document setNodeValueinDoc(String xmlElementXpath, String filename_value, Document doc) {
		filename_value=filename_value.replaceAll(".xml", "");
		XPath xPath = XPathFactory.newInstance().newXPath();
		Node node = null;
		try {
			node = (Node) xPath.compile(xmlElementXpath).evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		node.setTextContent(filename_value);
		return doc;
	}

	private static void writingfinaltestxmlFileFromdoc(String newfilename, Document doc) {
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
	}

}
