package WORTH.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class UserManager {
    /* HashMap degli utenti registrati al servizio */
    private final HashMap<String, User> utenti_registrati;

    /**
     * Costruttore della classe
     * @param utenti_registrati Insieme degli utenti registrati al servizio
     */
    public UserManager(HashMap<String, User> utenti_registrati) { this.utenti_registrati = utenti_registrati; }

    //todo: qui sono contenuti i metodi RMI necessari alla registrazione
    //todo: se un utente si registra non necessariamente e' online, quindi devi settare il flag isOnline a false appena lo registri

    /**
     * Effettua il login dell'utente
     * @param nickName Nickname dell'utente
     * @param password Password dell'utente
     * @throws Exception Nel caso in cui il login non vada a buon fine
     */
    public void login(String nickName, String password) throws Exception {
        if(utenti_registrati.containsKey(nickName) && !utenti_registrati.get(nickName).isOnline() && (utenti_registrati.get(nickName).getPassword()).equals(password)){
            utenti_registrati.get(nickName).setOnline();
        } else if(password == null){
            throw new Exception("Login failed. Check the password.");
        } else if(!utenti_registrati.containsKey(nickName)){
            User user = new User(nickName, password);
            utenti_registrati.put(nickName, user);
            user.setOnline();
            utenti_registrati.replace(nickName, user);
            //throw new Exception("Login failed. You are not registered.");
        } else if(utenti_registrati.get(nickName).isOnline()){
            throw new Exception("You are already online, you don't need to login again.");
        }
    }

    /**
     * Effettua il logout dell'utente
     * @param nickName Nickname dell'utente
     */
    public void logout(String nickName){
        if(utenti_registrati.containsKey(nickName)){
            utenti_registrati.get(nickName).setOffline();
        }
    }

    /**
     * Restituisce la lista degli utenti, sia offline che online
     * @return List<User> Lista degli utenti
     */
    public Collection<User> listUsers(){
        return  utenti_registrati.values();
    }

    /**
     * Restituisce la lista degli utenti online
     * @return List<User> Lista degli utenti
     */
    public List<User> listOnlineUsers(){
        List<User> onlineUsers = new LinkedList<>();
        for(User user: utenti_registrati.values()){
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
    public User getUtente(String nickutente){
        return utenti_registrati.get(nickutente);
    }

    /**
     * Restituisce la struttura dati contenente gli utenti registrati
     * @return HashMap<String, User> insieme degli utenti registrati
     */
    public HashMap<String, User> getUtentiRegistrati(){
        return utenti_registrati;
    }
}
