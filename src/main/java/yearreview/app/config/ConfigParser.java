package yearreview.app.config;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ConfigParser {

	public final static String XML_VERSION = "0.1";

	public ConfigParser(File f) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			Node root = doc.getElementsByTagName("Configuration").item(0);

			// test for correct version
			if (!root.getAttributes().getNamedItem("version").getNodeValue().equals(XML_VERSION))
				throw new Error("XML-version does not match project version.");


		} catch (Exception e) {
			throw new Error(e);
		}
	}
}