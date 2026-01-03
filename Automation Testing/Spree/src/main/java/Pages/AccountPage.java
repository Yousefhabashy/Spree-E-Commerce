package Pages;

import Base.PagesBase;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import javax.xml.xpath.XPath;
import java.security.PublicKey;
import java.util.Objects;

public class AccountPage extends PagesBase {

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(linkText = "Orders & Returns")
    WebElement ordersHistory;

    public void openOrdersHistory() {
        ordersHistory.click();
    }

    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr/td[1]")
    public WebElement order1Number;


    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr/td[2]")
    public WebElement order1Date;


    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr/td[3]")
    public WebElement order1PaymentStatus;


    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr/td[5]")
    public WebElement order1Price;



    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr[2]/td[1]")
    public WebElement order2Number;


    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr[2]/td[2]")
    public WebElement order2Date;


    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr[2]/td[3]")
    public WebElement order2PaymentStatus;


    @FindBy(xpath="/html/body/div[3]/div[2]/table/tbody/tr[2]/td[5]")
    public WebElement order2Price;





    @FindBy(css = "a[href='/account/addresses']")
    WebElement addresses;

    public void openAddresses() {

        addresses.click();
    }

    @FindBy(css = "button.btn-primary.block.w-full.lg:w-auto")
    WebElement addNewAddress;

    @FindBy(id = "address_country_id")
    public WebElement selectCountry;

    @FindBy(id = "address_firstname")
    WebElement firstNameBox;

    @FindBy(id = "address_lastname")
    WebElement lastNameBox;

    @FindBy(id = "address_address1")
    WebElement addressBox;

    @FindBy(id = "address_address2")
    WebElement apartmentBox;

    @FindBy(id = "address_city")
    WebElement cityBox;

    @FindBy(id = "address_state_id")
    WebElement selectState;

    @FindBy(id = "address_zipcode")
    WebElement postalCodeBox;

    @FindBy(id = "address_phone")
    WebElement phoneNumberBox;

    @FindBy(css = "input.btn-primary.w-48")
    WebElement addAddressButton;

    public void addNewAddress() {

        addAddressButton.click();
    }

    @FindBy(css = "button.btn-primary.block")
    WebElement addAddress;
    public void addAddress() {

        clickElementJS(addAddress);
    }

    public  void addNewAddress(String countryName, String firstName, String lastName,
                               String address, String apartment, String city,
                               String state, String postalCode, String phoneNumber) {

        Select select = new Select(selectCountry);
        select.selectByVisibleText(countryName);

        setElementText(firstNameBox, firstName);
        System.out.println();
        setElementText(lastNameBox, lastName);
        System.out.println();
        setElementText(addressBox, address);
        System.out.println();

        setElementText(apartmentBox, apartment);
        System.out.println();
        setElementText(cityBox, city);

        if(Objects.equals(countryName, "United States")) {

            Select select1 = new Select(selectState);
            select1.selectByVisibleText(state);
        }

        setElementText(postalCodeBox, postalCode);
        setElementText(phoneNumberBox, phoneNumber);

        clickElementJS(addAddressButton);
    }


    @FindBy(css = "button.flex.p-2.max-lg:absolute.top-2.right-2")
    WebElement deleteAddress;

    @FindBy(css = "button.flex.gap-2.lg:p-2")
    WebElement editAddress;

    public void deleteAddress() {

        clickElementJS(deleteAddress);
    }

    public void editAddress() {

        clickElementJS(editAddress);
    }

    @FindBy(css = "a[href='/account/profile/edit']")
    WebElement personalDetails;

    public void openPersonalDetails() {
        clickElementJS(personalDetails);
    }

    @FindBy(linkText = "Change password")
    WebElement changePassword;

    @FindBy(id="user_current_password")
    WebElement currentPasswordBox;

    @FindBy(id="user_password")
    WebElement newPasswordBox;

    @FindBy(id="user_password_confirmation")
    WebElement confirmNewPasswordBox;

    @FindBy(css = "input.btn-primary.w-full.mb-6")
    WebElement updatePassword;

    public void openChangePassword() {

        changePassword.click();
    }

    public void changePassword( String oldPassword, String newPassword) {

        setElementText(currentPasswordBox, oldPassword);
        setElementText(newPasswordBox, newPassword);
        setElementText(confirmNewPasswordBox, newPassword);

        clickElementJS(updatePassword);
    }

    @FindBy(linkText = "Wishlist")
    WebElement wishlist;

    public void openWishlist() {

        clickElementJS(wishlist);
    }

    @FindBy(className = "btn-icon")
    WebElement removeProduct1FromWishlist;

    public void remove1FromWishlist() {

        clickElementJS(removeProduct1FromWishlist);
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }

    @FindBy(css = "a.uppercase.font-medium")
    public WebElement wishlistProductTitle;

    @FindBy(css = "div.flex.flex-col.gap-3 ")
    public WebElement wishlistProductPrice;



    @FindBy(xpath = "/html/body/div[3]/div[1]/div/form/button")
    WebElement logOut;

    public void logoutUser() {

        clickElementJS(logOut);
    }

}
