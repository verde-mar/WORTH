package WORTH.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutionException;

//todo: se uno scrittore sta scrivendo e un lettore vuole leggere, il lettore rischia di leggere le informazioni vecchie. E' un problema?
public class MainClass {
    public static void main(String[] args) throws IOException {
        ConfigurationFile config = new ConfigurationFile();
        ConfigurationFile.createOrSet();

        try {
            /* Creazione di una istanza dell'oggetto RemoteRegister */
            RemoteRegister receptionist = new RemoteRegister();
        } catch(RemoteException e){
            System.out.println("Error in communication " + e);
        }

        try (SocketServices server = new SocketServices(8080, config)) {
            server.start();

        } catch (IOException | InterruptedException | ExecutionException e) {
            System.err.println("Error in start operation");
            e.printStackTrace();
        }
    }
}