package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CompleteCheckoutPage extends PagesBase {

    public CompleteCheckoutPage(WebDriver driver) {

        super(driver);
    }

    private List<WebElement> getAllProducts() {

        return  driver.findElements(By.cssSelector("div.overflow-y-auto.line-items.flex.flex-col.gap-5.pt-5.md:pt-0"));
    }

    public CheckoutProduct getProduct(int productNumber) {
        int index = productNumber - 1;
        List<WebElement> products = getAllProducts();
        if(index >= 0 && index < products.size()) {
            return new CheckoutProduct(products.get(index));
        }
        throw new IndexOutOfBoundsException("Product index " + index + " not found!");
    }


    public static class CheckoutProduct {

        private WebElement product;

        public CheckoutProduct(WebElement productElement) {
            this.product = productElement;
        }

        public String getTitle() {
            return product.findElement(By.cssSelector("p.font-bold.word-break")).getText();
        }

        public String getPrice() {

            WebElement priceContainer = product.findElement(By.cssSelector("div.font-semibold.text-sm.text-right"));
            String fullText = priceContainer.getText();

            try {
                if (fullText.contains("\n")) {
                    String[] prices = fullText.split("\n");
                    return prices[prices.length - 1].trim();
                }
                return fullText.trim();
            } catch (Exception e) {
                return "";
            }
        }

        private String[] splitProductColorAndSize() {
            WebElement productColorAndSize = driver.findElement(By.cssSelector("p.text-xs"));
            String text = productColorAndSize.getText();

            return text.split(",");
        }

        public String getProductColor() {

            String color = splitProductColorAndSize()[0];
            color = color.replace("Color:", "").trim();
            return color;
        }

        public String getProductSize() {

            String size = splitProductColorAndSize()[1];
            size = size.replace("Size:", "").trim();
            return size;
        }

        public double getSubTotal() {
            String value = product.findElement(By.xpath("//*[@id=\"checkout_summary\"]/div/div[1]/div[1]/span[2]")).getText();
            value = value.replace("$", "");
            return Double.parseDouble(value);
        }

        public int getQuantity() {
            String Q = product.findElement(By.cssSelector("span.rounded-full.text-xs")).getText();
            return  Integer.parseInt(Q);
        }
    }


    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div/p/strong")
    public WebElement orderID;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div/h4")
    public WebElement successMessage;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div/div[2]/div[2]/span[2]")
    public WebElement orderStatus;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div/div[3]/p")
    public WebElement emailAddress;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div/div[3]/div/div[1]/p[1]")
    public WebElement shippingDetails;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div/div[3]/div/div[1]/p[2]")
    public WebElement phoneNumber;

}
