package profile.crawler;

/**
 * Concept Extractor Software Website:
 * http://www.irit.fr/~Duy.Dinh/tools/cxtractor/ Contact:
 * cxtractor{at}irit{dot}fr University of Toulouse - Institute for Research in
 * Informatics of Toulouse http://www.irit.fr/
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is Settings.java
 *
 * The Original Code is Copyright (C) 2011-2012 the University of Toulouse. All
 * Rights Reserved.
 *
 * Contributor(s): Duy Dinh <dinh{at}irit{dot}fr> (original author)
 */
import java.io.File;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import utilities.FileManager;
import utilities.PropertyManager;

public class Settings {

    protected Properties properties;
    protected String propertyFile;
    protected String propertySuffix = "/config/settings.properties";
    /**
     * *
     * Arguments (options) of the application
     */
    protected Options options;
    /**
     * common properties
     */
    protected String[] processFields;
    protected String docnoTag;
    protected String contentTag;
    protected String charsetEncoding;
    protected String stopwordFile;
    private boolean resuming;
    protected Integer batchSize = 1;
    protected boolean showDetails = false;
    protected String VERSION;
    public static String FILE_SEPERATOR;
    public static Logger logger = null;
    /**
     * Default log4j config if no config/log.xml file exists
     */
    public static final String DEFAULT_LOG4J_CONFIG = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
            + "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">"
            + "<log4j:configuration xmlns:log4j=\"http://jakarta.apache.org/log4j/\">"
            + " <appender name=\"console\" class=\"org.apache.log4j.ConsoleAppender\">"
            + "  <param name=\"Target\" value=\"System.err\"/>"
            + "  <layout class=\"org.apache.log4j.SimpleLayout\"/>"
            + " </appender>"
            + " <root>"
            + "  <priority value=\"info\" />"
            + "  <appender-ref ref=\"console\" />"
            + " </root>"
            + "</log4j:configuration>";
    /**
     * The configuration file used by log4j
     */
    public static String LOG4J_CONFIG = null;

    public Settings() {

        FILE_SEPERATOR = System.getProperty("file.separator");
        // check properties file
        propertyFile = System.getProperty("user.dir") + propertySuffix;
        File f = new File(propertyFile);
        if (f == null || !f.exists()) {
            // copy a sample setting file to current settings file
            FileManager.copyFile(propertyFile + ".sample", propertyFile);
        }

        properties = PropertyManager.loadProperties(propertyFile);

        loadCommonProperties();
    }

    private void loadCommonProperties() {

        charsetEncoding = properties.getProperty("charset.name", "ISO-8859-1");
        VERSION = properties.getProperty("version", "1.0");

        setResuming(Boolean.parseBoolean(properties.getProperty("resuming",
                "false")));

        batchSize = Integer.parseInt(properties.getProperty("batch.size", "1"));
        showDetails = Boolean.parseBoolean(properties.getProperty(
                "show.details", "true"));

        LOG4J_CONFIG = makeAbsolute(getProperty("log4j.config", "log4j-config.xml"),
                String.format("%s%sconfig", System.getProperty("user.dir"),
                FILE_SEPERATOR));

        // setup log4j
        if (new File(LOG4J_CONFIG).exists()) {
            DOMConfigurator.configure(LOG4J_CONFIG);
        } else {
            // slightly ugly hack: record a good default configuration as a
            // String in this class, and load that
            // emulating DOMConfigurator.configure(Reader)
            // see
            // http://svn.apache.org/viewvc/logging/log4j/branches/v1_2-branch/src/java/org/apache/log4j/xml/DOMConfigurator.java?revision=311462&view=markup
            new DOMConfigurator().doConfigure(new StringReader(
                    DEFAULT_LOG4J_CONFIG), org.apache.log4j.LogManager
                    .getLoggerRepository());
        }

        // setup the logger for this class
        logger = Logger.getLogger(Settings.class);
    }

    static boolean isLog4JConfigured() {
        boolean log4jConfigured = false;
        Enumeration en = Logger.getRootLogger().getAllAppenders();
        if (!(en instanceof org.apache.log4j.helpers.NullEnumeration)) {
            log4jConfigured = true;
        } else {
            Enumeration cats = org.apache.log4j.LogManager.getCurrentLoggers();
            while (cats.hasMoreElements()) {
                Logger c = (org.apache.log4j.Logger) cats.nextElement();
                if (!(c.getAllAppenders() instanceof org.apache.log4j.helpers.NullEnumeration)) {
                    log4jConfigured = true;
                    return true;
                }
            }
        }
        return log4jConfigured;
        // return false;
    }

    /**
     * Checks whether the given filename is absolute and if not, it adds on the
     * default path to make it absolute. If a URI scheme is present, the
     * filename is assumed to be absolute
     *
     * @param filename String the filename to make absolute
     * @param DefaultPath String the prefix to add
     * @return the absolute filename
     */
    public static String makeAbsolute(String filename, String DefaultPath) {
        if (filename == null) {
            return null;
        }
        if (filename.length() == 0) {
            return filename;
        }
        if (filename.matches("^\\w+:.*")) {
            return filename;
        }
        if (new File(filename).isAbsolute()) {
            return filename;
        }
        if (!DefaultPath.endsWith(FILE_SEPERATOR)) {
            DefaultPath = DefaultPath + FILE_SEPERATOR;
        }
        return DefaultPath + filename;
    }

    public String getProperty(String property, String defaultValue) {

        return properties.getProperty(property, defaultValue);
    }

    public String getProperty(String property) {

        return properties.getProperty(property);
    }

    public void setProperty(String property, String defaultValue) {

        properties.setProperty(property, defaultValue);
    }

    /**
     * *** GETTERS ****
     */
    public String getPropertyFile() {
        return propertyFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public String[] getProcessedFields() {
        return processFields;
    }

    public String getDocnoTag() {

        return docnoTag;
    }

    public String getCharsetName() {

        return charsetEncoding;
    }

    public String getStopWordFile() {
        return stopwordFile;
    }

    /**
     * *** SETTERS ****
     */
    public void setPropertyFile(String filename) {
        propertyFile = filename;
    }

    public void setProperties(Properties propertySet) {
        properties = propertySet;
    }

    /**
     * parse options from arguments
     *
     * @param args input arguments
     * @return program's options
     */
    public CommandLine parseOptions(String[] args) {
        // Parse options
        CommandLineParser parser = new BasicParser();

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return commandLine;
    }

    public boolean isResuming() {
        return resuming;
    }

    public void setResuming(boolean resuming) {
        this.resuming = resuming;
    }
}