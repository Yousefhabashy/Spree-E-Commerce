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

public class CheckoutWithDeutschTest extends TestBase {
    HeaderComponent header;
    SignupPage signupPage;
    HomePage homePage;
    ProductPage productPage ;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    String email = TestData.generateEmail();
    String password = TestData.generatePassword();

    String firstName = TestData.generateFirstName();
    String lastName = TestData.generateLastName();

    String countryName = "Ägypten";
    String address = TestData.generateAddress();
    String apartment = TestData.generateApartment();
    String state = TestData.generateUSState();
    String city = TestData.generateCity();
    String postalCode = TestData.generatePostalCode();
    String phoneNumber = TestData.generatePhoneNumber();

    String masterCardNumber = TestData.generateVisaDebit();
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
    public void changeLanguageToDeutsch() {

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.currencyAndLanguageContainer));
        waitFor().until(ExpectedConditions.elementToBeClickable(header.currencyAndLanguageContainer));

        header.openCurrencyAndLanguageContainer();
        waitFor().until(ExpectedConditions.visibilityOf(header.selectLanguage));
        waitFor().until(ExpectedConditions.elementToBeClickable(header.selectLanguage));
        header.selectDeutsch();
        header.save();
    }

    @Test(dependsOnMethods = {"changeLanguageToDeutsch"})
    public void openProductPage() {

        header = new HeaderComponent(driver);
        header.openWomanFashion();
        waitFor().until(ExpectedConditions.urlContains("categories/fashion/women"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/women"));

        homePage = new HomePage(driver);
        WebElement product = homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("de/products/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("de/products/"));
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void addProductToCart() {

        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize("M");


        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));
        productPage.addToCart();
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void openProductCheckoutPage() {

        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.ZurKasse));
        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.ZurKasse));
        cartPage.zurKasse();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"openProductCheckoutPage"})
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

    @Test(dependsOnMethods = {"checkoutProduct"})
    public void logoutUser() {

        driver.navigate().to("https://demo.spreecommerce.org/account/orders");

        waitFor().until(ExpectedConditions.urlContains("/account/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("account/"));

        AccountPage accountPage = new AccountPage(driver);
        accountPage.logoutUser();

        header =  new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "ERFOLGREICH ABGEMELDET.");
        isLoggedIn = false;
    }
}
