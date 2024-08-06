import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;

public class Main {
    private static UserManagement userManagement = UserManagement.getInstance();
    private static User currentUser;  

    public static void main(String[] args) {
  
        userManagement.loadUsers();
        try {
            // Path to the Bash script
            String scriptPath = "bash/create_first_admin.sh";
            ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath);
            processBuilder.redirectErrorStream(true);
            
            Process process = processBuilder.start();
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                clearScreen();
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
            clearScreen();
            System.out.println("Patient Dashboard");
            System.out.println("1. View Profile");
            System.out.println("2. Edit Profile");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            String option = reader.readLine();

            switch (option) {
                case "1":
                    viewProfile();
                    break;
                case "2":
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
        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;
            System.out.println("Profile Details:");
            System.out.println("First Name: " + patient.getFirstName());
            System.out.println("Last Name: " + patient.getLastName());
            System.out.println("Email: " + patient.getEmail());
            System.out.println("Birth Date: " + patient.getBirthDate());
            System.out.println("Has Chronic Disease: " + patient.isHasChronicDisease());
            System.out.println("Chronic Disease Start Date: " + patient.getChronicDiseaseStartDate());
            System.out.println("Vaccinated: " + patient.isVaccinated());
            System.out.println("Vaccination Date: " + patient.getVaccinationDate());
            System.out.println("Country: " + patient.getCountry());
        }
    }

    private static void editProfile(BufferedReader reader) throws IOException {
        if (currentUser instanceof Patient) {
            Patient patient = (Patient) currentUser;

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
            String birthDate = reader.readLine();
            if (!birthDate.isEmpty()) {
                patient.setBirthDate(LocalDate.parse(birthDate));
            }

            System.out.print("Enter new chronic disease status (true/false) (leave blank to keep current): ");
            String hasChronicDisease = reader.readLine();
            if (!hasChronicDisease.isEmpty()) {
                patient.setHasChronicDisease(Boolean.parseBoolean(hasChronicDisease));
            }

            System.out.print("Enter new chronic disease start date (YYYY-MM-DD) (leave blank to keep current): ");
            String chronicDiseaseStartDate = reader.readLine();
            if (!chronicDiseaseStartDate.isEmpty()) {
                patient.setChronicDiseaseStartDate(LocalDate.parse(chronicDiseaseStartDate));
            }

            System.out.print("Enter new vaccination status (true/false) (leave blank to keep current): ");
            String vaccinated = reader.readLine();
            if (!vaccinated.isEmpty()) {
                patient.setVaccinated(Boolean.parseBoolean(vaccinated));
            }

            System.out.print("Enter new vaccination date (YYYY-MM-DD) (leave blank to keep current): ");
            String vaccinationDate = reader.readLine();
            if (!vaccinationDate.isEmpty()) {
                patient.setVaccinationDate(LocalDate.parse(vaccinationDate));
            }

            System.out.print("Enter new country (leave blank to keep current): ");
            String country = reader.readLine();
            if (!country.isEmpty()) {
                patient.setCountry(country);
            }

            // Update the file with new user data
            List<String> userStoreLines = FileUtil.readLines("data/user-store.txt");
            for (int i = 0; i < userStoreLines.size(); i++) {
                String[] parts = userStoreLines.get(i).split(",");
                if (parts[2].equals(patient.getEmail())) {
                    userStoreLines.set(i, String.join(",", patient.getFirstName(), patient.getLastName(), patient.getEmail(),
                            patient.getPassword(), patient.getSalt(), patient.getRole().toString()));
                    break;
                }
            }
            FileUtil.writeLines("data/user-store.txt", userStoreLines);
        }
    }
}
