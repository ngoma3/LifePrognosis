import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CountrySearchUtil {

    private static List<Country> countries;

    public static List<Country> loadCountriesFromCSV(String filePath) {
        countries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 7) {
                    String countryName = parts[0];
                    String englishShortName = parts[1];
                    String frenchShortName = parts[2];
                    String alpha2Code = parts[3];
                    String alpha3Code = parts[4];
                    String code = parts[5];
                    double lifeExpectancy = Double.parseDouble(parts[6]);

                    countries.add(new Country(countryName, englishShortName, frenchShortName,
                            alpha2Code, alpha3Code, code, lifeExpectancy));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public static List<Country> searchCountries(List<Country> countries, String query) {
        List<Country> results = new ArrayList<>();
        for (Country country : countries) {
            if (country.getCountryName().toLowerCase().contains(query.toLowerCase()) ||
                country.getEnglishShortName().toLowerCase().contains(query.toLowerCase()) ||
                country.getFrenchShortName().toLowerCase().contains(query.toLowerCase()) ||
                country.getAlpha2Code().equalsIgnoreCase(query) ||
                country.getAlpha3Code().equalsIgnoreCase(query) ||
                country.getCode().equalsIgnoreCase(query)) {
                results.add(country);
            }
        }
        return results;
    }

    public static double getCountryLifeExpectancy(String countryIdentifier) {
        if (countries == null) {
            countries = loadCountriesFromCSV("data/life-expectancy.csv");
            // throw new IllegalStateException("Countries data has not been loaded. Please load countries from CSV first.");
        }

        for (Country country : countries) {
            if (country.getCountryName().equalsIgnoreCase(countryIdentifier) ||
                country.getEnglishShortName().equalsIgnoreCase(countryIdentifier) ||
                country.getAlpha2Code().equalsIgnoreCase(countryIdentifier) ||
                country.getAlpha3Code().equalsIgnoreCase(countryIdentifier) ||
                country.getCode().equalsIgnoreCase(countryIdentifier)) {
                return country.getLifeExpectancy();
            }
        }
        throw new IllegalArgumentException("Country not found with the given identifier: " + countryIdentifier);
    }
}
