package Tests.Regression.Product;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class CheckProductDetailsAllOverPagesTest extends TestBase {

    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckoutPage;

    String productTitle;
    String productPrice;
    String productColor;
    String productSize = "M";
    int productQuantity;

    String homeProductTitle;

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


    String creditCardNumber = TestData.generateVisaCard();
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
    }

    @Test(dependsOnMethods = {"signupUser"})
    public void getProduct() {

        header = new HeaderComponent(driver);
        header.openWomanFashion();

        waitFor().until(ExpectedConditions.urlContains("categories/fashion/women"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/women"));

        homePage = new HomePage(driver);
        WebElement product = homePage.getRandomProduct();
        WebElement productTitle = product.findElement(By.cssSelector("h3.line-clamp-1.product-card-title"));
        homeProductTitle = productTitle.getText().toUpperCase();

        product.click();
        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("products/"));
    }

    @Test(dependsOnMethods = {"getProduct"})
    public void checkRightProduct() {

        productPage = new ProductPage(driver);

        productTitle = productPage.getTitle();
        Assert.assertEquals(productTitle, homeProductTitle);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize(productSize);

        productPrice = productPage.getPrice();
        productColor = productPage.getColor();
        productQuantity = productPage.getQuantity();
    }

    @Test(dependsOnMethods = {"checkRightProduct"})
    public void addToCart() {

        productPage = new ProductPage(driver);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));

        productPage.addToCart();
    }

    @Test(dependsOnMethods = {"addToCart"})
    public void checkCartProduct() {

        cartPage = new CartPage(driver);

        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));

        CartPage.CartProduct product = cartPage.getProduct(1);

        String cartProductTitle = product.getTitle();
        String cartProductPrice = product.getPrice();
        String cartProductColor = product.getColor();
        String cartProductSize = product.getSize();
        int cartProductQuantity = product.getQuantity();

        Assert.assertEquals(cartProductTitle, productTitle);
        Assert.assertEquals(cartProductPrice, productPrice);
        Assert.assertEquals(cartProductColor, productColor);
        Assert.assertEquals(cartProductSize, productSize);
        Assert.assertEquals(cartProductQuantity, productQuantity);

        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"checkCartProduct"})
    public void checkCheckoutProduct() {

        completeCheckoutPage = new CompleteCheckoutPage(driver);
        CompleteCheckoutPage.CheckoutProduct product = completeCheckoutPage.getProduct(1);

        String checkoutProductTitle = product.getTitle();
        String checkoutProductPrice = product.getPrice();
        String checkoutProductColor = product.getProductColor();
        String checkoutProductSize = product.getProductSize();
        int checkoutProductQuantity = product.getQuantity();

        Assert.assertEquals(checkoutProductTitle, productTitle);
        Assert.assertEquals(checkoutProductPrice, productPrice);
        Assert.assertEquals(checkoutProductColor, productColor);
        Assert.assertEquals(checkoutProductSize, productSize);
        Assert.assertEquals(checkoutProductQuantity, productQuantity);
    }

    @Test(dependsOnMethods = {"checkCheckoutProduct"})
    public void checkoutProduct() {

        CheckoutPage checkoutPage = new CheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailMeButton));

        checkoutPage.fillAddress(countryName, firstName, lastName, address,
                apartment, city, state, postalCode, phoneNumber);
        checkoutPage.clickSaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/delivery"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/delivery"));

        checkoutPage.selectNextDay();
        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliverySaveAndContinueButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.deliverySaveAndContinueButton));
        checkoutPage.clickDeliverySaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/payment"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/payment"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliveryMethod));

        checkoutPage.enterCardDetails(creditCardNumber, expiryData, CVV);
        checkoutPage.clickPayNow();

        CompleteCheckoutPage completeCheckoutPage = new CompleteCheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/complete"));

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckoutPage.successMessage));
        Assert.assertEquals(completeCheckoutPage.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }
}
