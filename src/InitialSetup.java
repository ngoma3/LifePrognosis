import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class InitialSetup {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Collect initial admin details
        System.out.print("Enter admin first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter admin last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter admin email: ");
        String email = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        // Generate a UUID for the admin
        String uuid = java.util.UUID.randomUUID().toString();
        
        // Hash the password
        String salt = PasswordUtil.getSalt();
        String hashedPassword = PasswordUtil.hashPassword(password, salt);

        // Write the admin to the user-store file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/user-store.txt", true))) {
            writer.write(firstName + "," + lastName + "," + email + "," + uuid + "," + "ADMIN" + "," + hashedPassword + "," + salt + "\n");
            System.out.println("Admin user created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close(); // Close the scanner
    }
}
