package WORTH.shared.rmi;

import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public interface NotifyUsersInterface extends Remote {

    void setUsers(ConcurrentHashMap<String, User> user) throws RemoteException;
}
