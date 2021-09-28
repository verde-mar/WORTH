package WORTH.server;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

//todo: se uno scrittore sta scrivendo e un lettore vuole leggere, il lettore rischia di leggere le informazioni vecchie. E' un problema?
public class MainClass {
    public static void main(String[] args) throws IOException {
        ConfigurationFile config = new ConfigurationFile();
        ConfigurationFile.createOrSet();

        try (SocketServices server = new SocketServices(8080, config)) {
            server.start();

        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("Error in start operation");
            e.printStackTrace();
        }
    }
}