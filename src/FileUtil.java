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

    public static void updateLine(String filePath, String oldLine, String newLine) {
        try {
            Process process = new ProcessBuilder("bash/update_file.sh", filePath, oldLine, newLine).start();
            process.waitFor();
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
