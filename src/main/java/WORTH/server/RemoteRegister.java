package WORTH.server;

import WORTH.shared.rmi.NotifyInterface;
import WORTH.shared.rmi.RemoteInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.util.ArrayList;
import java.util.List;

public class RemoteRegister extends RemoteServer implements RemoteInterface {
    /* Gestore dei metodi associati agli utenti */
    private final UserManager userManager;
    private final List<NotifyInterface> clients;

    protected RemoteRegister() throws Exception {
        super();
        this.userManager = UserManager.getIstance();
        clients = new ArrayList<>();
    }

    /**
     * Registra l'utente al servizio
     * @param nickUtente Nickname dell'utente
     * @param password Password dell'utente
     * @throws IOException Nel caso di un errore nella registrazione
     */
    public synchronized void register(String nickUtente, String password) throws IOException {
        userManager.register(nickUtente, password);
    }

    @Override
    public synchronized List<User> registerForCallback(NotifyInterface clientInterface) throws Exception {
        ArrayList<User> users = new ArrayList<>(userManager.getUtenti().values());
        if (!clients.contains(clientInterface)) {
            clients.add(clientInterface);
            System.out.println("New client registered." );
        } else throw new Exception("The client is already registered");

        return users;
    }

    @Override
    public synchronized void unregisterForCallback(NotifyInterface clientInterface) throws RemoteException {
        if(clients.remove(clientInterface))
            System.out.println("Client unregistered");
        else
            System.out.println("Unable to unregister client");
    }


}
