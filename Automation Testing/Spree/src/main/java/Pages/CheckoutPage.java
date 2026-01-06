package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class CheckoutPage extends PagesBase {

    public CheckoutPage(WebDriver driver) {
        super(driver);
        PagesBase.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    WebDriverWait wait;

    @FindBy(id = "order_ship_address_attributes_email")
    public WebElement emailBox;

    public void enterGuestEmail(String email) {

        setElementText(emailBox, email);
    }

    @FindBy(id="order_accept_marketing")
    public WebElement emailMeButton;

    public void activeEmailMe() {

        emailMeButton.click();
    }

    @FindBy(id = "order_signup_for_an_account")
    WebElement createAccountButton;

    public void createAccount() {
        clickElementJS(createAccountButton);
    }

    @FindBy(id = "order_ship_address_attributes_country_id")
    WebElement selectCountry;

    public void selectCountry(String countryName) {

        Select select = new Select(selectCountry);

        select.selectByVisibleText(countryName);
    }

    @FindBy(id = "order_ship_address_attributes_firstname")
    WebElement firstNameBox;

    @FindBy(id = "order_ship_address_attributes_lastname")
    WebElement lastNameBox;

    @FindBy(id = "order_ship_address_attributes_address1")
    WebElement addressBox;

    @FindBy(id = "order_ship_address_attributes_address2")
    WebElement apartmentBox;

    @FindBy(id = "order_ship_address_attributes_city")
    WebElement cityBox;

    @FindBy(id = "order_ship_address_attributes_state_id")
    WebElement stateSelect;

    public void selectState(String stateName) {

        Select select = new Select(stateSelect);

        select.selectByVisibleText(stateName);
    }

    @FindBy(id = "order_ship_address_attributes_zipcode")
    WebElement postalCodeBox;

    @FindBy(id = "order_ship_address_attributes_phone")
    WebElement phoneNumberBox;

    @FindBy(name = "button")
    public WebElement saveAndContinueButton;

    public void fillAddress(String countryName, String firstName, String lastName,
                            String address, String apartment , String city,
                            String state, String postalCode, String phoneNumber) {

        selectCountry(countryName);

        setElementText(firstNameBox, firstName);
        setElementText(lastNameBox, lastName);
        setElementText(addressBox, address);
        setElementText(apartmentBox, apartment);
        setElementText(cityBox, city);
        if (Objects.equals(countryName, "United States")) {
            selectState(state);
        }
        setElementText(postalCodeBox, postalCode);
        setElementText(phoneNumberBox, phoneNumber);
    }


    @FindBy(css = "p.font-bold")
    public WebElement fullName;

    @FindBy(css = "div.px-5 > p:nth-child(2)")
    public WebElement address;

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div[3]/div[2]/turbo-frame/div/div/ul/li[1]/div")
    public WebElement addedAddress;

    public void chooseAddedAddress() {

        clickElementJS(addedAddress);
    }


    public void clickSaveAndContinueButton() {

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll to the button
        js.executeScript("arguments[0].scrollIntoView({behavior:'auto',block:'center'});", saveAndContinueButton);

        clickElementJS(saveAndContinueButton);
    }

    @FindBy(css = "div.px-5")
    public WebElement shipEmail;

    @FindBy(linkText = "Edit")
    WebElement editAddressLink;

    public void editAddress() {

        editAddressLink.click();
    }

    @FindBy(name = "button")
    public WebElement deliverySaveAndContinueButton;

    public void selectStandard() {

        clickElementJS(deliverySaveAndContinueButton);
    }

    private List<WebElement> getDeliveryMethod() {
        return driver.findElements(By.cssSelector("input.custom-control-input.radio-input"));
    }

    public void selectPremium() {

        WebElement premiumDeliveryMethod = getDeliveryMethod().get(1);
        clickElementJS(premiumDeliveryMethod);
    }

    public void clickDeliverySaveAndContinueButton() {
        clickElementJS(deliverySaveAndContinueButton);
    }

    public void selectNextDay() {

        WebElement nextDayDeliveryMethod = getDeliveryMethod().get(2);
        clickElementJS(nextDayDeliveryMethod);
    }

    @FindBy(xpath = "/html/body/div[2]/div/div[1]/div/div/div[2]/div[3]/div[1]/div[2]/p")
    public WebElement deliveryMethod;


    @FindBy(id = "checkout-payment-submit")
    WebElement payNowButton;

    // ------------------ Main Payment IFrame ------------------
    private WebElement paymentFrame() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("iframe[name^='__privateStripeFrame'][title='Secure payment input frame']")
        ));
    }

    // ------------------ Input Fields Locators (in side iframe) ------------------
    private final By cardNumberInput = By.name("number");
    private final By expiryInput = By.name("expiry");
    private final By cvcInput = By.name("cvc");

    // ------------------ Input Methods ------------------
    public void enterCardNumber(String cardNumber) {
        driver.switchTo().frame(paymentFrame());
        Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberInput)))
                .sendKeys(cardNumber);
        driver.switchTo().defaultContent();
    }

    public void enterExpiryDate(String expiry) {
        driver.switchTo().frame(paymentFrame());
        Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(expiryInput)))
                .sendKeys(expiry);
        driver.switchTo().defaultContent();
    }

    public void enterCVC(String cvc) {
        driver.switchTo().frame(paymentFrame());
        Objects.requireNonNull(wait.until(ExpectedConditions.visibilityOfElementLocated(cvcInput)))
                .sendKeys(cvc);
        driver.switchTo().defaultContent();
    }

    // ------------------ Helper Method ------------------
    public void enterCardDetails(String cardNumber, String expiry, String cvc) {
        enterCardNumber(cardNumber);
        enterExpiryDate(expiry);
        enterCVC(cvc);
    }

    // ------------------ Submit Payment ------------------
    public void clickPayNow() {

        wait.until(ExpectedConditions.elementToBeClickable(payNowButton));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll to the button
        js.executeScript("arguments[0].scrollIntoView({behavior:'auto',block:'center'});", payNowButton);

        clickElementJS(payNowButton);
    }


    @FindBy(css = "div.custom-control.custom-radio.px-5.py-4.flex.items-center")
    public WebElement addedPayment;

    public void chooseAddedPayment() {

        clickElementJS(addedPayment);
    }

    @FindBy(id = "summary-order-total")
    public WebElement totalPrice;


}

