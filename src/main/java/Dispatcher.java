import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Integer.parseInt;

public class Dispatcher implements Callable<Tasks> {
    /* Buffer contenente i dati letti */
    private final ByteBuffer buffer;
    /* Oggetto che rappresenta una richiesta/risposta del client */
    private Tasks task;
    private ConcurrentHashMap<String, Project> projects;

    /***
     * Costruttore dell'oggetto
     * @param buffer contenente la richiesta
     */
    public Dispatcher(ByteBuffer buffer, ConcurrentHashMap<String, Project> projects) {
        this.buffer = buffer;
        this.projects = projects;
    }

    /***
     * Effettua il parsing della richiesta
     * @throws IOException se ci sono errori nell' I/O
     */
    private void parser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        /* Viene decodificata e letta la richiesta. Poi dalla deserializzazione viene creato un oggetto di tipo Tasks */
        String buffer_toString = StandardCharsets.UTF_8.decode(buffer).toString();
        task = objectMapper.readValue(buffer_toString, Tasks.class);
    }

    private void createProject(Project project){
        Project result = projects.putIfAbsent(project.getNameProject(), project);
        if(result!=null) System.out.println("C'era gia'");
        else System.out.println("Appena inserito un progetto");
    }

    private void cancelProject(Project project){
        projects.remove(project.getNameProject(), project);
    }

    /***
     * Override della chiamata call
     * @return Tasks cioe' la risposta in formato JSON
     */
    @Override
    public Tasks call() throws Exception {
        parser();
        if(task.getRequest().equals("createProject")){
            Project project = new Project(task.getProjectName(), new User()); //Questo utente deve essere definito nella registrazione
            createProject(project);
        }
        //esecuzione --> prevede di prendere l'istanza task e di fare l'esecuzione richiesta
        //task = response;
        return task;
    }
}
