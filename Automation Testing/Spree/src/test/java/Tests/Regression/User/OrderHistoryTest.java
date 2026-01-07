package Tests.Regression.User;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class OrderHistoryTest extends TestBase {

    SignupPage signupPage;
    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckout;
    OrderPage orderPage;

    String productTitle;
    String productPrice;
    String productColor;
    String productSize  = "M";
    int productQuantity ;

    String cartSubTotal;
    String orderID;

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
    public void openProductPage() {

        header = new HeaderComponent(driver);
        header.openAccessories();
        waitFor().until(ExpectedConditions.urlContains("categories/fashion/accessories"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/accessories"));

        homePage = new HomePage(driver);
        WebElement product =homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("products/"));
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void addProductToCart() {

        productPage = new ProductPage(driver);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.productTitle));
        productTitle = productPage.getTitle();
        productPrice = productPage.getPrice();
        try {
            productColor = productPage.getColor();
        } catch (Exception e) {
            productColor = "";
        }
        productQuantity = productPage.getQuantity();

        try {
            productPage.chooseSize(productSize);
        } catch (Exception e) {
            productSize = "";
        }

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));
        boolean available = productPage.checkAvailable();
        try {
            if (available) {
                productPage.addToCart();
            }
            else {
                System.out.println("product is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void checkCart() {

        cartPage =new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));
        CartPage.CartProduct cartProduct = cartPage.getProduct(1);

        String cartProductTitle = cartProduct.getTitle();
        String cartProductPrice = cartProduct.getPrice();
        String cartProductColor;
        try {
            cartProductColor = cartProduct.getColor();
        } catch (Exception e) {
            cartProductColor = "";
        }
        String cartProductSize;
        try {
            cartProductSize = cartProduct.getSize();
        } catch (Exception e) {
            cartProductSize = "";
        }

        int cartProductQuantity = cartProduct.getQuantity();

        Assert.assertEquals(cartProductTitle, productTitle);
        Assert.assertEquals(cartProductColor, productColor);
        Assert.assertEquals(cartProductPrice, productPrice);
        Assert.assertEquals(cartProductSize, productSize);
        Assert.assertEquals(cartProductQuantity, productQuantity);

        cartSubTotal = cartPage.totalPrice.getText();

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"checkCart"})
    public void checkCheckoutProduct() {

        checkoutPage = new CheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));
        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailMeButton));

        completeCheckout = new CompleteCheckoutPage(driver);
        CompleteCheckoutPage.CheckoutProduct checkoutProduct = completeCheckout.getProduct(1);

        String checkoutProductTitle = checkoutProduct.getTitle();
        String checkoutProductPrice = checkoutProduct.getPrice();
        String checkoutProductColor;
        try {
            checkoutProductColor = checkoutProduct.getProductColor();
        } catch (Exception e) {
            checkoutProductColor = "";
        }
        String checkoutProductSize;
        try {
            checkoutProductSize = checkoutProduct.getProductSize();
        } catch (Exception e) {
            checkoutProductSize = "";
        }
        int checkoutProductQuantity = checkoutProduct.getQuantity();


        Assert.assertEquals(checkoutProductTitle, productTitle);
        Assert.assertEquals(checkoutProductPrice, productPrice);
        Assert.assertEquals(checkoutProductColor, productColor);
        Assert.assertEquals(checkoutProductSize, productSize);
        Assert.assertEquals(checkoutProductQuantity, productQuantity);
    }

    @Test(dependsOnMethods = {"checkCheckoutProduct"})
    public void completeCheckout() {

        checkoutPage = new CheckoutPage(driver);
        checkoutPage.fillAddress(countryName, firstName, lastName, address,
                apartment, city, state, postalCode, phoneNumber);
        checkoutPage.clickSaveAndContinueButton();

        waitFor().until(ExpectedConditions.urlContains("/delivery"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/delivery"));

        waitFor().until(ExpectedConditions.elementToBeClickable(checkoutPage.deliverySaveAndContinueButton));
        checkoutPage.selectStandard();

        waitFor().until(ExpectedConditions.urlContains("/payment"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/payment"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.deliveryMethod));

        checkoutPage.enterCardDetails(masterCardNumber, expiryData, CVV);
        checkoutPage.clickPayNow();

        CompleteCheckoutPage completeCheckoutPage = new CompleteCheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/complete"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/complete"));

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckoutPage.orderID));
        orderID = completeCheckoutPage.orderID.getText().replace("Order", "").trim();

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckoutPage.successMessage));
        Assert.assertEquals(completeCheckoutPage.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }

    @Test(dependsOnMethods = {"completeCheckout"})
    public void checkOrder(){

        driver.navigate().to("https://demo.spreecommerce.org/");

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.myAccount));
        waitFor().until(ExpectedConditions.elementToBeClickable(header.myAccount));
        header.openAccount();
        waitFor().until(ExpectedConditions.urlContains("account/orders"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("account/orders"));

        orderPage = new OrderPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(orderPage.orderLink));
        waitFor().until(ExpectedConditions.elementToBeClickable(orderPage.orderLink));
        orderPage.openOrderPage();

        waitFor().until(ExpectedConditions.urlContains(orderID));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains(orderID));

        String ID = orderPage.getOrderId();
        String orderCity = orderPage.getCity();
        String orderAddress = orderPage.getAddress();
        String orderPostalCode = orderPage.getPostalCode();
        String orderSubTotal = orderPage.getProductSubTotal();

        String orderProductTitle = orderPage.getProductTitle();
        String orderProductPrice = orderPage.getProductPrice();
        String orderProductSize;
        try {
            orderProductSize = orderPage.getProductSize();
        } catch (Exception e) {
            orderProductSize = "";
        }

        Assert.assertEquals(orderProductTitle, productTitle);
        Assert.assertEquals(orderProductPrice, productPrice);
        Assert.assertEquals(orderProductSize, productSize);
        Assert.assertEquals(ID, orderID);
        Assert.assertEquals(orderCity, city);
        Assert.assertEquals(orderAddress, address + " " + apartment);
        Assert.assertEquals(orderPostalCode, postalCode);
        Assert.assertEquals(orderSubTotal, cartSubTotal);
    }
}
