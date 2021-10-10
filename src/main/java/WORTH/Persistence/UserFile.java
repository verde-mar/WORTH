package WORTH.Persistence;

import java.io.Serializable;
import java.util.List;

public class UserFile implements Serializable {
    private List<String> utenti;

    public List<String> getUtenti() {
        return utenti;
    }

    public void setUtenti(List<String> utenti) {
        this.utenti = utenti;
    }
}
