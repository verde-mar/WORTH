package WORTH.server.RMI;

import WORTH.server.UserManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    void register(String nickUtente, String password, UserManager userManager) throws RemoteException;
}
