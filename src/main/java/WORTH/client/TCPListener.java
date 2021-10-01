package WORTH.client;

import WORTH.shared.Response;

import java.io.IOException;

public class TCPListener extends Thread {
    /* Client TCP */
    private final TCPClient client;
    /* Nucleo dell'applicazione lato client */
    private final App app;

    /**
     * Costruttore della classe
     * @param tcpClient Client TCP
     * @param app Nucleo dell'applicazione lato client
     */
    public TCPListener(TCPClient tcpClient, App app) {
        client = tcpClient;
        this.app = app;
    }

    /**
     * Il thread rimane in ascolto della risposta finche' non termina
     */
    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                try {
                    Response response = client.receive();
                    parser(response);
                } catch (IOException e) {
                    handleError("Errore nella ricezione della risposta");
                }
            }
        } catch(Exception e){
            System.out.println("[TCP] Error while communicating: " + e);
            app.close();
        }
    }

    /**
     * Verifica l'esito della richiesta eseguita e agisce di conseguenza
     * @param response Risposta del server
     */
    private void parser(Response response) {
        /* Se la richiesta non e' stata eseguita con successo */
        if(!response.getDone()){
            handleError(response.getRequest().name());
        } else {
            handleResponse(response);
        }
    }

    private void handleError(String request) {
        /* Se il login non è andato a buon fine chiude la finestra */
        if (request.equals("login"))
            app.close();
    }

    /**
     * Parsa la risposta ricevuta dal server e chiama i metodi interessati
     * @param response Risposta del server
     */
    private void handleResponse(Response response) {
        switch(response.getRequest()){
            case listOnlineUsers: { //caso rmi
                app.listOnlineUsers();
                break;
            }
            case getCardHistory: {
                app.getCardHistory(response.getHistory());
                break;
            }
            case showMembers: {
                app.showMembers(response.getMembers());
                break;
            }
            case showCards: {
                app.showCards(response.getCards());
                break;
            }
            case listUsers: { //caso rmi
                app.listUsers();
                break;
            }
            case showCard: {
                app.showCard(response.getCard());
                break;
            }
            case listProjects: { //ancora non so
                app.listProjects();
                break;
            }
            case moveCard: { //deve metterlo in chat
                app.moveCard();
                break;
            }
        }
    }
}
