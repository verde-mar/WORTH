package WORTH.server;

import WORTH.shared.rmi.RemoteInterface;
import WORTH.shared.worthProtocol.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/**
 * SocketServices e' la classe del server che tramite un selettore
 * gestisce le nuove connessioni TCP
 */
public class SocketServices implements AutoCloseable{
    /* Selettore che legge i socket */
    private final Selector selector;

    /* Welcoming socket */
    private final ServerSocketChannel channel;

    /* Threadpool a cui assegnare i task */
    private final ThreadPoolExecutor threadPool;

    /* Mapper necessario alla serializzazione/deserializzazione JSON */
    private final ObjectMapper mapper;

    /* Progetti sul server */
    private final ConcurrentHashMap<String, Project> projects;

    /**
     * Costruttore della classe
     * @param portNumber Numero di porta del servizio TCP
     * @param projects Progetti iniziali gia' presenti sul server
     * @throws IOException Nel caso in cui ci sia un errore di tipo I/O
     */
    public SocketServices(int portNumber, ConcurrentHashMap<String, Project> projects) throws IOException {
        /* Inizializzazione del threadpool */
        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        /* Crea il selettore */
        selector = Selector.open();

        /* Crea il welcoming socket */
        channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress("localhost", portNumber));

        /* Registra il channel */
        channel.register(selector, SelectionKey.OP_ACCEPT);

        mapper = new ObjectMapper();

