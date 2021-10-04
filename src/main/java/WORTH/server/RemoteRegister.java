package WORTH.server;

import WORTH.shared.rmi.RemoteInterface;

import java.io.IOException;
import java.rmi.server.RemoteServer;

public class RemoteRegister extends RemoteServer implements RemoteInterface {
    private final UserManager userManager;

    protected RemoteRegister() throws Exception {
        super();
        this.userManager = UserManager.getIstance();
    }

    public synchronized void register(String nickUtente, String password) throws IOException {
        userManager.register(nickUtente, password);
    }
}
