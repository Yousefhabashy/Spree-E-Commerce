package Tests.Regression.User;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.AccountPage;
import Pages.SignupPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class SignupUserTest extends TestBase {

    HeaderComponent header;
    SignupPage signupPage;

    String email = TestData.generateEmail();
    String password = TestData.generatePassword();

    @Test(priority = 1)
    public void signupUser() {

        driver.navigate().to("https://demo.spreecommerce.org/user/sign_up");
        waitFor().until(ExpectedConditions.urlContains("https://demo.spreecommerce.org/user/sign_up"));

        signupPage = new SignupPage(driver);

        signupPage.signUpUser(email, password);

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "WELCOME! YOU HAVE SIGNED UP SUCCESSFULLY.");
        isLoggedIn = true;
    }

    @Test(dependsOnMethods = {"signupUser"})
    public void logoutUser() {

        header = new HeaderComponent(driver);
        header.openAccount();

        waitFor().until(ExpectedConditions.urlContains("/account/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("account/"));

        AccountPage accountPage = new AccountPage(driver);
        accountPage.logoutUser();

        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "SIGNED OUT SUCCESSFULLY.");
        isLoggedIn = false;
    }
}
