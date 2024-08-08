public class Country {
    private String countryName;
    private String englishShortName;
    private String frenchShortName;
    private String alpha2Code;
    private String alpha3Code;
    private String code;
    private double lifeExpectancy;

    public Country(String countryName, String englishShortName, String frenchShortName, 
                   String alpha2Code, String alpha3Code, String code, double lifeExpectancy) {
        this.countryName = countryName;
        this.englishShortName = englishShortName;
        this.frenchShortName = frenchShortName;
        this.alpha2Code = alpha2Code;
        this.alpha3Code = alpha3Code;
        this.code = code;
        this.lifeExpectancy = lifeExpectancy;
    }

    @Override
    public String toString() {
        return countryName + " (" + alpha2Code + ", " + alpha3Code + ", " + code + ")";
    }

    // Getters for all fields
    public String getCountryName() {
        return countryName;
    }

    public String getEnglishShortName() {
        return englishShortName;
    }

    public String getFrenchShortName() {
        return frenchShortName;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public String getCode() {
        return code;
    }

    public double getLifeExpectancy() {
        return lifeExpectancy;
    }
}
