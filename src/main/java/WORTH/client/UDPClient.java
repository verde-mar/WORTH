package WORTH.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 */
public class UDPClient extends Thread implements AutoCloseable {
    /* Socket multicast */
    private final MulticastSocket multicastSocket;
    /* Struttura locale che associa ad ogni nome di un progetto, un oggetto di tipo ProjectChat*/
    private final HashMap<String, ProjectChat> chat;

    /**
     * Costruttore della classe
     * @throws IOException Nel caso di un errore I/O
     */
    public UDPClient() throws IOException {
        multicastSocket = new MulticastSocket(8082);
        chat = new HashMap<>();
    }

    /**
     * Esecuzione del thread
     */
    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                /* Riceve un messaggio dalla chat */
                String message = receive();
                /* Separa il messaggio dal nome del progetto di cui fa parte la chat, dal messaggio effettivo */
                int endIndex = message.indexOf("\n");
                String text = message.substring(0, endIndex);
                String nameProject = message.substring(endIndex+1);
                /* Aggiunge il messaggio nel buffer contenente i messaggio inviati dai membri del progetto */
                chat.get(nameProject).addMessages(text);
            } catch (IOException e) {
                System.out.println("See ya!");
            }
        }

    }

    /**
     * Invia un messaggio nella chat del progetto
     * @param message Messaggio da inviare
     * @param address Indirizzo IP a cui mandare il messaggio
     * @param destination_port Porta di destinazione
     * @throws IOException Nel caso di un errore I/O
     */
    public void send(String message, InetAddress address, int destination_port) throws IOException {
        byte[] arrayRequest = message.getBytes(StandardCharsets.UTF_8);
        DatagramPacket datagramPacket = new DatagramPacket(arrayRequest, arrayRequest.length, address, destination_port);
        multicastSocket.send(datagramPacket);
    }

    /**
     * Riceve un messaggio dalla chat
     * @return Il messaggio in formato stringa
     * @throws IOException Nel caso di un errore I/O
     */
    public String receive() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet_received = new DatagramPacket(buffer, buffer.length);
        multicastSocket.receive(packet_received);
        byte[] destBuf = new byte[packet_received.getLength()];
        System.arraycopy(packet_received.getData(), packet_received.getOffset(), destBuf, 0, destBuf.length);

        return new String(destBuf, StandardCharsets.UTF_8);
    }

    /**
     * Restituisce i messaggi contenuti nel buffer, e che non erano ancora stati letti dall'utente
     * @param projectName Nome del progetto la cui chat e' utilizzata in questo momento
     * @return Vector<String> Il vettore dei messaggi ancora da leggere
     * @throws Exception Nel caso in cui il progetto non esista
     */
    public Vector<String> getMessages(String projectName) throws Exception {
        ProjectChat project = chat.get(projectName);
        if(project!=null)
            return project.getMessages();
        else
            throw new Exception("You don't know the IP address of " + projectName + " yet. You have to use 'listProjects' first.\nIf this doesn't work,then " + projectName + "doesn't exist");

    }

    /**
     * Inizializza l'indirizzo IP del progetto
     * @param address Indirizzo IP
     * @param projectName Nome del progetto
     * @throws IOException Nel caso di un errore I/O
     */
    public void set(InetAddress address, String projectName) throws IOException {
        ProjectChat object = chat.get(projectName);
        object.setAddress(address);
        multicastSocket.joinGroup(address);
    }

    /**
     * Restituisce l'indirizzo IP associato al progetto
     * @param projectName Nome del progetto
     * @return InetAddress Indirizzo IP del progetto
     * @throws Exception Nel caso in cui il progetto non esista
     */
    public InetAddress getAddress(String projectName) throws Exception {
        ProjectChat projectChat = chat.get(projectName);
        if(projectChat!=null)
            return projectChat.getAddress();
        else
            throw new Exception("You tried to move a card in " + projectName + ". But you don't know the IP address yet. You have to use 'listProjects' first.\nIf this doesn't work,then " + projectName + "doesn't exist");
    }

    /**
     * Restituisce la struttura contenente il riferimento al nome del progetto, agli indirizzo IP e al buffer di messaggi associati
     * @return HashMap<String, ProjectChat> La struttura contenente il riferimento al nome del progetto, agli indirizzo IP e al buffer di messaggi associati
     */
    public HashMap<String, ProjectChat> getChat() {
        return chat;
    }

    /**
     * Chiude la socket e interrompe il thread
     */
    @Override
    public void close() {
        multicastSocket.close();
        interrupt();
    }


}
