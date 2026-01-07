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

public class CompleteUserJourneyTest extends TestBase {

    HeaderComponent header;
    SignupPage signupPage;
    AccountPage accountPage;
    SignInPage signInPage;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckoutPage;

    String productTitle;
    String productPrice;
    String productColor;
    String productSize  = "M";
    int productQuantity ;

    String email = TestData.generateEmail();
    String password = TestData.generatePassword();
    String newPassword = TestData.generatePassword();

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
    public void changePassword() {

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.myAccount));
        waitFor().until(ExpectedConditions.elementToBeClickable(header.myAccount));
        header.openAccount();
        waitFor().until(ExpectedConditions.urlContains("account/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("account/"));

        accountPage= new AccountPage(driver);
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
    }

    @Test(dependsOnMethods = {"loginWithNewPassword"})
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
    public void addProductToCart() {

        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize(productSize);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));

        boolean available = productPage.checkAvailable();
        try {
            if (available) {

                productTitle = productPage.getTitle();
                productColor = productPage.getColor();
                productPrice = productPage.getPrice();
                productQuantity = productPage.getQuantity();

                productPage.addToCart();
            }
            else {
                System.out.println("product is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addProductToCart"})
    public void checkCartProduct() {

        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.checkoutButton));

        CartPage.CartProduct cartProduct = cartPage.getProduct(1);
        String cartProductTitle = cartProduct.getTitle();
        String cartProductPrice = cartProduct.getPrice();
        String cartProductColor = cartProduct.getColor();
        String cartProductSize = cartProduct.getSize();
        int cartProductQuantity = cartProduct.getQuantity();

        Assert.assertEquals(cartProductTitle, productTitle);
        Assert.assertEquals(cartProductPrice, productPrice);
        Assert.assertEquals(cartProductSize, productSize);
        Assert.assertEquals(cartProductColor, productColor);
        Assert.assertEquals(cartProductQuantity, productQuantity);

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
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

        checkoutPage.selectNextDay();
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
