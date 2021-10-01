package WORTH.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    void register(String nickUtente, String password) throws RemoteException;
}
