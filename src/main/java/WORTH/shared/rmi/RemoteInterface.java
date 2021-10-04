package WORTH.shared.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    void register(String nickUtente, String password) throws IOException;
}
