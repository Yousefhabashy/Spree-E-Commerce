package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Objects;

public class ProductPage extends PagesBase {

    WebDriverWait wait;

    public ProductPage(WebDriver driver) {
        super(driver);
        PagesBase.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(css = "h1.text-2xl.uppercase.tracking-tight.font-medium")
    public WebElement productTitle;

    public String getTitle() {
        return productTitle.getText().trim();
    }

    public String getPrice() {

        WebElement priceContainer = driver.findElement(By.id("block-40849"));
        try {

            WebElement salePrice = priceContainer.findElement(By.cssSelector("p.inline.text-danger"));
            return salePrice.getText().trim();
        } catch (Exception e) {
            return priceContainer.getText().trim();
        }
    }

    @FindBy(css = "span.text-sm.leading-4.uppercase.tracking-widest")
    WebElement productColor;

    public String getColor() {
        return productColor.getText().replace("COLOR: ", "");
    }

    @FindBy(id = "quantity")
    public WebElement quantity;

    public int getQuantity() {

        return Integer.parseInt(Objects.requireNonNull(quantity.getAttribute("value")));
    }

    @FindBy(css = "button.increase-quantity")
    WebElement increaseQuantity;

    public void increaseQuantity() {

        clickElementJS(increaseQuantity);
    }

    @FindBy(css = "button.decrease-quantity")
    WebElement decreaseQuantity;

    public void decreaseQuantity() {

        clickElementJS(decreaseQuantity);
    }

    public void waitForQuantityToStabilize() {
        wait.until(driver -> {

            String first = quantity.getAttribute("value");
            try { Thread.sleep(200); } catch (Exception ignored) {}
            String second = quantity.getAttribute("value");
            assert first != null;
            return first.equals(second);
        });
    }

    @FindBy(id = "option-23-value")
    public WebElement chooseSizeButton;

    @FindBy(xpath = "//*[@id=\"product-variant-picker\"]/fieldset[2]/div/div[2]/div/label[1]")
    WebElement smallSize;

    @FindBy(xpath = "//*[@id=\"product-variant-picker\"]/fieldset[2]/div/div[2]/div/label[2]")
    WebElement mediumSize;

    @FindBy(xpath = "//*[@id=\"product-variant-picker\"]/fieldset[2]/div/div[2]/div/label[3]")
    WebElement largeSize;


    public void chooseSize(String size) {

        chooseSizeButton.click();

        if(size.equalsIgnoreCase("s")) {
            smallSize.click();
        }
        else if (size.equalsIgnoreCase("m")) {
            mediumSize.click();
        }
        else if (size.equalsIgnoreCase("l")) {
            largeSize.click();
        }
        else return;
    }

    @FindBy(xpath = "//*[@id=\"block-40852\"]/div/div[3]/div/button[1]")
    public WebElement addToWishlist;

    public void addToWishlist() {

        clickElementJS(addToWishlist);
    }
    @FindBy(css = "button.btn-primary.btn-icon.w-full.h-12.add-to-cart-button")
    public WebElement addToCartButton;

    public void addToCart() {

        clickElementJS(addToCartButton);
    }

    public boolean checkAvailable() {
        try {
            WebElement SPAN = addToCartButton.findElement(By.tagName("span"));
            return SPAN.getText().contains("CART");
        } catch (Exception e) {
            return false;
        }
    }
}
