package WORTH.shared.rmi;

import WORTH.server.User;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interfaccia contenente gli header dei metodi di registrazione a WORTH e al servizio di notifica Callback
 */
public interface RemoteInterface extends Remote {

    /**
     * Registra l'utente
     * @param nickUtente Nickname dell'utente che sta richiedendo l'operazione
     * @param password Password dell'utente
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    void register(String nickUtente, String password) throws Exception;

    /**
     * Registra un utente al servizio di notifica RMICallback
     * @param clientInterface Oggetto di tipo NotifyInterface rappresentante un client che si iscrive al servizio di notifica RMICallback
     * @return HashMap<String, User> Insieme degli utenti registrati a WORTH
     * @throws Exception Nel caso in cui l'operazione non vada a buon fine
     */
    ConcurrentHashMap<String, User> UsersCallback(NotifyUsersInterface clientInterface) throws Exception;

    /**
     * Elimina un cerco utente dal servizio di notifica RMICallback
     * @param clientInterface Oggetto di tipo NotifyInterface rappresentante un client che si iscrive al servizio di notifica RMICallback
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    void UserUncallback(NotifyUsersInterface clientInterface) throws RemoteException;


}
