package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class FilterPage extends PagesBase {

    public FilterPage(WebDriver driver) {

        super(driver);
        PagesBase.driver = driver;
    }

    public List<WebElement> productNumbers() {
        return driver.findElements(By.cssSelector("div.product-card.flex.h-full.flex-col.w-full.gap-2.relative"));
    }
    public boolean isFiltered(int number) {

        List<WebElement> productsFound = driver.findElements(By.cssSelector("div.product-card.flex.h-full.flex-col.w-full.gap-2.relative"));
        return productsFound.size() == number;
    }

    @FindBy(css = "a[href='#availability_filter']")
    public WebElement availability;

    public void selectAvailability() {

        clickElementJS(availability);
    }

    public int selectInStock() {

        List<WebElement> availabilityOptions = driver.findElements(
                By.cssSelector("div[data-accordion-id='availability_filter'] ul li label")
        );
        for (WebElement option : availabilityOptions) {
            String text = option.getText().trim();
            if(text.contains("In stock")) {
                WebElement numberSpan = option.findElement(By.cssSelector("span.opacity-50"));
                String textNumber = numberSpan.getText().replaceAll("[^0-9]", "");
                clickElementJS(option);
                return Integer.parseInt(textNumber);
            }
        }
        return -1;
    }

    public int selectOutStock() {

        List<WebElement> availabilityOptions = driver.findElements(
                By.cssSelector("div[data-accordion-id='availability_filter'] ul li label")
        );
        for (WebElement option : availabilityOptions) {
            String text = option.getText().trim();
            if(text.contains("Out of Stock")) {
                WebElement numberSpan = option.findElement(By.cssSelector("span.opacity-50"));
                String textNumber = numberSpan.getText().replaceAll("[^0-9]", "");
                clickElementJS(option);
                return Integer.parseInt(textNumber);
            }
        }
        return -1;
    }

    @FindBy(css = "a[href='#taxonomy_filter_39']")
    public WebElement category;

    public void selectCategory() {

        clickElementJS(category);
    }

    public int selectFashion() {

        List<WebElement> categoryOptions = driver.findElements(
                By.cssSelector("div[data-accordion-id='taxonomy_filter_39'] ul li label")
        );
        for (WebElement option : categoryOptions) {
            String text = option.getText().trim();
            if(text.contains("Fashion")) {
                WebElement numberSpan = option.findElement(By.cssSelector("span.opacity-50"));
                String textNumber = numberSpan.getText().replaceAll("[^0-9]", "");
                clickElementJS(option);
                return Integer.parseInt(textNumber);
            }
        }
        return -1;
    }

    public int selectWellness() {

        List<WebElement> categoryOptions = driver.findElements(
                By.cssSelector("div[data-accordion-id='taxonomy_filter_39'] ul li label")
        );
        for (WebElement option : categoryOptions) {
            String text = option.getText().trim();
            if(text.contains("Wellness")) {
                WebElement numberSpan = option.findElement(By.cssSelector("span.opacity-50"));
                String textNumber = numberSpan.getText().replaceAll("[^0-9]", "");
                clickElementJS(option);
                return Integer.parseInt(textNumber);
            }
        }
        return -1;
    }



    @FindBy(xpath = "//*[@id=\"slideover-filters\"]/form/nav/div[2]/button")
    public WebElement applyFilter;

    public void applyFilter() {

        Actions actions = new Actions(driver);
        actions.moveToElement(applyFilter).click().perform();
    }

    @FindBy(xpath = "//*[@id=\"slideover-filters\"]/form/nav/div[2]/a")
    public WebElement clearFilter;

    public void clearFilter() {

        clickElementJS(clearFilter);
    }
}

