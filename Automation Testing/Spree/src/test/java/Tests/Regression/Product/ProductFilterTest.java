package Tests.Regression.Product;

import Base.TestBase;
import Components.HeaderComponent;
import Pages.FilterPage;
import Pages.HomePage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class ProductFilterTest extends TestBase {

    HomePage homePage;
    FilterPage filterPage;

    @Test(priority = 1)
    public void openShop() {

        isLoggedIn = false;
        HeaderComponent header = new HeaderComponent(driver);
        header.openShopAll();

        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));
    }

    @Test(priority = 2)
    public void filterInStock() {

        homePage = new HomePage(driver);
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.filterLink));
        homePage.openFilterLink();

        filterPage = new FilterPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(filterPage.availability));

        filterPage.selectAvailability();
        int numberInStock = filterPage.selectInStock();

        waitFor().until(ExpectedConditions.elementToBeClickable(filterPage.applyFilter));
        filterPage.applyFilter();

        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        Assert.assertEquals(filterPage.productNumbers().size(), numberInStock);
    }

    @Test(priority = 2)
    public void filterOutOfStock() {

        driver.navigate().refresh();
        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        homePage = new HomePage(driver);
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.filterLink));
        homePage.openFilterLink();

        filterPage = new FilterPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(filterPage.availability));

        filterPage.selectAvailability();
        int numberOutOfStock = filterPage.selectOutStock();

        waitFor().until(ExpectedConditions.elementToBeClickable(filterPage.applyFilter));
        filterPage.applyFilter();

        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        Assert.assertEquals(filterPage.productNumbers().size(), numberOutOfStock);
    }

    @Test(priority = 3)
    public void filterFashion() {

        driver.navigate().refresh();
        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        homePage = new HomePage(driver);
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.filterLink));
        homePage.openFilterLink();

        filterPage = new FilterPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(filterPage.category));

        int fashionProducts = filterPage.selectFashion();

        waitFor().until(ExpectedConditions.elementToBeClickable(filterPage.applyFilter));
        filterPage.applyFilter();

        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        Assert.assertEquals(filterPage.productNumbers().size(), fashionProducts);
    }

    @Test(priority = 4)
    public void filterWellness() {

        driver.navigate().refresh();
        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        homePage = new HomePage(driver);
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.filterLink));
        homePage.openFilterLink();

        filterPage = new FilterPage(driver);
        waitFor().until(ExpectedConditions.visibilityOf(filterPage.category));

        int wellnessProducts = filterPage.selectWellness();

        waitFor().until(ExpectedConditions.elementToBeClickable(filterPage.applyFilter));
        filterPage.applyFilter();

        waitFor().until(ExpectedConditions.urlContains("/products"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/products"));

        Assert.assertEquals(filterPage.productNumbers().size(), wellnessProducts);
    }
}
