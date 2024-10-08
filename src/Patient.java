import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;


public class Patient extends User {
    private LocalDate birthDate;
    private boolean hasChronicDisease;
    private LocalDate chronicDiseaseStartDate;
    private boolean vaccinated;
    private LocalDate vaccinationDate;
    private String country;

    // Constructor with UUID
    public Patient(String firstName, String lastName, String email, String password, String salt, String uuid,
                   LocalDate birthDate, boolean hasChronicDisease, LocalDate chronicDiseaseStartDate,
                   boolean vaccinated, LocalDate vaccinationDate, String country,GenderType gender) {
        super(firstName, lastName, email, password, UserRole.PATIENT, salt, uuid, gender);
        this.birthDate = birthDate;
        this.hasChronicDisease = hasChronicDisease;
        this.chronicDiseaseStartDate = chronicDiseaseStartDate;
        this.vaccinated = vaccinated;
        this.vaccinationDate = vaccinationDate;
        this.country = country;
    }

    // Constructor without UUID, generates one
    public Patient(String firstName, String lastName, String email, String password, String salt,
                   LocalDate birthDate, boolean hasChronicDisease, LocalDate chronicDiseaseStartDate,
                   boolean vaccinated, LocalDate vaccinationDate, String country, GenderType gender) {
        this(firstName, lastName, email, password, salt, UUID.randomUUID().toString(),
             birthDate, hasChronicDisease, chronicDiseaseStartDate, vaccinated, vaccinationDate, country, gender);
    }

    // Getters and Setters
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean isHasChronicDisease() {
        return hasChronicDisease;
    }

    public void setHasChronicDisease(boolean hasChronicDisease) {
        this.hasChronicDisease = hasChronicDisease;
    }

    public LocalDate getChronicDiseaseStartDate() {
        return chronicDiseaseStartDate;
    }

