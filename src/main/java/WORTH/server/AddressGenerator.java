package WORTH.server;

import WORTH.persistence.IPFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

/**
 * Classe che si occupa di gestire gli indirizzi IP da assegnare ai progetti
 */
public class AddressGenerator implements Serializable {
    /* Lista degli indirizzi in uso */
    private List<InetAddress> indirizziLiberi;
    @JsonIgnore /* Istanza di AddressGenerator */
    private static AddressGenerator instance;
    @JsonIgnore/* Istanza della classe ausiliaria per serializzazione/deserializzazione su disco degli indirizzi IP liberi */
    private IPFile ipFile;
    @JsonIgnore /* Necessario per la serializzazione/deserializzazione JSON */
    private final ObjectMapper mapper;
    /* File contenente gli indirizzi IP liberi */
    private final File ipsOnDisk;
    @JsonIgnore/* ID per la serializzazione/deserializzazione della classe */
    private static final long serialVersionUID = -1545255945990735659L;

    /**
     * Costruttore privato della classe, chiamato da getInstance()
     * @throws Exception Nel caso in cui la creazione dell'istanza non vada a buon fine
     */
    private AddressGenerator() throws Exception {
        ipsOnDisk = new File("./ips.json");
        mapper = new ObjectMapper();
        /* Se il file non esiste */
        if(!ipsOnDisk.exists()){
            boolean created = ipsOnDisk.createNewFile();
            /* Se viene creato con successo, anche la struttura in memoria viene inizializzata */
            if(created) {
                ipFile = new IPFile();
                indirizziLiberi = new Vector<>();
            }
            /* Se invece la creazione fallisce, viene lanciata una eccezione */
            else
                throw new Exception("The creating of the configuration file for ips went wrong");
        }
        /* Se il file esiste gia' */
        else {
            try {
                /* Il file non e' vuoto, quindi e' possibile leggerci */
                ipFile = mapper.readValue(ipsOnDisk, IPFile.class);
                indirizziLiberi = ipFile.getIndirizziLiberi();
            } catch (Exception e){
                /* Il file e' vuoto */
                System.out.println("[UDP] No used ips available yet");
            }
        }
    }

    /**
     * Restituisce l' unica istanza della classe
     * @return AddressGenerator Istanza della classe
     * @throws Exception Nel caso in cui la creazione dell'istanza non vada a buon fine
     */
    public static synchronized AddressGenerator getInstance() throws Exception {
        if(instance == null)
            instance = new AddressGenerator();
        return instance;
    }

    /**
     * Aggiunge l'indirizzo passato come parametro tra quelli liberi perche' il progetto a cui era associato e' stato cancellato
     * @param address Indirizzo IP associato ad un progetto
     * @throws IOException Nel caso di un errore nella scrittura su file
     */
    public synchronized void resetAddress(InetAddress address) throws IOException {
        /* Aggiunge l'indirizzo passato come parametro agli indirizzi liberi */
        indirizziLiberi.add(address);
        /* Aggiorna il file su disco */
        ipFile.setIndirizziLiberi(indirizziLiberi);
        mapper.writeValue(ipsOnDisk, ipFile);
    }

    /**
     * Cerca un indirizzo libero: se lo trova lo restituisce, altrimenti ne crea uno nuovo
     * @param projectName Nome del progetto a cui associare l'indirizzo IP
     * @return InetAddress Indirizzo IP per il progetto
     * @throws IOException Nel caso di un errore nella scrittura su disco
     */
    public synchronized InetAddress lookForAddress(String projectName) throws IOException {
        InetAddress address;
        if(indirizziLiberi == null || indirizziLiberi.size() == 0) {
            address = newAddress(projectName);
        } else {
            /* Preleva il primo indirizzo dalla lista */
            address = indirizziLiberi.get(0);
            indirizziLiberi.remove(0);
            /* Aggiorna il file su disco */
            ipFile.setIndirizziLiberi(indirizziLiberi);
            mapper.writeValue(ipsOnDisk, ipFile);
        }
        return address;
    }

    /**
     * Crea un nuovo indirizzo IP
     * @param projectName Nome del progetto a cui assegnare l'indirizzo IP
     * @return InetAddress Indirizzo IP da assegnare al progetto
     * @throws UnknownHostException Nel caso di un errore nel determinare l'indirizzo IP
     */
    public InetAddress newAddress(String projectName) throws UnknownHostException {
        int index = (projectName.hashCode()) / 256;
        return InetAddress.getByName("239." + Math.abs(((index / 256) / 256)) + "." + Math.abs(((index / 256) % 256)) + "." + Math.abs(index % 256));
    }
}
