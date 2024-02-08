package yearreview.app.config;

import org.w3c.dom.*;

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

			NodeList nodes = root.getChildNodes();

			Node current;
			Node settings = null, dataSources = null, widgets = null;
			for (int i = 0; i < nodes.getLength(); i++) {
				current = nodes.item(i);
				if (current.getNodeType() == Node.ELEMENT_NODE) {
					if (current.getNodeName().equals("Settings")) {
						settings = current;
						continue;
					}
					if (current.getNodeName().equals("DataSources")) {
						dataSources = current;
						continue;
					}
					if (current.getNodeName().equals("Widgets")) {
						widgets = current;
						continue;
					}
				}
			}

			if (settings == null)
				throw new Error("Configuration file lacks Settings node.");

			if (dataSources == null)
				throw new Error("Configuration file lacks DataSources node.");

			if (widgets == null)
				throw new Error("Configuration file lacks Widgets node.");

			parseSettings(settings);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private void parseSettings(Node settings) {
		System.out.println(settings.getNodeName());
		Node startTime = null, endTime = null;
		Node video = null;
		Node grid = null;

		NodeList subNodes = settings.getChildNodes();

		Node current;
		for (int i = 0; i < subNodes.getLength(); i++) {
			current = subNodes.item(i);
			if (current.getNodeType() == Node.ELEMENT_NODE) {
				if (current.getNodeName().equals("Start")) {
					startTime = current;
					continue;
				}
				if (current.getNodeName().equals("End")) {
					endTime = current;
				}
				if (current.getNodeName().equals("Video")) {
					video = current;
					continue;
				}
				if (current.getNodeName().equals("Grid")) {
					grid = current;
					continue;
				}
			}
		}

		if (startTime == null || endTime == null || video == null || grid == null)
			throw new Error("Settings part in configuration incomplete.");


	}
}