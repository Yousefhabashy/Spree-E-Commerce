package Tests.EndToEnd;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class SignInDuringCheckoutTest extends TestBase {

    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    SignInPage signInPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckout;

    String email = "elyse.stark@gmail.com";
    String password = "Errol006651";

    String firstName = TestData.generateFirstName();
    String lastName = TestData.generateLastName();

    String countryName = "United States";
    String address = TestData.generateAddress();
    String apartment = TestData.generateApartment();
    String state = TestData.generateUSState();
    String city = TestData.generateCity();
    String postalCode = TestData.generatePostalCodeByState(state);
    String phoneNumber = TestData.generatePhoneNumber();

    String masterCardNumber = TestData.generateVisaDebit();
    String expiryData = TestData.generateExpiry();
    String CVV = TestData.generateCVV();

    @Test(priority = 1)
    public void openProductPage() {

        header = new HeaderComponent(driver);
        header.openMenFashion();
        waitFor().until(ExpectedConditions.urlContains("categories/fashion/men"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/men"));

        homePage = new HomePage(driver);
        WebElement product = homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("products/"));
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void addProductToCart() {

        productPage = new ProductPage(driver);
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
                System.out.println("Item sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void processToCheckout() {

        cartPage = new CartPage(driver);

        waitFor().until(ExpectedConditions.visibilityOf(cartPage.productsContainer));

        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("user/sign_in"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("user/sign_in"));
    }

    @Test(dependsOnMethods = {"processToCheckout"})
    public void signInUser() {

        signInPage = new SignInPage(driver);
        signInPage.loginUser(email, password, false);

        HeaderComponent header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "SIGNED IN SUCCESSFULLY.");
        isLoggedIn = true;

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"signInUser"})
    public void checkoutProduct() {

        checkoutPage = new CheckoutPage(driver);
        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailMeButton));
        checkoutPage.activeEmailMe();
        checkoutPage.fillAddress(countryName, firstName, lastName, address,
                apartment, city, state, postalCode, phoneNumber);
        checkoutPage.clickSaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/delivery"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/delivery"));
        checkoutPage.selectPremium();
        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliverySaveAndContinueButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.deliverySaveAndContinueButton));
        checkoutPage.clickDeliverySaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/payment"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/payment"));
        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliveryMethod));
        checkoutPage.enterCardDetails(masterCardNumber, expiryData, CVV);
        checkoutPage.clickPayNow();

        completeCheckout = new CompleteCheckoutPage(driver);
        waitFor().until(ExpectedConditions.urlContains("/complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/complete"));
        waitFor().until(ExpectedConditions.visibilityOf(completeCheckout.successMessage));
        Assert.assertEquals(completeCheckout.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }
}
