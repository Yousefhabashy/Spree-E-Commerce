package Utils;

import org.openqa.selenium.WebElement;

public class ParseDouble {

    public static double parsePrice(WebElement priceElement) {

        String text = priceElement.getText().replace("$", "").trim();

        String[] prices = text.split("\\s+");

        double lowest = Double.MAX_VALUE;
        for (String p : prices) {
            double value = Double.parseDouble(p);
            if (value < lowest) {
                lowest = value;
            }
        }

        return lowest;
    }
}
