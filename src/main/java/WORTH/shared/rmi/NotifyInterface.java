package WORTH.shared.rmi;

import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface NotifyInterface extends Remote {

    void setUsers(HashMap<String, User> user) throws RemoteException;
}
