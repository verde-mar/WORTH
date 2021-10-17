package WORTH.persistence;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

public class ProjectUtils implements Serializable {
    /* Lista dei nomi degli utenti */
    private List<String> utenti;
    /* Indirizzo IP associato al progetto */
    private InetAddress ipAddress;

    /**
     * Restituisce la lista dei nomi degli utenti
     * @return List<String> Lista dei nomi degli utenti
     */
    public List<String> getUtenti() {
        return utenti;
    }

    /**
     * Inizializza la struttura dati contenente i nomi degli utenti
     * @param utenti Lista dei nomi degli utenti
     */
    public void setUtenti(List<String> utenti) {
        this.utenti = utenti;
    }

    /**
     * Restituisce l'indirizzo IP del progetto
     * @return InetAddress Indirizzo IP del progetto
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Inizializza l'indirizzo IP del progetto
     * @param ipAddress Indirizzo IP
     */
    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

}
