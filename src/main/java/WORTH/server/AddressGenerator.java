package WORTH.server;

import WORTH.Persistence.IPFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AddressGenerator implements Serializable {
    /* Lista degli indirizzi in uso */
    private List<InetAddress> indirizziLiberi;
    @JsonIgnore /* Istanza di AddressGenerator */
    private static AddressGenerator instance;
    private IPFile ipFile;
    private ObjectMapper mapper;
    private File ipsOnDisk;

    /**
     * Costruttore privato della classe, chiamato da getInstance()
     */
    private AddressGenerator() throws Exception {
        ipsOnDisk = new File("./ips.json");
        mapper = new ObjectMapper();
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
        } else {
            try {
                ipFile = mapper.readValue(ipsOnDisk, IPFile.class);
                indirizziLiberi = ipFile.getIndirizziLiberi();
            } catch (Exception e){
                System.out.println("Non c'e' ancora un indirizzo ip libero");
            }
        }
    }

    /**
     * Restituisce l' unica istanza della classe
     * @return AddressGenerator Istanza della classe
     */
    public static synchronized AddressGenerator getInstance() throws Exception {
        if(instance == null)
            instance = new AddressGenerator();
        return instance;
    }

    public synchronized void resetAddress(InetAddress address) throws IOException {
        indirizziLiberi.add(address);
        ipFile.setIndirizziLiberi(indirizziLiberi);
        mapper.writeValue(ipsOnDisk, ipFile);
    }

    public synchronized InetAddress lookForAddress(String projectName) throws IOException {
        InetAddress address;
        if(indirizziLiberi.size() == 0) {
            address = newAddress(projectName);
            System.out.println("IN INDIRIZZILIBERI.SIZE()==0");
        } else {
            address = indirizziLiberi.get(0);
            indirizziLiberi.remove(0);
            ipFile.setIndirizziLiberi(indirizziLiberi);
            mapper.writeValue(ipsOnDisk, ipFile);
        }
        System.out.println("INLOOKFORADDRESS" + address);
        return address;
    }

    public InetAddress newAddress(String projectName) throws UnknownHostException {
        int index = projectName.hashCode();
        return InetAddress.getByName("239." + (index / 256) / 256 + "." + (index / 256) % 256 + "." + index % 256);
    }
}
