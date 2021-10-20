package WORTH.server;

import WORTH.persistence.RegisteredFile;
import WORTH.shared.rmi.NotifyUsersInterface;
import WORTH.shared.rmi.RemoteInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe rappresentante il gestore degli utenti e del loro rispettivo stato
 */
public class UserManager implements RemoteInterface {
    /* HashMap degli utenti registrati al servizio */
    private final ConcurrentHashMap<String, User> utentiRegistrati;
    /* Unica istanza di AccountService che può essere presente nel sistema */
    private static UserManager instance;
    /* Mapper necessario all'interazione con il file JSON */
    private final ObjectMapper mapper;
    /* File contenente gli utenti registrati */
    private final File registeredOnDisk;
    /* Istanza della classe ausiliaria per serializzazione/deserializzazione su disco degli utenti registrati su WORTH */
    private RegisteredFile registeredFile;
    /* Lista dei client registrati al servizio di notifica */
    private final List<NotifyUsersInterface> clients;

    /**
     * Costruttore della classe
     * @throws Exception Nel caso non sia possibile creare il file di configurazione per gli utenti
     */
    private UserManager() throws Exception {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        registeredOnDisk = new File("./" + "registeredUsers.json");
        registeredFile = new RegisteredFile();
        clients = new ArrayList<>();

        /* Se il file degli utenti registrati esiste gia'*/
        if(registeredOnDisk.exists()){
            /* Legge il valore dal disco */
            registeredFile = mapper.readValue(registeredOnDisk, RegisteredFile.class);
            /* E aggiorna la struttura in memoria */
            utentiRegistrati = registeredFile.getUtentiRegistrati();
        }
        /* Se, invece, il file non esiste */
        else {
            /* Viene creato */
            boolean created = registeredOnDisk.createNewFile();
            /* Se viene creato con successo, anche la struttura in memoria viene inizializzata */
            if(created)
                utentiRegistrati = new ConcurrentHashMap<>();
            /* Se invece la creazione fallisce, viene lanciata una eccezione */
            else
                throw new Exception("The creating of the configuration file for users failed");
        }

    }

    /**
     * Restituisce l'unica istanza della classe
     * @return UserManager Istanza della classe
     * @throws Exception Nel caso non sia possibile creare il file di configurazione per gli utenti
     */
    public static synchronized UserManager getInstance() throws Exception {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    /**
     * Effettua il login dell'utente
     * @param nickName Nickname dell'utente
     * @param password Password dell'utente
     * @throws Exception Nel caso in cui il login non vada a buon fine
     */
    public synchronized void login(String nickName, String password) throws Exception {
        /* Se l'utente e' registrato */
        if(utentiRegistrati.containsKey(nickName)){
            User user = utentiRegistrati.get(nickName);
            /* Se l'utente non e' gia' online */
            if(!user.isOnline()){
                /* Se la password e' corretta */
                if(user.getPassword().equals(password)){
                    user.setOnline();
                    /* Aggiorna il file su disco e anche gli altri utenti del suo cambiamento di stato */
                    updateRegisteredFile();
                    updateOnline(user);
                }
                /* Se, invece, la password non e' corretta */
                else {
                    throw new Exception("Password not valid");
                }
            }
            /* Se, invece, l'utente e' gia' online */
            else {
                throw new Exception("You are already online");
            }
        }
        /* Se invece, l'utente non e' registrato */
        else {
            throw new Exception("Login failed. You are not registered");
        }
    }

    /**
     * Effettua il logout dell'utente
     * @param nickName Nickname dell'utente
     * @throws Exception Nel caso in cui il logout non vada a buon fine
     */
    public synchronized void logout(String nickName) throws Exception {
        /* Se l'utente e' registrato */
        if(utentiRegistrati.containsKey(nickName)) {
            User user = utentiRegistrati.get(nickName);
            /* Se l'utente e' online */
            if (user.isOnline()) {
                user.setOffline();
                /* Aggiorna il file su disco e anche gli altri utenti del suo cambiamento di stato */
                updateRegisteredFile();
                updateOffline(user);
            }
            /* Se, invece, l'utente e' offline */
            else {
                throw new Exception(nickName + " isn't online");
            }
        }
        /* Se, invece, l'utente non e' registrato */
        else {
            throw new Exception("Login failed. You are not registered.");
        }
    }

    /**
     * Restituisce l'utente con il nickname nickutente
     * @param nickutente Nickname dell'utente cercato
     * @return User Restituisce l'utente con il nickname nickutente
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    public synchronized User getUtente(String nickutente) throws IOException {
        updateRegisteredFile();
        return utentiRegistrati.get(nickutente);
    }

    /**
     * Restituisce la struttura dati contenente gli utenti registrati
     * @return HashMap<String, User> Insieme degli utenti registrati
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    public synchronized ConcurrentHashMap<String, User> getUtenti() throws IOException {
        /* Aggiorna la struttura dati da restituire  */
        updateRegisteredFile();

        return utentiRegistrati;
    }

