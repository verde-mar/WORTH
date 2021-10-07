package WORTH.server;

import WORTH.shared.rmi.RemoteInterface;

import java.io.IOException;
import java.rmi.server.RemoteServer;

public class RemoteRegister extends RemoteServer implements RemoteInterface {
    /* Gestore dei metodi associati agli utenti */
    private final UserManager userManager;

    /**
     * Costruttore della classe
     * @throws Exception
     */
    protected RemoteRegister() throws Exception {
        super();
        this.userManager = UserManager.getIstance();
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
}
