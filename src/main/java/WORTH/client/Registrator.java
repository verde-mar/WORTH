package WORTH.client;

import java.rmi.RemoteException;

public interface Registrator {
    /**
     * Registra un utente nel registro
     * @param username Nome del client che si vuole registrare
     * @param password Password del client
     * @throws RemoteException Errore nella comunicazione
     */
    void registerUser(String username, String password) throws RemoteException;
}
