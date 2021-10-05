package WORTH.server;

import java.util.HashMap;

public class RegisteredFile {
    private HashMap<String, User> utentiRegistrati;

    public HashMap<String, User> getUtentiRegistrati() {
        return utentiRegistrati;
    }

    public void setUtentiRegistrati(HashMap<String, User> utenti_registrati) {
        this.utentiRegistrati = utenti_registrati;
    }


}
