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

    public Dispatcher(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    private void parser() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        /* Viene decodificata e letta la richiesta. Poi dalla deserializzazione viene creato un oggetto di tipo Tasks */
        String buffer_toString = StandardCharsets.UTF_8.decode(buffer).toString();
        task = objectMapper.readValue(buffer_toString, Tasks.class);
    }


    @Override
    public Tasks call() throws Exception {
        parser();
        Project project = new Project("cane");
        //esecuzione --> prevede di prendere l'istanza task e di fare l'esecuzione richiesta
        //task = response;
        return task;
    }
}
