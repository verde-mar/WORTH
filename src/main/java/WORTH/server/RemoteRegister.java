package WORTH.server;

import WORTH.shared.UserManager;
import WORTH.shared.RemoteInterface;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.HashMap;

public class RemoteRegister extends RemoteServer implements RemoteInterface {
    private UserManager userManager;

    protected RemoteRegister(HashMap<String, User> utenti_registrati) throws RemoteException {
        super();
        this.userManager = UserManager.getIstance(utenti_registrati);
    }

    public synchronized void register(String nickUtente, String password) throws RemoteException {
        userManager.register(nickUtente, password);
    }
}
