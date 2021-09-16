import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainClass {
    public static void main(String[] args) throws IOException {
        ConfigurationFile config = new ConfigurationFile();
        config.createOrSet();

        try (SocketServices server = new SocketServices(8080, config)) {
            server.start();

        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("Error in start operation");
            e.printStackTrace();
        }
    }
}