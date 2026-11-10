
package com.rajat.automation.testdata;

import java.util.Random;

public class AddressTestData {

    private final String addressLabel;
    private final String addressLine;
    private final String city;
    private final String postalCode;
    private final String countryCode;

    private AddressTestData(
            String addressLabel,
            String addressLine,
            String city,
            String postalCode,
            String countryCode) {

        this.addressLabel = addressLabel;
        this.addressLine = addressLine;
        this.city = city;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public static AddressTestData generateAddress() {

        Random random = new Random();

        // 1. Generate unique address label using timestamp
        String timestamp = String.valueOf(System.currentTimeMillis());
        String addressLabel = "Home_" + timestamp;

        // 2. Generate random address line
        int streetNumber = random.nextInt(9000) + 1000;
        String addressLine = "Street_" + streetNumber;

        // 3. Generate random city using an array
        String[] cities = {"Bhubaneswar", "Cuttack", "Puri", "Rourkela"};

        String city = cities[random.nextInt(cities.length)];

        // 4. Generate random six-digit postal code
        int postalNumber = random.nextInt(900000) + 100000;
        String postalCode = String.valueOf(postalNumber);

        // 5. Generate random country code using an array
        String[] countryCodes = {"IN", "US", "GB", "DE"};

        String countryCode = countryCodes[random.nextInt(countryCodes.length)];

        return new AddressTestData(addressLabel, addressLine, city, postalCode, countryCode);
    }

    // Getters

    public String getAddressLabel() {
        return addressLabel;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
