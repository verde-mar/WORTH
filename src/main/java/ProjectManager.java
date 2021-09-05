import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectManager implements Callable<Tasks>  {
    /* Buffer contenente i dati letti */
    private final ByteBuffer buffer;
    /* Oggetto che rappresenta una richiesta del client */
    private Tasks task_request;
    /* Oggetto che rappresenta una risposta del client */
    private Tasks task_response;
    /* Oggetto che rappresenta l'insieme di progetti minchia*/
    private ConcurrentHashMap<String, Project> projects;
    /* Oggetto necessario per la serializzazione dei file JSON, formato usato sia per risposte e per richieste */
    ObjectMapper objectMapper;
    private ProjectWorker worker;

    /***
     * Costruttore dell'oggetto
     * @param buffer contenente la richiesta
     */
    public ProjectManager(ByteBuffer buffer, ConcurrentHashMap<String, Project> projects) {
        this.buffer = buffer;
        this.projects = projects;
        objectMapper = new ObjectMapper();
    }

    /***
     * Effettua il parsing della richiesta
     * @throws IOException se ci sono errori nell' I/O
     */
    private void parser() throws IOException {
        /* Viene decodificata e letta la richiesta. Poi dalla deserializzazione viene creato un oggetto di tipo Tasks */
        String buffer_toString = StandardCharsets.UTF_8.decode(buffer).toString();
        task_request = objectMapper.readValue(buffer_toString, Tasks.class);
    }

    private void response(boolean outcome_b, String outcome_s){
        if(outcome_b){
            task_response.setSuccess();
        } else {
            task_response.setFailure(outcome_s);
        }
    }

    /***
     * Override della chiamata call
     * @return Tasks cioe' la risposta in formato JSON
     */
    @Override
    public Tasks call() {
        try{
            response(true, "success");
        } catch(Exception e){
            response(false, e.getLocalizedMessage());
        }
        return task_response;
    }
}
