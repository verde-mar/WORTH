package WORTH.client;

import WORTH.server.Project;
import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * La classe invia richieste TCP e UDP al Server
 */
public class WORTHClient implements AutoCloseable {
    /* Istanza della classe TCPClient contenente i metodi per l'interazione TCP con il server */
    private final TCPClient tcpClient;
    /* Thread che si occupa dell'interazione connectionless con il server */
    private final UDPClient udpClient;

    /**
     * Costruttore della classe
     * @param tcpClient Istanza della classe TCPClient
     * @param udpClient Istanza della classe UDPClient
     */
    public WORTHClient(TCPClient tcpClient, UDPClient udpClient) {
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
    }

    /**
     * Invia la richiesta di login dell'utente
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @return Response La risposta del server
     * @throws Exception Nel caso di un errore nell'invio della richiesta di login
     */
    public Response login(String username, String password) throws Exception {
        /* Costruzione e invio della richiesta */
        Request request_login = new Request();
        request_login.setNickUtente(username);
        request_login.setPassword(password);
        request_login.setRequest(Request.RequestType.login);
        tcpClient.send(request_login);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Effettua il logout dell'utente
     * @param username Username dell'utente
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response logout(String username) throws IOException {
        /* Costruzione e invio della richiesta */
        Request requestLogOut = new Request();
        requestLogOut.setNickUtente(username);
        requestLogOut.setRequest(Request.RequestType.logout);
        tcpClient.send(requestLogOut);
        Response response = tcpClient.receive();
        if(!response.getDone()) throw new IOException(response.getExplanation());

        /* Restituisce la risposta a WORTHClient */
        return response;
    }

    /**
     * Effettua la close() del channel che si riferisce alla comunicazione TCP
     * @throws IOException Nel caso di un errore I/O
     */
    public void close() throws IOException {
        tcpClient.close();
    }

    /**
     * Invia la richiesta di creazione di un progetto
     * @param projectName Nome del progetto
     * @param username Username dell'utente che ha richiesto la creazione del progetto
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response createProject(String projectName, String username) throws IOException {
        /* Costruzione e invio della richiesta */
        Request createPrj = new Request();
        createPrj.setRequest(Request.RequestType.createProject);
        createPrj.setProjectName(projectName);
        createPrj.setNickUtente(username);
        tcpClient.send(createPrj);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Restituisce la lista dei progetti a cui appartiene un utente
     * @param username Nome dell'utente che ha richiesto l'operazione
     * @return Response la risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response listProjects(String username) throws IOException {
        /* Costruzione e invio della richiesta */
        Request listPrjs = new Request();
        listPrjs.setRequest(Request.RequestType.listProjects);
        listPrjs.setNickUtente(username);
        tcpClient.send(listPrjs);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Richiede l'aggiunta di una card ad un progetto
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto a cui deve appartenere la card
     * @param cardName Nome della card
     * @param description Descrizione della card, che potrebbe anche essere null
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response addCard(String nickUtente, String projectName, String cardName, String description) throws IOException {
        /* Costruzione e invio della richiesta */
        Request addCard = new Request();
        addCard.setNickUtente(nickUtente);
        addCard.setProjectName(projectName);
        addCard.setRequest(Request.RequestType.addCard);
        addCard.setCardName(cardName);
        addCard.setDescription(description);
        tcpClient.send(addCard);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione di una card
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto a cui appartiene la card
     * @param cardName Nome della card
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response showCard(String nickUtente, String projectName, String cardName) throws IOException {
        /* Costruzione e invio della richiesta */
        Request showCard = new Request();
        showCard.setNickUtente(nickUtente);
        showCard.setProjectName(projectName);
        showCard.setRequest(Request.RequestType.showCard);
        showCard.setCardName(cardName);
        tcpClient.send(showCard);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di cancellazione di un progetto
     * @param projectName Nome del progetto da cancellare
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response cancelProject(String projectName, String nickUtente) throws IOException {
        /* Costruzione e invio della richiesta */
        Request cancelPrj = new Request();
        cancelPrj.setRequest(Request.RequestType.cancelProject);
        cancelPrj.setProjectName(projectName);
        cancelPrj.setNickUtente(nickUtente);
        tcpClient.send(cancelPrj);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Richiesta di spostamento di una card da una lista ad un'altra
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto a cui appartiene la card
     * @param cardName Nome della card
     * @param fromList Lista di partenza della card
     * @param toList Lista di destinazione della card
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response moveCard(String nickUtente, String projectName, String cardName, String fromList, String toList) throws IOException {
        /* Costruzione e invio della richiesta */
        Request moveC = new Request();
        moveC.setRequest(Request.RequestType.moveCard);
        moveC.setNickUtente(nickUtente);
        moveC.setProjectName(projectName);
        moveC.setCardName(cardName);
        moveC.setListaPartenza(fromList);
        moveC.setListaDestinazione(toList);
        tcpClient.send(moveC);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione delle card di un progetto
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response showCards(String nickUtente, String projectName) throws IOException {
        /* Costruzione e invio della richiesta */
        Request showCs = new Request();
        showCs.setRequest(Request.RequestType.showCards);
        showCs.setNickUtente(nickUtente);
        showCs.setProjectName(projectName);
        tcpClient.send(showCs);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione dei membri di un progetto
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto interessato
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response showMembers(String nickUtente, String projectName) throws IOException {
        /* Costruzione e invio della richiesta */
        Request showMs = new Request();
        showMs.setRequest(Request.RequestType.showMembers);
        showMs.setNickUtente(nickUtente);
        showMs.setProjectName(projectName);
        tcpClient.send(showMs);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di aggiunta di un membro
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto interessato
     * @param nickToAdd Username dell'utente da aggiungere
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response addMember(String nickUtente, String projectName, String nickToAdd) throws IOException {
        /* Costruzione e invio della richiesta */
        Request addM = new Request();
        addM.setRequest(Request.RequestType.addMember);
        addM.setNickUtente(nickUtente);
        addM.setProjectName(projectName);
        addM.setNickToAdd(nickToAdd);

        tcpClient.send(addM);

        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione della history di una card
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto interessato
     * @param cardName Nome della card interessata
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response getCardHistory(String nickUtente, String projectName, String cardName) throws IOException {
        /* Costruzione e invio della richiesta */
        Request getCH = new Request();
        getCH.setNickUtente(nickUtente);
        getCH.setProjectName(projectName);
        getCH.setRequest(Request.RequestType.getCardHistory);
        getCH.setCardName(cardName);
        tcpClient.send(getCH);

        /* Restituisce la risposta a WORTHClient */
        return tcpClient.receive();
    }


    /**
     * Legge dalla chat i messaggi precedentemente inviati dagli utenti
     * @param projectName Nome del progetto a cui appartiene la chat
     * @throws Exception Nel caso di un errore che non possa portare a termine la richiesta
     */
    public void readChat(String projectName) throws Exception {
        /* Scorre i messaggi, man mano che vengono letti, vengono anche eliminati */
        for (String msg : udpClient.getMessages(projectName)) {
            System.out.println("< " + msg);
        }
        if(udpClient.getMessages(projectName).size()==0)
            System.out.println("No unread messages");
        udpClient.getMessages(projectName).removeAllElements();
    }

    /**
     * Invia un messaggio nella chat del progetto
     * @param projectName Nome del progetto
     * @param message Messaggio da inviare
     * @throws Exception Nel caso di un errore che non possa portare a termine la richiesta
     */
    public void sendMsg(String projectName, String message) throws Exception {
        InetAddress address = udpClient.getAddress(projectName);
        udpClient.send(message, address, 8082);
    }


    /**
     * Per ogni progetto di cui l'utente fa parte, vengono memorizzati l'indirizzo IP e la porta di destinazione
     * @param projects Lista dei progetti di cui l'utente fa parte
     * @throws IOException Nel caso di un errore I/O
     */
    public void setIPAddresses(List<Project> projects) throws IOException {
        for(Project project: projects){
            ProjectChat communicator = udpClient.getChat().putIfAbsent(project.getNameProject(), new ProjectChat());
            if(communicator==null) udpClient.set(project.getAddressUdp(), project.getNameProject());
        }
    }
}
