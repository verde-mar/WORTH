package WORTH.client;

import java.net.InetAddress;
import java.util.Vector;

/**
 *
 */
public class ProjectChat {
    /* Indirizzo IP da associare al progetto */
    private InetAddress address;
    /* Vettore dei messaggi da leggere */
    private final Vector<String> messages;


    /**
     * Costruttore della classe
     */
    public ProjectChat(){
        messages = new Vector<>();
    }


    /**
     * Inizializza l'indirizzo IP a cui associare il buffer di messaggi
     * @param address Indirizzo IP associato al progetto
     */
    public void setAddress(InetAddress address) {
        this.address = address;
    }

    /**
     * Aggiunge un messaggio al vettore
     * @param text Messaggio
     */
    public void addMessages(String text) {
        messages.add(text);
    }

    /**
     * Restituisce il vettore dei messaggi
     * @return Vector<String> Vettore dei messaggi
     */
    public Vector<String> getMessages() {
        return messages;
    }

    /**
     * Restituisce l'indirizzo IP
     * @return InetAddress L'indirizzo IP
     */
    public InetAddress getAddress() {
        return address;
    }

}