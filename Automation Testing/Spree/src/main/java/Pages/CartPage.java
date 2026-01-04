package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Objects;


public class CartPage extends PagesBase {

    public CartPage(WebDriver driver) {

        super(driver);
        PagesBase.driver = driver;
    }

    @FindBy(xpath = "//*[@id=\"slideover-cart\"]/div[1]/button")
    public WebElement closeCartButton;

    public void closeCart() {

        clickElementJS(closeCartButton);
    }

    @FindBy(css = "p.mb-8.mt-4.uppercase.text-sm")
    public WebElement emptyCartMessage;

    @FindBy(id = "line-items")
    public WebElement productsContainer;

    private List<WebElement> getAllProducts() {

        return  driver.findElements(By.id("line-items"));
    }

    public CartProduct getProduct(int productNumber) {
        int index = productNumber - 1;
        List<WebElement> products = getAllProducts();
        if(index >= 0 && index < products.size()) {
            return  new CartProduct(products.get(index));
        }
        throw  new IndexOutOfBoundsException("Product index " + index + " not found!");
    }


    public static class CartProduct {

        private WebElement product;

        public CartProduct(WebElement productElement) {
            this.product = productElement;
        }

        public String getTitle() {
            return product.findElement(By.cssSelector("a.font-semibold.text-text")).getText().toUpperCase();
        }

        public String getPrice() {

            WebElement priceContainer = product.findElement(By.cssSelector("div.mb-2.text-sm"));
            try {

                WebElement salePrice = priceContainer.findElement(By.cssSelector("span.text-danger"));
                return salePrice.getText().trim();
            } catch (Exception e) {
                return priceContainer.getText().trim();
            }
        }

        public String getColor() {
            return Objects.requireNonNull(product.findElement(By.cssSelector("input.color-input")).getAttribute("value")).toUpperCase();
        }

        public String getSize() {
            return product.findElement(By.cssSelector("div.flex.flex-wrap.gap-2 > div:nth-child(2)")).getText();
        }

        public int getQuantity() {
            String value = product.findElement(By.cssSelector("input#line_item_quantity")).getAttribute("value");
            assert value != null;
            return Integer.parseInt(value);
        }

        public void increaseQuantity() {
            product.findElement(By.cssSelector("button.quantity-increase-button")).click();
        }

        public void decreaseQuantity() {
            product.findElement(By.cssSelector("button.quantity-decrease-button")).click();
        }
    }

    @FindBy(css = "span.shopping-cart-total-amount")
    public WebElement totalPrice;


    @FindBy(xpath = "//*[@id=\"cart_summary\"]/div/div[2]/div[2]/a")
    public WebElement checkoutButton;

    public void openCheckoutPage() {

        checkoutButton.click();
    }

    @FindBy(id = "gpay-button-online-api-id")
    WebElement checkoutWithGooglePay;

    public void checkoutWithGooglePay() {

        clickElementJS(checkoutWithGooglePay);
    }

    @FindBy(css = "button.LinkButton.LinkButton--showFocusIndicator")
    WebElement checkoutWithLink;

    public void checkoutWithLink() {

        clickElementJS(checkoutWithLink);
    }

}