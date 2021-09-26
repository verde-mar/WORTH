package WORTH.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;

public class RemoteRegister extends RemoteServer implements RemoteInterface {
    UserManager userManager;
    public void register(String nickUtente, String password, UserManager userManager) throws RemoteException {
        this.userManager = userManager;

    }
}
