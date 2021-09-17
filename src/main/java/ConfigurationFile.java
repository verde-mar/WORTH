import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationFile {
    /* Insieme totale dei progetti all'interno del server*/
    private Map<String, Project> all_projects;

    private Map<String, String> register_informations;

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

    public void setRegister_informations(HashMap<String, String> register_informations) {
        this.register_informations = register_informations;
    }

    public Map<String, String> getRegister_informations() {
        return register_informations;
    }

    /**
     * Crea o setta il file JSON di configurazione
     * @throws JsonProcessingException Se si presentano errori nell'interazione con il file JSON
     */
    public void createOrSet() throws IOException {
        File direct = new File("./");
        boolean found = false;
        ObjectMapper mapper = new ObjectMapper();
        File[] fil = direct.listFiles();
        assert fil != null;
        for (File file_corr : fil) {
            String name = file_corr.getName();
            System.out.println(name);
            if (!found) {
                if (file_corr.isDirectory() && name.equals("./WORTH_config")) {
                    found = true;
                    mapper.readValue("./WORTH_config/config.json", ConfigurationFile.class);
                }
            }
        }
        if (!found) {
            File worth = new File("./WORTH_config");
            boolean worth_dir = worth.mkdir();
            mapper.writeValue(new File("./WORTH_config/config.json"), ConfigurationFile.class);
        }

    }
}
