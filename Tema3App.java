import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tema3App {
    
    private static final Logger logger = Logger.getLogger(Tema3App.class.getName());
    
    private static final String OUTPUT_FOLDER = "/var/www/output";
    
    public static void main(String[] args) throws IOException, Exception {
         // Build output directory path
        Path outputDirectory = Paths.get(OUTPUT_FOLDER);

        // Check whether output directory path exists or not
        if (outputDirectory.toFile() == null || !outputDirectory.toFile().exists()) {
           logger.log(Level.SEVERE, String.format("El directorio \"%s\" no ha sido creado.", OUTPUT_FOLDER));
           throw new Exception("Output directory doesn't exist.");
        }            
        
        // Ask for user's full name
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Ingrese su nombre completo: ");
        
        if (!scanner.hasNext()) {
            logger.log(Level.SEVERE, "Debe ingresar su nombre completo.");
            throw new Exception("User's full name cannot be empty.");
        }
        
        String username = scanner.next();
        
        if (username == null || username.trim().length() <= 0) {
            logger.log(Level.SEVERE, "Debe ingresar su nombre completo.");
            throw new Exception("User's full name cannot be empty.");
        }
        
        // Create output file
        File outputFile = null;
        
        try {
            outputFile = File.createTempFile("salida", ".html");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());            
            throw ex;
        }
        
        // Add contents to file
        FileWriter writer = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        
        bufferedWriter.write(String.format("<html><header><title>Parcial 2/Tema 3</title></header><body><h1>Hola %s!!!<h1></body></html>", username));
        
        bufferedWriter.close();
        
        // Move file to output folder
        Path source = Paths.get(outputFile.getAbsolutePath());
        Path target = Paths.get(OUTPUT_FOLDER, outputFile.getName());
        
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
        
        System.out.println(String.format("Hola %s.  El archivo \"%s\" ha sido creado en el folder \"%s\".", username, outputFile.getName(), OUTPUT_FOLDER));
    }
    
}
