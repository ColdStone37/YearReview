package yearreview.app.config;

import org.w3c.dom.*;
import sun.security.krb5.Config;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.Instant;

public class ConfigParser {

	public final static String XML_VERSION = "0.1";

	public ConfigParser(File f) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			ConfigNode root = new ConfigNode(doc.getElementsByTagName("Configuration").item(0));

			// test for correct version
			if (!root.getAttributeByName("version").equals(XML_VERSION))
				throw new Error("XML-version does not match project version.");

			root.assertChildNodesExist("Settings", "DataSources", "Widgets");
			ConfigNode settings = root.getChildByName("Settings");
			ConfigNode dataSources = root.getChildByName("DataSources");
			ConfigNode widgets = root.getChildByName("Widgets");

			parseSettings(settings);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private void parseSettings(ConfigNode settings) {
		settings.assertChildNodesExist("Start", "End");
		GlobalSettings.setVideoStart(Instant.parse(settings.getChildContent("Start")));
		GlobalSettings.setVideoEnd(Instant.parse(settings.getChildContent("End")));
		ConfigNode video = settings.getChildByName("Video");
		if (video != null) {
			if (video.hasChild("Width"))
				GlobalSettings.setVideoWidth(Integer.parseInt(video.getChildContent("Width")));
			if (video.hasChild("Height"))
				GlobalSettings.setVideoHeight(Integer.parseInt(video.getChildContent("Height")));
			if (video.hasChild("Framerate"))
				GlobalSettings.setVideoFramerate(Integer.parseInt(video.getChildContent("Framerate")));
			if (video.hasChild("Supersampling"))
				GlobalSettings.setSupersampling(Integer.parseInt(video.getChildContent("Supersampling")));
		}
		ConfigNode grid = settings.getChildByName("Grid");
		if (grid != null) {
			if (grid.hasChild("Width"))
				GlobalSettings.setGridWidth(Integer.parseInt(grid.getChildContent("Width")));
			if (grid.hasChild("Height"))
				GlobalSettings.setGridWidth(Integer.parseInt(grid.getChildContent("Height")));
		}
	}
}