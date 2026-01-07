package Tests.Regression.Checkout;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.CartPage;
import Pages.SignupPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutWithEmptyCartTest extends TestBase {

    SignupPage signupPage;
    HeaderComponent header;
    CartPage cartPage;

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
    public void checkEmptyCart() {

        header = new HeaderComponent(driver);
        header.openCart();

        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.emptyCartMessage));

        Assert.assertEquals(cartPage.emptyCartMessage.getText(), "YOUR CART IS EMPTY.");
    }
}
