package WORTH.server;


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
     * Se non esiste gia', crea la directory 'projects' dove saranno memorizzatu tutti i progetti.
     * Poi chiama una funzione ausiliaria per inizializzare i progetti in memoria
     * @param projects Struttura dati in memoria contenente i progetti degli utenti
     * @throws Exception Nel caso in cui non possa essere creata la directory dei progetti 'projects'
     */
    private static void set(ConcurrentHashMap<String, Project> projects) throws Exception {
        File direct = new File("./projects");
        /* Se la directory non esiste gia' */
        if (!direct.exists()) {
            /* La directory viene creata */
            boolean worth_dir = direct.mkdir();
            if(!worth_dir)
                throw new Exception("Couldnt create the directory 'projects'");
        } else {
            setProject(direct, projects);
        }
    }

    /**
     * La funzione inizializza i progetti in memoria
     * @param direct Il file della directory
     * @param projects Struttura in memoria che conterra' i progetti nel server
     * @throws Exception Nel caso in cui non possa essere creato un nuovo progetto in memoria
     */
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
                    if(prj == null){
                        System.out.println("A new project was added");
                    } else {
                        System.out.println("The project was already there");
                    }
                }
            }
        }
    }
}