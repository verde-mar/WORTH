package WORTH.server;

import java.util.HashMap;

public class UserManager {
    /* HashMap degli utenti registrati al servizio */
    private HashMap<String, User> utenti_registrati;

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
        if(utenti_registrati.containsKey(nickName) && (utenti_registrati.get(nickName).getPassword()).equals(password)){
            utenti_registrati.get(nickName).setOnline();
        } else if(password == null){
            throw new Exception("Login failed. Check the password.");
        } else if(!utenti_registrati.containsKey(nickName)){
            throw new Exception("Login failed. You are not registered.");
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
}
