package WORTH.shared.rmi;

import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interfaccia contenente i metodi utilizzati per notificare i client
 * della registrazione e dei cambi di stato di altri utenti
 */
public interface NotifyUsersInterface extends Remote {

    /**
     * Restituisce la lista degli utenti registrati su WORTH
     * @param users Lista degli utenti
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    void setUsers(ConcurrentHashMap<String, User> users) throws RemoteException;

    /**
     * Funzione usata per aggiornare lo stato di un utente ad online, tramite RMICallback
     * @param user Utente corrente
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    void setOnline(User user) throws RemoteException;

    /**
     * Funzione usata per aggiornare lo stato di un utente ad online, tramite RMICallback
     * @param user Utente corrente
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    void setOffline(User user) throws RemoteException;
}
