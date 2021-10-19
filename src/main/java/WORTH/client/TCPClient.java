package WORTH.client;

import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Classe che implementa l'invio e la ricezione di richieste e risposte
 */
public class TCPClient implements AutoCloseable{
    /* Socket necessaria per la connessione tcp */
    private final SocketChannel client;
    /* Mapper per serializzazione/deserializzazione in JSON */
    private final ObjectMapper mapper;

    /**
     * Costruttore della classe
     * @param address Indirizzo a cui collegarsi
     * @throws IOException Eccezione nel caso di errore nella connessione
     */
    public TCPClient(SocketAddress address) throws IOException {
        client = SocketChannel.open(address);
        mapper = new ObjectMapper();
    }

    /**
     * Chiude il client
     * @throws IOException Eccezione nel caso di errore nella connessione
     */
    public void close() throws IOException {
        client.close();
    }

    /**
     * Invia una richiesta al server
     * @param request Richiesta da inviare al server
     * @throws IOException Nel caso in cui ci sia un errore I/O
     */
    public void send(Request request) throws IOException {
        byte[] arrayRequest = mapper.writeValueAsBytes(request);
        ByteBuffer requestByteBuffer = ByteBuffer.allocate(Integer.BYTES + arrayRequest.length);
        requestByteBuffer.putInt(arrayRequest.length);
        requestByteBuffer.put(arrayRequest);
        requestByteBuffer.flip();
        while(requestByteBuffer.hasRemaining())
            client.write(requestByteBuffer);
    }

    /**
     * Riceve la risposta dal server
     * @return Response La risposta del server
     * @throws IOException Nel caso in cui ci sia un errore I/O
     */
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
