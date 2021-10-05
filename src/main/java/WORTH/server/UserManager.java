package WORTH.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserManager {
    /* HashMap degli utenti registrati al servizio */
    private final HashMap<String, User> utentiRegistrati;
    /* Unica istanza di AccountService che può essere presente nel sistema */
    private static UserManager instance;

    private final ObjectMapper mapper;
    private final File utentiRegistrati_ondisk;
    private RegisteredFile registeredFile;



    private UserManager() throws Exception {
        mapper = new ObjectMapper();
        utentiRegistrati_ondisk = new File("./" + "utentiRegistrati_ondisk.json");
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
        System.out.println(getUtenti());
        if(utentiRegistrati.containsKey(nickName) && !utentiRegistrati.get(nickName).isOnline() && (utentiRegistrati.get(nickName).getPassword()).equals(password)){
            utentiRegistrati.get(nickName).setOnline();
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
    public void logout(String nickName){
        System.out.println(utentiRegistrati);
        if(utentiRegistrati.containsKey(nickName)){
            System.out.println("DENTRO L'IF DI LOGOUT");
            utentiRegistrati.get(nickName).setOffline();
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

    public void register(String nickUtente, String password) throws IOException {
        User verify = utentiRegistrati.putIfAbsent(nickUtente, new User(nickUtente, password));
        if(verify == null) {
            registeredFile.setUtentiRegistrati(utentiRegistrati);
            mapper.writeValue(utentiRegistrati_ondisk, registeredFile);
        }
    }
}
