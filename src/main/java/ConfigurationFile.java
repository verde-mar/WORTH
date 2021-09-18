import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class ConfigurationFile {
    /* Insieme totale dei progetti all'interno del server*/
    private Map<String, Project> all_projects;

    public ConfigurationFile(){   }

    /**
     * Inizializza la struttura locale che indica i progetti totali all'interno del server
     * @param all_projects Struttura dati che indica tutti i progetti
     */
    public void setAll_projects(Map<String, Project> all_projects) {
        this.all_projects = all_projects;
    }

    /**
     * Restituisce tutti i progetti all'interno del server in quel momento
     * @return Map<String, Project> Struttura che indica l'insieme dei progetti nel server
     */
    public Map<String, Project> getAll_projects(){
        return all_projects;
    }

    public static void createOrSet() throws IOException {
        File direct = new File("./WORTH_config");
        if (!direct.exists()) {
            boolean worth_dir = direct.mkdir();
            File config_file = new File("./WORTH_config/config.json");
            if(worth_dir) {
                config_file.createNewFile();
            }
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(Paths.get("./WORTH_config/config.json").toFile(), ConfigurationFile.class);
        }

    }
}
