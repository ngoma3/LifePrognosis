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

    // Method to create and add a new Admin
    public void createAdmin(String firstName, String lastName, String email, String password, String salt) {
        // Generate UUID for the new admin
        String uuid = UUID.randomUUID().toString();
        Admin newAdmin = new Admin(firstName, lastName, email, password, salt, uuid);
        UserManagement.getInstance().addUser(newAdmin);
    }

    // Method to add an email to the onboarding list
    public void initiatePatientRegistration(String email) {
        FileUtil.appendLine("data/onboarding-list.txt", email);
    }
}
