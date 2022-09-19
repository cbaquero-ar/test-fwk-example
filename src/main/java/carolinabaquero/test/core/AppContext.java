package com.carolinabaquero.test.core;

import com.carolinabaquero.test.core.exceptions.MissingPropertiesConfigFile;
import com.carolinabaquero.test.core.exceptions.WrongPropertiesFormatException;
import com.carolinabaquero.test.core.utils.reports.CustomReporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppContext {
    //available browsers
    public static final String BROWSER_FF = "FIREFOX";
    public static final String BROWSER_EDGE = "EDGE";
    public static final String BROWSER_CHROME = "CHROME";

    // Default config file name
    private String configPropFile = "config.properties";

    // Common context information (with default values)
    private String appURL;
    private String browser = BROWSER_CHROME;
    private String logLevel = "INFO";
    private String reportsDirectory = "reports";
    private boolean takeFailureScreenShots = true;

    //Selenium Grid parameters (with default values)
    private boolean useSeleniumGrid = false;
    private String seleniumGridHub = "http://localhost:4444/wd/hub";
    private String platform = "WINDOWS";
    private String browserVersion = "";
    private String downloadsDir = "";


    /**
     * Sets application test bed According to the configProFile parameter,
     * loads this file if present into the jar root path folder, and uses all its properties.
     * Default file name if the parameter is empty is config.properties
     *
     * @param configPropFile the properties file to load the specific values for this run from
     */
    public AppContext(String configPropFile) throws MissingPropertiesConfigFile, WrongPropertiesFormatException {
        // Get a proper full filename – should be on the same directory as the tests
        String propertiesFullName = System.getProperty("user.dir") + "/" + configPropFile;
        Properties properties = new Properties();

        // Load file
        try {
            FileInputStream resource = new FileInputStream(propertiesFullName);
            properties.load(resource);
            CustomReporter.info("Loading properties from '" + propertiesFullName + "' ...");
        } catch (IOException e) {
            throw new MissingPropertiesConfigFile(propertiesFullName);
        }

        // Get properties values
        this.appURL = properties.getProperty("appURL");
        if (appURL != null) {
            this.browser = properties.getProperty("browser").toUpperCase();
            loadOtherProperties(properties);
        }else{
            throw new WrongPropertiesFormatException(propertiesFullName, "appURL is missing.");
        }
    }

    private void loadOtherProperties(Properties properties) {
        this.takeFailureScreenShots = properties.getProperty("screenShotOnFailure").equalsIgnoreCase("Yes");
        this.useSeleniumGrid = properties.getProperty("useSeleniumGrid").equalsIgnoreCase("Yes");
        if (this.useSeleniumGrid) {
            this.seleniumGridHub = properties.getProperty("seleniumGridHub");
            this.platform = properties.getProperty("platform");
            this.browserVersion = properties.getProperty("browserVersion");
            //TODO load other properties in file
        }
    }

    public String getAppURL() {
        return appURL;
    }

    public boolean takingFailureScreenShots() {
        return takeFailureScreenShots;
    }

    public String getBrowser() {
        return browser;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getReportsDirectory() {
        return reportsDirectory;
    }

    public boolean isTakeFailureScreenShots() {
        return takeFailureScreenShots;
    }

    public boolean useSeleniumGrid() {
        return useSeleniumGrid;
    }

    public String getSeleniumGridHub() {
        return seleniumGridHub;
    }

    public String getPlatform() {
        return platform;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getDownloadsDir() {
        return downloadsDir;
    }

}