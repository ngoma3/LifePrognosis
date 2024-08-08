import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import java.util.UUID;

public class Main {
    private static UserManagement userManagement = UserManagement.getInstance();
    private static User currentUser;  

    public static void main(String[] args) {
  
        userManagement.loadUsers();
        try {
            // Path to the Bash script
            String scriptPath = "bash/create_first_admin.sh";
            String firstName = "Super";
            String lastName = "Admin";
            String email = "admin@gmail.com";
            String password = "123";
            String uuid = UUID.randomUUID().toString();
            String salt = PasswordUtil.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath,firstName,lastName,email, hashedPassword, salt, uuid);
            processBuilder.redirectErrorStream(true);
            
            Process process = processBuilder.start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                // Success - create the admin object
                Admin admin = new Admin(firstName, lastName, email, hashedPassword, salt);
                userManagement.addSuperAdmin(admin);
                // System.out.println("Admin created successfully.");
            } 
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                // clearScreen();
                System.out.println("Welcome to the System");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                String option = reader.readLine();

                switch (option) {
                    case "1":
                        login(reader);
                        break;
                    case "2":
                        register(reader);
                        break;
                    case "3":
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("bash", "-c", "clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void login(BufferedReader reader) throws IOException {
        System.out.print("Enter email: ");
        String email = reader.readLine();
        System.out.print("Enter password: ");
        String password = reader.readLine();
        // System.out.println(password);
        
        User user = userManagement.getUserByEmail(email);
        if (user != null && PasswordUtil.verifyPassword(password, user.getSalt(), user.getPassword())) {
            currentUser = user;
            System.out.println("Login successful!");

            if (user.getRole() == UserRole.ADMIN) {
                adminMenu(reader);
            } else if (user.getRole() == UserRole.PATIENT) {
                patientMenu(reader);
            }
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void register(BufferedReader reader) throws IOException {
        System.out.print("Enter your email: ");
        String email = reader.readLine();
    
        // Check if email is in the onboarding list
        List<String> onboardingList = FileUtil.readLines("data/onboarding-list.txt");
        if (onboardingList.contains(email)) {
            System.out.println("Your email is found in the onboarding list. Please complete your registration.");
    
            // Collect additional user details
            System.out.print("Enter your first name: ");
            String firstName = reader.readLine();
            System.out.print("Enter your last name: ");
            String lastName = reader.readLine();
            System.out.print("Enter your password: ");
            String password = reader.readLine();
    
            // Hash the password
            String salt = PasswordUtil.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
    
            // Create a new Patient instance
            Patient newPatient = new Patient(firstName, lastName, email, hashedPassword, salt, null, false, null, false, null, null);
            
            // Add the new patient to the user management
            userManagement.addUser(newPatient);
    
            // Inform the user about successful registration
            System.out.println("Registration successful! You can now log in.");
        } else {
            System.out.println("Your email is not found in the onboarding list. Please register your email first.");
        }
    }
    

    private static void adminMenu(BufferedReader reader) throws IOException {
        while (true) {
            clearScreen();
            System.out.println("Admin Dashboard");
            System.out.println("1. Add Admin");
            System.out.println("2. Initiate Patient Registration");
            System.out.println("3. Download User CSV");
            System.out.println("4. Download Analytics CSV");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            String option = reader.readLine();

            switch (option) {
                case "1":
                    addAdmin(reader);
                    break;
                case "2":
                    initiatePatientRegistration(reader);
                    break;
                case "3":
                    downloadUserCSV();
                    break;
                case "4":
                    downloadAnalyticsCSV();
                    break;
                case "5":
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addAdmin(BufferedReader reader) throws IOException {
        System.out.print("Enter first name: ");
        String firstName = reader.readLine();
        System.out.print("Enter last name: ");
        String lastName = reader.readLine();
        System.out.print("Enter email: ");
        String email = reader.readLine();
        System.out.print("Enter password: ");
        String password = reader.readLine();

        String salt = PasswordUtil.getSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        Admin admin = new Admin(firstName, lastName, email, hashedPassword, salt);
        userManagement.addUser(admin);
        System.out.println("Admin added successfully.");
    }

    private static void initiatePatientRegistration(BufferedReader reader) throws IOException {
        System.out.print("Enter patient email: ");
        String email = reader.readLine();
        FileUtil.appendLine("data/onboarding-list.txt", email);
        System.out.println("Patient registration initiated.");
    }

    private static void downloadUserCSV() {
        try {
            Process process = new ProcessBuilder("bash/create_csv.sh").start();
            process.waitFor();
            System.out.println("User CSV downloaded.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void downloadAnalyticsCSV() {
        try {
            Process process = new ProcessBuilder("bash/create_csv.sh").start();
            process.waitFor();
            System.out.println("Analytics CSV downloaded.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void patientMenu(BufferedReader reader) throws IOException {
        while (true) {
            // clearScreen();
            System.out.println("Patient Dashboard");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            String option = reader.readLine();

            switch (option) {
                case "1":
                    clearScreen();
                    viewProfile();
                    break;
                case "2":
                    clearScreen();
                    editProfile(reader);
                    break;
                case "3":
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewProfile() {
        if (currentUser == null) {
            System.out.println("No user is currently logged in.");
            return;
        }
    
        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;
            System.out.println("+------------------------------------+------------------------------------+");
            System.out.println("|               Field                |               Value                |");
            System.out.println("+------------------------------------+------------------------------------+");
            System.out.printf("| %-34s | %-34s |%n", "First Name", (patient.getFirstName() != null ? patient.getFirstName() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Last Name", (patient.getLastName() != null ? patient.getLastName() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Email", (patient.getEmail() != null ? patient.getEmail() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Birth Date", (patient.getBirthDate() != null ? patient.getBirthDate() : "Not provided"));
            System.out.printf("| %-34s | %-34s |%n", "Has Chronic Disease", (patient.isHasChronicDisease() ? "Yes" : "No"));
            System.out.printf("| %-34s | %-34s |%n", "Chronic Disease Start Date", (patient.getChronicDiseaseStartDate() != null ? patient.getChronicDiseaseStartDate() : "N/A"));
            System.out.printf("| %-34s | %-34s |%n", "Vaccinated", (patient.isVaccinated() ? "Yes" : "No"));
            System.out.printf("| %-34s | %-34s |%n", "Vaccination Date", (patient.getVaccinationDate() != null ? patient.getVaccinationDate() : "N/A"));
            System.out.printf("| %-34s | %-34s |%n", "Country", (patient.getCountry() != null ? patient.getCountry() : "Not provided"));
            
            // Lifespan computation
            if (patient.isHasChronicDisease() && patient.getChronicDiseaseStartDate() != null && patient.getVaccinationDate() != null && patient.getCountry() != null) {
                double lifespan = calculateRemainingLifespan(patient);
                System.out.printf("| %-34s | %-34s |%n", "Estimated Remaining Lifespan (Years)", String.format("%.2f", lifespan));
            } else {
                System.out.printf("| %-34s | %-34s |%n", "Estimated Remaining Lifespan (Years)", "Insufficient data to calculate");
            }
    
            System.out.println("+------------------------------------+------------------------------------+");
        } else {
            System.out.println("The current user is not a patient.");
        }
    }
    
    private static double calculateRemainingLifespan(Patient patient) {
        double averageLifespan = CountrySearchUtil.getCountryLifeExpectancy(patient.getCountry());
        int age = LocalDate.now().getYear() - patient.getBirthDate().getYear();
        double remainingLifespan = averageLifespan - age;
    
        if (!patient.isVaccinated()) {
            return Math.min(remainingLifespan, 5);
        }
    
        int delayYears = patient.getVaccinationDate().getYear() - patient.getChronicDiseaseStartDate().getYear();
        remainingLifespan *= Math.pow(0.9, delayYears + 1); // Reduce by 10% for each year of delay
    
        return remainingLifespan;
    }
    
    private static void editProfile(BufferedReader reader) throws IOException {
        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;
    
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
                patient.setBirthDate(LocalDate.parse(birthDateStr));
            }
    
            System.out.print("Enter new chronic disease status (true/false) (leave blank to keep current): ");
            String hasChronicDiseaseStr = reader.readLine();
            if (!hasChronicDiseaseStr.isEmpty()) {
                patient.setHasChronicDisease(Boolean.parseBoolean(hasChronicDiseaseStr));
            }
    
            System.out.print("Enter new chronic disease start date (YYYY-MM-DD) (leave blank to keep current): ");
            String chronicDiseaseStartDateStr = reader.readLine();
            if (!chronicDiseaseStartDateStr.isEmpty()) {
                patient.setChronicDiseaseStartDate(LocalDate.parse(chronicDiseaseStartDateStr));
            }
    
            System.out.print("Enter new vaccination status (true/false) (leave blank to keep current): ");
            String vaccinatedStr = reader.readLine();
            if (!vaccinatedStr.isEmpty()) {
                patient.setVaccinated(Boolean.parseBoolean(vaccinatedStr));
            }
    
            System.out.print("Enter new vaccination date (YYYY-MM-DD) (leave blank to keep current): ");
            String vaccinationDateStr = reader.readLine();
            if (!vaccinationDateStr.isEmpty()) {
                patient.setVaccinationDate(LocalDate.parse(vaccinationDateStr));
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
            // Update the user-store.txt with new general user data
            List<String> userStoreLines = FileUtil.readLines("data/user-store.txt");
            boolean updated = false;
            for (int i = 0; i < userStoreLines.size(); i++) {
                String[] parts = userStoreLines.get(i).split(",");
                if (parts[0].equals(patient.getUuid())) {  // Match by UUID
                    userStoreLines.set(i, String.join(",", patient.getUuid(), patient.getFirstName(), patient.getLastName(),
                            patient.getEmail(), patient.getPassword(), patient.getSalt(), patient.getRole().toString()));
                    updated = true;
                    break;
                }
            }
            if (updated) {
                FileUtil.writeLines("data/user-store.txt", userStoreLines);
            }
    
            // Update or add new entry in health-data.txt with patient-specific data
            List<String> healthDataLines = FileUtil.readLines("data/health-data.txt");
            updated = false;
            for (int i = 0; i < healthDataLines.size(); i++) {
                String[] parts = healthDataLines.get(i).split(",");
                if (parts[0].equals(patient.getUuid())) {  // Match by UUID
                    healthDataLines.set(i, String.join(",", patient.getUuid(), patient.getBirthDate() != null ? patient.getBirthDate().toString() : "",
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
    }
    
    
}
