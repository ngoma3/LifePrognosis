import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            new ProcessBuilder("bash/read_file.sh", filePath).start().getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeLines(String filePath, List<String> lines) {
        try {
            // Prepare the command and arguments
            String[] command = new String[lines.size() + 2];
            command[0] = "bash/update_file.sh";
            command[1] = filePath;  
    
            // Add each line as an argument
            
            for (int i = 0; i < lines.size(); i++) {
               
                command[i + 2] = lines.get(i);
            }
           
            // Execute the Bash script
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            
            Process process = processBuilder.start();
            
            // Read the output from the script
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            // e.printStackTrace();
        }
    }
    public static boolean checkEmailExists(String filePath, String email) {
        try {
            // Use a bash script to check if the email exists in the file
            Process process = new ProcessBuilder("bash/check_email_exists.sh", filePath, email).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            process.waitFor();
    
            // Return true if the email exists, otherwise return false
            return "exists".equals(result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkEmailAndUuidExist(String filePath, String email, String uuid) {
        try {
            // Use a bash script to check if the email and UUID exist together in the file
            Process process = new ProcessBuilder("bash/check_email_uuid_exists.sh", filePath, email, uuid).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            System.out.println(result+" "+email+" "+uuid);
            process.waitFor();
    
            // Return true if both the email and UUID exist together, otherwise return false
            return "exists".equals(result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkEmailExists(String filePath, String email) {
        try {
            // Use a bash script to check if the email exists in the file
            Process process = new ProcessBuilder("bash/check_email_exists.sh", filePath, email).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            process.waitFor();
    
            // Return true if the email exists, otherwise return false
            return "exists".equals(result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean checkEmailAndUuidExist(String filePath, String email, String uuid) {
        try {
            // Use a bash script to check if the email and UUID exist together in the file
            Process process = new ProcessBuilder("bash/check_email_uuid_exists.sh", filePath, email, uuid).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            System.out.println(result+" "+email+" "+uuid);
            process.waitFor();
    
            // Return true if both the email and UUID exist together, otherwise return false
            return "exists".equals(result);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String> getWaitingList(String filePath) {
        List<String> waitingList = new ArrayList<>();
        try {
            Process process = new ProcessBuilder("bash/get_waiting_list.sh", filePath).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                waitingList.add(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return waitingList;
    }
    public static void removeEmailFromWaitingList(String filePath, String email) {
        try {
            Process process = new ProcessBuilder("bash/remove_email.sh", filePath, email).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
        
    
    public static List<String> getWaitingList(String filePath) {
        List<String> waitingList = new ArrayList<>();
        try {
            Process process = new ProcessBuilder("bash/get_waiting_list.sh", filePath).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                waitingList.add(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return waitingList;
    }
    public static void removeEmailFromWaitingList(String filePath, String email) {
        try {
            Process process = new ProcessBuilder("bash/remove_email.sh", filePath, email).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
        
    

    public static void appendEmailToFile(String filePath, String email) {
        try {
            Process process = new ProcessBuilder("bash/check_and_append.sh", filePath, email).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void appendEmailToFile(String filePath, String email) {
        try {
            Process process = new ProcessBuilder("bash/check_and_append.sh", filePath, email).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void appendLine(String filePath, String line) {
        try {
            Process process = new ProcessBuilder("bash/write_to_file.sh", filePath, line).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void initiatePatientRegistration(String filePath, String email, String uuid) {
        try {
            Process process = new ProcessBuilder("bash/initiate_registration.sh", filePath, email, uuid).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    public static void updateLine(String filePath, String oldLine, String newLine) {
        try {
            Process process = new ProcessBuilder("bash/update_file.sh", filePath, oldLine, newLine).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void updateUserLine(String filePath, String uuid, Patient updatedPatient) {
        try {
            // Construct the updated line for the patient
            String updatedLine = String.format("%s,%s,%s,%s,%s,%s", 
                    updatedPatient.getUuid(),
                    updatedPatient.getEmail(), 
                    updatedPatient.getFirstName(), 
                    updatedPatient.getLastName(), 
                    updatedPatient.getPassword(),
                    updatedPatient.getSalt());
    
            // Call the bash script using ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "bash/update_user_line.sh", 
                    filePath, 
                    uuid, 
                    updatedLine);
    
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            process.waitFor();
    
            if ("success".equals(result)) {
                System.out.println("User information updated successfully.");
            } else {
                System.out.println("User UUID not found in the file.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    public static void deleteLine(String filePath, String line) {
        try {
            Process process = new ProcessBuilder("bash/delete_from_file.sh", filePath, line).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
