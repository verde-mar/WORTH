import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationFile {
    private Map<String, Project> all_projects;
    private Map<String, String> register_informations;

    public void setAll_projects(Map<String, Project> all_projects) {
        this.all_projects = all_projects;
    }

    public Map<String, Project> getAll_projects(){
        return all_projects;
    }

    public void setRegister_informations(HashMap<String, String> register_informations) {
        this.register_informations = register_informations;
    }

    public Map<String, String> getRegister_informations() {
        return register_informations;
    }

    public void createOrSet() throws JsonProcessingException {
        File direct = new File("/home/verdemar");
        boolean found = false;

        File[] fil = direct.listFiles();
        assert fil != null;
        for (File file_corr : fil) {
            String name = file_corr.getName();
            if (!found) {
                if (file_corr.isDirectory() && name.equals("/home/verdemar/WORTH")) {
                    found = true;
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.readValue("/home/verdemar/WORTH/config.json", ConfigurationFile.class);
                }
            }
        }
        if (!found) {
            File worth = new File("/home/verdemar/WORTH");
            boolean worth_dir = worth.mkdir();
        }

    }
}
