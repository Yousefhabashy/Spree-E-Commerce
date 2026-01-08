package Tests.Regression.Checkout;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class InvalidPaymentDetailsTest extends TestBase {

    HeaderComponent header;

    String email = TestData.generateEmail();
    String password = TestData.generateNumericPassword();

    String firstName = TestData.generateFirstName();
    String lastName = TestData.generateLastName();
    String countryName = "United States";
    String address = TestData.generateAddress();
    String apartment = TestData.generateApartment();
    String state = TestData.generateUSState();
    String city = TestData.generateCity();
    String postalCode = TestData.generatePostalCodeByState(state);
    String phoneNumber = TestData.generatePhoneNumber();

    String creditCardNumber = TestData.generateDeclinedCardNumber();
    String expiryData = TestData.generateExpiry();
    String CVV = TestData.generateCVV();

    @Test(priority = 1)
    public void signupUser() {

        driver.navigate().to("https://demo.spreecommerce.org/user/sign_up");
        waitFor().until(ExpectedConditions.urlContains("https://demo.spreecommerce.org/user/sign_up"));

        SignupPage signupPage = new SignupPage(driver);

        signupPage.signUpUser(email, password);

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "WELCOME! YOU HAVE SIGNED UP SUCCESSFULLY.");
        isLoggedIn = true;
    }

    @Test(dependsOnMethods = {"signupUser"})
    public void openProductPage(){

        header = new HeaderComponent(driver);
        header.openMenFashion();

        waitFor().until(ExpectedConditions.urlContains("categories/fashion/men"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/men"));

        HomePage homePage = new HomePage(driver);
        WebElement product = homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("products/"));
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void addToCart(){

        ProductPage productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize("M");

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));
        boolean available = productPage.checkAvailable();
        try {
            if (available) {
                productPage.addToCart();
            }
            else {
                productPage.addToCart();
                Assert.fail("Item is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addToCart"})
    public void openCheckout(){

        CartPage cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));

        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"openCheckout"})
    public void checkoutProduct(){

        CheckoutPage checkoutPage = new CheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailMeButton));

        checkoutPage.fillAddress(countryName, firstName, lastName, address,
                apartment, city, state, postalCode, phoneNumber);

        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.saveAndContinueButton));
        checkoutPage.clickSaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/delivery"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/delivery"));

        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.deliverySaveAndContinueButton));
        checkoutPage.selectStandard();

        waitFor().until(ExpectedConditions.urlContains("/payment"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/payment"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliveryMethod));

        checkoutPage.enterCardDetails(creditCardNumber, expiryData, CVV);
        checkoutPage.clickPayNow();

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "YOUR CARD NUMBER IS INVALID.");
    }
}
