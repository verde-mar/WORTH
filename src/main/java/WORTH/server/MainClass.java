package WORTH.server;

import WORTH.Persistence.CardFile;
import WORTH.Persistence.UserFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

//todo: se uno scrittore sta scrivendo e un lettore vuole leggere, il lettore rischia di leggere le informazioni vecchie. E' un problema?
public class MainClass {
    public static void main(String[] args) throws Exception {
        ConcurrentHashMap<String, Project> projects = new ConcurrentHashMap<>();
        set(projects);
        try (SocketServices server = new SocketServices(8080, projects)) {
            server.start();

        } catch (Exception e) {
            System.err.println("Error in start operation");
            e.printStackTrace();
        }
    }

    /**
     * Inizializza le strutture dati in memoria
     * @param projects Insieme dei progetti nel server
     * @throws Exception
     */
    private static void set(ConcurrentHashMap<String, Project> projects) throws Exception {
        File direct = new File("./projects");
        if (!direct.exists()) {
            boolean worth_dir = direct.mkdir();
            if(!worth_dir)
                throw new Exception("Couldnt create the directory 'projects'");
        } else {
            setProject(direct, projects);
        }
    }

    private static void setProject(File direct, ConcurrentHashMap<String, Project> projects) throws Exception {
        if (direct.isDirectory()) {
            File[] fil = Objects.requireNonNull(direct).listFiles();
            assert fil != null;
            /* Scorre ogni progetto */
            for (File file_corr : fil) {
                String name = file_corr.getName();
                /* Controlla se il progetto corrente e' una directory */
                if (file_corr.isDirectory()) {
                    Project project = new Project(name);
                    Project prj = projects.putIfAbsent(name, project);
                    if(prj==null) {
                        /* Scorre il file contenente i membri del progetto, e li aggiunge in memoria */
                        setUsers(name, project);
                        /* Scorre i file rappresentanti le card del progetto, e li aggiunge in memoria */
                        setCards(project, file_corr);
                    }
                }
            }
        }
    }

    /**
     * Scorre il file contenente i membri del progetto, e li aggiunge in memoria
     * @param name Nome del progetto
     * @param project Il progetto corrente
     * @throws Exception
     */
    private static void setUsers(String name, Project project) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        File members = new File("./projects/" + name + "/members.json");
        UserFile mbrs = mapper.readValue(members, UserFile.class);
        project.addMembers(mbrs);

    }

    /**
     * Scorre i file rappresentanti le card del progetto, e li aggiunge in memoria
     * @param project Progetto corrente
     * @param currentDirectory Directory corrente
     * @throws Exception
     */
    private static void setCards(Project project, File currentDirectory) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        if(currentDirectory.isDirectory()){
            File[] filesName = currentDirectory.listFiles();
            assert filesName != null;
            for (File curr_f : filesName) {
                if(!curr_f.isDirectory() && !curr_f.getName().equals("members.json")) {
                    CardFile cardFile = mapper.readValue(curr_f, CardFile.class);
                    int endIndex = curr_f.getName().indexOf(".");
                    project.addCard(curr_f.getName().substring(0, endIndex), cardFile.getDescription(), cardFile.getCurrentList());
                }
            }
        }
    }
}