package WORTH.Persistence;

import WORTH.server.User;

import java.util.HashMap;

public class RegisteredFile {
    /* Struttura in memoria che si riferisce agli utenti registrati al servizio */
    private HashMap<String, User> utentiRegistrati;

    /**
     * Restituisce gli utenti registrati
     * @return HashMap<String, User> Struttura degli utenti registrati
     */
    public HashMap<String, User> getUtentiRegistrati() {
        return utentiRegistrati;
    }

    /**
     * Inizializza la struttura contenente gli utenti registrati
     * @param utenti_registrati Struttura dati contenente gli utenti registrati
     */
    public void setUtentiRegistrati(HashMap<String, User> utenti_registrati) {
        this.utentiRegistrati = utenti_registrati;
    }


}
