package Pages;

import Base.PagesBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPage extends PagesBase {

    public SignupPage(WebDriver driver) {

        super(driver);
    }

    @FindBy(id = "user_email")
    WebElement emailBox;

    @FindBy(id = "user_password")
    WebElement passwordBox;

    @FindBy(id = "user_password_confirmation")
    WebElement confirmPasswordBox;

    @FindBy(css = "input.btn.btn-primary.w-full")
    WebElement signupButton;

    public void signUpUser(String email, String password) {

        setElementText(emailBox, email);
        setElementText(passwordBox, password);
        setElementText(confirmPasswordBox, password);

        signupButton.click();
    }

    @FindBy(linkText = "Login")
    WebElement loginLink;

    public void openLogin() {

        loginLink.click();
    }


    @FindBy(id = "errorExplanation")
    public WebElement errorMessage;
}
