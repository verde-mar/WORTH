package WORTH.client;

import WORTH.server.User;
import WORTH.shared.rmi.NotifyInterface;
import WORTH.shared.rmi.RemoteInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;

public class RMIClient extends RemoteObject implements NotifyInterface {
    /* Oggetto remoto RMI */
    private final RemoteInterface rmiClient;
    /* Oggetto remoto per l'interazione RMI Callback */
    private NotifyInterface stub;
    private Collection<User> users;

    /**
     * Costruttore della classe
     * @param hostname Nome del server
     * @throws RemoteException Errore nella comunicazione
     * @throws NotBoundException Errore nel caso di un accesso al registro errato
     */
    public RMIClient(String hostname) throws Exception {
        Registry registry = LocateRegistry.getRegistry(hostname, 8081);
        rmiClient = (RemoteInterface) registry.lookup("RegistrationService");
    }

    /**
     * Registra un utente nel registro
     * @param username Nome del client che si vuole registrare
     * @param password Password del client
     * @throws RemoteException Nel caso di un errore nella comunicaizone
     */
    public void register(String username, String password) throws Exception {
        rmiClient.register(username, password);
    }

    /**
     * Registra l'utente al servizio di notifica del server
     * @throws Exception Nel caso di un errore generico
     */
    public void registerForCallback() throws Exception {
        stub = (NotifyInterface) UnicastRemoteObject.exportObject(this, 0);
        users = (rmiClient.registerForCallback(stub)).values();
        System.out.println(users);
    }

    /**
     * De-registra l'utente al servizio di notifica del server
     * @throws RemoteException Nel caso di un errore nella comunicaizone
     */
    public void unregisterForCallback() throws RemoteException {
        rmiClient.unregisterForCallback(stub);
    }

    /**
     * Restituisce la lista degli utenti registrati al servizio
     * @param users Lista degli utenti
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public void setUsers(HashMap<String, User> users) throws RemoteException {
        this.users = users.values();
    }

    /**
     * Stampa la lista degli utenti online
     * @throws RemoteException Nel caso di un errore nella comunicazione tramite RMI
     */
    public void listOnlineUsers() throws RemoteException {
        for(User user : users){
            if(user.isOnline())
                System.out.println("user: " + user.getName());
        }
    }

    /**
     * Stampa la lista degli utenti registrati al servizio e il loro stato
     * @throws RemoteException Nel caso di un errore nella comunicazione tramite RMI
     */
    public void listUsers() throws RemoteException {
        for(User user : users){
            System.out.println(user.toString());
        }
    }
}
