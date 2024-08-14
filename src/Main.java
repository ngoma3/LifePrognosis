import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;


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
    public static final String cyanText =  "\033[38;2;173;216;230m";          // Cyan text
    public static final String greenText = "\033[32m";           // Green text
    public static final String yellowText = "\033[33m";          // Yellow text
    public static final String blueBackground = "\033[44m";      // Blue background
    public static final String boldText = "\033[1m";             // Bold text
    public static final String reset = "\033[0m";  

    public static void main(String[] args) throws InterruptedException {
        userManagement.loadUsers();
        createFirstAdmin();
        displayWelcomeMessage();
                     // Reset to default

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                

        // Printing with colors and formatting
        System.out.println(cyanText + boldText + "========================================" + reset);
        System.out.println(cyanText + boldText + "  Welcome to the Life Prognosis System" + reset);
        System.out.println(cyanText  + boldText + "========================================" + reset);
        System.out.println();
        System.out.println(yellowText + "          1. Login" + reset);
        System.out.println(yellowText + "          2. Register" + reset);
        System.out.println(yellowText + "          3. Exit" + reset);
        System.out.println();
        System.out.println(cyanText + boldText + "========================================" + reset);
        System.out.print( "          Choose an option: " + reset);
                // clearScreen();
                // System.out.println("=====================================");
                // System.out.println("  Welcome to the Life Prognosis System");
                // System.out.println("=====================================");
                // System.out.println();
                // System.out.println("          1. Login");
                // System.out.println("          2. Register");
                // System.out.println("          3. Exit");
                // System.out.println();
                // System.out.println("=====================================");
                // System.out.print("          Choose an option: ");
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

    private static void createFirstAdmin() {
        try {
            String scriptPath = "bash/create_first_admin.sh";
            String firstName = "Super";
            String lastName = "Admin";
            String email = "admin@gmail.com";
            String password = "123";
            String uuid = UUID.randomUUID().toString();
            String salt = PasswordUtil.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);
            ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath, firstName, lastName, email, hashedPassword, salt, uuid);
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
                User admin = new Admin(firstName, lastName, email, hashedPassword, salt,GenderType.MALE);
                userManagement.addSuperAdmin(admin);
            } 
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static String generateUUIDFromEmail(String email) {
        // Example implementation: Generate UUID based on the email
        return UUID.nameUUIDFromBytes(email.getBytes()).toString();
    }

    private static void displayWelcomeMessage() throws InterruptedException {
        clearScreen();
        String message = "Welcome to the Lifespan Estimation Tool...";
        // System.out.println();
        // System.out.println();
        // System.out.println();
        // System.out.println();
        for (char c : message.toCharArray()) {
            System.out.print(c);
            Thread.sleep(70); // Adjust speed of typing
        }
        System.out.println();
        clearScreen();
    }

    private static void login(BufferedReader reader) throws IOException {
        System.out.print("Enter email: ");
        String email = reader.readLine();
        
        // Hide password input
        String password = readPassword("Enter password: ");
        
        User user = userManagement.getUserByEmail(email);
        if (user != null && PasswordUtil.verifyPassword(password, user.getSalt(), user.getPassword())) {
            currentUser = user;
            System.out.println("Login successful!");
    
            // Call the appropriate dashboard based on the user's role
            user.viewDashboard(reader);
        } else {
            
            System.out.println("Invalid email or password.");
        }
    }
    
    // Method to read password with masking
    public static String readPassword(String prompt) throws IOException {
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
    

    private static void register(BufferedReader reader) throws IOException {
        while (true) {
            clearScreen();
            System.out.println(cyanText + boldText + "=================================" + reset);
System.out.println(cyanText + boldText + "       Registration          " + reset);
System.out.println(cyanText + boldText + "=================================" + reset);
System.out.println();
System.out.println(yellowText + "       1. Finalize Registration  " + reset);
System.out.println(yellowText + "       2. Registration Request   " + reset);
System.out.println(yellowText + "       3. Exit                   " + reset);
System.out.println();
System.out.println(cyanText + boldText + "=================================" + reset);
System.out.print( "       Choose an option: " + reset);
            String option = reader.readLine();

            switch (option) {
                case "1":
                    finalizeRegistration(reader);
                    return;
                case "2":
                    registrationRequest(reader);
                    return;
                case "3":
                    clearScreen();
                    System.out.println("Registration exited.");
                    return;
                default:
                    clearScreen();
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void finalizeRegistration(BufferedReader reader) throws IOException {
    clearScreen();
    System.out.println("===== Finalize Registration =====");
    System.out.print("Enter your email: ");
    String email = reader.readLine();
    System.out.print("Enter your UUID: ");
    String uuid = reader.readLine();

    boolean emailAndUuidExist = FileUtil.checkEmailAndUuidExist("data/user-store.txt", email, uuid);
    if (emailAndUuidExist) {
        System.out.print("Enter your first name: ");
String firstName = reader.readLine();
System.out.print("Enter your last name: ");
String lastName = reader.readLine();

// Prompt for gender
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


        // Use Console to read password and confirmation without displaying it
        Console console = System.console();
        if (console == null) {
            throw new IOException("No console available to securely read the password.");
        }

        char[] passwordArray;
        char[] confirmPasswordArray;

        while (true) {
            passwordArray = console.readPassword("Enter your password: ");
            confirmPasswordArray = console.readPassword("Confirm your password: ");

            // Convert char[] to String
            String password = new String(passwordArray);
            String confirmPassword = new String(confirmPasswordArray);

            // Clear password arrays for security
            Arrays.fill(passwordArray, ' ');
            Arrays.fill(confirmPasswordArray, ' ');

            if (password.equals(confirmPassword)) {
                String salt = PasswordUtil.getSalt();
                String hashedPassword = PasswordUtil.hashPassword(password, salt);
                Patient newPatient = new Patient(firstName, lastName, email, hashedPassword, salt, uuid, null, false, null, false, null, null,gender);
                FileUtil.updateUserLine("data/user-store.txt", uuid, newPatient);
                userManagement.updateUsers(newPatient);

                clearScreen();
                System.out.println("===== Registration Successful =====");
                System.out.println("You can now log in.");
                System.out.println("====================================");
                break;
            } else {
                System.out.println("Passwords do not match. Please try again.");
            }
        }
    } else {
        clearScreen();
        System.out.println("===== Error =====");
        System.out.println("The email and UUID combination was not found.");
        System.out.println("Please try again.");
        System.out.println("=================");
    }
}


    private static void registrationRequest(BufferedReader reader) throws IOException {
        clearScreen();
        System.out.println("===== Registration Request =====");
        System.out.print("Enter your email: ");
        String requestEmail = reader.readLine();

        boolean emailInOnboarding = FileUtil.checkEmailExists("data/onboarding-list.txt", requestEmail);
        if (!emailInOnboarding) {
            FileUtil.appendEmailToFile("data/onboarding-list.txt", requestEmail);
            clearScreen();
            System.out.println("===== Request Submitted =====");
            System.out.println("You are on the waiting list to be registered by an admin.");
            System.out.println("=============================");
        } else {
            clearScreen();
            System.out.println("===== Already Registered =====");
            System.out.println("Your email is already on the onboarding list.");
            System.out.println("Please wait for an admin to complete your registration.");
            System.out.println("===============================");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public static void setCurrentUser(User user) {
        
    }


    public static void clearScreen() {
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
    public static User getCurrentUser() {
        return currentUser;
    }
    
        
}
