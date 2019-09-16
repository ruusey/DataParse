package xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class XMLParse {
	static final Logger LOGGER = LogManager.getLogger(XMLParse.class);
	static Genson gen = new Genson();
	static List<String> log = new ArrayList<String>();

	public static void main(String[] args) {
		mapXML("resources/objects.xml");
	}

	public static List<String> mapXML(String path) {
		long startTime = System.currentTimeMillis();
		Document doc = parseXMLFile(path);
		Node root = doc.getDocumentElement();
		LOGGER.info("[XMLParser] Parsing XML tree '" + root.getNodeName() + "'");
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			recurseNode(child);
		}
		LOGGER.info("[XMLParser] Mapped XML file in [" + (System.currentTimeMillis() - startTime) + "ms]");
		return log;
	}

	private static Node recurseNode(Node root) {
		if (root == null)
			return null;
		if (root.getNodeType() == Node.TEXT_NODE || root.getNodeType() == Node.COMMENT_NODE)
			return null;
		NamedNodeMap nodeMap = root.getAttributes();
		if (nodeMap != null) {
			for (int k = 0; k < nodeMap.getLength(); k++) {
				Node mappedNode = nodeMap.item(k);
				String mappedName = mappedNode.getNodeName();
				String mappedValue = mappedNode.getNodeValue();
				LOGGER.info("[XMLParser] 	(" + root.getNodeName() + ") " + mappedName + "=" + mappedValue);
				log.add("(" + root.getNodeName() + ") " + mappedName + "=" + mappedValue);
			}
		}
		NodeList childList = root.getChildNodes();
		for (int j = 0; j < childList.getLength(); j++) {
			if (childList.item(j).getNodeType() == Node.TEXT_NODE
					|| childList.item(j).getNodeType() == Node.COMMENT_NODE)
				continue;
			Node mappedNode = childList.item(j);
			if (mappedNode.hasChildNodes()) {
				LOGGER.info("[XMLParser] 		(" + root.getNodeName() + ") " + mappedNode.getNodeName() + "<"
						+ mappedNode.getFirstChild().getTextContent().replace("\n", "") + ">");
				log.add("(" + root.getNodeName() + ") " + mappedNode.getNodeName() + "<"
						+ mappedNode.getFirstChild().getTextContent().replace("\n", "") + ">");
			} else {
				LOGGER.info("[XMLParser] 		(" + root.getNodeName() + ") " + mappedNode.getNodeName() + "<"
						+ mappedNode.getTextContent().replace("\n", "") + ">");
				log.add("(" + root.getNodeName() + ") " + mappedNode.getNodeName() + "<"
						+ mappedNode.getTextContent().replace("\n", "") + ">");
			}
			recurseNode(childList.item(j));
		}
		return null;
	}

	private static Document parseXMLFile(String path) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(path);
			dom.normalize();
			return dom;
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}
