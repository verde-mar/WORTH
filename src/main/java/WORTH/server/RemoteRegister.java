package WORTH.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteRegister extends UnicastRemoteObject implements RemoteInterface {
    UserManager userManager;

    /**
     * Costruttore della classe
     * @throws RemoteException
     */
    protected RemoteRegister() throws RemoteException {
        super();
    }

    public synchronized void register(String nickUtente, String password, UserManager userManager) throws RemoteException {
        this.userManager = userManager;
        userManager.getUtentiRegistrati().putIfAbsent(nickUtente, new User(nickUtente, password));
    }
}
