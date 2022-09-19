package com.carolinabaquero.test.core;


import com.carolinabaquero.test.core.exceptions.ElementNotClickableException;
import com.carolinabaquero.test.core.utils.reports.CustomReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;


/**
 * @author Carolina Baquero
 *         PageObject is the parent class for all Page Objects to be created within the test framework
 */

public abstract class PageObject {

    private static int waitForElementPresentTimeout = 20; //seconds
    private static int waitForElementGroupPresentTimeout = 40; //seconds
    private static int waitForNotPresentElementTimeout = 5; //seconds

    /**
     * Implementation note: driver instance is only passed along between Page Objects,
     * this instance should never be used out of a page object,
     * except from the time of instantiation on SeleniumTest.startDriver method.
     */
    protected WebDriver driver;

    /**
     * PageObject constructor
     * @param driver the web driver already instantiated
     */
    public PageObject(final WebDriver driver) {
        this.driver = driver;
    }

    public void closeBrowser(){
        driver.close();
    }

    public void quitBrowser(){
        driver.quit();
    }

    /**
     * Takes a screenshot of the current state of the page.
     * @param screenShotDir relative path to project folder and full name for the screenshot
     * @throws IOException if some error occurs when trying to save the screenshot
     */

    public void takeScreenShot(String screenShotDir) throws IOException {
        File ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        if(ScreenShot.exists()) {
            FileUtils.copyFile(ScreenShot,
                    new File(screenShotDir + "\\" + ScreenShot.getName()));
        }
    }

    protected static int getWaitForNotPresentElementTimeout() {
        return waitForNotPresentElementTimeout;
    }
    protected static int getWaitForElementPresentTimeout() {
        return waitForElementPresentTimeout;
    }
    protected static int getWaitForElementGroupPresentTimeout() {
        return waitForElementGroupPresentTimeout;
    }

    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Clears the current input element and types the testData in it.
     * @param webElement {@link org.openqa.selenium.WebElement}
     * @param testData   String to type
     */
    protected void clearAndType(final WebElement webElement, final String testData) {
        CustomReporter.debug("clearAndType");
        webElement.clear();
        Actions action = new Actions(driver);
        action.sendKeys(webElement, testData);
        action.perform();
    }

    /**
     * Clicks on the given element
     * @param element {@link org.openqa.selenium.WebElement} to click
     * @throws com.carolinabaquero.test.core.exceptions.ElementNotClickableException if the element is not visible or clickable for any other reason
     */
    protected void click(final WebElement element) throws ElementNotClickableException {
        CustomReporter.debug("Click element  (" + element.toString() + ")");
        if (!element.isEnabled()) {
            throw new ElementNotClickableException(element.toString());
        }
        element.click();
    }

    /**
     * Executes JavaScript in the context of the currently selected
     * frame or window as the body of an anonymous function
     * @param script text
     * @return One of Boolean, Long, String, List or {@link org.openqa.selenium.WebElement}. Or null
     */
    protected Object executeScript(final String script) {
        CustomReporter.debug("executeScript");
        return ((JavascriptExecutor) driver).executeScript(script);
    }

    /**
     * Selects one option from a SELECT element, by its visible text.
     * @param webElement the {@link org.openqa.selenium.WebElement} for the select list
     * @param text       text value
     */
    protected void selectByVisibleText(final WebElement webElement, final String text) {
        CustomReporter.debug("selectByVisibleText webElement ("
                                + webElement.toString() + "), text (" + text + ")");
        new Select(webElement).selectByVisibleText(text);
    }

    /**
     * Selects multiple options from a SELECT element by its visible text.
     * Previous selections are unselected before proceeding.
     * @param selectList the {@link org.openqa.selenium.WebElement} for the select list
     * @param textSet    text to select in the list
     */
    protected void selectByVisibleText(final WebElement selectList, final String[] textSet) {
        CustomReporter.debug("selectByVisibleText webElement ("+ selectList.toString() + "), textSet (" + textSet + ")");
        Select select = new Select(selectList);
        select.deselectAll();
        for (String text : textSet) {
            select.selectByVisibleText(text);
        }
    }

    /**
     * Selects one option from a SELECT element, by its value.
     * @param webElement the {@link org.openqa.selenium.WebElement} for the select list
     * @param value      value to select from the list
     */
    protected void selectByValue(final WebElement webElement, final String value) {
        CustomReporter.debug("selectByValue webElement (" + webElement.toString() + "), value (" + value + ")");
        new Select(webElement).selectByValue(value);
    }

    /**
     * Selects multiple options from a SELECT element, by its values.
     * Previous selections are unselected before proceeding.
     * @param selectList the {@link org.openqa.selenium.WebElement} for the select list
     * @param values values to select from the list
     */
    protected void selectByValues(final WebElement selectList, final String[] values) {
        CustomReporter.debug("selectByValues webElement (" + selectList.toString() + "), values (" + values + ")");
        Select select = new Select(selectList);
        select.deselectAll();
        for (String val : values) {
            select.selectByValue(val);
        }
    }

