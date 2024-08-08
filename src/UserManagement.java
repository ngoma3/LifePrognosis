import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UserManagement {
    private static UserManagement instance = new UserManagement();
    private List<User> users = new ArrayList<>();

    private UserManagement() {
    }

    public static UserManagement getInstance() {
        return instance;
    }

    
    public void loadUsers() {
        // Step 1: Load users from user-store.txt
        Map<String, User> userMap = new HashMap<>();
        List<String> userLines = FileUtil.readLines("data/user-store.txt");
        
        for (String line : userLines) {
            String[] parts = line.split(",");
            String uuid = parts[0];
            String firstName = parts[1];
            String lastName = parts[2];
            String email = parts[3];
            String password = parts[4];
            String salt = parts[5];
            UserRole role = UserRole.valueOf(parts[6]);
    
            User user = null;
            if (role == UserRole.ADMIN) {
                user = new Admin(firstName, lastName, email, password, salt, uuid);
            } else if (role == UserRole.PATIENT) {
                user = new Patient(firstName, lastName, email, password, salt, uuid, null, false, null, false, null, null);
            }
    
            if (user != null) {
                userMap.put(uuid, user);
            }
        }
    
        // Step 2: Load additional patient details from health-data.txt
        List<String> healthLines = FileUtil.readLines("data/health-data.txt");
    
        for (String line : healthLines) {
            String[] parts = line.split(",");
            String uuid = parts[0]; // UUID must be the first column
            String birthDateStr = parts[1];
            boolean hasChronicDisease = Boolean.parseBoolean(parts[2]);
            String chronicDiseaseStartDateStr = parts[3];
            boolean vaccinated = Boolean.parseBoolean(parts[4]);
            String vaccinationDateStr = parts[5];
            String country = parts[6];

            User user = userMap.get(uuid);
            if (user instanceof Patient) {
                Patient patient = (Patient) user;
                
                // Convert strings to LocalDate, handle possible parsing exceptions
                LocalDate birthDate = parseLocalDate(birthDateStr);
                LocalDate chronicDiseaseStartDate = parseLocalDate(chronicDiseaseStartDateStr);
                LocalDate vaccinationDate = parseLocalDate(vaccinationDateStr);
                patient.setBirthDate(birthDate);
                patient.setHasChronicDisease(hasChronicDisease);
                patient.setChronicDiseaseStartDate(chronicDiseaseStartDate);
                patient.setVaccinated(vaccinated);
                patient.setVaccinationDate(vaccinationDate);
                patient.setCountry(country);
            }
        }
    
        // Add users to the list
        users.addAll(userMap.values());
    }
    private LocalDate parseLocalDate(String dateStr) {
        try {
            return dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
        } catch (DateTimeParseException e) {
            // Handle invalid date format here, e.g., log the error
            System.err.println("Invalid date format: " + dateStr);
            return null;
        }
    }
    public void addSuperAdmin(User user) {
        users.add(user);
    }

    public void addUser(User user) {
        users.add(user);
        String userString = String.join(",",user.getUuid(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getSalt(), user.getRole().toString());
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
