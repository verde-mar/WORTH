package WORTH.Persistence;

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
     *
     * @return
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     *
     * @param ipAddress
     */
    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

}
