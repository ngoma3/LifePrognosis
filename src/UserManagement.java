import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    private static UserManagement instance = new UserManagement();
    private List<User> users = new ArrayList<>();

    private UserManagement() {
    }

    public static UserManagement getInstance() {
        return instance;
    }

    public void loadUsers() {
        List<String> lines = FileUtil.readLines("data/user-store.txt");
        for (String line : lines) {
            String[] parts = line.split(",");
            String firstName = parts[0];
            String lastName = parts[1];
            String email = parts[2];
            String password = parts[3];
            String salt = parts[4];
            UserRole role = UserRole.valueOf(parts[5]);

            User user = null;
            if (role == UserRole.ADMIN) {
                user = new Admin(firstName, lastName, email, password, salt);
            } else if (role == UserRole.PATIENT) {
                user = new Patient(firstName, lastName, email, password, salt, null, false, null, false, null, null);
            }

            if (user != null) {
                users.add(user);
            }
        }
    }

    public void addUser(User user) {
        users.add(user);
        String userString = String.join(",", user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getSalt(), user.getRole().toString());
        FileUtil.appendLine("data/user-store.txt", userString);
    }

    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return users;
    }
}
