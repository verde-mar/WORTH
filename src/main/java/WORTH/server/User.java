package WORTH.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Vector;

/**
 * Oggetto rappresentante un utente all'interno di WORTH
 */
public class User implements Serializable {
    /* Nome dell'utente */
    private String name;
    /* Password dell'utente */
    private transient String password;
    /* Lista dei progetti a cui appartiene */
    private Vector<Project> list_prj;
    /* Flag che indica se l'utente e' online */
    private boolean online;
    @JsonIgnore /* ID per la serializzazione/deserializzazione della classe */
    private static final long serialVersionUID = -2301496499581089147L;

    /**
     * Costruttore necessario a Jackson per la serializzazione/deserializzazione della classe
     */
    public User(){}

    /**
     * Costruttore della classe
     * @param name Nome dell'utente
     * @param password Password dell'utente
     */
    public User(String name, String password){
        this.password = password;
        list_prj = new Vector<>();
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
     * Mette l'utente corrente online
     */
    public void setOnline() {
        online = true;
    }

    /**
     * Restituisce un valore booleano che indica se l'utente e' online o no
     * @return boolean Se l'utente e' online o no
     */
    public boolean isOnline() {
        return online;
    }

    /**
     * Mette l'utente corrente offline
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
     * Restituisce la lista dei progetti a cui appartiene l'utente
     * @return Vector<String> Vettore dei progetti
     */
    public Vector<Project> getList_prj() {
        return list_prj;
    }

    /**
     * Restituisce solo alcuni campi di User, sovrascrivendo toString()
     * @return String Stringa contenente alcuni campi dello user
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", online=" + online +
                '}';
    }
}
