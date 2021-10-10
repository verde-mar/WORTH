package WORTH.server;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class User implements Serializable {
    /* Password dell'utente */
    private String password;
    /* Lista dei progetti a cui appartiene */
    private LinkedList<Project> list_prj;
    /* Flag che indica se l'utente e' online */
    private boolean online;
    /* Nome dell'utente */
    private String name;

    /**
     * Costruttore necessario a Jackson
     */
    public User(){}

    /**
     * Costruttore della classe
     * @param name Nome dell'utente
     * @param password Password dell'utente
     */
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

    /**
     * Restituisce la lista dei progetti a cui appartiene la lista
     * @return List<String> Lista dei progetti
     */
    public List<Project> getList_prj() {
        return list_prj;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", online=" + online +
                '}';
    }
}
