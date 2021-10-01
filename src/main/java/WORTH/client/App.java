package WORTH.client;

import WORTH.server.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class App extends JFrame {
    /* Componente che invia i comandi al server tramite TCP */
    private final TCPDriver tcpDriver;

    /* Componente che invia i comandi al server tramite UDP */
    private final UDPDriver udpDriver;

    /* Lista dei progetti */
    private ProjectList projectsList;

    /* UserName dell'utente corrente */
    private String username;


    /**
     * Costruttore della classe
     * @param tcpDriver Componente TCP che instrada le richieste
     * @param udpDriver Componente UDP che instrada le richieste
     */
    public App(TCPDriver tcpDriver, UDPDriver udpDriver) {
        /* Inizializzazione del nome della finestra */
        super("WORTH");
        this.tcpDriver = tcpDriver;
        this.udpDriver = udpDriver;

        /* Pannello che contiene i controlli */
        JPanel jPanel = new JPanel(new BorderLayout());

        /* Lista dei progetti */
        //todo: deve fare listprojects
        projectsList = new ProjectList(tcpDriver);
        jPanel.add(projectsList, BorderLayout.PAGE_START);

        /* Pulsante per creare un nuovo progetto */
        Creator newProject = new Creator();
        jPanel.add(newProject, BorderLayout.NORTH);

        /* Primo modo di uscita: listener che permette la chiusura della finestra */
        registerCloseOperation();

        add(jPanel);
        setResizable(false);
        setSize(1000, 500);
        setLocationRelativeTo(null);
    }

    public void setUserName(String username){
        this.username = username;
    }

    private void registerCloseOperation() {
        App app = this;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                /* Invia la richiesta di logout */
                try {
                    tcpDriver.logoutRequest(username);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(app, e1.getMessage(), "Logout error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void close() {
        try {
            tcpDriver.close();
            udpDriver.close();
            this.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Closing error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void listOnlineUsers() { //caso rmi
    }

    public void getCardHistory(List<String> history) {
        //si restituisce in una finestra
        //che poi si chiude
    }

    public void showMembers(List<String> members) {//todo: i membri del progetto li posso determinare dal client, costa meno

    }

    public void showCards(List<Card> cards) {//todo: menu a tendina?
        //si restituisce in una finestra
        //che poi si chiude
    }

    public void listUsers() { //caso rmi
    }

    public void showCard(Card card) {
    }

    public void listProjects() {
    }

    public void moveCard() {//in chat
    }

}
