package Components;

import Base.PagesBase;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class HeaderComponent extends PagesBase {

    public HeaderComponent(WebDriver driver) {

        super(driver);
        PagesBase.driver = driver;
    }

    @FindBy(xpath = "//*[@id=\"flashes\"]/div/div/div/p")
    public WebElement successMessage;

    @FindBy(id = "open-search")
    public WebElement openSearchButton;

    public void openSearch() {

        clickElementJS(openSearchButton);
    }

    @FindBy(id = "q")
    public WebElement searchBar;

    @FindBy(css = "button.text-sm.font-semibold.absolute.top-2.right-2.uppercase.h-6.tracking-widest")
    WebElement clearSearch;

    public void searchProduct(String productName) {

        setElementText(searchBar, productName);
        searchBar.sendKeys(Keys.ENTER);
    }

    public void clearSearch() {

        clearSearch.click();
    }


    @FindBy(id = "block-37886")
    WebElement shopAll;

    public void openShopAll() {

        shopAll.click();
    }

    @FindBy(id = "block-37887")
    WebElement fashion;

    public void openFashion() {

        fashion.click();
    }

    @FindBy(xpath = "//*[@id=\"block-37887\"]/div/div/div/div[1]/a")
    WebElement womanFashion;

    @FindBy(xpath = "//*[@id=\"block-37887\"]/div/div/div/div[2]/a")
    WebElement menFashion;

    @FindBy(xpath = "//*[@id=\"block-37887\"]/div/div/div/div[3]/a")
    WebElement accessories;

    public void openWomanFashion() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(womanFashion).click().perform();
    }

    public void openMenFashion() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(menFashion).click().perform();
    }
    public void openAccessories() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(accessories).click().perform();
    }

    @FindBy(id = "block-37888")
    WebElement wellness;

    public void openWellness() {

        clickElementJS(wellness);
    }

    @FindBy(xpath = "//*[@id=\"block-37888\"]/div/div/div/div[1]/a")
    WebElement Fitness;

    @FindBy(xpath = "//*[@id=\"block-37888\"]/div/div/div/div[2]/a")
    WebElement Relaxation;

    @FindBy(xpath = "//*[@id=\"block-37888\"]/div/div/div/div[3]/a")
    WebElement mentalStimulation;

    @FindBy(xpath = "//*[@id=\"block-37888\"]/div/div/div/div[4]/a")
    WebElement Nutrition;

    public void openFitness() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(Fitness).click().perform();
    }

    public void openRelaxation() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(Relaxation).click().perform();
    }

    public void openMentalStimulation() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(mentalStimulation).click().perform();
    }

    public void openNutrition() {

        Actions actions = new Actions(driver);
        actions.moveToElement(fashion).moveToElement(Nutrition).click().perform();
    }

    @FindBy(id = "block-37889")
    WebElement newArrivals;

    public void openNewArrivals() {

        clickElementJS(newArrivals);
    }

    @FindBy(id = "block-37890")
    WebElement sale;

    public void openSale() {

        clickElementJS(sale);
    }

    @FindBy(css = "button.flex.gap-2.items-center")
    WebElement currencyAndLanguageContainer;



    public void openCurrencyAndLanguageContainer() {

        clickElementJS(currencyAndLanguageContainer);
    }

    @FindBy(id = "switch_to_currency")
    WebElement selectCurrency;

    public void selectDollar() {

        Select select = new Select(selectCurrency);
        select.selectByVisibleText("Euro (EUR)");
    }

    public void selectEuro() {

        Select select = new Select(selectCurrency);
        select.selectByVisibleText("United States Dollar (USD)");
    }

    @FindBy(id = "switch_to_locale")
    WebElement selectLanguage;

    public void selectEnglish() {

        Select select = new Select(selectLanguage);
        select.selectByVisibleText("English (US)");
    }

    public void selectDeutsch() {

        Select select = new Select(selectLanguage);
        select.selectByVisibleText("Deutsch (DE)");
    }

    @FindBy(css = "input.btn-primary")
    WebElement saveButton;

    public void save() {

        saveButton.click();
    }

    @FindBy(css = "a[href='/account']")
    WebElement myAccount;

    public void openAccount() {

        clickElementJS(myAccount);
    }

    @FindBy(id = "wishlist-icon")
    WebElement wishlist;

    public void openWishlist() {

        clickElementJS(wishlist);
    }

    @FindBy(id = "wishlist-icon-count")
    public WebElement wishlistCount;

    @FindBy(css = "a[href='/cart']")
    WebElement cartIcon;

    public void openCart() {
        clickElementJS(cartIcon);
    }
    @FindBy(id = "site-logo")
    WebElement siteLogo;

    public void backHome() {

        clickElementJS(siteLogo);
    }
}
