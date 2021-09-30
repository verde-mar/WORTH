package WORTH.server;

import WORTH.shared.Project;
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

    /**
     * Costruttore vuoto della classe (necessario a jackson)
     */
    public ConfigurationFile(){   }

    /**
     * Inizializza la struttura locale che indica i progetti totali all'interno del WORTH.server e gli utenti registrati
     * @param all_projects Struttura dati che indica tutti i progetti
     */
    public void setAll(Map<String, Project> all_projects, HashMap<String, User> utenti_registrati) {
        this.all_projects = all_projects;
        this.utenti_registrati = utenti_registrati;
    }

    /**
     * Restituisce tutti i progetti all'interno del WORTH.server in quel momento
     * @return Map<String, WORTH.shared.Project> Struttura che indica l'insieme dei progetti nel WORTH.server
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

}
