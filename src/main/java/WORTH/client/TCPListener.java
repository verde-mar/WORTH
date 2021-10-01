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
                    app.showError();
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
            System.out.println("[TCP] Something went wrong");
            app.showError();
        } else {
            handleResponse(response);
        }
    }

    /**
     * Parsa la risposta ricevuta dal server e chiama i metodi interessati
     * @param response Risposta del server
     */
    private void handleResponse(Response response) {
        switch(response.getRequest()){
            case listOnlineUsers: {
                app.listOnlineUsers();
                break;
            }
            case getCardHistory: {
                app.getCardHistory();
                break;
            }
            case showMembers: {
                app.showMembers();
                break;
            }
            case showCards: {
                app.showCards();
                break;
            }
            case listUsers: {
                app.listUsers();
                break;
            }
            case addMember: {
                app.addMember();
                break;
            }
            case showCard: {
                app.showCard();
                break;
            }
            case listProjects: {
                app.listProjects();
                break;
            }
            case moveCard: {
                app.moveCard();
                break;
            }
            case addCard: {
                app.addCard();
                break;
            }
            case logout: {
                app.logout();
                break;
            }
            case createProject: {
                app.createProject();
                break;
            }
            case cancelProject: {
                app.cancelProject();
                break;
            }
        }
    }
}
