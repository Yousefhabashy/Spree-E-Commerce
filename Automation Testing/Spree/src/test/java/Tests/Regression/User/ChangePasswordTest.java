package Tests.Regression.User;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.AccountPage;
import Pages.SignInPage;
import Pages.SignupPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class ChangePasswordTest extends TestBase {

    SignupPage signupPage;
    SignInPage signInPage;
    AccountPage accountPage;
    HeaderComponent header;

    String email = TestData.generateEmail();
    String password = TestData.generatePassword();
    String newPassword = TestData.generatePassword();

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
    public void changePassword() {

        header = new HeaderComponent(driver);
        header.openAccount();
        waitFor().until(ExpectedConditions.urlContains("/account/"));

        accountPage = new AccountPage(driver);
        accountPage.openPersonalDetails();

        waitFor().until(ExpectedConditions.urlContains("/account/profile/edit"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/account/profile/edit"));
        accountPage.openChangePassword();
        waitFor().until(ExpectedConditions.urlContains("user/edit"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("user/edit"));
        accountPage.changePassword(password, newPassword);

        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "YOU UPDATED YOUR ACCOUNT SUCCESSFULLY.");
    }

    @Test(dependsOnMethods = {"changePassword"})
    public void logoutUser() {

        header = new HeaderComponent(driver);
        header.openAccount();

        waitFor().until(ExpectedConditions.urlContains("/account/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("account/"));

        accountPage = new AccountPage(driver);
        accountPage.logoutUser();

        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "SIGNED OUT SUCCESSFULLY.");
        isLoggedIn = false;
    }
    @Test(dependsOnMethods = {"logoutUser"})
    public void loginWithNewPassword() {

        driver.navigate().to("https://demo.spreecommerce.org/user/sign_in");
        waitFor().until(ExpectedConditions.urlContains("https://demo.spreecommerce.org/user/sign_in"));

        signInPage = new SignInPage(driver);
        signInPage.loginUser(email, newPassword, true);

        HeaderComponent header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "SIGNED IN SUCCESSFULLY.");
        isLoggedIn = true;
    }
}
