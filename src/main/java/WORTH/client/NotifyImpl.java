package WORTH.client;

import WORTH.server.User;
import WORTH.shared.rmi.NotifyInterface;

import java.rmi.RemoteException;
import java.util.List;

public class NotifyImpl implements NotifyInterface {

    public NotifyImpl(){
        super();
    }

    @Override
    public List<User> notifyUsers(List<User> users) {
        return users;
    }

    @Override
    public List<User> notifyOnlineUsers(List<User> onlineUsers) throws RemoteException {
        return onlineUsers;
    }
}
