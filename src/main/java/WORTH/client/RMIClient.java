package WORTH.client;

import WORTH.shared.Configuration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    /* Oggetto remoto RMI */
    private Registrator rmiClient;

    /**
     * Costruttore della classe
     * @param hostname Nome del server
     * @throws RemoteException Errore nella comunicazione
     * @throws NotBoundException Errore nel caso di un accesso al registro errato
     */
    public RMIClient(String hostname) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostname, Configuration.RMI_PORT);
        rmiClient = (Registrator) registry.lookup("RegistrationService");
    }

    /**
     * Registra un utente nel registro
     * @param username Nome del client che si vuole registrare
     * @param password Password del client
     * @throws RemoteException Errore nella comunicazione
     */
    public void registerUser(String username, String password) throws RemoteException {
        rmiClient.registerUser(username, password);
    }
}
