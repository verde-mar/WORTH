package WORTH.client;

import javax.swing.*;
//todo: creare l'ascoltatore di swing
//todo: creare bottoni adatti
//todo: bottoni progetti(dati da listProjects), creaProgetto, Esci
public class App extends JFrame {
    /* Componente che invia i comandi al server tramite TCP */
    private TCPDriver tcpDriver;

    /* Componente che invia i comandi al server tramite UDP */
    private UDPDriver udpDriver;

    /* Lista dei progetti */
    private ProjectList projectsList;

    public App(TCPDriver tcpDriver, UDPDriver udpDriver) {
    }

    public void showError() {
    }

    public void close() {
    }

    public void listOnlineUsers() {
    }

    public void getCardHistory() {
    }

    public void showMembers() {
    }

    public void showCards() {
    }

    public void listUsers() {
    }

    public void addMember() {
    }

    public void showCard() {
    }

    public void listProjects() {
    }

    public void moveCard() {
    }

    public void addCard() {
    }

    public void logout() {
    }

    public void createProject() {
    }

    public void cancelProject() {
    }
}
