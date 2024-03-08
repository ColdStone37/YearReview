package yearreview.app.config;

import org.w3c.dom.*;
import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.Instant;
import java.util.logging.*;

import yearreview.app.util.xml.XmlNode;

/**
 * Parser for the command line arguments and the configuration file.
 *
 * @author ColdStone37
 */
public class ConfigParser {
	/**
	 * Version of the configuration.
	 **/
	public final static String XML_VERSION = "0.2";
	/**
	 * Configuration for the widgets.
	 */
	private XmlNode widgets;
	/**
	 * Configuration for the data sources.
	 */
	private XmlNode dataSources;
	/**
	 * Configuration for the data processors.
	 */
	private XmlNode dataProcessors;
	private final static Logger logger = Logger.getLogger(ConfigParser.class.getName());

	/**
	 * Constructs a ConfigParser and parses the arguments and config file.
	 *
	 * @param args command line arguments
	 */
	public ConfigParser(String[] args) {
		// Add command line options
		Options options = getOptions();

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
				if(!f.delete())
					logger.log(Level.WARNING, "Unable to delete file \"" + f.getAbsolutePath() + "\".");

			// ffmpeg doesn't like it when the file already exists
			if (f.isFile()){
				logger.log(Level.SEVERE, "ffmpeg-process cannot be created if the output-file \"" + f.getAbsolutePath() + "\" does already exist.");
				System.exit(1);
			}

			// Parse the Config file
			parseConfigFile(cmd.getOptionValue("config"));
		} catch (ParseException e) {
			// If the arguments couldn't be parsed throw Error and show help
			logger.log(Level.SEVERE, "Passed arguments couldn't be parsed.", e);
			formatter.printHelp("set the configuration file with the -c option, all other arguments are optional", options);
			throw new Error(e);
		}
	}

	/**
	 * Gets the Command line options.
	 * @return options with descriptions
	 */
	private static Options getOptions() {
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
		return options;
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
			XmlNode root = XmlNode.parseXmlFile(f).getChildByName("Configuration");

			// Test for correct version
			if (!root.getAttributeByName("version").equals(XML_VERSION))
				logger.log(Level.WARNING, "Configuration-Version does not match project-version.");

			// Make sure the needed Children exist
			root.assertChildNodesExist("Settings", "DataSources", "Widgets");

			// Get the children
			XmlNode settings = root.getChildByName("Settings");
			dataSources = root.getChildByName("DataSources");
			dataProcessors = root.getChildByName("Processors");
			widgets = root.getChildByName("Widgets");

			parseSettings(settings);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Configuration couldn't be parsed.");
			throw new Error(e);
		}
	}

	/**
	 * Gets the configuration for the {@link yearreview.app.grid.widgets.Widget widgets}.
	 *
	 * @return configuration
	 */
	public XmlNode getWidgetSettings() {
		return widgets;
	}

	/**
	 * Gets the configuration for the data sources.
	 *
	 * @return configuration
	 */
	public XmlNode getDataSourcesSettings() {
		return dataSources;
	}

	/**
	 * Gets the configuration for the data processors.
	 *
	 * @return configuration
	 */
	public XmlNode getDataProcessorsSettings() {
		return dataProcessors;
	}

	/**
	 * Parses the settings part of the configuration file.
	 *
	 * @param settings configuration node containing the settings
	 */
	private void parseSettings(XmlNode settings) {
		settings.assertChildNodesExist("Start", "End");
		GlobalSettings.setVideoStart(Instant.parse(settings.getChildContent("Start")));
		GlobalSettings.setVideoEnd(Instant.parse(settings.getChildContent("End")));
		XmlNode video = settings.getChildByName("Video");
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
		XmlNode grid = settings.getChildByName("Grid");
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