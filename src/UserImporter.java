import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class UserImporter {

    public static void importUsersFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/import.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (line.startsWith("First Name")) {
                    continue;
                }

                // Split the CSV line into its components
                String[] userDetails = line.split(",");

                String firstName = userDetails[0];
                String lastName = userDetails[1];
                String email = userDetails[2];
                LocalDate birthDate = LocalDate.parse(userDetails[3]);
                LocalDate hivStartDate = LocalDate.parse(userDetails[4]);
                LocalDate artStartDate = LocalDate.parse(userDetails[5]);
                // String countryISOCode = userDetails[6];
                // String country = userDetails[7];
                String country3CharISOCode = userDetails[8];

                // Assign a random gender
                GenderType gender = getRandomGender();

                // Generate UUID and default password
                String uuid = UUID.randomUUID().toString();
                String password = "123";
                String salt = PasswordUtil.getSalt();
                String hashedPassword = PasswordUtil.hashPassword(password, salt);

                // Create the Patient object
                Patient newPatient = new Patient(firstName, lastName, email, hashedPassword, salt, uuid,
                        birthDate, true, hivStartDate, true, artStartDate, country3CharISOCode, gender);

                // Append to user-store.txt
                String userStoreLine = String.join(",", newPatient.getUuid(), newPatient.getEmail(), newPatient.getFirstName(),
                        newPatient.getLastName(), newPatient.getPassword(), newPatient.getSalt(), newPatient.getRole().toString(),newPatient.getGender().toString());
                FileUtil.appendLine("data/user-store.txt", userStoreLine);

                // Append to health-data.txt
                String healthDataLine = String.join(",", newPatient.getUuid(),
                        newPatient.getBirthDate() != null ? newPatient.getBirthDate().toString() : "",
                        Boolean.toString(newPatient.isHasChronicDisease()),
                        newPatient.getChronicDiseaseStartDate() != null ? newPatient.getChronicDiseaseStartDate().toString() : "",
                        Boolean.toString(newPatient.isVaccinated()),
                        newPatient.getVaccinationDate() != null ? newPatient.getVaccinationDate().toString() : "",
                        newPatient.getCountry());
                FileUtil.appendLine("data/health-data.txt", healthDataLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        importUsersFromCSV();
        System.out.println("User import completed successfully.");
    }
    private static GenderType getRandomGender() {
        Random random = new Random();
        return random.nextBoolean() ? GenderType.MALE : GenderType.FEMALE;
    }
}
