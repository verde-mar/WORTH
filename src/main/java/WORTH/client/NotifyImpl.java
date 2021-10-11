package WORTH.client;

import WORTH.server.User;
import WORTH.shared.rmi.NotifyInterface;

import java.rmi.RemoteException;
import java.util.List;

public class NotifyImpl implements NotifyInterface {

    /**
     * Costruttore della classe
     */
    public NotifyImpl(){
        super();
    }

    /**
     * Restituisce la lista degli utenti registrati al servizio
     * @param users Lista degli utenti
     * @return List<User> Lista degli utenti registrati al servizio
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public List<User> notifyUsers(List<User> users) throws RemoteException {
        return users;
    }

    /**
     * Restotiosce la lista degli utenti online registrati al servizio
     * @param onlineUsers Lista degli utenti online
     * @return List<User> Lista degli utenti online registrati al servizio
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public List<User> notifyOnlineUsers(List<User> onlineUsers) throws RemoteException {
        return onlineUsers;
    }
}
