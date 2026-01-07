package Tests.Regression.Product;

import Base.TestBase;
import Components.HeaderComponent;
import Pages.CartPage;
import Pages.HomePage;
import Pages.ProductPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class AddProductToCartTest extends TestBase {

    HeaderComponent header;
    HomePage homePage;
    ProductPage productPage;
    CartPage cartPage;

    String productSize = "M";
    String productName;
    String productPrice;
    String productColor;
    int productQuantity;

    @Test(priority = 1)
    public void openProductPage() {

        isLoggedIn = false;
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
                productName = productPage.getTitle();
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
    public void checkAddedToCart() {

        cartPage = new CartPage(driver);

        waitFor().until(ExpectedConditions.visibilityOf(cartPage.productsContainer));
        CartPage.CartProduct product = cartPage.getProduct(1);

        String cartProductTitle = product.getTitle();
        String cartProductPrice = product.getPrice();
        String cartProductSize = product.getSize();
        String cartProductColor = product.getColor();
        int cartProductQuantity = product.getQuantity();

        Assert.assertEquals(cartProductTitle, productName);
        Assert.assertEquals(cartProductPrice, productPrice);
        Assert.assertEquals(cartProductSize, productSize);
        Assert.assertEquals(cartProductColor, productColor);
        Assert.assertEquals(cartProductQuantity, productQuantity);
    }
}
