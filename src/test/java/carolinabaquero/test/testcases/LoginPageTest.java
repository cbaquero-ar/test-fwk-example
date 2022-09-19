package com.carolinabaquero.test.testcases;

import com.carolinabaquero.test.core.SeleniumTest;
import com.carolinabaquero.test.core.exceptions.InvalidLoginException;
import com.carolinabaquero.test.pageobjects.MyAccountPage;
import com.carolinabaquero.test.core.exceptions.FailedLoginException;
import com.carolinabaquero.test.pageobjects.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;

public class LoginPageTest extends SeleniumTest {

    //TODO Use TestNG Data Providers to load test cases from file

    public LoginPage getStartPage(WebDriver driver) {
        return new LoginPage(driver);
    }

    @DataProvider(name="valid-login")
    public Object[][] validLoginProvider(){
        String[][] data = null;
        try {

            String cvsFIle = "/test/resources/LoginTCs.csv";
            String fileName = "c:\\test\\csv\\country.csv";

            List<LoginData> beans = new CsvToBeanBuilder(new FileReader(fileName))
                    .withType(Country.class)
                    .build()
                    .parse();

            beans.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("The exception is: " + e.getMessage());
        }
        return data;
    }

    // Test that a valid user can do login and go to landing page titled "Home Page"
    @Test(testName = "Valid login", groups = {"smoke", "regression"})
    public void testValidLogin() throws FailedLoginException, InvalidLoginException {
        LoginPage loginPage = (LoginPage) startPage;
        MyAccountPage home = loginPage.login("baquero.carolina@gmail.com", "autoExample!8879");
        Assert.assertEquals(home.getUserInitials(), "CB", "User avatar is not present");
    }

    // Test that user and password fields are validated
    @Test(testName = "Test credentials validation", groups = {"smoke", "regression"})
    public void testLoginCredentialsValidation() throws FailedLoginException {
        LoginPage loginPage = (LoginPage) startPage;
        try {
            MyAccountPage home = loginPage.login("user1", "");
        } catch (InvalidLoginException e) {
            Assert.assertTrue(loginPage.formValidationErrorsVisible(), "Validation messages not found.");
        }
    }

    // Test that an invalid combination of user and password fails to login.
    @Test(testName = "Wrong Credentials", groups = {"smoke", "regression"})
    public void testLoginFailure() throws InvalidLoginException {
        LoginPage loginPage = (LoginPage) startPage;
        try {
            MyAccountPage home = loginPage.login("any@mail.com", "AnyPassword");
        } catch (FailedLoginException e) {
            Assert.assertTrue(loginPage.loginFailedErrorVisible(), "Login error message not found.");
        }
    }
}