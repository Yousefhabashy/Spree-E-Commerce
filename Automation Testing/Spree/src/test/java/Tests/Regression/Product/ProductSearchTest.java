package Tests.Regression.Product;

import Base.TestBase;
import Components.HeaderComponent;
import Pages.HomePage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Objects;

public class ProductSearchTest extends TestBase {

    HeaderComponent header;
    HomePage homePage;

    String productTitle = "Beige Handbag";
    @Test(priority = 1)
    public void productSearch(){

        isLoggedIn =false;

       header = new HeaderComponent(driver);
       waitFor().until(ExpectedConditions.elementToBeClickable(header.openSearchButton));
       header.openSearch();
       header.searchProduct(productTitle);

       waitFor().until(ExpectedConditions.urlContains("search?q"));
       Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("search?q"));
    }

    @Test(dependsOnMethods = {"productSearch"})
    public void checkProductAppear() {
        homePage = new HomePage(driver);

        boolean isFound = homePage.isProductFound(productTitle);
        if (isFound) {
            homePage.openProduct(productTitle);
            waitFor().until(ExpectedConditions.urlContains("products/"));
            Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("products/"));
            return;
        }
        Assert.fail("Search does not work correctly");
    }
}
