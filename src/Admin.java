import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


public class Admin extends User {
    private static UserManagement userManagement = UserManagement.getInstance();

    // Constructor with UUID
    public Admin(String firstName, String lastName, String email, String password, String salt, String uuid,GenderType gender) {
        super(firstName, lastName, email, password, UserRole.ADMIN, salt, uuid,gender);
    }

    // Constructor without UUID, generates one
    public Admin(String firstName, String lastName, String email, String password, String salt,GenderType gender) {
        this(firstName, lastName, email, password, salt, UUID.randomUUID().toString(),gender);
    }

    @Override
    public void viewDashboard(BufferedReader reader) throws IOException {
        Main.clearScreen();
        while (true) {
            
            System.out.println(Main.cyanText + Main.boldText + "=================================" + Main.reset);
System.out.println(Main.cyanText + Main.boldText + "         Admin Dashboard         " + Main.reset);
System.out.println(Main.cyanText + Main.boldText + "=================================" + Main.reset);
System.out.println();
System.out.println(Main.yellowText + "       1. Add Admin               " + Main.reset);
System.out.println(Main.yellowText + "       2. Initiate Patient Registration" + Main.reset);
System.out.println(Main.yellowText + "       3. Download User CSV       " + Main.reset);
System.out.println(Main.yellowText + "       4. Download Analytics CSV  " + Main.reset);
System.out.println(Main.yellowText + "       5. View users              " + Main.reset);
System.out.println(Main.yellowText + "       6. Logout                  " + Main.reset);
System.out.println();
System.out.println(Main.cyanText + Main.boldText + "=================================" + Main.reset);
System.out.print("       Choose an option: " + Main.reset);
            String option = reader.readLine();

            switch (option) {
                case "1":
                Main.clearScreen();
                    addAdmin(reader);
                    break;
                case "2":
                Main.clearScreen();
                    initiatePatientRegistration(reader);
                    break;
                case "3":
                Main.clearScreen();
                    downloadUserCSV();
                    break;
                case "4":
                // Main.clearScreen();
                    downloadAnalyticsCSV();
                    break;
                case "5":
                    Main.clearScreen();
                    viewUsers(reader);
                    break;
                case "6":
                    // Log out the admin and exit the loop
                    Main.setCurrentUser(null);
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addAdmin(BufferedReader reader) throws IOException {
    System.out.print("Enter first name: ");
    String firstName = reader.readLine();
    System.out.print("Enter last name: ");
    String lastName = reader.readLine();
    System.out.print("Enter email: ");
    String email = reader.readLine();
    GenderType gender = null;
boolean validChoice = false;

while (!validChoice) {
    System.out.println("Select your gender:");
    System.out.println("1. Male");
    System.out.println("2. Female");
    System.out.print("Enter choice: ");
    String genderInput = reader.readLine();

    switch (genderInput) {
        case "1":
            gender = GenderType.MALE;
            validChoice = true;
            break;
        case "2":
            gender = GenderType.FEMALE;
            validChoice = true;
            break;
        default:
            System.out.println("Invalid choice. Please try again.");
            break;
    }
}
    
    // Hide password input and ask for confirmation
    String password;
    String confirmPassword;
    do {
        password = readPassword("Enter password: ");
        confirmPassword = readPassword("Confirm password: ");
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Please try again.");
        }
    } while (!password.equals(confirmPassword));

    String salt = PasswordUtil.getSalt();
    String hashedPassword = PasswordUtil.hashPassword(password, salt);

    Admin admin = new Admin(firstName, lastName, email, hashedPassword, salt,gender);
    UserManagement.getInstance().addUser(admin);
    System.out.println("Admin added successfully.");
}

// Method to read password with masking
private String readPassword(String prompt) throws IOException {
    Console console = System.console();
    if (console != null) {
        char[] passwordChars = console.readPassword(prompt);
        return new String(passwordChars);
    } else {
        // Fallback if System.console() is not available (e.g., in some IDEs)
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
private void viewUsers(BufferedReader reader) throws IOException {
    
    List<User> users = userManagement.getUsers(); // Load users from the data source
    int currentPage = 0;
    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) users.size() / pageSize);

    while (true) {
        Main.clearScreen();
        // System.out.println(Main.cyanText + Main.boldText + "========== Users List ==========" + Main.reset);
        System.out.println(Main.cyanText + Main.boldText + "================== Search Results ==================" + Main.reset);
        System.out.println(Main.yellowText + String.format("%-5s %-20s %-25s %-10s %-15s %-20s %-20s %-15s %-20s %-15s", 
            "No.", "Name", "Email", "Gender", "Birthdate", "Chronic Disease", "Chronic Disease Start", 
            "Vaccinated", "Vaccination Date", "Country") + Main.reset);
        System.out.println(Main.cyanText + "==============================================================================================" 
            + "==============================================================================================" + Main.reset);

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, users.size());

        for (int i = start; i < end; i++) {
            User user = users.get(i);
            // System.out.println((i + 1) + ". " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
            if (user instanceof Patient) {
                Patient patient = (Patient) user;
                System.out.println(String.format("%-5s %-20s %-25s %-10s %-15s %-20s %-20s %-15s %-20s %-15s", 
                (i + 1) + ".", user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getGender(),patient.getBirthDate(), patient.isHasChronicDisease(), 
                patient.getChronicDiseaseStartDate() != null ? patient.getChronicDiseaseStartDate() : "N/A", 
                patient.isVaccinated(), patient.getVaccinationDate() != null ? patient.getVaccinationDate() : "N/A", patient.getCountry()));
        
            }
            if (user instanceof Admin) {
                // Admin admin = (Admin) user;
                System.out.println(String.format("%-5s %-20s %-25s %-10s %-15s %-20s %-20s %-15s %-20s %-15s", 
                (i + 1) + ".", user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getGender(), 
                 " ", 
               " ", " ", " ", 
               " "," "));
        
            }
        }

        System.out.println();
        System.out.println(Main.cyanText + Main.boldText + "==============================" + Main.reset);
        System.out.println("Page " + (currentPage + 1) + " of " + totalPages);
        System.out.println("Options: (n)ext, (p)revious, (s)earch, (b)ack to Dashboard");
        System.out.print("Choose an option: ");
        String input = reader.readLine();

        if (input.equalsIgnoreCase("n") && currentPage < totalPages - 1) {
            currentPage++;
        } else if (input.equalsIgnoreCase("p") && currentPage > 0) {
            currentPage--;
        } else if (input.equalsIgnoreCase("s")) {
            System.out.print("Enter search term: ");
            String searchTerm = reader.readLine().trim().toLowerCase();
            List<User> searchResults = users.stream()
                .filter(user -> user.getFirstName().toLowerCase().contains(searchTerm)
                        || user.getLastName().toLowerCase().contains(searchTerm)
                        || user.getEmail().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
            if (searchResults.isEmpty()) {
                System.out.println("No users found matching '" + searchTerm + "'. Press Enter to continue...");
                reader.readLine();
            } else {
                displaySearchResults(searchResults, reader);
            }
        } else if (input.equalsIgnoreCase("b")) {
            break;
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }
}
private void displaySearchResults(List<User> searchResults, BufferedReader reader) throws IOException {
    int currentPage = 0;
    int pageSize = 10;
    int totalPages = (int) Math.ceil((double) searchResults.size() / pageSize);

    while (true) {
        Main.clearScreen();
        System.out.println(Main.cyanText + Main.boldText + "================== Search Results ==================" + Main.reset);
        System.out.println(Main.yellowText + String.format("%-5s %-20s %-25s %-10s %-15s %-20s %-20s %-15s %-20s %-15s", 
            "No.", "Name", "Email", "Gender", "Birthdate", "Chronic Disease", "Chronic Disease Start", 
            "Vaccinated", "Vaccination Date", "Country") + Main.reset);
        System.out.println(Main.cyanText + "==============================================================================================" 
            + "==============================================================================================" + Main.reset);


        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, searchResults.size());

        for (int i = start; i < end; i++) {
            User user = searchResults.get(i);
            if (user instanceof Patient) {
                Patient patient = (Patient) user;
                System.out.println(String.format("%-5s %-20s %-25s %-10s %-15s %-20s %-20s %-15s %-20s %-15s", 
                (i + 1) + ".", user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getGender(),patient.getBirthDate(), patient.isHasChronicDisease(), 
                patient.getChronicDiseaseStartDate() != null ? patient.getChronicDiseaseStartDate() : "N/A", 
                patient.isVaccinated(), patient.getVaccinationDate() != null ? patient.getVaccinationDate() : "N/A", patient.getCountry()));
        
            }
            if (user instanceof Admin) {
                // Admin admin = (Admin) user;
                System.out.println(String.format("%-5s %-20s %-25s %-10s %-15s %-20s %-20s %-15s %-20s %-15s", 
                (i + 1) + ".", user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getGender(), 
                 " ", 
               " ", " ", " ", 
               " "," "));
        
            }
            // System.out.println(String.format("%-5s %-20s %-25s %-15s %-10s %-15s %-10s %-20s %-20s %-20s %-20s %-20s", 
            //     (i + 1) + ".", user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getUuid(), user.getGender(), 
            //     user.getRole(), user.getBirthDate(), user.isHasChronicDisease(), 
            //     user.getChronicDiseaseStartDate() != null ? user.getChronicDiseaseStartDate() : "N/A", 
            //     user.isVaccinated(), user.getVaccinationDate() != null ? user.getVaccinationDate() : "N/A", user.getCountry()));
        }

        System.out.println(Main.cyanText + "======================================================" 
            + "======================================================" 
            + "======================================================" + Main.reset);
        System.out.println("Page " + (currentPage + 1) + " of " + totalPages);
        System.out.println("Options: (n)ext, (p)revious, (b)ack to User List");
        System.out.print("Choose an option: ");
        String input = reader.readLine();

        if (input.equalsIgnoreCase("n") && currentPage < totalPages - 1) {
            currentPage++;
        } else if (input.equalsIgnoreCase("p") && currentPage > 0) {
            currentPage--;
        } else if (input.equalsIgnoreCase("b")) {
            break;
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }
}


    private void initiatePatientRegistration(BufferedReader reader) throws IOException {
        System.out.println("1. View Waiting List");
        System.out.println("2. Register Email");
        System.out.print("Choose an option: ");
        String option = reader.readLine();

        switch (option) {
            case "1":
                // View Waiting List
                Main.clearScreen();
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
            Main.clearScreen();
                // Register New Email
                System.out.print("Enter patient email: ");
                String email = reader.readLine();
                String uuid = Main.generateUUIDFromEmail(email);
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

    private void registerPatientFromWaitingList(String email, BufferedReader reader) throws IOException {
        System.out.println("Registering email: " + email);
        String uuid = Main.generateUUIDFromEmail(email);
        FileUtil.initiatePatientRegistration("data/user-store.txt", email, uuid);
        FileUtil.removeEmailFromWaitingList("data/onboarding-list.txt", email);
        System.out.println("Registration completed for email: " + email);
    }

    private void downloadUserCSV() {
        try {
            Process process = new ProcessBuilder("bash/create_csv.sh").start();
            process.waitFor();
            System.out.println("User CSV downloaded.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // private void downloadAnalyticsCSV() {
    //     try {
    //         List<Patient> patients = userManagement.getAllPatients();
    //         List<Double> lifespans = new ArrayList<>();
    //         Process process = new ProcessBuilder("bash/analytics_csv.sh").start();
    //         process.waitFor();
    //         System.out.println("Analytics CSV downloaded.");
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }
    private void downloadAnalyticsCSV() {
        try {
            List<Patient> patients = UserManagement.getInstance().getAllPatients();
    
            // Data structures to store analytics
            Map<String, List<Double>> countryLifespanMap = new HashMap<>();
            Map<String, Integer> countryPatientCountMap = new HashMap<>();
            Map<String, Integer> countryMaleCountMap = new HashMap<>();
            Map<String, Integer> countryFemaleCountMap = new HashMap<>();
            Map<String, Map<String, Integer>> countryAgeGroupMap = new HashMap<>();
    
            // Initialize total counts
            int totalMales = 0;
            int totalFemales = 0;
    
            for (Patient patient : patients) {
                double remainingLifespan = Patient.calculateRemainingLifespan(patient);
                if (remainingLifespan == 0) continue;
                String countryCode = patient.getCountry(); // This gives you the code
                String country = CountrySearchUtil.getCountryName(countryCode); // Use the country name
                int age = LocalDate.now().getYear() - patient.getBirthDate().getYear();
                GenderType gender = patient.getGender(); // Assuming getGender() returns GenderType
    
                String ageGroup = getAgeGroup(age);
    
                // Update lifespan statistics per country
                countryLifespanMap.computeIfAbsent(country, k -> new ArrayList<>()).add(remainingLifespan);
    
                // Update patient count per country
                countryPatientCountMap.put(country, countryPatientCountMap.getOrDefault(country, 0) + 1);
    
                // Update gender counts
                if (gender == GenderType.MALE) {
                    totalMales++;
                    countryMaleCountMap.put(country, countryMaleCountMap.getOrDefault(country, 0) + 1);
                } else if (gender == GenderType.FEMALE) {
                    totalFemales++;
                    countryFemaleCountMap.put(country, countryFemaleCountMap.getOrDefault(country, 0) + 1);
                }
    
                // Update age group counts per country
                countryAgeGroupMap.computeIfAbsent(country, k -> new HashMap<>())
                        .put(ageGroup, countryAgeGroupMap.get(country).getOrDefault(ageGroup, 0) + 1);
            }
    
            // Top 5 countries with the most patients
            List<Map.Entry<String, Integer>> sortedCountriesByPatients = countryPatientCountMap.entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                    .limit(5)
                    .collect(Collectors.toList()); // Changed to Collectors.toList()
    
            // Prepare arguments for the bash script
            List<String> scriptArgs = new ArrayList<>();
            scriptArgs.add("bash/analytics_csv.sh");
            scriptArgs.add(String.valueOf(totalMales));
            scriptArgs.add(String.valueOf(totalFemales));
    
            StringBuilder topCountriesSummary = new StringBuilder();
            for (Map.Entry<String, Integer> entry : sortedCountriesByPatients) {
                topCountriesSummary.append(entry.getKey())
                        .append(" (")
                        .append(entry.getValue())
                        .append(" patients), ");
            }
            if (topCountriesSummary.length() > 0) {
                topCountriesSummary.setLength(topCountriesSummary.length() - 2); // Remove trailing comma and space
            }
            scriptArgs.add(topCountriesSummary.toString());
    
            for (String country : countryLifespanMap.keySet()) {
                double totalLifespan = countryLifespanMap.get(country).stream().mapToDouble(Double::doubleValue).sum();
                double averageLifespan = totalLifespan / countryLifespanMap.get(country).size();
                double medianLifespan = calculateMedian(countryLifespanMap.get(country));
                double percentile90 = calculatePercentile(countryLifespanMap.get(country), 90);
    
                scriptArgs.add(country);
                scriptArgs.add(String.valueOf(averageLifespan));
                scriptArgs.add(String.valueOf(medianLifespan));
                scriptArgs.add(String.valueOf(percentile90));
                scriptArgs.add(String.valueOf(countryPatientCountMap.get(country)));
                scriptArgs.add(String.valueOf(countryMaleCountMap.getOrDefault(country, 0)));
                scriptArgs.add(String.valueOf(countryFemaleCountMap.getOrDefault(country, 0)));
                scriptArgs.add(countryAgeGroupMap.get(country).toString());
            }
    
            // Execute the bash script with all the arguments
            Process process = new ProcessBuilder(scriptArgs.toArray(new String[0])).start();
            process.waitFor();
    
            System.out.println("Statistics CSV generated.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    

private String getAgeGroup(int age) {
    if (age < 20) return "0-20";
    if (age < 40) return "20-40";
    if (age < 60) return "40-60";
    if (age < 80) return "60-80";
    return "80+";
}


private double calculateMedian(List<Double> values) {
    Collections.sort(values);
    int size = values.size();
    if (size % 2 == 0) {
        return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
    } else {
        return values.get(size / 2);
    }
}

private double calculatePercentile(List<Double> values, int percentile) {
    Collections.sort(values);
    int index = (int) Math.ceil(percentile / 100.0 * values.size());
    return values.get(index - 1);
}

}
