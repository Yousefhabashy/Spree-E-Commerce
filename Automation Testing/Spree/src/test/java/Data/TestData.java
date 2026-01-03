package Data;

import com.github.javafaker.Faker;

import java.util.List;
import java.util.Random;

public class TestData {

    static  Faker faker = new Faker();

    public static String generateFirstName() {

        return faker.name().firstName();
    }

    public static String generateLastName() {

        return faker.name().lastName();
    }

    public static String generateEmail() {

        return faker.internet().emailAddress();
    }

    public static String generatePassword() {

        return faker.name().firstName() +faker.number().digits(6);
    }

    public static String generateNumericPassword() {

        return faker.number().digits(9);
    }

    public static String generateCountryName() {
        return faker.country().name();
    }

    public static String generateAddress() {

        return faker.address().streetAddress();
    }

    public static String generateCity() {

        return faker.country().capital();
    }

    public static String generateApartment() {
        int apt = new Random().nextInt(500) + 1;
        return "Apt " + apt;
    }

    public static String generatePostalCodeByState(String state) {
        try {
            return faker.address().zipCodeByState(state);
        } catch (Exception e) {
            return faker.address().zipCode();
        }
    }
    public static String generatePostalCode() {

        return faker.address().zipCode();
    }

    public static String generateState() {
        return faker.country().capital();
    }

    private static final List<String> US_STATES = List.of(
            "Alabama", "Alaska", "Arizona", "Arkansas", "California",
            "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
            "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
            "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
            "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
            "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio",
            "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
            "South Carolina", "South Dakota", "Tennessee", "Texas",
            "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    );

    public static String generateUSState() {
        return US_STATES.get(new Random().nextInt(US_STATES.size()));
    }

    public static String generatePhoneNumber() {

        return faker.phoneNumber().phoneNumber();
    }

    // ===== Stripe Test Cards =====
    public static String generateVisaCard() {
        return "4242424242424242";
    }

    public static String generateMasterCard() {
        return "5555555555554444";
    }

    public static String generateVisaDebit() {
        return "4000056655665556";
    }

    public static String generateMasterDebit() {
        return "5200828282828210";
    }

    public static  String generateDeclinedCardNumber() {
        return "4413 8521 1584 3046";
    }

    public static String generateExpiry() {
        Random rand = new Random();

        String month = String.format("%02d", rand.nextInt(12) + 1);

        int year = rand.nextInt(7) + 26;

        return month + "/" + year;
    }

    public static String generateCVV() {

        return String.format("%03d", faker.number().numberBetween(0, 999));
    }

}
