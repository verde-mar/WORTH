package WORTH.client;

import WORTH.server.User;

import java.io.IOException;
import java.util.HashMap;

/**
 * Classe che si occupa di formulare le richieste e parsare le risposte
 */
public class TCPDriver {
    private TCPClient tcpClient;
    private HashMap<String, User> utenti_registrati;

    public TCPDriver(TCPClient tcpClient, HashMap<String, User> utenti_registrati) {
        this.tcpClient = tcpClient;
    }

    /**
     *
     * @param username
     * @param password
     * @throws IOException
     */
    public void loginRequest(String username, String password) throws IOException {
    }
}
