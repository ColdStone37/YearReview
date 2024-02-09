package yearreview.app.config;

import org.w3c.dom.*;
import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.Instant;

/**
 * Parser for the command line arguments and the configuration file.
 *
 * @author ColdStone37
 */
public class ConfigParser {
	/**
	 * Version of the configuration.
	 **/
	public final static String XML_VERSION = "0.1";
	/**
	 * Configuration for the widgets.
	 */
	private ConfigNode widgets;
	/**
	 * Configuration for the data sources.
	 */
	private ConfigNode dataSources;

	/**
	 * Constructs a ConfigParser and parses the arguments and config file.
	 *
	 * @param args command line arguments
	 */
	public ConfigParser(String[] args) {
		// Add command line options
		Options options = new Options();

		Option config = new Option("c", "config", true, "config file path");
		config.setRequired(true);
		options.addOption(config);

		Option output = new Option("o", "output", true, "output file name");
		options.addOption(output);

		Option delete = new Option("d", "delete", false, "deletes file with same name as output if exists");
		options.addOption(delete);

		Option help = new Option("h", "help", false, "Prints this menu");
		options.addOption(help);

		HelpFormatter formatter = new HelpFormatter();

		try {
			// Parse the arguments
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				formatter.printHelp("set the configuration file with the -c option, all other arguments are optional", options);
				System.exit(0);
			}

			if (cmd.hasOption("output"))
				GlobalSettings.setOutputFilename(cmd.getOptionValue("output"));

			File f = new File(GlobalSettings.getOutputFilename());

			// Delete the video file if it already exists and the -d flag is set
			if (cmd.hasOption("delete") && f.isFile())
				f.delete();

			// ffmpeg doesn't like it when the file already exists
			if (f.isFile())
				throw new Error("Output file does already exists. Either change output name or add -d flag to automatically delete it.");

			// Parse the Config file
			parseConfigFile(cmd.getOptionValue("config"));
		} catch (ParseException e) {
			// If the arguments couldn't be parsed throw Error and show help
			System.out.println(e.getMessage());
			formatter.printHelp("set the configuration file with the -c option, all other arguments are optional", options);
			System.exit(1);
		}
	}

	/**
	 * Parses the configuration file specified in the command line arguments.
	 *
	 * @param file configuration file to parse
	 */
	private void parseConfigFile(String file) {
		GlobalSettings.setInputFilename(file);
		File f = new File(file);
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(f);
			doc.getDocumentElement().normalize();
			ConfigNode root = new ConfigNode(doc.getElementsByTagName("Configuration").item(0));

			// Test for correct version
			if (!root.getAttributeByName("version").equals(XML_VERSION))
				throw new Error("XML-version does not match project version.");

			// Make sure the needed Children exist
			root.assertChildNodesExist("Settings", "DataSources", "Widgets");

			// Get the children
			ConfigNode settings = root.getChildByName("Settings");
			dataSources = root.getChildByName("DataSources");
			widgets = root.getChildByName("Widgets");

			parseSettings(settings);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	/**
	 * Gets the configuration for the {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @return configuration
	 */
	public ConfigNode getWidgetSettings() {
		return widgets;
	}

	/**
	 * Gets the configuration for the data sources.
	 *
	 * @return configuration
	 */
	public ConfigNode getDataSourcesSettings() {
		return dataSources;
	}

	/**
	 * Parses the settings part of the configuration file.
	 *
	 * @param settings configuration node containing the settings
	 */
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