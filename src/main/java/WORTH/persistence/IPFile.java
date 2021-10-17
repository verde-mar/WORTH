package WORTH.persistence;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

public class IPFile implements Serializable {
    /* Lista contenente tutti gli indirizzi liberi */
    private List<InetAddress> indirizziLiberi;

    /**
     * Costruttore della classe
     */
    public IPFile(){}

    /**
     * Restituisce la lista di tutti gli indirizzi liberi
     * @return List<InetAddress> Lista di tutti gli indirizzi liberi
     */
    public List<InetAddress> getIndirizziLiberi() {
        return indirizziLiberi;
    }

    /**
     * Inizializza la lista contenente tutti gli indirizzi liberi
     * @param indirizziLiberi Parametro con cui inizializzare la lista contenente tutti gli indirizzi liberi
     */
    public void setIndirizziLiberi(List<InetAddress> indirizziLiberi) {
        this.indirizziLiberi = indirizziLiberi;
    }
}
