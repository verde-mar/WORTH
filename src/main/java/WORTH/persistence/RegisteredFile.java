package WORTH.persistence;

import WORTH.server.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Classe ausiliaria per scrivere su disco gli utenti registrati su WORTH
 */
public class RegisteredFile {
    /* Struttura in memoria che si riferisce agli utenti registrati a WORTH */
    private ConcurrentHashMap<String, User> utentiRegistrati;

    public RegisteredFile(){}

    /**
     * Restituisce gli utenti registrati
     * @return HashMap<String, User> Struttura degli utenti registrati
     */
    public ConcurrentHashMap<String, User> getUtentiRegistrati() {
        return utentiRegistrati;
    }

    /**
     * Inizializza la struttura contenente gli utenti registrati
     * @param utenti_registrati Struttura dati contenente gli utenti registrati
     */
    public void setUtentiRegistrati(ConcurrentHashMap<String, User> utenti_registrati) {
        this.utentiRegistrati = utenti_registrati;
    }


}
