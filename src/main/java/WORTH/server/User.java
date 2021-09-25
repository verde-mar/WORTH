package WORTH.server;

import java.util.LinkedList;
import java.util.List;

//todo:testing
public class User {
    /* Nickname dell'utente */
    private String nickUtente;
    /* Password dell'utente */
    private String password;
    /* Lista dei progetti a cui appartiene */
    private List<String> list_prj;
    /* Flag che indica se l'utente e' online */
    private boolean isOnline;

    public User(String nickUtente, String password){
        this.nickUtente = nickUtente;
        this.password = password;
        list_prj = new LinkedList<>();
        isOnline = false;
    }

    public String getName() {
        return nickUtente;
    }
}
