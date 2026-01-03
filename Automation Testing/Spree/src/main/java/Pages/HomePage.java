package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomePage extends PagesBase {

    public HomePage(WebDriver driver) {

        super(driver);
        PagesBase.driver = driver;
    }

    @FindBy(css = "div.product-card.flex.h-full.flex-col.w-full.gap-2.relative")
    List<WebElement> productCards;

    @FindBy(xpath = "//*[@id=\"product-323\"]/div[2]/h3")
    public WebElement productTitle;

    @FindBy(xpath = "//*[@id=\"product-323\"]/div[2]/div")
    public WebElement productPrice;

    public void openProductPage() {

        productTitle.click();
    }


    @FindBy(css = "div.product-card.flex.h-full.flex-col.w-full.gap-2.relative")
    public List<WebElement> homeProductCards;

    public WebElement getRandomProduct() {

        if(homeProductCards== null || homeProductCards.isEmpty()) {
            throw new RuntimeException("No products found!");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(homeProductCards.size());

        return homeProductCards.get(randomIndex);
    }

    public boolean isProductFound(String title) {
        List<WebElement> productsTitles = driver.findElements(
                By.cssSelector("h3.product-card-title")
        );

        for (WebElement productTitle : productsTitles) {
            if (productTitle.getText().trim().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }
    public void openProduct(String title) {
        List<WebElement> products = driver.findElements(By.cssSelector("div.product-card.flex.h-full.flex-col.w-full.gap-2.relative"));
        if(isProductFound(title)) {
            for (WebElement productCard : products) {
                WebElement product = productCard.findElement(By.cssSelector("h3.line-clamp-1.product-card-title"));
                if(product.getText().equalsIgnoreCase(title)) {
                    WebElement productLink = productCard.findElement(By.tagName("a"));
                    productLink.click();
                }
            }
        }
    }

    @FindBy(css = "button.uppercase.flex.gap-2.text-sm.items-center.font-semibold.py-3")
    public WebElement filterLink;

    public void openFilterLink() {

        clickElementJS(filterLink);
    }




    @FindBy(id = "dropdown-button")
    public WebElement sortList;


    public void openSortList() {

        clickElementJS(sortList);
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[2]")
    public WebElement sortByBestSelling;

    public void sortByBestSelling() {

        Actions actions = new Actions(driver);

        actions.moveToElement(sortByBestSelling).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[3]")
    public WebElement sortByAToZ;

    public void sortByAToZ() {

        Actions actions = new Actions(driver);

        actions.moveToElement(sortByAToZ).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[4]")
    public WebElement sortByZToA;

    public void sortByZToA() {

        Actions actions = new Actions(driver);

        actions.moveToElement(sortByZToA).click().perform();

    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[5]")
    public WebElement sortByPriceLowToHigh;

    public void sortByPriceLowToHigh() {
        Actions actions = new Actions(driver);

        actions.moveToElement(sortByPriceLowToHigh).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[6]")
    public WebElement sortByPriceHighToLow;

    public void sortByPriceHighToLow() {

        Actions actions = new Actions(driver);

        actions.moveToElement(sortByPriceHighToLow).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[7]")
    public WebElement newest;

    public void sortByNewest() {

        Actions actions = new Actions(driver);

        actions.moveToElement(newest).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/div/div/form/label[8]")
    public WebElement oldest;

    public void sortByOldest() {

        Actions actions = new Actions(driver);

        actions.moveToElement(oldest).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"dropdown-button\"]/button/span[2]")
    public WebElement sortMethod;

    public boolean isSortedAToZ() {

        List<String> productNames = new ArrayList<>();

        for (WebElement productCard : productCards) {

            String cardProductName = productCard.findElement(By.cssSelector("h3.line-clamp-1.product-card-title")).getText();
            productNames.add(cardProductName.toLowerCase());
        }
        List<String> sortedNames = new ArrayList<>(productNames);
        Collections.sort(sortedNames);

        return productNames.equals(sortedNames);
    }


    public boolean isSortedZToA() {

        List<String> productNames = new ArrayList<>();

        for (WebElement productCard : productCards) {

            String cardProductName = productCard.findElement(By.cssSelector("h3.line-clamp-1.product-card-title")).getText();
            productNames.add(cardProductName.toLowerCase());
        }
        List<String> sortedNames = new ArrayList<>(productNames);

        sortedNames.sort(Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        return productNames.equals(sortedNames);
    }



    private List<Double> getAllProductPrices() {
        List<Double> productPrices = new ArrayList<>();

        for (WebElement productCard : productCards) {
            String priceText = "";

            List<WebElement> discountedPrice = productCard.findElements(By.cssSelector("p.text-danger"));
            if (!discountedPrice.isEmpty()) {

                priceText = discountedPrice.getFirst().getText();
            } else {

                priceText = productCard.findElement(By.cssSelector("p:not(.line-through)")).getText();
            }

            priceText = priceText.replaceAll("[^0-9.]", "");
            productPrices.add(Double.parseDouble(priceText));
        }

        return productPrices;
    }


    public boolean isSortedByPriceLowToHigh() {

        List<Double> productPrices = getAllProductPrices();

        List<Double> sortedPrices = new ArrayList<>(productPrices);
        Collections.sort(sortedPrices);

        return productPrices.equals(sortedPrices);
    }

    public boolean isSortedByPriceHighToLow() {

        List<Double> productPrices = getAllProductPrices();

        List<Double> reversedPrices = new ArrayList<>(productPrices);
        reversedPrices.sort(Collections.reverseOrder());

        return productPrices.equals(reversedPrices);
    }
}
