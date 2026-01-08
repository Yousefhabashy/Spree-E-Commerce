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

public class OrderIDTest extends TestBase {

    SignupPage signupPage;
    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckout;

    String product1Title;
    String product1Price;
    String product1Color;
    String product1Size  = "M";
    int product1Quantity;
    String order1ID;

    String product2Title;
    String product2Price;
    String product2Color;
    String product2Size  = "M";
    int product2Quantity;
    String order2ID;

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

    String masterCardNumber = TestData.generateVisaCard();
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
    public void openProduct1Page(){

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

    @Test(dependsOnMethods = {"openProduct1Page"})
    public void addProduct1ToCart() {

        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize(product1Size);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));

        boolean available = productPage.checkAvailable();
        try {
            if (available) {

                product1Title = productPage.getTitle();
                product1Color = productPage.getColor();
                product1Price = productPage.getPrice();
                product1Quantity = productPage.getQuantity();

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

    @Test(dependsOnMethods = {"addProduct1ToCart"})
    public void checkCartProduct1() {

        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));

        CartPage.CartProduct cartProduct = cartPage.getProduct(1);
        String cartProductTitle = cartProduct.getTitle();
        String cartProductPrice = cartProduct.getPrice();
        String cartProductColor = cartProduct.getColor();
        String cartProductSize = cartProduct.getSize();
        int cartProductQuantity = cartProduct.getQuantity();

        Assert.assertEquals(cartProductTitle, product1Title);
        Assert.assertEquals(cartProductPrice, product1Price);
        Assert.assertEquals(cartProductSize, product1Size);
        Assert.assertEquals(cartProductColor, product1Color);
        Assert.assertEquals(cartProductQuantity, product1Quantity);

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"checkCartProduct1"})
    public void checkoutOrder1(){
        checkoutPage = new CheckoutPage(driver);

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

        checkoutPage.enterCardDetails(masterCardNumber, expiryData, CVV);
        checkoutPage.clickPayNow();

        completeCheckout = new CompleteCheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/complete"));

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckout.successMessage));
        Assert.assertEquals(completeCheckout.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }

    @Test(dependsOnMethods = {"checkoutOrder1"})
    public void checkCheckoutProduct1() {

        completeCheckout = new CompleteCheckoutPage(driver);

        order1ID = completeCheckout.orderID.getText().replace("Order", "").trim();
        CompleteCheckoutPage.CheckoutProduct product = completeCheckout.getProduct(1);

        String checkoutProductTitle = product.getTitle();
        String checkoutProductColor = product.getProductColor();
        String checkoutProductSize = product.getProductSize();
        int checkoutProductQuantity = product.getQuantity();

        Assert.assertEquals(checkoutProductTitle, product1Title);
        Assert.assertEquals(checkoutProductColor, product1Color);
        Assert.assertEquals(checkoutProductSize, product1Size);
        Assert.assertEquals(checkoutProductQuantity, product1Quantity);

        driver.navigate().to("https://demo.spreecommerce.org/");
    }
    @Test(dependsOnMethods = {"checkCheckoutProduct1"})
    public void openProduct2Page(){

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

    @Test(dependsOnMethods = {"openProduct2Page"})
    public void addProduct2ToCart() {

        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize(product2Size);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));

        boolean available = productPage.checkAvailable();
        try {
            if (available) {

                product2Title = productPage.getTitle();
                product2Color = productPage.getColor();
                product2Price = productPage.getPrice();
                product2Quantity = productPage.getQuantity();

                productPage.addToCart();
            }
            else {
                System.out.println("product is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addProduct2ToCart"})
    public void checkCartProduct2() {

        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));

        CartPage.CartProduct cartProduct = cartPage.getProduct(1);
        String cartProductTitle = cartProduct.getTitle();
        String cartProductPrice = cartProduct.getPrice();
        String cartProductColor = cartProduct.getColor();
        String cartProductSize = cartProduct.getSize();
        int cartProductQuantity = cartProduct.getQuantity();

        Assert.assertEquals(cartProductTitle, product2Title);
        Assert.assertEquals(cartProductPrice, product2Price);
        Assert.assertEquals(cartProductSize, product2Size);
        Assert.assertEquals(cartProductColor, product2Color);
        Assert.assertEquals(cartProductQuantity, product2Quantity);

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"checkCartProduct2"})
    public void checkoutOrder2(){

        checkoutPage = new CheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.saveAndContinueButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.saveAndContinueButton));
        checkoutPage.clickSaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/delivery"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/delivery"));

        checkoutPage.selectStandard();

        waitFor().until(ExpectedConditions.urlContains("/payment"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/payment"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliveryMethod));
        checkoutPage.clickPayNow();

        completeCheckout = new CompleteCheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/complete"));

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckout.successMessage));
        Assert.assertEquals(completeCheckout.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }

    @Test(dependsOnMethods = {"checkoutOrder2"})
    public void checkCheckoutProduct2() {

        completeCheckout = new CompleteCheckoutPage(driver);

        order2ID = completeCheckout.orderID.getText().replace("Order", "").trim();
        CompleteCheckoutPage.CheckoutProduct product = completeCheckout.getProduct(1);

        String checkoutProductTitle = product.getTitle();
        String checkoutProductColor = product.getProductColor();
        String checkoutProductSize = product.getProductSize();
        int checkoutProductQuantity = product.getQuantity();

        Assert.assertEquals(checkoutProductTitle, product2Title);
        Assert.assertEquals(checkoutProductColor, product2Color);
        Assert.assertEquals(checkoutProductSize, product2Size);
        Assert.assertEquals(checkoutProductQuantity, product2Quantity);
    }
    @Test(dependsOnMethods = {"checkCheckoutProduct2"})
    public void checkOrderIDs() {
        Assert.assertNotEquals(order2ID, order1ID);
    }
}
