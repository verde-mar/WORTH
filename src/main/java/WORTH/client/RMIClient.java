package WORTH.client;

import WORTH.server.User;
import WORTH.shared.rmi.NotifyUsersInterface;
import WORTH.shared.rmi.RemoteInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe che si occupa di interagire con il server tramite RMI e RMICallback
 */
public class RMIClient extends RemoteObject implements NotifyUsersInterface {
    /* Istanza della classe che implementa NotifyUsersInterfaccia, necessaria al servizio di notifica RMICallback*/
    private final RemoteInterface rmiClient;
    /* Istanza dell'interfaccia necessaria al servizio di notifica RMICallback */
    private NotifyUsersInterface stubUsers;
    /* Lista degli utenti registrati a WORTH */
    private ConcurrentHashMap<String, User> users;

    /**
     * Costruttore della classe
     * @param hostname Hostname del server
     * @throws RemoteException Nel caso in cui non possa essere trovato nessun Registry
     * @throws NotBoundException Nel caso in cui l'oggetto remoto di nome "RegistrationService" non viene trovato
     */
    public RMIClient(String hostname) throws RemoteException, NotBoundException {
        Registry registryUsers = LocateRegistry.getRegistry(hostname, 8081);
        rmiClient = (RemoteInterface) registryUsers.lookup("RegistrationService");
    }

    /**
     * Registra un utente su WORTH
     * @param username Nome del client che si vuole registrare
     * @param password Password del client
     * @throws RemoteException Nel caso di un errore nella comunicazione
     */
    public void register(String username, String password) throws Exception {
        rmiClient.register(username, password);
    }

    /**
     * Registra l'utente al servizio di notifica RMICallback
     * @throws Exception Nel caso di un errore nella registrazione al servizio di notifica
     */
    public void registerForCallback() throws Exception {
        stubUsers = (NotifyUsersInterface) UnicastRemoteObject.exportObject(this, 0);
        users = rmiClient.UsersCallback(stubUsers);
    }

    /**
     * De-registra l'utente al servizio di notifica RMICallback
     * @throws RemoteException Nel caso di un errore nella comunicazione
     */
    public void unregisterForCallback() throws RemoteException {
        rmiClient.UserUncallback(stubUsers);
    }

    /**
     * Inizializza la lista degli utenti corrente con quella degli utenti registrati su WORTH
     * @param users HashMap degli utenti
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public void setUsers(ConcurrentHashMap<String, User> users) throws RemoteException {
        this.users = users;
    }

    /**
     * Funzione usata per aggiornare lo stato di un utente ad online, tramite RMICallback
     * @param user Utente corrente
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public void setOnline(User user) throws RemoteException {
        User utente = users.get(user.getName());
        utente.setOnline();
    }

    /**
     * Funzione usata per aggiornare lo stato di un utente ad online, tramite RMICallback
     * @param user Utente corrente
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public void setOffline(User user) throws RemoteException {
        User utente = users.get(user.getName());
        utente.setOffline();
    }


    /**
     * Stampa la lista degli utenti registrati su WORTH online
     */
    public void listOnlineUsers() {
        for(User user : users.values()){
            if(user.isOnline())
                System.out.println("user: " + user.getName());
        }
    }

    /**
     * Stampa la lista degli utenti registrati su WORTH e il loro stato
     */
    public void listUsers() {
        for(User user : users.values()){
            System.out.println(user.toString());
        }
    }
}
