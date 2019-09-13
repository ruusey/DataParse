package xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.owlike.genson.Genson;

import csv.CSVParse;

public class XMLParse {
	static final Logger LOGGER = LogManager.getLogger(XMLParse.class);
	static Genson gen = new Genson();
	public static void main(String[] args) {
		mapXML("resources/objects.xml");
	}
	public static void mapXML(String path) {
		Document doc = parseXMLFile(path);
		
		Node root= doc.getDocumentElement();
		LOGGER.info("[XMLParser] parsing XML tree '"+root.getNodeName()+"'");
		NodeList nodeList = root.getChildNodes();
		for(int i = 0; i<nodeList.getLength();i++) {
			
			Node child = nodeList.item(i);
			if(child.getNodeType()==Node.TEXT_NODE) continue;
			NodeList childList = root.getChildNodes();
			for(int j = 0; j < child.getChildNodes().getLength();j++) {
				childList.item(j).getNodeName();
			}
			
			LOGGER.info("[XMLParser] "+gen.serialize(child.getTextContent()));
		}
		
	}
	private static Document parseXMLFile(String path){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(path);
			dom.normalize();
			//dom.normalizeDocument();
			return dom;
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}
