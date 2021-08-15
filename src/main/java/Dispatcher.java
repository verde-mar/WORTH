import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class Dispatcher implements Callable<Tasks> {
    /* Buffer contenente i dati letti */
    private ByteBuffer buffer;
    /* Oggetto che rappresenta una richiesta/risposta del client */
    private Tasks task;

    /***
     * Costruttore dell'oggetto
     * @param buffer contenente la richiesta
     */
    public Dispatcher(ByteBuffer buffer) {
        this.buffer = buffer;
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

    /***
     * Override della chiamata call
     * @return Tasks cioe' la risposta in formato JSON
     */
    @Override
    public Tasks call() throws Exception {
        parser();
        if(task.getRequest().equals("createProject")){
            Project project = new Project(task.getProjectName(), new User());
            Card card = new Card(task.getCardName());
            project.addCard("toDo", card);
            card.addHistory("toDo");
        }
        //esecuzione --> prevede di prendere l'istanza task e di fare l'esecuzione richiesta
        //task = response;
        return task;
    }
}
