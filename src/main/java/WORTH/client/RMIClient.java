package WORTH.client;

import WORTH.server.User;
import WORTH.shared.rmi.NotifyInterface;
import WORTH.shared.rmi.RemoteInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIClient {
    /* Oggetto remoto RMI */
    private final RemoteInterface rmiClient;
    /* Oggetto remoto per l'interazione RMI Callback */
    private NotifyInterface stub;
    /* Lista degli utenti registrati */
    private List<User> users;

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
     * @throws RemoteException Nel caso di un errore nella comunicaizone
     */
    public void register(String username, String password) throws IOException {
        rmiClient.register(username, password);
    }

    /**
     * Registra l'utente al servizio di notifica del server
     * @throws Exception Nel caso di un errore generico
     */
    public void registerForCallback() throws Exception {
        NotifyInterface notify = new NotifyImpl();
        stub = (NotifyInterface) UnicastRemoteObject.exportObject(notify, 0);
        users = rmiClient.registerForCallback(stub);
    }

    /**
     * De-registra l'utente al servizio di notifica del server
     * @throws RemoteException Nel caso di un errore nella comunicaizone
     */
    public void unregisterForCallback() throws RemoteException {
        rmiClient.unregisterForCallback(stub);
    }

    /**
     * Stampa i campi specificati in toString()
     */
    public void listUsers() {
        for(User user : users){
            System.out.println(user.toString());
        }
    }

    /**
     * Stampa il nome degli utenti online
     */
    public void listOnlineUsers() {
        for(User user : users){
            if(user.isOnline())
                System.out.println("user: " + user.getName());
        }
    }
}
