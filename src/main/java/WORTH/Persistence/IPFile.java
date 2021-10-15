package WORTH.Persistence;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

public class IPFile implements Serializable {
    private List<InetAddress> indirizziLiberi;

    public IPFile(){}

    public List<InetAddress> getIndirizziLiberi() {
        return indirizziLiberi;
    }

    public void setIndirizziLiberi(List<InetAddress> indirizziLiberi) {
        this.indirizziLiberi = indirizziLiberi;
    }
}
