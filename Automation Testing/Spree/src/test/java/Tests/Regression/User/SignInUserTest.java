package Tests.Regression.User;

import Base.TestBase;
import Components.HeaderComponent;
import Pages.SignInPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignInUserTest extends TestBase {

    SignInPage signInPage;

    String email = "elyse.stark@gmail.com";
    String password = "Errol006651";

    @Test(priority = 1)
    public void loginUser() {

        driver.navigate().to("https://demo.spreecommerce.org/user/sign_in");
        waitFor().until(ExpectedConditions.urlContains("https://demo.spreecommerce.org/user/sign_in"));

        signInPage = new SignInPage(driver);
        signInPage.loginUser(email, password, true);

        HeaderComponent header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "SIGNED IN SUCCESSFULLY.");
        isLoggedIn = true;
    }
}
