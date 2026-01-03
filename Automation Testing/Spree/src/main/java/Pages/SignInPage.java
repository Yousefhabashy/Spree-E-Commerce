package Pages;

import Base.PagesBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignInPage extends PagesBase {

    public SignInPage(WebDriver driver) {

        super(driver);
    }

    @FindBy(id = "user_email")
    WebElement emailBox;

    @FindBy(id = "user_password")
    WebElement passwordBox;

    @FindBy(id = "user_remember_me")
    WebElement rememberMe;

    @FindBy(id = "login-button")
    WebElement signInButton;

    public void loginUser(String email, String password, boolean remember) {

        setElementText(emailBox, email);
        setElementText(passwordBox, password);

        if(remember) rememberMe.click();

        signInButton.click();
    }


    @FindBy(linkText = "Forgot password?")
    WebElement forgotPasswordLink;

    public void forgotPassword() {

        forgotPasswordLink.click();
    }

    @FindBy(css = "input.btn.btn-primary.w-full")
    WebElement resetPassword;

    public void resetPasswordProcess(String email) {

        setElementText(emailBox, email);
        resetPassword.click();
    }

    @FindBy(linkText = "Sign Up")
    WebElement signUpLink;

    public void signUp() {

        signUpLink.click();
    }


    @FindBy(linkText = "Continue as a guest")
    public WebElement continueAsGuestButton;

    public void openGuestCheckout() {

        clickElementJS(continueAsGuestButton);
    }
}
