import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class Admin extends User {

    // Constructor with UUID
    public Admin(String firstName, String lastName, String email, String password, String salt, String uuid) {
        super(firstName, lastName, email, password, UserRole.ADMIN, salt, uuid);
    }

    // Constructor without UUID, generates one
    public Admin(String firstName, String lastName, String email, String password, String salt) {
        this(firstName, lastName, email, password, salt, UUID.randomUUID().toString());
    }

    @Override
    public void viewDashboard(BufferedReader reader) throws IOException {
        while (true) {
            
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
                Main.clearScreen();
                    downloadAnalyticsCSV();
                    break;
                case "5":
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
        System.out.print("Enter password: ");
        String password = reader.readLine();

        String salt = PasswordUtil.getSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        Admin admin = new Admin(firstName, lastName, email, hashedPassword, salt);
        UserManagement.getInstance().addUser(admin);
        System.out.println("Admin added successfully.");
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

    private void downloadAnalyticsCSV() {
        try {
            Process process = new ProcessBuilder("bash/analytics_csv.sh").start();
            process.waitFor();
            System.out.println("Analytics CSV downloaded.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
