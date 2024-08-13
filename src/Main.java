import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import java.util.UUID;
import java.io.Console;

public class Main {
    private static UserManagement userManagement = UserManagement.getInstance();
    private static User currentUser;  
    public static final String RESET = "\033[0m";

    // ANSI escape codes for background colors
    public static final String BLACK_BACKGROUND = "\033[40m";
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String PURPLE_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String WHITE_BACKGROUND = "\033[47m";

    public static void main(String[] args) throws InterruptedException{
  
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
        // String text = "This text is centered!";
        // String asciiArt = 
        //     "  #####  \n" +
        //     " #     # \n" +
        //     " #       \n" +
        //     " #  #### \n" +
        //     " #     # \n" +
        //     " #     # \n" +
        //     "  #####  ";

        // Determine terminal width
        // int terminalWidth = getTerminalWidth();
        
        // Print centered text
        // printCenteredText(text, terminalWidth);
        
        // Print centered ASCII art
        // printCenteredText(asciiArt, terminalWidth);
        // System.out.println(BLUE_BACKGROUND + "This text has a blue background!" + RESET);
        // System.out.println(RED_BACKGROUND + "This text has a red background!" + RESET);
        // System.out.println(GREEN_BACKGROUND + "This text has a green background!" + RESET);
        // System.out.println("\033[1mThis text will be bold.\033[0m"+ terminalWidth);
        // // Reset back to the default background color
        // System.out.println("This text has the default background color.");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String message = "Welcome to the Lifespan Estimation Tool...";
    for (char c : message.toCharArray()) {
        System.out.print(c);
        Thread.sleep(100); // Adjust speed of typing
    }
    System.out.println();
    // System.out.print("Loading: ");
    // for (int i = 0; i <= 20; i++) {
    //     System.out.print("[");
    //     for (int j = 0; j < 20; j++) {
    //         if (j < i) {
    //             System.out.print("#");
    //         } else {
    //             System.out.print(" ");
    //         }
    //     }
    //     System.out.print("] " + (i * 5) + "%");
    //     Thread.sleep(100); // Adjust speed of animation
    //     if (i < 20) {
    //         System.out.print("\r"); // Move cursor back to start of line
    //     }
    // }
    // System.out.println("\nLoading Complete!");
    // String[] spinner = {"|", "/", "-", "\\"};
    // for (int i = 0; i < 20; i++) {
    //     System.out.print("\rCalculating... " + spinner[i % spinner.length]);
    //     Thread.sleep(200); // Adjust speed of animation
    // }
    // System.out.println("\rCalculation Complete!    ");
            while (true) {
                // clearScreen();
                System.out.println("Welcome to the Life Prognosis System");
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
    public static int getTerminalWidth() {
        Console console = System.console();
        if (console != null) {
            // Get terminal size using ANSI escape code
            System.out.println("Invalid option. Please try again."+console.readLine("\033[6n"));
            return Integer.parseInt(console.readLine("\033[6n")) - 1;
        } else {
            // Default width (fallback)
            System.out.println("Invalid option. ");
            return 80;
        }
    }

    public static void printCenteredText(String text, int terminalWidth) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            int padding = (terminalWidth - line.length()) / 2;
            System.out.printf("%" + padding + "s%s%n", "", line);
        }
    }
    public static String generateUUIDFromEmail(String email) {
        // Generate UUID based on the email's byte array
        UUID uuid = UUID.nameUUIDFromBytes(email.getBytes());
        return uuid.toString();
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
        while (true) {
            clearTerminal();
    
            System.out.println("===== Registration Menu =====");
            System.out.println("1. Finalize Registration");
            System.out.println("2. Registration Request");
            System.out.println("3. Exit");
            System.out.println("==============================");
            System.out.print("Choose an option: ");
            String option = reader.readLine();
    
            switch (option) {
                case "1":
                    clearTerminal();
                    System.out.println("===== Finalize Registration =====");
                    System.out.print("Enter your email: ");
                    String email = reader.readLine();
                    System.out.print("Enter your UUID: ");
                    String uuid = reader.readLine();
    
                    // Check if the email and UUID exist together in user-store
                    boolean emailAndUuidExist = FileUtil.checkEmailAndUuidExist("data/user-store.txt", email, uuid);
    
                    if (emailAndUuidExist) {
                        System.out.print("Enter your first name: ");
                        String firstName = reader.readLine();
                        System.out.print("Enter your last name: ");
                        String lastName = reader.readLine();
                        System.out.print("Enter your password: ");
                        String password = reader.readLine();
    
                        // Hash the password
                        String salt = PasswordUtil.getSalt();
                        String role = "PATIENT";
                        String hashedPassword = PasswordUtil.hashPassword(password, salt);
    
                        // Create a new Patient instance
                        Patient newPatient = new Patient(firstName, lastName, email, hashedPassword, salt, uuid, null, false, null, false, null, null);
    
                        // Update the user information in the user-store file
                        FileUtil.updateUserLine("data/user-store.txt", uuid, newPatient);
    
                        clearTerminal();
                        System.out.println("===== Registration Successful =====");
                        System.out.println("You can now log in.");
                        System.out.println("====================================");
                        return;
                    } else {
                        clearTerminal();
                        System.out.println("===== Error =====");
                        System.out.println("The email and UUID combination was not found.");
                        System.out.println("Please try again.");
                        System.out.println("=================");
                    }
                    break;
    
                case "2":
                    clearTerminal();
                    System.out.println("===== Registration Request =====");
                    System.out.print("Enter your email: ");
                    String requestEmail = reader.readLine();
    
                    // Check if the email exists in the onboarding list
                    boolean emailInOnboarding = FileUtil.checkEmailExists("data/onboarding-list.txt", requestEmail);
    
                    if (!emailInOnboarding) {
                        FileUtil.appendEmailToFile("data/onboarding-list.txt", requestEmail);
                        clearTerminal();
                        System.out.println("===== Request Submitted =====");
                        System.out.println("You are on the waiting list to be registered by an admin.");
                        System.out.println("=============================");
                    } else {
                        clearTerminal();
                        System.out.println("===== Already Registered =====");
                        System.out.println("Your email is already on the onboarding list.");
                        System.out.println("Please wait for an admin to complete your registration.");
                        System.out.println("===============================");
                    }
    
                    // Returning to the login page after a few seconds
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return;
    
                case "3":
                    clearTerminal();
                    System.out.println("Registration exited.");
                    return;
    
                default:
                    clearTerminal();
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    // Utility method to clear the terminal
    private static void clearTerminal() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error clearing the terminal.");
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
        System.out.println("1. View Waiting List");
        System.out.println("2. Register Email");
        System.out.print("Choose an option: ");
        String option = reader.readLine();
    
        switch (option) {
            case "1":
                // View Waiting List
                List<String> waitingList = FileUtil.getWaitingList("data/onboarding-list.txt");
                if (waitingList.isEmpty()) {
                    System.out.println("No emails in the waiting list.");
                } else {
                    System.out.println("Emails in Waiting List:");
                    for (int i = 0; i < waitingList.size(); i++) {
                        System.out.println((i + 1) + ". " + waitingList.get(i));
                    }
                    System.out.println("\n1. Register All");
                    System.out.println("2. Register One by One");
                    System.out.print("Choose an option: ");
                    String registerOption = reader.readLine();
    
                    if ("1".equals(registerOption)) {
                        // Register All
                        for (String email : waitingList) {
                            registerPatientFromWaitingList(email, reader);
                        }
                    } else if ("2".equals(registerOption)) {
                        // Register One by One
                        System.out.print("Enter the number of the email you want to register: ");
                        int emailIndex = Integer.parseInt(reader.readLine()) - 1;
                        if (emailIndex >= 0 && emailIndex < waitingList.size()) {
                            registerPatientFromWaitingList(waitingList.get(emailIndex), reader);
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    }
                }
                break;
    
            case "2":
                // Register New Email
                System.out.print("Enter patient email: ");
                String email = reader.readLine();
                String uuid = generateUUIDFromEmail(email);
                FileUtil.initiatePatientRegistration("data/user-store.txt", email, uuid);
                System.out.println("Patient registration initiated with");
                System.out.println("Email: " + email);
                System.out.println("UUID: " + uuid);
                break;
    
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }
    private static void registerPatientFromWaitingList(String email, BufferedReader reader) throws IOException {
        System.out.println("Registering email: " + email);
        String uuid = generateUUIDFromEmail(email);
        FileUtil.initiatePatientRegistration("data/user-store.txt", email, uuid);
        FileUtil.removeEmailFromWaitingList("data/onboarding-list.txt", email);
        System.out.println("Registration completed for email: " + email);
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
    
    
    private static double calculateRemainingLifespan(Patient patient) {
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
                    userStoreLines.set(i, String.join(",", patient.getUuid(),patient.getEmail(), patient.getFirstName(), patient.getLastName(),
                             patient.getPassword(), patient.getSalt(), patient.getRole().toString()));
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
