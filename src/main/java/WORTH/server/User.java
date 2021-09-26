package WORTH.server;

import java.util.LinkedList;
import java.util.List;

//todo:testing
public class User {
    /* Password dell'utente */
    private String password;
    /* Lista dei progetti a cui appartiene */
    private List<String> list_prj;
    /* Flag che indica se l'utente e' online */
    private boolean online;
    private String name;

    public User(){}

    public User(String name, String password){
        this.password = password;
        list_prj = new LinkedList<>();
        online = false;
        this.name = name;
    }


    /**
     * Restituisce la password
     * @return String Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Assegna al valore online true
     */
    public void setOnline() {
        online = true;
    }

    /**
     * Restituisce online
     * @return boolean L'attributo online
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Assegna al valore online false
     */
    public void setOffline() {
        online = false;
    }

    /**
     * Restituisce il nickname dell'utente
     * @return String Il nickname dell'utente
     */
    public String getName() {
        return name;
    }
}