    /**
     * Waits for a set of elements to appear for waitForElementPresentTimeout seconds
     * NOTE: the element being present (on the DOM) does not mean tht is visible on the UI,
     *       use waitForElementToBVisible for that purpose
     * @param locator {@link org.openqa.selenium.By}
     * @return {@link org.openqa.selenium.WebElement} the waited element
     */
    protected List<WebElement> waitForElementsToBePresent(final By locator) {
        return waitForElementsToBePresent(locator, waitForElementGroupPresentTimeout);
    }

    /**
     * Waits for a set of elements to appear for the given time
     * NOTE: the element being present (on the DOM) does not mean tht is visible on the UI,
     *       use waitForElementToBVisible for that purpose
     * @param locator {@link org.openqa.selenium.By}
     * @param timeout wait time in seconds
     * @return {@link org.openqa.selenium.WebElement} the waited element
     */
    protected List<WebElement> waitForElementsToBePresent(final By locator, int timeout) {
        CustomReporter.debug("waitForElementsToBePresent locator (" + locator.toString() + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return webDriverWait.ignoring(NoSuchElementException.class).until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits the default timeout for the element to be visible and returns it
     * @param locator the web element {@link org.openqa.selenium.By} locator
     * @return the {@link org.openqa.selenium.WebElement} for the given locator
     */
    protected List<WebElement> waitForElementsToBeVisible(final By locator) {
       return waitForElementsToBeVisible(locator, waitForElementGroupPresentTimeout);
    }

    /**
     * Wait for all elements with the given locator to be visible for the specified timeout
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return A list of all {@link org.openqa.selenium.WebElement}s
     */
    protected final List<WebElement> waitForElementsToBeVisible(final By locator, final int timeout) {
        CustomReporter.debug("waitForElementsToBeVisible locator (" + locator.toString() + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return webDriverWait.ignoring(NoSuchElementException.class).until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits the default timeout for the element to be clickable and returns it
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return {@link org.openqa.selenium.WebElement}
     */
    protected WebElement waitForElementToBeClickable(final By locator) {
        return waitForElementToBeClickable(locator,waitForElementPresentTimeout);
    }

    /**
     * Waits for element to be clickeable for the given timeout
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return {@link org.openqa.selenium.WebElement}
     */
    protected WebElement waitForElementToBeClickable(final By locator, final int timeout) {
        CustomReporter.debug("waitForElementToBeClickable locator (" + locator.toString()
                                    + "), timeout (" + timeout + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return webDriverWait.ignoring(NoSuchElementException.class).until(
                ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for the element to be present for the default timeout and returns it
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return the @link{WebElement} for the given locator
     */
    protected final WebElement waitForElementToBePresent(final By locator) {
        return waitForElementToBePresent(locator, waitForElementPresentTimeout);
    }

    /**
     * Waits for element to appear for the given timeout
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @param timeout the time after the driver stops waiting for the element to be present
     * @return {@link org.openqa.selenium.WebElement}
     */
    protected WebElement waitForElementToBePresent(final By locator, final int timeout) {
        CustomReporter.debug("waitForElementToBePresent locator(" + locator.toString()
                + "), timeout (" + timeout + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return webDriverWait.ignoring(NoSuchElementException.class).until(
                ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Waits for the element to be visible in the UI for the default timeout and returns it
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return the @link{WebElement} for the given locator
     */
    protected WebElement waitForElementToBeVisible(final By locator) {
        return waitForElementToBeVisible(locator, waitForElementPresentTimeout);
    }

    /**
     * Waits for the element to be visible in the UI for the given timeout and returns it
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @param timeout to wait for the element to be visible
     * @return the @link{WebElement} for the given locator
     */
    protected WebElement waitForElementToBeVisible(final By locator, final int timeout) {
        CustomReporter.debug("waitForElementToBeVisible locator (" + locator.toString()
                + "), timeout (" + timeout + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return webDriverWait.ignoring(NoSuchElementException.class).until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for element to disappear for 'waitForElementPresentTimeout' seconds
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
    */
    protected void waitForElementToDisappear(final By locator) {
        this.waitForElementToDisappear(locator, waitForElementPresentTimeout);
    }

    /**
     * Waits for element to disappear for the given time
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @param seconds amount of time to wait in seconds
     * */
    protected void waitForElementToDisappear(final By locator, final int seconds) {
        CustomReporter.debug("waitForElementToDisappear locator (" + locator.toString() + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

//    /**
//     * Waits for element to disappear for 'waitForElementPresentTimeout' seconds
//     * @param webElement the instantiated {@link org.openqa.selenium.WebElement} to wait to disappear
//     */
//    protected void waitForElementToDisappear(final WebElement webElement) throws MissingElementException {
//        CustomReporter.debug("waitForElementToDisappear webElement (" + webElement.toString() + ")");
//        for (int i = 0; i < waitForNotPresentElementTimeout; i++) {
//            if (webElement.isDisplayed()) {
//                break;
//            } else {
//                pause(1);
//            }
//        }
//    }

    public void pause(int seconds) {
        try{
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Waits for the given text to be present into an element for the default timeout
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return the waited text within the element
     */
    protected WebElement waitForElementToHaveText(final By locator, final String text) {
        return waitForElementToHaveText(locator, text, waitForElementPresentTimeout);
    }

    /**
     * Waits for the given text to be prensent into an element for the given timeout
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return the waited text within the element
     */
    protected WebElement waitForElementToHaveText(final By locator, final String text, int timeout) {
        CustomReporter.debug("waitForElementToHaveText locator (" + locator.toString()
                + "), text (" + text + ")");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(waitForElementPresentTimeout));
        webDriverWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        return driver.findElement(locator);
    }

    /**
     * Takes a screen shot of the current state of the browser.
     * @param screenShotFullName the relative path to project folder and full name for the screenshot
     * @throws java.io.IOException if some error occurs when trying to save the screen shot
     */
    public void takeScreenshot(String screenShotFullName) throws IOException {
        File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShot, new File(screenShotFullName));
    }


    /**
     * Moves the focus to the first Child window from the Main window
     * Assumes the window is present when the switch method is called
     */
    protected void switchToChildWindow() {

        Set<String> allWindows = getDriver().getWindowHandles();
        String mainWindow = getDriver().getWindowHandle();

        String firstChildWindow = "";
        for (String window : allWindows) {
            if (!window.contains(mainWindow)) {
                firstChildWindow = window;
            }
        }

        if (!firstChildWindow.equals(""))
            getDriver().switchTo().window(firstChildWindow);

    }


    /**
     * Moves the focus to the given Child window by its title, from the Main window
     *
     * @param targetWindow the name of the child window the focus should be set to
     */
    protected void switchToChildWindowByTitle(String targetWindow) {

        Set<String> allWindows = getDriver().getWindowHandles();
        String mainWindow = getDriver().getWindowHandle();

        for (String window : allWindows) {
            if ((!window.contains(mainWindow)) && (window.contains(targetWindow))) {
                targetWindow = window;
            }
        }

        getDriver().switchTo().window(targetWindow);

    }


    /**
     * Some Wrappers that allow log debugging info and control the use of this WebDriver
     * members through the Page Object interface.
     * This could be or not be present, depending on the project requirements.
     */

    /**
     * Wrapper for the getPageSource member of WebDriver
     * Gets the source code of the current page
     *
     * @return the page source in a String
     */
    protected String getPageSource() {

        CustomReporter.debug("getPageSource");

        return driver.getPageSource();
    }

    /**
     * Wrapper of web driver findElement method.
     * <p/>
     * It finds the element without extra waits.
     *
     * @param locator the {@link org.openqa.selenium.By} locator for the element to find
     * @return the {@link org.openqa.selenium.WebElement} for the given locator
     */
    protected final WebElement findElement(final By locator) {

        CustomReporter.debug("findElement locator(" + locator.toString() + ")");
        return this.driver.findElement(locator);
    }


    /**
     * Looks for the element within the current DOM.
     * Wrapper for the WebDriver.findElements() method.
     *
     * @param locator the {@link org.openqa.selenium.By} locator for the elements to find
     * @return the @link{WebElement} for the given locator
     */
    protected final List<WebElement> findElements(final By locator) {

        CustomReporter.debug("findElements locator (" + locator.toString() + ")");
        return this.driver.findElements(locator);

    }


    /**
     * Finds the first element according to the By locator within the from @link{WebElement}
     *
     * @param from    the {@link org.openqa.selenium.WebElement} which contains what you are looking for
     * @param locator the locator for the {@link org.openqa.selenium.WebElement}s inside the 'from' element
     * @return A {@link org.openqa.selenium.WebElement} with the first occurence of the locator's element
     */
    protected final WebElement getSubElementFromWebElement(final WebElement from, final By locator) {

        CustomReporter.debug("getElement from locator (" + locator.toString() + ")");
        return from.findElement(locator);
    }


    /**
     * Finds some elements according to the By locator within the from {@link org.openqa.selenium.WebElement}
     * Wrapper for the Selenium method findElements
     *
     * @param from    the {@link org.openqa.selenium.WebElement} which contains what you are looking for
     * @param locator the locator for the {@link org.openqa.selenium.WebElement}s inside the 'from' element
     * @return A list of all {@link org.openqa.selenium.WebElement}s in
     */
    protected final List<WebElement> getSubElementsFromWebElement(final WebElement from, final By locator) {

        CustomReporter.debug("getElements from locator (" + locator.toString() + ")");
        return from.findElements(locator);
    }

    public void deleteCoookies() {
        getDriver().manage().deleteAllCookies();
    }
}
