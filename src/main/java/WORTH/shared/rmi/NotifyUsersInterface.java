package WORTH.shared.rmi;

import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * todo
 */
public interface NotifyUsersInterface extends Remote {

    /**
     * Restituisce la lista degli utenti registrati su WORTH
     * @param users Lista degli utenti
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    void setUsers(ConcurrentHashMap<String, User> users) throws RemoteException;
}
