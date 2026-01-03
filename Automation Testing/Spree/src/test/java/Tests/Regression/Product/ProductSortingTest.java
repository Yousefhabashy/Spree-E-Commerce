package Tests.Regression.Product;

import Base.TestBase;
import Components.HeaderComponent;
import Pages.HomePage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class ProductSortingTest extends TestBase {

    HomePage homePage;

    @Test(priority = 1)
    public void openWomanFashion() {

        HeaderComponent header = new HeaderComponent(driver);
        header.openWomanFashion();
        waitFor().until(ExpectedConditions.urlContains("/fashion/women"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/fashion/women"));
    }

    @Test( priority = 2)
    public void sortAToz() {

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.sortByAToZ));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.sortByAToZ));

        homePage.sortByAToZ();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Relevance")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Alphabetically, A-Z");
        boolean isSorted = homePage.isSortedAToZ();
        Assert.assertTrue(isSorted);
    }

    @Test(priority = 3)
    public void sortByPriceLowToHigh(){

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.sortByPriceLowToHigh));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.sortByPriceLowToHigh));

        homePage.sortByPriceLowToHigh();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Alphabetically, A-Z")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Price (low-high)");

        boolean isSorted = homePage.isSortedByPriceLowToHigh();
        Assert.assertTrue(isSorted);
    }

    @Test(priority = 4)
    public void sortByPriceHighToLow(){

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.sortByPriceHighToLow));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.sortByPriceHighToLow));

        homePage.sortByPriceHighToLow();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Price (low-high)")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Price (high-low)");

        boolean isSorted = homePage.isSortedByPriceHighToLow();
        Assert.assertTrue(isSorted);
    }

    @Test(priority = 5)
    public void sortZToA(){

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.sortByZToA));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.sortByZToA));

        homePage.sortByZToA();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Price (high-low)")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Alphabetically, Z-A");

        boolean isSorted = homePage.isSortedZToA();
        Assert.assertTrue(isSorted);
    }

    @Test(priority = 6)
    public void sortByBestSelling(){

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.sortByBestSelling));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.sortByBestSelling));

        homePage.sortByBestSelling();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Alphabetically, Z-A")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Best Selling");
    }

    @Test(priority = 7)
    public void sortByNewest(){

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.newest));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.newest));

        homePage.sortByNewest();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Best Selling")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Newest");
    }

    @Test(priority = 8)
    public void sortByOldest(){

        homePage = new HomePage(driver);
        homePage.openSortList();

        waitFor().until(ExpectedConditions.visibilityOf(homePage.oldest));
        waitFor().until(ExpectedConditions.elementToBeClickable(homePage.oldest));

        homePage.sortByOldest();

        waitFor().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(homePage.sortMethod, "Newest")));
        Assert.assertEquals(homePage.sortMethod.getText(), "Oldest");
    }
}



