package com.carolinabaquero.test.pageobjects;

import com.carolinabaquero.test.core.PageObject;
import com.carolinabaquero.test.core.exceptions.FailedLoginException;
import com.carolinabaquero.test.core.exceptions.InvalidLoginException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for the login page
 */
public class LoginPage extends PageObject {

    private static final By USER_INPUT_LOCATOR = By.id("email");
    private static final By PASSWORD_INPUT_LOCATOR = By.id("password");
    private static final By LOGIN_BUTTON_LOCATOR = By.xpath("//button[@type='submit']");
    private static final By FORM_ERROR_MESSAGE = By.className("dp-error-form");
    private static final By LOGIN_ERROR_MSG_LOCATOR = By.className("dp-error");

    /**
     * PageObject constructor
     * @param driver the web driver already instantiated
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Logs in to the application with the givenuser and password
     * @param user
     * @param password
     * @return HomePage page object, the landing page after login
     * @throws FailedLoginException if the login fails, attaching the errors on the exception message
     */
    public MyAccountPage login(String user, String password) throws FailedLoginException, InvalidLoginException {
        //input username and password using wait mathod defined on PageObject
        WebElement username = waitForElementToBeVisible(USER_INPUT_LOCATOR);
        clearAndType(username, user);

        WebElement passwd = waitForElementToBeVisible(PASSWORD_INPUT_LOCATOR);
        clearAndType(passwd, password);

        click(waitForElementToBeVisible(LOGIN_BUTTON_LOCATOR));

        try {
            waitForElementToDisappear(LOGIN_BUTTON_LOCATOR,2);
            return new MyAccountPage(getDriver());
        } catch (TimeoutException notLoggedIn) {
            //check if there are elements with "error" class
            try{
                WebElement error = waitForElementToBeVisible(LOGIN_ERROR_MSG_LOCATOR,2);
                throw new FailedLoginException(error.toString());
            }catch(TimeoutException noLoginError){
                List<WebElement> errors = waitForElementsToBeVisible(FORM_ERROR_MESSAGE,2);
                throw new InvalidLoginException(errors.toString());
            }
        }
    }

    public boolean loginFailedErrorVisible(){
        try {
            waitForElementToBeVisible(LOGIN_ERROR_MSG_LOCATOR, 2);
            return true;
        }catch(TimeoutException timeout){
            return false;
        }
    }

    public boolean formValidationErrorsVisible(){
        try {
            waitForElementsToBeVisible(FORM_ERROR_MESSAGE, 2);
            return true;
        }catch(TimeoutException timeout){
            return false;
        }
    }
}