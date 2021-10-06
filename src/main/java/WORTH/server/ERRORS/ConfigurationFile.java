package WORTH.server.ERRORS;

import WORTH.server.Project;
import WORTH.server.User;
import WORTH.server.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * ConfigurationFile e' la classe che rappresenta
 * il file di configurazione usato per inizializzare le strutture dati locali del server all'inizio
 */
public class ConfigurationFile {
    /* Insieme totale dei progetti all'interno del WORTH.server*/
    private Map<String, Project> all_projects;
    /* Insieme totale degli utenti registrati */
    private Map<String, User> utenti_registrati;
    private static ConfigurationFile instance;

    /**
     * Costruttore vuoto della classe (necessario a jackson)
     */
    public ConfigurationFile(){   }

    /**
     * Restituisce tutti i progetti all'interno del WORTH.server in quel momento
     * @return Map<String, WORTH.server.Project> Struttura che indica l'insieme dei progetti nel WORTH.server
     */
    public Map<String, Project> getAll_projects(){
        return all_projects;
    }

    /**
     * Se non esiste la directory contenente i file di configurazione la crea, e cosi' crea anche i file;
     * altrimenti li legge per inzializzare le strutture dati locali
     * @throws IOException Nel caso in cui ci sia un errore di IO
     */
    public static void createOrSet() throws IOException {
        File direct = new File("./WORTH_config");
        if (!direct.exists()) {
            boolean worth_dir = direct.mkdir();
            File config_file = new File("./WORTH_config/config.json");
            if(worth_dir) {
                boolean new_file = config_file.createNewFile();
                System.out.println(new_file + ": was created a new config file");
            }
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(Paths.get("./WORTH_config/config.json").toFile(), ConfigurationFile.class);
        }
    }

    /**
     * Restituisce gli utenti registrati
     * @return Map<String, User> Insieme degli utenti registrati
     */
    public Map<String, User> getUtenti_registrati() {
        return utenti_registrati;
    }

    public void setAll_projects(Map<String, Project> all_projects) {
        this.all_projects = all_projects;
    }

    public void setUtenti_registrati(Map<String, User> utenti_registrati) {
        this.utenti_registrati = utenti_registrati;
    }
}