        this.projects = projects;
    }

    /**
     * La funzione associa ad ogni richiesta di un WORTH.client un thread del threadPool
     * @param buffer contenente la richiesta in JSON
     * @return Future<WORTH.server.Message> un thread del threadpool
     */
    private Future<Response> elaborateRequest(ByteBuffer buffer) throws Exception {
        return threadPool.submit(new Handler(buffer, projects));
    }

    /**
     * La funzione fa girare il selector finche' il thread main non viene interrotto
     * @throws IOException se avviene un errore nell'I/O
     * @throws InterruptedException se avviene un errore nella lettura del messaggio in readMessage(key)
     */
    public void start() throws Exception {
        System.out.printf("TCP Server started on local address %s\n", channel.getLocalAddress());
        registerRMIService(8081);
        while (!Thread.interrupted()){
            selector.select();

            /* Iteratore sulle chiavi pronte */
            Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
            while (keysIterator.hasNext()) {
                /* Chiave corrente che viene rimossa dal selected set */
                SelectionKey key = keysIterator.next();
                keysIterator.remove();

                if(!key.isValid()){
                    return;
                }
                /* Registrazione del WORTH.client */
                if(key.isAcceptable()){
                    registerClient(key);
                }
                /* La chiave e' pronta in lettura */
                else if(key.isReadable()){
                    readMessage(key);
                }

                /* La chiave e' pronta in scrittura */
                else if(key.isWritable()){
                    writeMessage(key);
                }
            }
        }
    }

    /**
     * Registra quel WORTH.client per operazioni in lettura
     * @param key chiave associata al channel
     * @throws IOException se avviene un errore nell'I/O
     */
    private void registerClient(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        SocketChannel client = server.accept();
        client.configureBlocking(false);

        /* Registrazione della chiave per la lettura */
        SelectionKey reg_key = client.register(selector, SelectionKey.OP_READ);
        reg_key.attach(buffer);
        System.out.println("Accepted connection from " + client);
    }

    /**
     * Legge il messaggio inviato dal WORTH.client
     * @param key chiave associata al
     * @throws IOException se avviene un errore nell'I/O
     * @throws InterruptedException se il thread viene interrotto durante la sleep()
     */
    private void readMessage(SelectionKey key) throws Exception {
        /* Contiene i dati */
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        /* Active socket associato al WORTH.client */
        SocketChannel client_read = (SocketChannel) key.channel();

        /*Legge i dati */
        int bytesRead = client_read.read(buffer);
        System.out.printf("Read %d bytes from %s\n", bytesRead, client_read.getRemoteAddress());

        /* Il WORTH.client ha terminato */
        if (bytesRead == -1) {
            /* Cancella la chiave dal selettore e chiude la connessione */
            key.cancel();
            client_read.close();
            return;
        }

        /* Se deve leggere altro, esce e lo fara' la volta seguente */
        if (buffer.hasRemaining()) {
            System.out.println("Read not finished yet\n");
            return;
        }

        /* Mette il buffer in modalita' lettura */
        buffer.flip();

        /* Se e' stata letta la dimensione della richiesta */
        if (buffer.array().length == Integer.BYTES) {
            int size = buffer.getInt();
            System.out.printf("Waiting for a message %d bytes long\n", size);

            /* Alloca la dimensione del messaggio nel buffer che lo conterra' */
            buffer = ByteBuffer.allocate(size);
            key.attach(buffer);

        }
        /* Altrimenti se e' stato letto il messaggio */
        else {
            /* Dorme per 200 ms, per distanziare le submit */
            sleep(200);

            /* Elabora la richiesta */
            Future<Response> future = elaborateRequest(buffer);

            /* Registra la chiave per la scrittura */
            SelectionKey reg_key = client_read.register(selector, SelectionKey.OP_WRITE);
            reg_key.attach(future);

            System.out.printf("Just read message from %s\n", client_read.getRemoteAddress());
        }
    }

    /**
     * Attende che il messaggio sia pronto e poi lo scrive a blocchi
     * @param key Chiave che rappresenta il WORTH.client pronto per la scrittura
     * @throws IOException Se c'è un problema a scrivere il messaggio
     * @throws ExecutionException Se c'e' stato un errore nella computazione
     * @throws InterruptedException Se il thread e' stato interrotto
     */
    private void writeMessage(SelectionKey key) throws IOException, ExecutionException, InterruptedException {
        /* Oggetto che rappresenta i dati da scrivere (non è possibile evitare l'unchecked warning) */
        @SuppressWarnings("unchecked")
        Future<Response> future = (Future<Response>) key.attachment();
        if (!future.isDone())
            return;
        /* Active socket associato al WORTH.client */
        SocketChannel client = (SocketChannel) key.channel();

        /* Si fa restituire la risposta in formato WORTH.server.Message, e crea il file JSON da inviare al WORTH.client */
        Response response_t = future.get();
        byte[] byteResponse = mapper.writeValueAsBytes(response_t);
        ByteBuffer response = ByteBuffer.allocate(Integer.BYTES);

        /* Invia la size della risposta al WORTH.client */
        response.putInt(byteResponse.length);
        response.flip();
        client.write(response);

        /* Se non ha ancora finito esce (rientrerà nella funzione per scrivere ancora) */
        if(response.hasRemaining())
            return;

        /* Inserisce la risposta e la invia */
        response = ByteBuffer.allocate(byteResponse.length);
        response.put(byteResponse);
        response.flip();
        client.write(response);

        /* Se non ha ancora finito esce (rientrerà nella funzione per scrivere ancora) */
        if (response.hasRemaining())
            client.write(response);
        System.out.printf("Just sent message to %s\n", client.getRemoteAddress());

        /* Buffer necessario a leggere la dimensione del messaggio */
        ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);

        /* Altrimenti registra di nuovo il WORTH.client per la lettura */
        SelectionKey wr_key = client.register(selector, SelectionKey.OP_READ);
        wr_key.attach(lengthBuffer);
    }

    /**
     * Effettua la close() della classe
     * @throws IOException Nel caso di un errore IO
     */
    @Override
    public void close() throws IOException {
        selector.close();
        channel.close();
    }

    /**
     * Inizializzazione degli oggetti necessari al servizio RMI
     * @param portNumber Numero di porta necessaria all'esportazione degli oggetti
     * @throws Exception Nel cas
     */
    public void registerRMIService(int portNumber) throws Exception {
        RemoteInterface server = new RemoteRegister();
        RemoteInterface stub = (RemoteInterface) UnicastRemoteObject.exportObject(server, 0);

        Registry registry = LocateRegistry.createRegistry(portNumber);
        registry.rebind("RegistrationService", stub);
        System.out.printf("[REGISTER] Register service is started on port %d\n", portNumber);


    }
}
