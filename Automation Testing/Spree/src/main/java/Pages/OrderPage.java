package Pages;

import Base.PagesBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OrderPage extends PagesBase {

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(css = "div.font-medium.uppercase")
    public WebElement productTitle;

    @FindBy(xpath = "/html/body/div[3]/div[2]/ul/li/div[2]/div/div[1]/div[2]/div[2]")
    public WebElement productPrice;

    @FindBy(xpath = "/html/body/div[3]/div[2]/ul/li/div[2]/div/div[1]/div[2]/div[3]/div[1]/div/div[2]")
    public WebElement productColor;

    @FindBy(xpath = "/html/body/div[3]/div[2]/ul/li/div[2]/div/div[1]/div[2]/div[3]/div[2]/div")
    public WebElement productSize;

    @FindBy(xpath = "/html/body/div[3]/div[2]/ul/li/div[2]/div/div[1]/div[2]/div[3]/div[3]")
    public WebElement productQuantity;
}
