package WORTH.shared.rmi;

import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public interface RemoteInterface extends Remote {

    void register(String nickUtente, String password) throws Exception;

    ConcurrentHashMap<String, User> UsersCallback(NotifyUsersInterface clientInterface) throws Exception;

    /**
     * Cancella la registrazione per la callback
     * @param clientInterface
     */
    void UserUncallback(NotifyUsersInterface clientInterface) throws RemoteException;


}
