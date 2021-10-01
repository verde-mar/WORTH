package WORTH.client;

import WORTH.shared.Request;
import WORTH.shared.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Classe che implementa l'invio e la ricezione di richieste e risposte, rispettivamente
 */
public class TCPClient implements Client {
    /* Socket necessaria pe rla connessione tcp */
    SocketChannel client;
    ObjectMapper mapper;

    /**
     * Costruttore della classe
     * @param address Indirizzo a cui collegarsi
     * @throws IOException Eccezione nel caso di errore nella connessione
     */
    public TCPClient(SocketAddress address) throws IOException {
        client = SocketChannel.open(address);
    }

    /**
     * Restituisce l'indirizzo locale
     * @return Object Indirizzo locale
     * @throws IOException Eccezione nel caso di errore nella connessione
     */
    public Object getLocalAddress() throws IOException {
        return client.getLocalAddress();
    }

    /**
     * Il client si chiude
     * @throws IOException Eccezione nel caso di errore nella connessione
     */
    public void close() throws IOException {
        client.close();
    }

    /**
     * Invia una richiesta al server
     * @param request Richiesta da inviare al server
     */
    @Override
    public void send(Request request) throws IOException {
        byte[] byteRequest = mapper.writeValueAsBytes(request);
        ByteBuffer request_byte = ByteBuffer.allocate(Integer.BYTES);
        request_byte.putInt(byteRequest.length);
        request_byte.flip();
        while(request_byte.hasRemaining())
            client.write(request_byte);
    }

    /**
     * Riceve la risposta dal server
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore nella comunicazione
     */
    @Override
    public Response receive() throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);
        while(lengthBuffer.hasRemaining())
            client.read(lengthBuffer);
        lengthBuffer.flip();
        ByteBuffer buffer_response = ByteBuffer.allocate(lengthBuffer.getInt());
        while(buffer_response.hasRemaining())
            client.read(buffer_response);
        buffer_response.flip();
        String message = StandardCharsets.UTF_8.decode(buffer_response).toString();

        return mapper.readValue(message, Response.class);
    }
}
