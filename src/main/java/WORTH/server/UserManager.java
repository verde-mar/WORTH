package WORTH.server;

import WORTH.Persistence.RegisteredFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Gestore della registrazione e del login degli utenti.
 * Si occupa anche della persistenza degli utenti registrati al servizio.
 */
public class UserManager {
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

    /**
     * Costruttore della classe
     * @throws Exception Nel caso non sia possibile creare il file di configurazione per gli utenti
     */
    private UserManager() throws Exception {
        mapper = new ObjectMapper();
        utentiRegistrati_ondisk = new File("./" + "UsersOnDisk.json");
        registeredFile = new RegisteredFile();

        if(utentiRegistrati_ondisk.exists()){
            registeredFile = mapper.readValue(utentiRegistrati_ondisk, RegisteredFile.class);
            utentiRegistrati = registeredFile.getUtentiRegistrati();
        } else {
            boolean created = utentiRegistrati_ondisk.createNewFile();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            if(created)
                utentiRegistrati = new HashMap<>();
            else
                throw new Exception("Couldnt create the configuration file for users");
        }

    }

    /**
     * Restituisce una istanza della classe
     * @return UserManager Una istanza della classe
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
        if(utentiRegistrati.containsKey(nickName) && !utentiRegistrati.get(nickName).isOnline() && (utentiRegistrati.get(nickName).getPassword()).equals(password)){
            utentiRegistrati.get(nickName).setOnline();
            registeredFile.setUtentiRegistrati(utentiRegistrati);
            mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        } else if(password == null){
            throw new Exception("Login failed. Check the password.");
        } else if(!utentiRegistrati.containsKey(nickName)){
            throw new Exception("Login failed. You are not registered.");
        } else if(utentiRegistrati.get(nickName).isOnline()){
            throw new Exception("You are already online, you don't need to login again.");
        }
    }

    /**
     * Effettua il logout dell'utente
     * @param nickName Nickname dell'utente
     */
    public void logout(String nickName) throws IOException {
        if(utentiRegistrati.containsKey(nickName)){
            utentiRegistrati.get(nickName).setOffline();
            registeredFile.setUtentiRegistrati(utentiRegistrati);
            mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        }
    }

    /**
     * Restituisce la lista degli utenti, sia offline che online
     * @return List<User> Lista degli utenti
     */
    public Collection<User> listUsers(){
        return  utentiRegistrati.values();
    }

    /**
     * Restituisce la lista degli utenti online
     * @return List<User> Lista degli utenti
     */
    //todo: puoi migliorarlo con una struttura dati separata
    public List<User> listOnlineUsers(){
        List<User> onlineUsers = new LinkedList<>();
        for(User user: utentiRegistrati.values()){
            if(user.isOnline()){
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }

    /**
     * Restituisce l'utente con il nickname nickutente
     * @param nickutente Nickname dell'utente cercato
     * @return String Restituisce l'utente con il nickname nickutente
     */
    public User getUtente(String nickutente) throws IOException {
        registeredFile.setUtentiRegistrati(utentiRegistrati);
        mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        return utentiRegistrati.get(nickutente);
    }

    /**
     * Restituisce la struttura dati contenente gli utenti registrati
     * @return HashMap<String, User> insieme degli utenti registrati
     */
    public HashMap<String, User> getUtenti(){
        return utentiRegistrati;
    }

    /**
     * Registra l'utente
     * @param nickUtente Nickname dell'utente che sta richiedendo l'operazione
     * @param password Password dell'utente
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    public void register(String nickUtente, String password) throws IOException {
        User verify = utentiRegistrati.putIfAbsent(nickUtente, new User(nickUtente, password));
        if(verify == null) {
            registeredFile.setUtentiRegistrati(utentiRegistrati);
            mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        }
    }
}
