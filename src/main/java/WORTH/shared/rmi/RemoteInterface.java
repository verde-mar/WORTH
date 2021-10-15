package WORTH.shared.rmi;

import WORTH.server.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface RemoteInterface extends Remote {

    void register(String nickUtente, String password) throws Exception;

    HashMap<String, User> registerForCallback(NotifyInterface clientInterface) throws Exception;

    /**
     * Cancella la registrazione per la callback
     * @param clientInterface
     */
    void unregisterForCallback(NotifyInterface clientInterface) throws RemoteException;
}
