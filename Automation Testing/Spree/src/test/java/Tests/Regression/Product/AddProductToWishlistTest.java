package Tests.Regression.Product;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class AddProductToWishlistTest extends TestBase {

    SignupPage signupPage;
    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    WishlistPage wishlistPage;

    String email = TestData.generateEmail();
    String password = TestData.generatePassword();

    String productName;
    String productPrice;
    String productColor;

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
        header.openWomanFashion();

        waitFor().until(ExpectedConditions.urlContains("fashion/women"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("fashion/women"));

        homePage = new HomePage(driver);
        homePage.getRandomProduct().click();

        waitFor().until(ExpectedConditions.urlContains("products/"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("products/"));
    }

    @Test(dependsOnMethods = {"openProductPage"})
    public void addProductToWishlist() {

        productPage = new ProductPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(productPage.productTitle));
        productName = productPage.productTitle.getText();
        productColor = productPage.getColor();
        productPrice = productPage.getPrice();

        productPage.addToWishlist();
    }

    @Test(dependsOnMethods = {"addProductToWishlist"})
    public void checkAddedToWishlist() {

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.wishlistCount));
        header.openWishlist();

        waitFor().until(ExpectedConditions.urlContains("account/wishlist"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("account/wishlist"));

        wishlistPage = new WishlistPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(wishlistPage.wishlistContainer));

//        WishlistPage.wishListProduct product = wishlistPage.getProduct(1);

//        String wishlistProductTitle = product.getTitle();
//        String wishlistProductPrice = product.getPrice();
//        String wishlistProductColor = product.getColor();
//
//        Assert.assertEquals(wishlistProductTitle, productName);
//        Assert.assertEquals(wishlistProductPrice, productPrice);
//        Assert.assertEquals(wishlistProductColor, productColor);
    }
}
