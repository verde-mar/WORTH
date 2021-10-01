package WORTH.client;

import WORTH.shared.RemoteInterface;
import WORTH.shared.UserManager;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements RemoteInterface {
    /* Oggetto remoto RMI */
    private final RemoteInterface rmiClient;

    /**
     * Costruttore della classe
     * @param hostname Nome del server
     * @throws RemoteException Errore nella comunicazione
     * @throws NotBoundException Errore nel caso di un accesso al registro errato
     */
    public RMIClient(String hostname) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(hostname, 8081);
        rmiClient = (RemoteInterface) registry.lookup("RegistrationService");
    }

    /**
     * Registra un utente nel registro
     * @param username Nome del client che si vuole registrare
     * @param password Password del client
     * @throws RemoteException Errore nella comunicazione
     */
    public void register(String username, String password) throws RemoteException {
        rmiClient.register(username, password);
    }
}
