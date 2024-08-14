import java.util.*;
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
            try {
                String[] parts = line.split(",");
    
                // Validate the number of parts
                if (parts.length != 8) {
                    // System.err.println("Invalid user data format: " + line);
                    continue;
                }
    
                String uuid = parts[0].trim();
                String email = parts[1].trim();
                String firstName = parts[2].trim();
                String lastName = parts[3].trim();
                String password = parts[4].trim();
                String salt = parts[5].trim();
                UserRole role = UserRole.valueOf(parts[6].trim());
                GenderType gender = GenderType.valueOf(parts[7].trim());
    
                User user = null;
                if (role == UserRole.ADMIN) {
                    // System.out.println("Login successful!"+firstName+" "+ lastName+" "+ email+" "+password+" "+salt+" "+uuid);
                    user = new Admin(firstName, lastName, email, password, salt, gender);
                } else if (role == UserRole.PATIENT) {
                    // System.out.println("Login successful!"+firstName+" "+ lastName+" "+ email+" "+password+" "+salt+" "+uuid);
                    // user = new Patient(firstName, lastName, email, password, salt, uuid, null, false, null, false, null, null, gender);
                    user = new Patient(firstName, lastName, email, password, salt, uuid, 
                   null, false, null, false, null, null, gender);

                }
    
                if (user != null) {
                    userMap.put(uuid, user);
                }
    
            } catch (Exception e) {
                System.err.println("Error processing line: " + line);
                e.printStackTrace(); // Optional: to get detailed error information
            }
        }
    
        // Step 2: Load additional patient details from health-data.txt
        List<String> healthLines = FileUtil.readLines("data/health-data.txt");
    
        for (String line : healthLines) {
            try {
                String[] parts = line.split(",");
                
                // Validate the number of parts for health data
                if (parts.length != 7) {
                    System.err.println("Invalid health data format: " + line);
                    continue;
                }
    
                String uuid = parts[0].trim(); // UUID must be the first column
                String birthDateStr = parts[1].trim();
                boolean hasChronicDisease = Boolean.parseBoolean(parts[2].trim());
                String chronicDiseaseStartDateStr = parts[3].trim();
                boolean vaccinated = Boolean.parseBoolean(parts[4].trim());
                String vaccinationDateStr = parts[5].trim();
                String country = parts[6].trim();
    
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
    
            } catch (Exception e) {
                System.err.println("Error processing health data line: " + line);
                e.printStackTrace(); // Optional: to get detailed error information
            }
        }
    
        // Add users to the list
        users.addAll(userMap.values());
    }
    // loadUsers() ;
    private LocalDate parseLocalDate(String dateStr) {
        try {
            return dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
        } catch (DateTimeParseException e) {
            // Handle invalid date format here, e.g., log the error
            System.err.println("Invalid date format: " + dateStr);
            return null;
        }
    }
    public List<Patient> getAllPatients() {
    List<Patient> patients = new ArrayList<>();
    for (User user : users) {
        if (user instanceof Patient) {
            patients.add((Patient) user);
        }
    }
    return patients;
    }

    public void addSuperAdmin(User user) {
        users.add(user);
    }

    public void addUser(User user) {
        users.add(user);
        String userString = String.join(",",user.getUuid(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getSalt(), user.getRole().toString());
        FileUtil.appendLine("data/user-store.txt", userString);
    }
    public void updateUsers(User user) {
        users.add(user);
    }

    public User getUserByEmail(String email) {
        for (User user : users) {
            // System.out.println(user.getEmail());
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