    public void setChronicDiseaseStartDate(LocalDate chronicDiseaseStartDate) {
        this.chronicDiseaseStartDate = chronicDiseaseStartDate;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
public void viewDashboard(BufferedReader reader) throws IOException {
    Main.clearScreen();
    while (true) {
        
        System.out.println(Main.cyanText + Main.boldText + "=================================" + Main.reset);
System.out.println(Main.cyanText + Main.boldText + "        Patient Dashboard        " + Main.reset);
System.out.println(Main.cyanText + Main.boldText + "=================================" + Main.reset);
System.out.println();
System.out.println(Main.yellowText + "        1. View Profile           " + Main.reset);
System.out.println(Main.yellowText + "        2. Edit Profile           " + Main.reset);
System.out.println(Main.yellowText + "        3. Logout                 " + Main.reset);
System.out.println();
System.out.println(Main.cyanText + Main.boldText + "=================================" + Main.reset);
System.out.print( "        Choose an option: " + Main.reset);
        String option = reader.readLine();

        switch (option) {
            case "1":
                // Main.clearScreen();
                viewProfile();
                break;
            case "2":
                Main.clearScreen();
                editProfile(reader);
                break;
            case "3":
                Main.setCurrentUser(null); // Use Main.setMain.getCurrentUser() instead of Main.getCurrentUser() = null
                return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}


private void editProfile(BufferedReader reader) throws IOException {
    if (Main.getCurrentUser() instanceof Patient) {
        Patient patient = (Patient) Main.getCurrentUser();

        // Collect updated information from the user
        System.out.print("Enter new first name (leave blank to keep current): ");
        String firstName = reader.readLine();
        if (!firstName.isEmpty()) {
            patient.setFirstName(firstName);
        }

        System.out.print("Enter new last name (leave blank to keep current): ");
        String lastName = reader.readLine();
        if (!lastName.isEmpty()) {
            patient.setLastName(lastName);
        }

        System.out.print("Enter new birth date (YYYY-MM-DD) (leave blank to keep current): ");
        String birthDateStr = reader.readLine();
        if (!birthDateStr.isEmpty()) {
            try {
                patient.setBirthDate(LocalDate.parse(birthDateStr));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Skipping birth date update.");
            }
        }

        // Edit gender
        while (true) {
            System.out.print("Enter new gender (1. Male, 2. Female) (leave blank to keep current): ");
            String genderInput = reader.readLine();
            if (genderInput.isEmpty()) {
                break;
            }
            switch (genderInput) {
                case "1":
                    patient.setGender(GenderType.MALE);
                    break;
                case "2":
                    patient.setGender(GenderType.FEMALE);
                    break;
                default:
                    System.out.println("Invalid gender selection. Please try again.");
                    continue;
            }
            break;
        }

        // Edit password
        System.out.print("Do you want to update your password? (yes/no): ");
        String updatePassword = reader.readLine();
        if (updatePassword.equalsIgnoreCase("yes")) {
            while (true) {
                // System.out.print("Enter current password: ");
                String currentPassword = Main.readPassword("Enter current password: ");

                if (!PasswordUtil.verifyPassword(currentPassword, patient.getSalt(), patient.getPassword())) {
                    System.out.println("Current password is incorrect. Please try again.");
                    continue;
                }

                // System.out.print("Enter new password: ");
                String newPassword = Main.readPassword("Enter new password: ");
                // System.out.print("Confirm new password: ");
                String confirmPassword = Main.readPassword("Confirm new password: ");

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("Passwords do not match. Please try again.");
                } else {
                    patient.setPassword(newPassword);
                    break;
                }
            }
        }

        System.out.print("Enter new chronic disease status (true/false) (leave blank to keep current): ");
        String hasChronicDiseaseStr = reader.readLine();
        if (!hasChronicDiseaseStr.isEmpty()) {
            try {
                patient.setHasChronicDisease(Boolean.parseBoolean(hasChronicDiseaseStr));
            } catch (Exception e) {
                System.out.println("Invalid input for chronic disease status. Skipping update.");
            }
        }

        System.out.print("Enter new chronic disease start date (YYYY-MM-DD) (leave blank to keep current): ");
        String chronicDiseaseStartDateStr = reader.readLine();
        if (!chronicDiseaseStartDateStr.isEmpty()) {
            try {
                patient.setChronicDiseaseStartDate(LocalDate.parse(chronicDiseaseStartDateStr));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format for chronic disease start date. Skipping update.");
            }
        }

        System.out.print("Enter new vaccination status (true/false) (leave blank to keep current): ");
        String vaccinatedStr = reader.readLine();
        if (!vaccinatedStr.isEmpty()) {
            try {
                patient.setVaccinated(Boolean.parseBoolean(vaccinatedStr));
            } catch (Exception e) {
                System.out.println("Invalid input for vaccination status. Skipping update.");
            }
        }

        System.out.print("Enter new vaccination date (YYYY-MM-DD) (leave blank to keep current): ");
        String vaccinationDateStr = reader.readLine();
        if (!vaccinationDateStr.isEmpty()) {
            try {
                patient.setVaccinationDate(LocalDate.parse(vaccinationDateStr));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format for vaccination date. Skipping update.");
            }
        }

        System.out.println("Do you want to update the country? (yes/no): ");
        String updateCountry = reader.readLine();
        if (updateCountry.equalsIgnoreCase("yes")) {
            System.out.print("Enter search query for the new country: ");
            String countryQuery = reader.readLine();

            List<Country> countries = CountrySearchUtil.loadCountriesFromCSV("data/life-expectancy.csv");
            List<Country> results = CountrySearchUtil.searchCountries(countries, countryQuery);

            if (results.isEmpty()) {
                System.out.println("No countries found.");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    System.out.println((i + 1) + ". " + results.get(i));
                }

                System.out.print("Select a result by number: ");
                int selectedIndex = Integer.parseInt(reader.readLine()) - 1;

                if (selectedIndex >= 0 && selectedIndex < results.size()) {
                    Country selectedCountry = results.get(selectedIndex);
                    patient.setCountry(selectedCountry.getCode()); // Store the selected country's code
                    System.out.println("Selected country: " + selectedCountry);
                } else {
                    System.out.println("Invalid selection.");
                }
            }
        }

        // Continue with updating files as before...
        updateUserStore(patient);
        updateHealthData(patient);
    }
}

private void updateUserStore(Patient patient) throws IOException {
    List<String> userStoreLines = FileUtil.readLines("data/user-store.txt");
    boolean updated = false;
    for (int i = 0; i < userStoreLines.size(); i++) {
        String[] parts = userStoreLines.get(i).split(",");
        if (parts[0].equals(patient.getUuid())) {  // Match by UUID
            userStoreLines.set(i, String.join(",", patient.getUuid(), patient.getEmail(), patient.getFirstName(), patient.getLastName(),
                    patient.getPassword(), patient.getSalt(), patient.getRole().toString(),patient.getGender().toString()));
            updated = true;
            break;
        }
    }
    if (updated) {
        FileUtil.writeLines("data/user-store.txt", userStoreLines);
    }
}

private void updateHealthData(Patient patient) throws IOException {
    List<String> healthDataLines = FileUtil.readLines("data/health-data.txt");
    boolean updated = false;
    for (int i = 0; i < healthDataLines.size(); i++) {
        String[] parts = healthDataLines.get(i).split(",");
        if (parts[0].equals(patient.getUuid())) {  // Match by UUID
            healthDataLines.set(i, String.join(",", patient.getUuid(), 
                    patient.getBirthDate() != null ? patient.getBirthDate().toString() : "",
                    Boolean.toString(patient.isHasChronicDisease()), 
                    patient.getChronicDiseaseStartDate() != null ? patient.getChronicDiseaseStartDate().toString() : "",
                    Boolean.toString(patient.isVaccinated()), 
                    patient.getVaccinationDate() != null ? patient.getVaccinationDate().toString() : "",
                    patient.getCountry()));
            updated = true;
            break;
        }
    }
    if (!updated) {
        // Add new entry if it was not updated
        healthDataLines.add(String.join(",", patient.getUuid(), 
                patient.getBirthDate() != null ? patient.getBirthDate().toString() : "",
                Boolean.toString(patient.isHasChronicDisease()), 
                patient.getChronicDiseaseStartDate() != null ? patient.getChronicDiseaseStartDate().toString() : "",
                Boolean.toString(patient.isVaccinated()), 
                patient.getVaccinationDate() != null ? patient.getVaccinationDate().toString() : "",
                patient.getCountry()));
    }
    FileUtil.writeLines("data/health-data.txt", healthDataLines);
}



    private void viewProfile() {
        if (Main.getCurrentUser() == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
        Main.clearScreen();

        if (Main.getCurrentUser() instanceof Patient) {
            Patient patient = (Patient) Main.getCurrentUser();
            System.out.println("+------------------------------------+------------------------------------+");
            System.out.println("|               Field                |               Value                |");
            System.out.println("+------------------------------------+------------------------------------+");
            System.out.printf("| %-34s | %-34s |%n", "First Name", (patient.getFirstName() != null ? patient.getFirstName() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Last Name", (patient.getLastName() != null ? patient.getLastName() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Email", (patient.getEmail() != null ? patient.getEmail() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Gender", (patient.getGender() != null ? patient.getGender() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Birth Date", (patient.getBirthDate() != null ? patient.getBirthDate() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Has Chronic Disease", (patient.isHasChronicDisease() ? "Yes" : "No"));
            System.out.printf("| %-34s | %-34s |%n", "Chronic Disease Start Date", (patient.getChronicDiseaseStartDate() != null ? patient.getChronicDiseaseStartDate() : "N/A"));
            System.out.printf("| %-34s | %-34s |%n", "Vaccinated", (patient.isVaccinated() ? "Yes" : "No"));
            System.out.printf("| %-34s | %-34s |%n", "Vaccination Date", (patient.getVaccinationDate() != null ? patient.getVaccinationDate() : "N/A"));
            System.out.printf("| %-34s | %-34s |%n", "Country", (patient.getCountry() != null ? CountrySearchUtil.getCountryName(patient.getCountry()) : "Not provided"));

            System.out.println("+------------------------------------+------------------------------------+");

            // Lifespan computation
            if (patient.isHasChronicDisease() && patient.getChronicDiseaseStartDate() != null && patient.getVaccinationDate() != null && patient.getCountry() != null) {
                double lifespan = calculateRemainingLifespan(patient);
                int delayYears = patient.getVaccinationDate().getYear() - patient.getChronicDiseaseStartDate().getYear();

                // Calculate the number of months and round up the years
                int years = (int) Math.floor(lifespan);
                int months = (int) ((lifespan - years) * 12);
                int roundedYears = (int) Math.ceil(lifespan);

                System.out.println("\n===== Patient Lifespan Details =====\n");

                // Display when the patient was supposed to start medication
                System.out.println("1. Year Supposed to Start Medication: " + patient.getChronicDiseaseStartDate().getYear());

                // Display the year the patient actually started medication
                System.out.println("2. Year Started Medication: " + patient.getVaccinationDate().getYear());

                // Provide a summary of how the lifespan was reduced
                System.out.println("\n===== Summary of Lifespan Reduction =====");
                System.out.printf("1. Expected lifespan without HIV: %.2f years%n", CountrySearchUtil.getCountryLifeExpectancy(patient.getCountry()) - (LocalDate.now().getYear() - patient.getBirthDate().getYear()));
                System.out.printf("2. Initial reduction due to HIV: %.2f%%%n", 10.0);
                System.out.printf("3. Further reduction for each year of delay (%d years): %.2f%%%n", delayYears, 10.0 * (delayYears + 1));

                // Show lifespan in years and months
                System.out.println("\n===== Final Lifespan Estimation =====");
                System.out.printf("Estimated Remaining Lifespan: %d years and %d months%n", years, months);
                System.out.println("Or, rounded to the nearest year: " + roundedYears + " years");

                System.out.println("\n=====================================\n");
            } else {
                System.out.println("Insufficient data to estimate lifespan.");
            }

        } else {
            System.out.println("The current user is not a patient.");
        }
    }

    public static double calculateRemainingLifespan(Patient patient) {
        // Check if the patient's country is null
        if (patient.getCountry() == null || patient.getBirthDate() == null) {
            return 0;
        }
    
        double averageLifespan = CountrySearchUtil.getCountryLifeExpectancy(patient.getCountry());
        int age = LocalDate.now().getYear() - patient.getBirthDate().getYear();
        double remainingLifespan = averageLifespan - age;
    
        // Check if the patient is not vaccinated
        if (!patient.isVaccinated()) {
            return Math.min(remainingLifespan, 5);
        }
    
        // Calculate the number of years of delay in starting the ART drugs
        int delayYears = patient.getVaccinationDate().getYear() - patient.getChronicDiseaseStartDate().getYear();
    
        // Deduct 10% of remaining lifespan for each year of delay
        for (int i = 0; i <= delayYears; i++) {
            remainingLifespan *= 0.9; // Reduce by 10% for each year of delay
        }
    
        return remainingLifespan;
    }
    

}
