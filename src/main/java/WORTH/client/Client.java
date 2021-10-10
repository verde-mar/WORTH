package WORTH.client;

import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;

import java.io.IOException;

public interface Client extends AutoCloseable {
    /**
     * Invia la richiesta al server
     * @param request La richiesta per il server
     * @throws IOException In caso di un errore nella comunicazione
     */
    void send(Request request) throws IOException;


    /**
     * Riceve la risposta dal server
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore nella comunicazione
     */
    Response receive() throws IOException;
}
