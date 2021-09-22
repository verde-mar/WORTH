package WORTH.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigurationFile {
    /* Insieme totale dei progetti all'interno del WORTH.server*/
    private Map<String, Project> all_projects;

    /**
     * Costruttore vuoto della classe (necessario a jackson)
     */
    public ConfigurationFile(){   }

    /**
     * Inizializza la struttura locale che indica i progetti totali all'interno del WORTH.server
     * @param all_projects Struttura dati che indica tutti i progetti
     */
    public void setAll_projects(Map<String, Project> all_projects) {
        this.all_projects = all_projects;
    }

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
}
