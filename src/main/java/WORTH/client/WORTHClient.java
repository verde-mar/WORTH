package WORTH.client;

import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;

import java.io.IOException;


public class WORTHClient implements AutoCloseable {
    private final TCPClient tcpClient;

    /**
     * Costruttore della classe
     * @param tcpClient Oggetto che si occupa dell'interazione TCP con il server
     */
    public WORTHClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    /**
     * Invia la richiesta di login dell'utente
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @return Response La risposta del server
     * @throws Exception Nel caso di un errore generico
     */
    public Response login(String username, String password) throws Exception {
        Request request_login = new Request();
        request_login.setNickUtente(username);
        request_login.setPassword(password);
        request_login.setRequest(Request.RequestType.login);
        tcpClient.send(request_login);
        return tcpClient.receive();
    }

    /**
     * Effettua il logout dell'utente
     * @param username Username dell'utente
     * @return Response La response dell'utente
     * @throws IOException Nel caso di un errore I/O
     */
    public Response logout(String username) throws IOException {
        Request requestLogOut = new Request();
        requestLogOut.setNickUtente(username);
        requestLogOut.setRequest(Request.RequestType.logout);
        tcpClient.send(requestLogOut);
        Response response = tcpClient.receive();
        if(!response.getDone()) throw new IOException(response.getExplanation());
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
        Request createPrj = new Request();
        createPrj.setRequest(Request.RequestType.createProject);
        createPrj.setProjectName(projectName);
        createPrj.setNickUtente(username);
        tcpClient.send(createPrj);

        return tcpClient.receive();
    }

    /**
     * Restituisce la lista dei progetti a cui appartiene un utente
     * @param username Nome dell'utente che ha richiesto l'operazione
     * @return Response la risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response listProjects(String username) throws IOException {
        Request listPrjs = new Request();
        listPrjs.setRequest(Request.RequestType.listProjects);
        listPrjs.setNickUtente(username);
        tcpClient.send(listPrjs);

        return tcpClient.receive();
    }

    /**
     * Richiede l'aggiunta di una card ad un progetto
     * @param nickUtente Username dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto a cui deve appartenere la card
     * @param cardName Nome della card
     * @param description Descrizione della card
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response addCard(String nickUtente, String projectName, String cardName, String description) throws IOException {
        Request addCard = new Request();
        addCard.setNickUtente(nickUtente);
        addCard.setProjectName(projectName);
        addCard.setRequest(Request.RequestType.addCard);
        addCard.setCardName(cardName);
        addCard.setDescription(description);

        tcpClient.send(addCard);

        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione di una card
     * @param nickUtente Nick dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto a cui appartiene la card
     * @param cardName Nome della card
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response showCard(String nickUtente, String projectName, String cardName) throws IOException {
        Request showCard = new Request();
        showCard.setNickUtente(nickUtente);
        showCard.setProjectName(projectName);
        showCard.setRequest(Request.RequestType.showCard);
        showCard.setCardName(cardName);

        tcpClient.send(showCard);

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
        Request cancelPrj = new Request();
        cancelPrj.setRequest(Request.RequestType.cancelProject);
        cancelPrj.setProjectName(projectName);
        cancelPrj.setNickUtente(nickUtente);

        tcpClient.send(cancelPrj);

        return tcpClient.receive();
    }

    /**
     * Richiesta di spostamento di una card da una lista ad un'altra
     * @param nickUtente Nickname dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto a cui appartiene la card
     * @param cardName Nome della card
     * @param fromList Lista di partenza della card
     * @param toList Lista di destinazione della card
     * @return Response La risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response moveCard(String nickUtente, String projectName, String cardName, String fromList, String toList) throws IOException {
        Request moveC = new Request();
        moveC.setRequest(Request.RequestType.moveCard);
        moveC.setNickUtente(nickUtente);
        moveC.setProjectName(projectName);
        moveC.setCardName(cardName);
        moveC.setListaPartenza(fromList);
        moveC.setListaDestinazione(toList);

        tcpClient.send(moveC);

        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione delle card di un progetto
     * @param nickUtente Nickname dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response showCards(String nickUtente, String projectName) throws IOException {
        Request showCs = new Request();
        showCs.setRequest(Request.RequestType.showCards);
        showCs.setNickUtente(nickUtente);
        showCs.setProjectName(projectName);

        tcpClient.send(showCs);

        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di visione dei membri di un progetto
     * @param nickUtente Nickname dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto interessato
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response showMembers(String nickUtente, String projectName) throws IOException {
        Request showMs = new Request();
        showMs.setRequest(Request.RequestType.showMembers);
        showMs.setNickUtente(nickUtente);
        showMs.setProjectName(projectName);

        tcpClient.send(showMs);

        return tcpClient.receive();
    }

    /**
     * Invia la richiesta di aggiunta di un membro
     * @param nickUtente Nickname dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto interessato
     * @param nickToAdd Nickname dell'utente da aggiungere
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response addMember(String nickUtente, String projectName, String nickToAdd) throws IOException {
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
     * @param nickUtente Nickname dell'utente che ha richiesto l'operazione
     * @param projectName Nome del progetto interessato
     * @param cardName Nome della card interessata
     * @return Response Risposta del server
     * @throws IOException Nel caso di un errore I/O
     */
    public Response getCardHistory(String nickUtente, String projectName, String cardName) throws IOException {
        Request getCH = new Request();
        getCH.setNickUtente(nickUtente);
        getCH.setProjectName(projectName);
        getCH.setRequest(Request.RequestType.getCardHistory);
        getCH.setCardName(cardName);

        tcpClient.send(getCH);

        return tcpClient.receive();
    }
}
