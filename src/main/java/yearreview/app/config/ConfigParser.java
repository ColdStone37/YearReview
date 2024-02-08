package yearreview.app.config;

import org.w3c.dom.*;
import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.Instant;

import yearreview.app.grid.GridManager;
import yearreview.app.render.Renderer;

public class ConfigParser {

	public final static String XML_VERSION = "0.1";

	private ConfigNode widgets;
	private ConfigNode dataSources;

	public ConfigParser(String[] args) {
		Options options = new Options();

		Option config = new Option("c", "config", true, "config file path");
		config.setRequired(true);
		options.addOption(config);

		Option output = new Option("o", "output", true, "output file name");
		options.addOption(output);

		Option delete = new Option("d", "delete", false, "deletes file with same name as output if exists");
		options.addOption(delete);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();

		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("output"))
				GlobalSettings.setOutputFilename(cmd.getOptionValue("output"));
			File f = new File(GlobalSettings.getOutputFilename());
			if (cmd.hasOption("delete")) {
				if (f.isFile())
					f.delete();
			}
			if (f.isFile())
				throw new Error("Output file does already exists. Either change output name or add -d flag to automatically delete it.");
			parseConfigFile(new File(cmd.getOptionValue("config")));
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
		}
	}

	private void parseConfigFile(File f) {
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
			dataSources = root.getChildByName("DataSources");
			widgets = root.getChildByName("Widgets");

			parseSettings(settings);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	public ConfigNode getWidgetSettings() {
		return widgets;
	}

	public ConfigNode getDataSourcesSettings() {
		return dataSources;
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
				GlobalSettings.setGridHeight(Integer.parseInt(grid.getChildContent("Height")));
			if (grid.hasChild("InnerSpacing"))
				GlobalSettings.setGridInnerSpacing(Integer.parseInt(grid.getChildContent("InnerSpacing")));
			if (grid.hasChild("OuterSpacing"))
				GlobalSettings.setGridOuterSpacing(Integer.parseInt(grid.getChildContent("OuterSpacing")));
		}
	}
}