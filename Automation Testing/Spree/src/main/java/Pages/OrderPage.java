package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class OrderPage extends PagesBase {

    public OrderPage(WebDriver driver) {

        super(driver);
        PagesBase.driver = driver;
    }

    @FindBy(xpath = "/html/body/div[3]/div[2]/table/tbody/tr[1]/td[1]/a")
    public WebElement orderLink;

    public void openOrderPage() {

        clickElementJS(orderLink);
    }

    @FindBy(css = "h1.text-xl.uppercase.font-medium.my-4 ")
    WebElement orderId;
    public String getOrderId() {
        return orderId.getText().replace("ORDER ", "");
    }

    @FindBy(css = "div.mb-1")
    public WebElement fullName;
    public String getFullName() {
        return fullName.getText().trim();
    }

    @FindBy(css = "div.street-address-line")
    WebElement street;
    public String getAddress() {
        return street.getText().trim();
    }

    @FindBy(css = "span.locality")
    WebElement city;
    public String getCity() {
        return city.getText().trim();
    }

    @FindBy(css = "span.postal-code")
    WebElement postalCode;
    public String getPostalCode() {
        return postalCode.getText().trim();
    }

    private List<WebElement> getTitleAndPrice() {
        return driver.findElements(By.cssSelector("div.font-medium"));
    }

    public String getProductTitle() {
        List<WebElement> elements =getTitleAndPrice();
        WebElement productTitle = elements.getFirst();
        return productTitle.getText().trim().toUpperCase();
    }

    public String getProductPrice() {
        List<WebElement> elements =getTitleAndPrice();
        WebElement productPrice = elements.get(1);
        return productPrice.getText().trim().toUpperCase();
    }

    @FindBy(css = "//div[.//div[text()='Yellow']]//input[@class='color-input']")
    WebElement productColor;
    public String getProductColor() {
        return productColor.getAttribute("value");
    }

    @FindBy(xpath = "div.h-[30px].border.border-default.px-2.inline-flex.items-center.hover:border-dashed.hover:border-primary.text-sm")
    WebElement productSize;
    public String getProductSize() {
        return productSize.getText().trim().toUpperCase();
    }

    @FindBy(xpath = "//div[contains(text(),'Quantity')]")
    WebElement productQuantity;
    public int getProductQuantity() {
        String quantityText = productQuantity.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(quantityText);
    }
    @FindBy(xpath = "/html/body/div[3]/div[2]/div/div[2]/div[1]/div[2]")
    WebElement productSubTotal;
    public String getProductSubTotal() {
        return productSubTotal.getText();
    }
}
