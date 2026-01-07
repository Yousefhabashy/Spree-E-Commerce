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

public class MultipleProductPurchaseTest extends TestBase {

    HeaderComponent header;
    SignupPage signupPage;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckoutPage;

    String product1Title ;
    String product1Price;
    String product1Color;
    String product1Size = "M";
    int product1Quantity;

    String product2Title ;
    String product2Price;
    String product2Color;
    String product2Size = "M";
    int product2Quantity;

    String product3Title ;
    String product3Price;
    String product3Color;
    String product3Size = "L";
    int product3Quantity;


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
    public void addProduct1ToCart() {

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

                cartPage = new CartPage(driver);
                waitFor().until(ExpectedConditions.visibilityOf(cartPage.closeCartButton));
                waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.closeCartButton));
                cartPage.closeCart();
            }
            else {
                System.out.println("product is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addProduct1ToCart"})
    public void addProduct2ToCart() {

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.cartCounter));

        header.openAccessories();
        waitFor().until(ExpectedConditions.urlContains("categories/fashion/accessories"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/fashion/accessories"));
        homePage = new HomePage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(homePage.filterLink));
        WebElement product = homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("products/"));

        productPage = new ProductPage(driver);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));
        boolean available = productPage.checkAvailable();
        try {
            if(available) {
                product2Title = productPage.getTitle();
                product2Price = productPage.getPrice();
                try {
                    product2Color = productPage.getColor();
                } catch (RuntimeException e) {
                    product2Color = "";
                }

                product2Quantity = productPage.getQuantity();

                productPage.addToCart();

                cartPage = new CartPage(driver);
                waitFor().until(ExpectedConditions.visibilityOf(cartPage.closeCartButton));
                waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.closeCartButton));
                cartPage.closeCart();
            }
            else {
                System.out.println("product is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addProduct2ToCart"})
    public void addProduct3ToCart() {

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.cartCounter));

        waitFor().until(ExpectedConditions.elementToBeClickable(header.wellness));
        header.openWellness();
        waitFor().until(ExpectedConditions.urlContains("categories/wellness"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("categories/wellness"));
        homePage = new HomePage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(homePage.filterLink));
        WebElement product = homePage.getRandomProduct();
        waitFor().until(ExpectedConditions.elementToBeClickable(product));
        product.click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("products/"));

        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.chooseSizeButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.chooseSizeButton));
        productPage.chooseSize(product3Size);

        waitFor().until(ExpectedConditions.visibilityOf(productPage.addToCartButton));
        waitFor().until(ExpectedConditions.elementToBeClickable(productPage.addToCartButton));

        boolean available = productPage.checkAvailable();
        try {
            if (available) {
                product3Title = productPage.getTitle();
                product3Price = productPage.getPrice();
                product3Color = productPage.getColor();
                product3Quantity = productPage.getQuantity();

                productPage.addToCart();

                cartPage = new CartPage(driver);
                waitFor().until(ExpectedConditions.visibilityOf(cartPage.closeCartButton));
                waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.closeCartButton));
                cartPage.closeCart();
            }
            else {
                System.out.println("product is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(dependsOnMethods = {"addProduct3ToCart"})
    public void checkCartProducts() {

        cartPage = new CartPage(driver);

        CartPage.CartProduct product1 = cartPage.getProduct(1);
        CartPage.CartProduct product2 = cartPage.getProduct(2);
        CartPage.CartProduct product3 = cartPage.getProduct(3);

        String cartProduct1Title = product1.getTitle();
        String cartProduct2Title = product2.getTitle();
        String cartProduct3Title = product3.getTitle();

        String cartProduct1Price = product1.getPrice();
        String cartProduct2Price = product2.getPrice();
        String cartProduct3Price = product3.getPrice();

        String cartProduct1Color = product1.getColor();
        String cartProduct2Color = product2.getColor();
        String cartProduct3Color = product3.getColor();

        String cartProduct1Size = product1.getSize();
        String cartProduct2Size = "";
        if(!Objects.equals(product2Size, "")) {
            cartProduct2Size = product2.getSize();
        }
        String cartProduct3Size = product3.getSize();

        int cartProduct1Quantity = product1.getQuantity();
        int cartProduct2Quantity = product2.getQuantity();
        int cartProduct3Quantity = product3.getQuantity();

        Assert.assertEquals(cartProduct1Title, product1Title);
        Assert.assertEquals(cartProduct1Price, product1Price);
        Assert.assertEquals(cartProduct1Color, product1Color);
        Assert.assertEquals(cartProduct1Size, product1Size);
        Assert.assertEquals(cartProduct1Quantity, product1Quantity);


        Assert.assertEquals(cartProduct2Title, product2Title);
        Assert.assertEquals(cartProduct2Price, product2Price);
        Assert.assertEquals(cartProduct2Color, product2Color);
        Assert.assertEquals(cartProduct2Size, product2Size);
        Assert.assertEquals(cartProduct2Quantity, product2Quantity);

        Assert.assertEquals(cartProduct3Title, product3Title);
        Assert.assertEquals(cartProduct3Price, product3Price);
        Assert.assertEquals(cartProduct3Color, product3Color);
        Assert.assertEquals(cartProduct3Size, product3Size);
        Assert.assertEquals(cartProduct3Quantity, product3Quantity);

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();
        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(dependsOnMethods = {"checkCartProducts"})
    public void checkCheckoutProducts() {

        completeCheckoutPage = new CompleteCheckoutPage(driver);
        CompleteCheckoutPage.CheckoutProduct product1 = completeCheckoutPage.getProduct(1);
        CompleteCheckoutPage.CheckoutProduct product2 = completeCheckoutPage.getProduct(2);
        CompleteCheckoutPage.CheckoutProduct product3 = completeCheckoutPage.getProduct(3);

        String checkoutProduct1Title = product1.getTitle();
        String checkoutProduct2Title = product2.getTitle();
        String checkoutProduct3Title = product3.getTitle();

        String checkoutProduct1Price = product1.getPrice();
        String checkoutProduct2Price = product2.getPrice();
        String checkoutProduct3Price = product3.getPrice();

        String checkoutProduct1Color = product1.getProductColor();
        String checkoutProduct2Color = product2.getProductColor();
        String checkoutProduct3Color = product3.getProductColor();

        String checkoutProduct1Size = product1.getProductSize();
        String checkoutProduct2Size = "";
        if(!Objects.equals(product2Size, "")) {
            checkoutProduct2Size = product2.getProductSize();
        }
        String checkoutProduct3Size = product3.getProductSize();

        int cartProduct1Quantity = product1.getQuantity();
        int cartProduct2Quantity = product2.getQuantity();
        int cartProduct3Quantity = product3.getQuantity();

        Assert.assertEquals(checkoutProduct1Title, product1Title);
        Assert.assertEquals(checkoutProduct1Price, product1Price);
        Assert.assertEquals(checkoutProduct1Color, product1Color);
        Assert.assertEquals(checkoutProduct1Size, product1Size);
        Assert.assertEquals(cartProduct1Quantity, product1Quantity);


        Assert.assertEquals(checkoutProduct2Title, product2Title);
        Assert.assertEquals(checkoutProduct2Price, product2Price);
        Assert.assertEquals(checkoutProduct2Color, product2Color);
        Assert.assertEquals(checkoutProduct2Size, product2Size);
        Assert.assertEquals(cartProduct2Quantity, product2Quantity);

        Assert.assertEquals(checkoutProduct3Title, product3Title);
        Assert.assertEquals(checkoutProduct3Price, product3Price);
        Assert.assertEquals(checkoutProduct3Color, product3Color);
        Assert.assertEquals(checkoutProduct3Size, product3Size);
        Assert.assertEquals(cartProduct3Quantity, product3Quantity);
    }

    @Test(dependsOnMethods = {"checkCheckoutProducts"})
    public void checkoutProduct() {

        checkoutPage = new CheckoutPage(driver);

        waitFor().until(ExpectedConditions.urlContains("/address"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/address"));

        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailMeButton));

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

        waitFor().until(ExpectedConditions.visibilityOf(completeCheckoutPage.successMessage));
        Assert.assertEquals(completeCheckoutPage.successMessage.getText(), "Thanks "+ firstName +" for your order!");
    }
}
