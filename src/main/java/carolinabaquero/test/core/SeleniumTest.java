package com.carolinabaquero.test.core;

import com.carolinabaquero.test.core.exceptions.NonSupportedBrowserException;
import com.carolinabaquero.test.core.exceptions.WrongTestConfigurationDataException;
import com.carolinabaquero.test.core.utils.reports.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;

public abstract class SeleniumTest {
    protected PageObject startPage;

    //Test context info
    private AppContext testContext;

    // Default timeouts for wait conditions - Note: These timeouts can also be overriden by some properties on the properties file
    private static int waitForElementPresentTimeout = 10; //seconds
    private static int waitForElementGroupPresentTimeout = 20; //seconds
    private static int waitForNotPresentElementTimeout = 5; //seconds

    /**
     * Loads the testContext from properties file and sets up an AppContext instance.
     * Note: This method can be expanded to support arguments from (Maven) command line
     * or have default values for when the properties file is not present or is wrong.
     */
    @Parameters({ "config-file" })
    @BeforeClass(alwaysRun = true)
    public void setEnvironment(@Optional("chrome.properties") String configFile) {
        // Set environment configuration on AppContext from properties file
        try {
            testContext = new AppContext(configFile);
        } catch (WrongTestConfigurationDataException wrongData) {
            CustomReporter.error("Missing minimal configuration parameters on startup.", wrongData);
        }
    }


    /**
     * Starts the browser and launches the web application
     * @return the Page object of the main page for the Web App
     */
    @BeforeMethod(alwaysRun = true)//, dependsOnMethods = "setEnvironment")
    public void startDriver() throws NonSupportedBrowserException {
        this.startPage = launchApplication(getDriverInstance(testContext));
    }


    public PageObject launchApplication(WebDriver driver) {
        driver.get(testContext.getAppURL());
        //hook to define which is the start page of SeleniumTest<Subclass>
        return getStartPage(driver);
    }

    /**
     * Starts the driver with the configuration given on the properties file
     * @return an instance of WebDriver configured (browser started)
     */
    public final WebDriver getDriverInstance(AppContext testContext) throws NonSupportedBrowserException {
        String browser = testContext.getBrowser();
        AbstractDriverOptions browserOptions = getBrowserOptions(browser);

        WebDriver webDriver;
        if (testContext.useSeleniumGrid()) {
            try {
                return new RemoteWebDriver(new URL(testContext.getSeleniumGridHub()), browserOptions);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getConfiguredWebDriver(browser, browserOptions);
        }
    }

    private WebDriver getConfiguredWebDriver(String browser, AbstractDriverOptions browserOptions) throws NonSupportedBrowserException {
        WebDriver webDriver;
        switch (browser.toUpperCase()) {
            case AppContext.BROWSER_FF: {
                System.setProperty("webdriver.gecko.driver", "./src/main/resources/drivers/geckodriver.exe");
                webDriver = new FirefoxDriver((FirefoxOptions) browserOptions);
                break;
            }
            case AppContext.BROWSER_EDGE: {
                String ieDriverFilePath = "./src/main/resources/drivers/msedgedriver.exe";
                System.setProperty("webdriver.edge.driver", ieDriverFilePath);
                webDriver = new EdgeDriver((EdgeOptions) browserOptions);
                break;
            }
            case AppContext.BROWSER_CHROME: {
                System.setProperty("webdriver.chrome.webDriver", "./src/main/resources/drivers/chromedriver.exe");
                webDriver = new ChromeDriver((ChromeOptions) browserOptions);
                break;
            }
            default: {
                throw new NonSupportedBrowserException(browser);
            }
        }
        return webDriver;
    }

    private AbstractDriverOptions getBrowserOptions(String browser) throws NonSupportedBrowserException {
        AbstractDriverOptions options;
        switch (browser){
            case AppContext.BROWSER_FF:{
                options = new FirefoxOptions();
                break;
            }
            case AppContext.BROWSER_EDGE:{
                options = new EdgeOptions();
                break;
            }
            case AppContext.BROWSER_CHROME:{
                options = new ChromeOptions();
                break;
            }
            default:{
                throw new NonSupportedBrowserException(browser);
            }
        }
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.setCapability("platformName", testContext.getPlatform());
        return options;
    }


    /**
     * Hook method to define on project using this API
     * @return the PageObject for the first page of the application under test, for example the login page or home page
     */
    public abstract PageObject getStartPage(WebDriver driver);


    /**
     * Takes a screenshot of the current page on failure
     * @param result the test result given by TestNG for a Test method
     */
    @AfterMethod(alwaysRun = true)
    public void takeScreenshotOnFailure(ITestResult result) {
        if (testContext.takingFailureScreenShots() && (result.getStatus() == ITestResult.FAILURE)) {
            //create dir if necessary
            File directory = new File(testContext.getReportsDirectory());
            if (! directory.exists()){
                directory.mkdirs();
            }
            //save screenshot
            Date date = Date.from(Instant.now());
            String scrshotDir = String.format(System.getProperty("user.dir") + "\\"
                    + testContext.getReportsDirectory()+"\\screenshots");
            try {
                startPage.takeScreenShot(scrshotDir);
            } catch (Exception e) {
                CustomReporter.error("[SCREENSHOT ERROR] Something failed trying" +
                                             " to save the failure screenshot as " + scrshotDir, e);
            }
        }
        quit();
    }

    public void quit(){
        startPage.closeBrowser();
        startPage.quitBrowser();
    }

    /**
     * Gets the AppContext for this run
     * @return AppContext
     */
    public AppContext getTestContext() {
        return testContext;
    }

}