    /**
     * Registra l'utente
     * @param nickUtente Nickname dell'utente che sta richiedendo l'operazione
     * @param password Password dell'utente
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    public synchronized void register(String nickUtente, String password) throws Exception {
        User verify = utentiRegistrati.putIfAbsent(nickUtente, new User(nickUtente, password));
        /* Se l'utente non esisteva gia' */
        if(verify == null) {
            /* Aggiorna i file su disco e gli altri utenti di questa nuova registrazione */
            updateRegisteredFile();
            updateAll();
        }
        /* Se, invece, esisteva gia' */
        else {
            throw new Exception("You are already registered");
        }
    }

    /**
     * Registra un utente al servizio di notifica RMICallback
     * @param clientInterface Oggetto di tipo NotifyInterface rappresentante un client che si iscrive al servizio di notifica RMICallback
     * @return HashMap<String, User> Insieme degli utenti registrati a WORTH
     * @throws Exception Nel caso in cui l'operazione non vada a buon fine
     */
    @Override
    public synchronized ConcurrentHashMap<String, User> UsersCallback(NotifyUsersInterface clientInterface) throws Exception {
        if (!clients.contains(clientInterface)) {
            clients.add(clientInterface);
            System.out.println("New client registered." );
        } else throw new Exception("The client is already registered");
        return utentiRegistrati;
    }

    /**
     * Elimina un cerco utente dal servizio di notifica RMICallback
     * @param clientInterface Oggetto di tipo NotifyInterface rappresentante un client che si iscrive al servizio di notifica RMICallback
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    @Override
    public synchronized void UserUncallback(NotifyUsersInterface clientInterface) throws RemoteException {
        if(clients.remove(clientInterface))
            System.out.println("Client unregistered");
        else
            System.out.println("Unable to unregister client");
    }

    /**
     * La funzione notifica a tutti i client iscritti quando ci sono dei nuovi iscritti
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    public synchronized void updateAll() throws RemoteException {
        for (NotifyUsersInterface client : clients) {
            client.setUsers(utentiRegistrati);
        }
    }

    /**
     * La funzione notifica a tutti i client iscritti quando un utente diventa online
     * @param user Utente corrente
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    public synchronized void updateOnline(User user) throws RemoteException {
        for (NotifyUsersInterface client : clients) {
            client.setOnline(user);
        }
    }

    /**
     * La funzione notifica a tutti i client iscritti quando un utente diventa offline
     * @param user Utente corrente
     * @throws RemoteException Nel caso di un errore nella comunicazione RMI
     */
    public synchronized void updateOffline(User user) throws RemoteException {
        for (NotifyUsersInterface client : clients) {
            client.setOffline(user);
        }
    }

    /**
     * Funzione che aggiorna il file di formato JSON su disco
     * @throws IOException Nel caso di un errore nella scrittura su disco
     */
    public synchronized void updateRegisteredFile() throws IOException {
        registeredFile.setUtentiRegistrati(utentiRegistrati);
        mapper.writeValue(registeredOnDisk, registeredFile);
    }
}
