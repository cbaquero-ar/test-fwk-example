package com.carolinabaquero.test.pageobjects;

import com.carolinabaquero.test.core.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PublicHomePage extends PageObject {
    private static final By LOGIN_BUTTON = By.xpath("//*[contains(@data-purpose,'header-login')]");

    /**
     * PageObject constructor
     * @param driver the web driver already instantiated
     */
    public PublicHomePage(WebDriver driver) {
        super(driver);
    }

    public LoginPage goToLoginPage(){
        return new LoginPage(getDriver());
    }
}
