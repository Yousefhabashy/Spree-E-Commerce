package Pages;

import Base.PagesBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class WishlistPage extends PagesBase {

    public WishlistPage(WebDriver driver) {
        super(driver);
        PagesBase.driver= driver;
    }

    @FindBy(css = "div.grid.lg\\:grid-cols-3")
    public WebElement wishlistContainer;

    private List<WebElement> getAllProducts() {

        return  driver.findElements(By.cssSelector("div.w-full.flex.justify-between.flex-col"));
    }

    public wishListProduct getProduct(int productNumber) {
        int index = productNumber - 1;
        List<WebElement> products = getAllProducts();
        if(index >= 0 && index < products.size()) {
            return  new wishListProduct(products.get(index));
        }
        throw  new IndexOutOfBoundsException("Product index " + index + " not found!");
    }


    public static class wishListProduct {

        private WebElement product;

        public wishListProduct(WebElement productElement) {
            this.product = productElement;
        }

        public String getTitle() {
            try {
                // Try different selectors
                List<WebElement> links = product.findElements(By.tagName("a"));
                for (WebElement link : links) {
                    String text = link.getText().trim();
                    if (!text.isEmpty() && !text.equalsIgnoreCase("Remove from wishlist")) {
                        return text;
                    }
                }
                // Fallback: try any link with text
                return product.findElement(By.cssSelector("a")).getText().trim();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        public String getPrice() {
            try {
                // Try to find sale price first
                return product.findElement(By.cssSelector("p.text-danger")).getText().trim();
            } catch (Exception e) {
                // If no sale, get regular price
                try {
                    return product.findElement(By.cssSelector("div.mt-1 p")).getText().trim();
                } catch (Exception ex) {
                    return "";
                }
            }
        }

        public String getColor() {

            try {
                List<WebElement> attributes = product.findElements(By.cssSelector("div.border-default.border-t.py-2.flex.gap-1.uppercase.tracking-widest.text-sm"));
                WebElement colorDiv = attributes.getFirst();
                String color = colorDiv.getText().replace("COLOR: ", "");
                return color.toUpperCase();
            }
            catch (Exception e) {
                return "Not found";
            }
        }

        public String getSize() {
            try {
                List<WebElement> attributes = product.findElements(By.cssSelector("div.border-default.border-t.py-2.flex.gap-1.uppercase.tracking-widest.text-sm"));
                WebElement sizeDiv = attributes.get(1);
                String size = sizeDiv.getText().replace("SIZE: ", "");
                return size.toUpperCase();
            }
            catch (Exception e) {
                return "Not found";
            }
        }

        public void  removeFromWishlist() {
            WebElement removeFromWishlist = driver.findElement(By.cssSelector("button.btn-icon[aria-label='Remove from wishlist']"));
            clickElementJS(removeFromWishlist);
        }

        public void  addToCart() {
            WebElement addToCart = driver.findElement(By.cssSelector("button.btn-primary.btn-icon.w-full"));
            clickElementJS(addToCart);
        }
    }
}
