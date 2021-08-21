import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.Thread.sleep;

public class SocketServices implements AutoCloseable{
    /* Selettore */
    private final Selector selector;

    /* Welcoming socket */
    private final ServerSocketChannel channel;

    /* Threadpool a cui assegnare i task */
    private ThreadPoolExecutor threadPool;

    private ConcurrentHashMap<Integer, Project> projects;

    /***
     * Costruttore della classe
     * @param portNumber numero di porta del servizio offerto dal server
     * @throws IOException se avviene un errore nell' I/O
     */
    public SocketServices(int portNumber) throws IOException {
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

        /* Crea la struttura dati per i progetti */
        projects = new ConcurrentHashMap<>();
    }

    /***
     * La funzione associa ad ogni richiesta di un client un thread del threadPool
     * @param buffer contenente la richiesta in JSON
     * @return Future<Tasks> un thread del threadpool
     */
    private Future<Tasks> elaborateRequest(ByteBuffer buffer) {
        return threadPool.submit(new Dispatcher(buffer, projects));
    }

    /***
     * La funzione fa girare il selector finche' il thread main non viene interrotto
     * @throws IOException se avviene un errore nell'I/O
     * @throws InterruptedException se avviene un errore nella lettura del messaggio in readMessage(key)
     */
    public void start() throws IOException, InterruptedException {
        System.out.printf("TCP Server started on local address %s\n", channel.getLocalAddress());
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
                /* Registrazione del client */
                if(key.isAcceptable()){
                    registerClient(key);
                }
                /* La chiave e' pronta in lettura */
                if(key.isReadable()){
                    readMessage(key);
                }
                /* La chiave e' pronta in scrittura */
                // if(key.isWritable()){
                //    writeMessage(key);
                //}
            }
        }
    }

    /***
     * Registra quel client per operazioni in lettura
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

    /***
     * Legge il messaggio inviato dal client
     * @param key chiave associata al
     * @throws IOException se avviene un errore nell'I/O
     * @throws InterruptedException se il thread viene interrotto durante la sleep()
     */
    private void readMessage(SelectionKey key) throws IOException, InterruptedException {
        /* Contiene i dati */
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        /* Active socket associato al client */
        SocketChannel client_read = (SocketChannel) key.channel();

        /*Legge i dati */
        int bytesRead = client_read.read(buffer);
        System.out.printf("Read %d bytes from %s\n", bytesRead, client_read.getRemoteAddress());

        /* Il client ha terminato */
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
            Future<Tasks> future = elaborateRequest(buffer);

            /* Registra la chiave per la scrittura */
            SelectionKey writekey = client_read.register(selector, SelectionKey.OP_WRITE);
            writekey.attach(future);

            System.out.printf("Just read message from %s\n", client_read.getRemoteAddress());
        }
    }

    /*private void writeMessage(SelectionKey key) throws IOException {
        /* Oggetto che rappresenta i dati da scrivere (non è possibile evitare l'unchecked warning) */
    //    @SuppressWarnings("unchecked")
    //    Future<Tasks> future = (Future<Tasks>) key.attachment();
        /* Active socket associato al client */
    //    SocketChannel client = (SocketChannel) key.channel();

        /* Buffer contenente la risposta da inviare al client */
    //    ByteBuffer response; //todo: ma da dove lo prende il bytebuffer?
        /* Scrive sul canale */
    //    client.write(response);
        /* Se non ha ancora finito esce (rientrerà nella funzione per scrivere ancora) */
    //    if (response.hasRemaining())
    //        return;
    //    System.out.printf("[SERVER] Just sent message to %s\n", client.getRemoteAddress());
        /* Altrimenti registra di nuovo il client per la lettura */
    //    key.interestOps(SelectionKey.OP_READ);
        /* Buffer necessario a leggere la dimensione del messaggio */
    //    ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);
        /* Usa il buffer per la lettura */
    /*    key.attach(lengthBuffer);
    }*/

    /***
     * Funzione che prevede la chiusura del selector e il server socket
     * @throws IOException se avviene un errore nell'I/O
     */
    @Override
    public void close() throws IOException {
        selector.close();
        channel.close();
    }
}
