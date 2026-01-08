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

public class GuestCheckoutTest extends TestBase {

    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    CompleteCheckoutPage completeCheckout;

    String productTitle;
    String productPrice;
    String productColor;
    String productSize  = "M";
    int productQuantity ;

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
        Assert.assertTrue(driver.getCurrentUrl().contains("products/"));
    }

    @Test(priority = 2)
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
                productPage.addToCart();
                Assert.fail("Item is sold out");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 3)
    public void checkCartProduct() {
        cartPage = new CartPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(cartPage.closeCartButton));

        CartPage.CartProduct cartProduct = cartPage.getProduct(1);

        String cartProductTitle = cartProduct.getTitle();
        String cartProductPrice = cartProduct.getPrice();
        String cartProductColor = cartProduct.getColor();
        String cartProductSize = cartProduct.getSize();
        int cartProductQuantity = cartProduct.getQuantity();

        Assert.assertEquals(cartProductTitle, productTitle);
        Assert.assertEquals(cartProductPrice, productPrice);
        Assert.assertEquals(cartProductColor, productColor);
        Assert.assertEquals(cartProductSize, productSize);
        Assert.assertEquals(cartProductQuantity, productQuantity);

        waitFor().until(ExpectedConditions.elementToBeClickable(cartPage.checkoutButton));
        cartPage.openCheckoutPage();

        waitFor().until(ExpectedConditions.urlContains("checkout/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("checkout/"));
    }

    @Test(priority = 4)
    public void checkoutAsGuest() {

        checkoutPage = new CheckoutPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(checkoutPage.emailBox));

        checkoutPage.enterGuestEmail(email);
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
