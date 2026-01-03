package Tests.Regression.User;

import Base.TestBase;
import Components.HeaderComponent;
import Data.TestData;
import Pages.AccountPage;
import Pages.SignupPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;

public class AddNewAddressTest extends TestBase {
    SignupPage signupPage;
    HeaderComponent header;
    AccountPage accountPage;

    String email = TestData.generateEmail();
    String password = TestData.generateNumericPassword();
    String firstName = TestData.generateFirstName();
    String lastName = TestData.generateLastName();
    String countryName = "United States";
    String address = TestData.generateAddress();
    String apartment = TestData.generateApartment();
    String state = TestData.generateUSState();
    String city = TestData.generateCity();
    String postalCode = TestData.generatePostalCodeByState(state);
    String phoneNumber = TestData.generatePhoneNumber();


    @Test(priority = 1)
    public void signupUser() {

        driver.navigate().to("https://demo.spreecommerce.org/user/sign_up");
        waitFor().until(ExpectedConditions.urlContains("https://demo.spreecommerce.org/user/sign_up"));

        signupPage = new SignupPage(driver);

        signupPage.signUpUser(email, password);

        header = new HeaderComponent(driver);
        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "WELCOME! YOU HAVE SIGNED UP SUCCESSFULLY.");
    }

    @Test(dependsOnMethods = {"signupUser"})
    public void addNewAddress() {

        header = new HeaderComponent(driver);
        header.openAccount();
        waitFor().until(ExpectedConditions.urlContains("/account"));

        accountPage = new AccountPage(driver);
        accountPage.openAddresses();
        waitFor().until(ExpectedConditions.urlContains("/account/addresses"));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains("/account/addresses"));

        accountPage.addAddress();
        waitFor().until(ExpectedConditions.visibilityOf(accountPage.selectCountry));
        accountPage.addNewAddress(countryName, firstName, lastName, address,
                apartment, city, state, postalCode, phoneNumber);

        waitFor().until(ExpectedConditions.visibilityOf(header.successMessage));
        Assert.assertEquals(header.successMessage.getText(), "ADDRESS HAS BEEN SUCCESSFULLY CREATED.");
    }
}
