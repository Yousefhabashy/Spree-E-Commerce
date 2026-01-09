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

public class CheckoutWithEuroTest extends TestBase {

    HeaderComponent header;
    SignupPage signupPage;
    HomePage homePage;
    ProductPage productPage ;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckout;

    String euro = "€";
    String email = TestData.generateEmail();
    String password = TestData.generatePassword();

    String firstName = TestData.generateFirstName();
    String lastName = TestData.generateLastName();

    String countryName = "United States";
    String address = TestData.generateAddress();
    String apartment = TestData.generateApartment();
    String state = TestData.generateUSState();
    String city = TestData.generateCity();
    String postalCode = TestData.generatePostalCodeByState(state);
    String phoneNumber = TestData.generatePhoneNumber();

    String masterCardNumber = TestData.generateMasterCard();
    String expiryData = TestData.generateExpiry();
    String CVV = TestData.generateCVV();

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
    public void changeCurrencyToEuro() {

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.currencyAndLanguageContainer));
        waitFor().until(ExpectedConditions.elementToBeClickable(header.currencyAndLanguageContainer));

        header.openCurrencyAndLanguageContainer();
        waitFor().until(ExpectedConditions.visibilityOf(header.selectCurrency));
        waitFor().until(ExpectedConditions.elementToBeClickable(header.selectCurrency));
        header.selectEuro();
        header.save();
    }

    @Test(dependsOnMethods = {"changeCurrencyToEuro"})
    public void openProductPage() {

        header = new HeaderComponent(driver);
        header.openWomanFashion();
        waitFor().until(ExpectedConditions.urlContains("categories/fashion/women"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/women"));

        homePage = new HomePage(driver);
        WebElement product = homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("products/"));
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void checkProductPagePrice() {
        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize("M");

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));

        boolean available = productPage.checkAvailable();
        try {
            if (available) {
                String productPrice = productPage.getPrice();

                Assert.assertTrue(productPrice.contains(euro));
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

    @Test(dependsOnMethods = {"checkProductPagePrice"})
    public void checkCartPagePrice() {

        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));
        CartPage.CartProduct cartProduct = cartPage.getProduct(1);
        String cartProductPrice = cartProduct.getPrice();

        Assert.assertTrue(cartProductPrice.contains(euro));

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"checkCartPagePrice"})
    public void checkCheckoutPagePrice() {

        completeCheckout = new CompleteCheckoutPage(driver);
        CompleteCheckoutPage.CheckoutProduct product = completeCheckout.getProduct(1);

        String checkoutProductPrice = product.getPrice();

        Assert.assertTrue(checkoutProductPrice.contains(euro));
    }

    @Test(dependsOnMethods = {"checkCheckoutPagePrice"})
    public void checkoutProduct(){
        checkoutPage = new CheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailMeButton));

        checkoutPage.fillAddress(countryName, firstName, lastName, address,
                apartment, city, state, postalCode, phoneNumber);
        checkoutPage.clickSaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/delivery"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/delivery"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliverySaveAndContinueButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.deliverySaveAndContinueButton));
        checkoutPage.clickDeliverySaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/payment"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/payment"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliveryMethod));

        checkoutPage.enterCardDetails(masterCardNumber, expiryData, CVV);
        checkoutPage.clickPayNow();

        CompleteCheckoutPage completeCheckoutPage = new CompleteCheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/complete"));

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckoutPage.successMessage));
        Assert.assertEquals(completeCheckoutPage.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }
}
