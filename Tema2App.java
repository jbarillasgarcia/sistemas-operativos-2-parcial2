import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tema2App implements Runnable {

    private static final Logger logger = Logger.getLogger(Tema2App.class.getName());
    
    private static final Long SLEEP_TIME = 5000l;
    
    private static final String INPUT_FOLDER = "/home/root/so2/parcial2/tema2/input";
    private static final String OUTPUT_FOLDER = "/home/root/so2/parcial2/tema2/output";
    
    private boolean exitFlag = false;
    
    public static void main(String[] args) {
        logger.log(Level.INFO, "Programa iniciado...");
        
        mostrarInstruccionesImplementacion();
        
        mostrarInstruccionesUso();
        
        procesarArchivos();
    }
    
    public static void mostrarInstruccionesImplementacion() {
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("INSTRUCCIONES DE IMPLEMENTACION");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("1.\tImplemente el Dockerfile para crear la imagen Docker correspondiente al programa Tema2App.java; El Dockerfile debe contener las instrucciones necesarias para construir la estructura de directorios indicada en el temario.");        
        System.out.println("2.\tConstruya la imagen Docker utilizando el programa contenido en Tema2App.java y el Dockerfile que usted ha implementado en el paso anterior.");
        System.out.println("3.\tAsegúrese de que usted puede ejecutar el contenedor Docker a partir de la imagen Docker creada.");
        System.out.println("4.\tPublique la imagen Docker en el Docker Hub con la etiqueta \"so2-parcial-tema2\". Por ejemplo: \"jbarillas/so2-parcial-tema2\"");
        System.out.println("5.\tRemueva cualquier instancia del contenedor Docker de su Docker Engine local.");
        System.out.println("6.\tRemueva la imagen de su Docker Engine local.");
        System.out.println("7.\tAsegúrese de que usted puede ejecutar el contenedor Docker a partir de la imagen Docker publicada.");
        System.out.println("8.\tIndique en su temario el contenido del Dockerfile que ha implementado para el tema 2.");
        System.out.println("9.\tIndique en su temario el comando docker pull ... para descargar la imagen correspondiente al tema 2.");
        System.out.println("10.\tIndique en su temario el comando docker run ... para ejecutar un contenedor para la imagen Docker correspondiente al tema 2.");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }
    
    public static void mostrarInstruccionesUso() {
        System.out.println("FUNCIONAMIENTO");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.println("1.\tLuego de ejecutar el contenedor Docker, usted debera colocar N archivos de texto (.txt) dentro del folder \"/home/root/so2/parcial2/tema2/input\" adentro del contenedor. ");
        System.out.println("2.\tEl programa detectara automaticamente estos archivos y generara un archivo de salida de texto (.txt) en el folder \"/home/root/so2/parcial2/tema2/output\" por cada archivo de entrada.  Cada archivo de salida contendra la cantidad de lineas de texto en su archivo de entrada correspondiente.");
        System.out.println("3.\tIndique en su temario el comando para enviar cada uno de los archivos de entrada desde su maquina local.");
        System.out.println("4.\tIndique en su temario el comando para ver el contenido de cada uno de los archivos de salida desde su maquina local.");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }

    @Override
    public void run() {
        int cycle = 1;
        
        while (!exitFlag) {
            // Sleep SLEEP_TIME nanoseconds
            try {                
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getMessage());
            }
            
            logger.log(Level.INFO, String.format("Cycle %d", cycle++));
            
            // Build input directory path
            Path inputDirectory = Paths.get(INPUT_FOLDER);
            
            // Check whether input directory path exists or not
            if (inputDirectory.toFile() == null || !inputDirectory.toFile().exists()) {
               logger.log(Level.SEVERE, String.format("El directorio \"%s\" no ha sido creado.", INPUT_FOLDER));
               continue; 
            }
            
            // Build output directory path
            Path outputDirectory = Paths.get(OUTPUT_FOLDER);
            
            // Check whether output directory path exists or not
            if (outputDirectory.toFile() == null || !outputDirectory.toFile().exists()) {
               logger.log(Level.SEVERE, String.format("El directorio \"%s\" no ha sido creado.", OUTPUT_FOLDER));
               continue; 
            }
            
            for (File inputFile : inputDirectory.toFile().listFiles()) {
                
                // If file is not a text file
                if (!inputFile.getName().toLowerCase().endsWith(".txt")) {
                    logger.log(Level.WARNING, String.format("El archivo \"%s\" no es valido.", inputFile.getName()));
                    continue;
                }
                
                System.out.println(String.format("Procesando archivo \"%s\".", inputFile.getName()));
                
                int lineCount = 0;
                
                // Read input file lines                
                try {
                    FileReader reader = null;
                    
                    reader = new FileReader(inputFile);
                    
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    
                    String line;                    
                    
                    while (bufferedReader.readLine() != null) {
                        lineCount++;
                    }
                    
                    bufferedReader.close();
                } catch (FileNotFoundException ex) {
                    logger.log(Level.SEVERE, ex.getMessage());
                    continue;
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, ex.getMessage());
                    continue;
                }
                
                // Write output file
                try {
                    FileWriter writer = null;
                    
                    writer = new FileWriter(Paths.get(OUTPUT_FOLDER, inputFile.getName()).toString());
                    
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    
                    bufferedWriter.write(String.format("El archivo de entrada contiene %d lineas.%s", lineCount, System.lineSeparator()));
                    
                    bufferedWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(Tema2App.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }                
                
                // Delete input file after processing
                inputFile.delete();
            }            
        }
    }
    
    public static void procesarArchivos() {
        Tema2App tema2App = new Tema2App();
        
        Thread thread = new Thread(tema2App);
        
        thread.start();
    }
    
}
