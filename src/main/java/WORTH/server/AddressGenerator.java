package WORTH.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Vector;

public class AddressGenerator implements Serializable {
    /* Lista degli indirizzi in uso */
    private final List<Address> indirizziInUso;
    /* Otteti necessari alla creazione degli indirizzi IP */
    private int ott1, ott2, ott3, ott4;
    /* Istanza di AddressGenerator */
    private static AddressGenerator instance;

    /**
     * Costruttore privato della classe, chiamato da getInstance()
     */
    private AddressGenerator(){
        indirizziInUso = new Vector<>();
        ott1 = 239;
        ott2 = 0;
        ott3 = 0;
        ott4 = 0;
    }

    /**
     * Restituisce l' unica istanza della classe
     * @return AddressGenerator Istanza della classe
     */
    public static synchronized AddressGenerator getInstance() {
        if(instance == null)
            instance = new AddressGenerator();
        return instance;
    }

    /**
     * La funzione restituisce un indirizzo IP da utilizzare.
     * Prima cerca tra quelli gia' creati se ce n'e' di disponibili, altrimenti ne crea uno nuovo e lo aggiunge tra quelli in uso
     * @return InetAddress Indirizzo IP
     * @throws UnknownHostException Nel caso non esista un hosto associato a quell'indirizzo
     */
    public synchronized InetAddress lookForAddress() throws UnknownHostException {
        /* Cerca un indirizzo IP disponibile */
        for(Address address : indirizziInUso){
            if(!address.isTaken())
                return address.getAddress();
        }
        /* Se non trova l'indirizzo tra quelli gia' in uso lo crea e lo aggiunge tra quelli in uso */
        Address address = new Address(AddAddress());
        indirizziInUso.add(address);

        return address.getAddress();
    }

    /**
     * Inizializza la variabile di un oggetto Address a false
     * @param lookingAddress L'indirizzo che sara' di nuovo disponibile dopo aver inizializzato la variabile associata a false
     */
    public void setFalse(InetAddress lookingAddress){
        for(Address address : indirizziInUso){
            if(lookingAddress.equals(address.getAddress())){
                address.setTaken(false);
            }
        }
    }

    /**
     * Crea nuovi indirizzi da poter utilizzare per la comunicazione UDP
     * @return InetAddress Un indirizzo IP per l'indirizzo creato
     * @throws UnknownHostException Nel caso non esista un hosto associato a quell'indirizzo
     */
    public InetAddress AddAddress() throws UnknownHostException {
        /* Crea nuovi indirizzi */
        if(ott4<255){
            ott4++;
        } else if(ott3 < 255){
            ott4 = 1;
            ott3++;
        } else if(ott2 < 255){
            ott4 = 1;
            ott3 = 1;
            ott2++;
        } else if(ott1<239){
            ott4 = 1;
            ott3 = 1;
            ott2 = 1;
            ott1++;
        }
        return InetAddress.getByName(ott1 + "." + ott2 + "." + ott3 + "." + ott4);
    }

    /**
     *
     */
    private static class Address implements Serializable{
        /* Variabile che indica se quell'indirizzo e' disponibile oppure no */
        private boolean taken;
        /* Indirizzo IP */
        private final InetAddress address;

        /**
         * Costruttore della classe: un nuovo oggetto di tipo Address e' stato creato
         * @param address Indirizzo IP da assegnare a this.address
         */
        public Address(InetAddress address){
            taken = true;
            this.address = address;
        }

        /**
         * Inizializza la variabile taken
         * @param taken Variabile da assegnare a this.taken
         */
        public void setTaken(boolean taken) {
            this.taken = taken;
        }

        /**
         * Restituisce l'indirizzo IP
         * @return InetAddress L'indirizzo IP
         */
        public InetAddress getAddress() {
            return address;
        }

        /**
         * Verifica se un determinato indirizzo IP e' gia' stato assegnato
         * @return boolean Se l'indirizzo IP considerato e' gia' stato assegnato
         */
        public boolean isTaken() {
            return taken;
        }
    }
}
