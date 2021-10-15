package WORTH.server;

import WORTH.Persistence.RegisteredFile;
import WORTH.shared.rmi.NotifyInterface;
import WORTH.shared.rmi.RemoteInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class UserManager implements RemoteInterface {
    /* HashMap degli utenti registrati al servizio */
    private final HashMap<String, User> utentiRegistrati;
    /* Unica istanza di AccountService che può essere presente nel sistema */
    private static UserManager instance;
    /* Mapper necessario all'interazione con il file JSON */
    private final ObjectMapper mapper;
    /* File contenente gli utenti registrati */
    private final File utentiRegistrati_ondisk;
    /* File JSON per gli utenti registrati al servizio */
    private RegisteredFile registeredFile;
    /* Lista dei client registrati al servizio di notifica */
    private final List<NotifyInterface> clients;

    /**
     * Costruttore della classe
     * @throws Exception Nel caso non sia possibile creare il file di configurazione per gli utenti
     */
    private UserManager() throws Exception {
        mapper = new ObjectMapper();
        utentiRegistrati_ondisk = new File("./" + "UsersOnDisk.json");
        registeredFile = new RegisteredFile();
        clients = new ArrayList<>();

        /* Se il file degli utenti registrati esiste gia'*/
        if(utentiRegistrati_ondisk.exists()){
            /* Legge il valore dal disco */
            registeredFile = mapper.readValue(utentiRegistrati_ondisk, RegisteredFile.class);
            /* E aggiorna la struttura in memoria */
            utentiRegistrati = registeredFile.getUtentiRegistrati();
        }
        /* Se, invece, il file non esiste */
        else {
            /* Viene creato */
            boolean created = utentiRegistrati_ondisk.createNewFile();
            /* Se viene creato con successo, anche la struttura in memoria viene inizializzata */
            if(created)
                utentiRegistrati = new HashMap<>();
            /* Se invece la creazione fallisce, viene lanciata una eccezione */
            else
                throw new Exception("The creating of the confiuration file for users failed");
        }

    }

    /**
     * Restituisce l'unica istanza della classe
     * @return UserManager Istanza della classe
     * @throws Exception Nel caso non sia possibile creare il file di configurazione per gli utenti
     */
    public static synchronized UserManager getIstance() throws Exception {
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
    public void login(String nickName, String password) throws Exception {
        /* Se l'utente e' registrato */
        if(utentiRegistrati.containsKey(nickName)){
            User user = utentiRegistrati.get(nickName);
            /* Se l'utente non e' gia' online */
            if(!user.isOnline()){
                /* Se la password e' corretta */
                if(user.getPassword().equals(password)){
                    user.setOnline();
                    /* Aggiorna il file su disco e anche gli altri utenti del suo cambiamento di stato */
                    registeredFile.setUtentiRegistrati(utentiRegistrati);
                    mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
                    update();
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
     */
    public void logout(String nickName) throws Exception {
        /* Se l'utente e' registrato */
        if(utentiRegistrati.containsKey(nickName)) {
            User user = utentiRegistrati.get(nickName);
            /* Se l'utente e' online */
            if (user.isOnline()) {
                user.setOffline();
                /* Aggiorna il file su disco e anche gli altri utenti del suo cambiamento di stato */
                registeredFile.setUtentiRegistrati(utentiRegistrati);
                mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
                update();
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
     * @return String Restituisce l'utente con il nickname nickutente
     */
    public User getUtente(String nickutente) throws IOException {
        //registeredFile.setUtentiRegistrati(utentiRegistrati);
        //mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        return utentiRegistrati.get(nickutente);
    }

    /**
     * Restituisce la struttura dati contenente gli utenti registrati
     * @return HashMap<String, User> insieme degli utenti registrati
     */
    public HashMap<String, User> getUtenti() throws IOException {
        //registeredFile.setUtentiRegistrati(utentiRegistrati);
        //mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        return utentiRegistrati;
    }

    /**
     * Registra l'utente
     * @param nickUtente Nickname dell'utente che sta richiedendo l'operazione
     * @param password Password dell'utente
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    public void register(String nickUtente, String password) throws Exception {
        User verify = utentiRegistrati.putIfAbsent(nickUtente, new User(nickUtente, password));
        /* Se l'utente non esisteva gia' */
        if(verify == null) {
            /* Aggiorna i file su disco e gli altri utenti di questa nuova registrazione */
            registeredFile.setUtentiRegistrati(utentiRegistrati);
            mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
            update();
        }
        /* Se, invece, esisteva gia' */
        else {
            throw new Exception("You are already registered");
        }
    }

    /**
     * //todo
     * @param clientInterface
     * @return
     * @throws Exception
     */
    @Override
    public synchronized HashMap<String, User> registerForCallback(NotifyInterface clientInterface) throws Exception {
        if (!clients.contains(clientInterface)) {
            clients.add(clientInterface);
            System.out.println("New client registered." );
        } else throw new Exception("The client is already registered");
        return utentiRegistrati;
    }

    /**
     * //todo
     * @param clientInterface
     * @throws RemoteException
     */
    @Override
    public synchronized void unregisterForCallback(NotifyInterface clientInterface) throws RemoteException {
        if(clients.remove(clientInterface))
            System.out.println("Client unregistered");
        else
            System.out.println("Unable to unregister client");
    }

    /**
     * //todo
     * @throws RemoteException
     */
    public synchronized void update() throws RemoteException {
        Iterator<NotifyInterface> i = clients.iterator();;
        while(i.hasNext()){
            NotifyInterface client = i.next();
            client.setUsers(utentiRegistrati);
            System.out.println("client in update : " + client);
        }
    }

}
