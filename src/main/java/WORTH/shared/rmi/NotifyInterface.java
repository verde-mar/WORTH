package WORTH.shared.rmi;

import WORTH.server.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NotifyInterface extends Remote {

    List<User> notifyUsers(List<User> users) throws RemoteException;

    List<User> notifyOnlineUsers(List<User> onlineUsers) throws RemoteException;
}
