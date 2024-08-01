public class Admin extends User {
    public Admin(String firstName, String lastName, String email, String password, String salt) {
        super(firstName, lastName, email, password, UserRole.ADMIN, salt);
    }

    public void createAdmin(String firstName, String lastName, String email, String password, String salt) {
        Admin newAdmin = new Admin(firstName, lastName, email, password, salt);
        UserManagement.getInstance().addUser(newAdmin);
    }

    public void initiatePatientRegistration(String email) {
        FileUtil.appendLine("data/onboarding-list.txt", email);
    }
}
