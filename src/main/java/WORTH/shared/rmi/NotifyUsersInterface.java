package WORTH.shared.rmi;

import WORTH.server.Project;
import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface NotifyUsersInterface extends Remote {

    void setUsers(HashMap<String, User> user) throws RemoteException;
}
