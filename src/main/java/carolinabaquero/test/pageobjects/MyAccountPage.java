package com.carolinabaquero.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import com.carolinabaquero.test.core.PageObject;

/**
 * @author cbaquero
 *         Landing page after login (mock)
 */
public class MyAccountPage extends PageObject {

    private static By USER_PROFILE_ICON = By.className("user-avatar");
    private static By START_OVERLAY_BUTTON = By.xpath("//*[qa-data='start-button']");

    public MyAccountPage(WebDriver driver) {

        super(driver);
    }

    public String getUserInitials() {
       return waitForElementToBeVisible(USER_PROFILE_ICON).getText();
    }
}
