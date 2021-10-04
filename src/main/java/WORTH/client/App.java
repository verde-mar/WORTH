package WORTH.client;

import WORTH.server.Card;
import WORTH.shared.worthProtocol.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class App extends JFrame {
    /* Componente che invia i comandi al server tramite TCP */
    private final WORTHClient WORTHClient;

    /* Componente che invia i comandi al server tramite UDP */
    private final UDPDriver udpDriver;

    /* Lista dei progetti */
    private ProjectList projectsList;

    /* UserName dell'utente corrente */
    private String username;


    /**
     * Costruttore della classe
     * @param WORTHClient Componente TCP che instrada le richieste
     * @param udpDriver Componente UDP che instrada le richieste
     */
    public App(WORTHClient WORTHClient, UDPDriver udpDriver) {
        /* Inizializzazione del nome della finestra */
        super("WORTH");
        this.WORTHClient = WORTHClient;
        this.udpDriver = udpDriver;

        /* Pannello che contiene i controlli */
        JPanel jPanel = new JPanel(new BorderLayout());

        /* Lista dei progetti */
        //todo: deve fare listprojects
        projectsList = new ProjectList(WORTHClient);
        jPanel.add(projectsList, BorderLayout.PAGE_START);

        /* Pulsante per creare un nuovo progetto */
        JButton creator = new JButton("Crea un nuovo progetto");
        jPanel.add(creator);
        creator.addActionListener(e -> createNewProject());

        registerCloseOperation();

        add(jPanel);
        setResizable(false);
        setSize(1000, 500);
        setLocationRelativeTo(null);
    }

    private void createNewProject() {
        //todo: 1- manda richiesta di creare un nuovo progetto
        //todo: 2- se la richiesta ha avuto successo devo aggiungere il bottone di questo progetto in ProjectsList
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
                    WORTHClient.logoutRequest(username);
                    app.close();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(app, e1.getMessage(), "Logout error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void close() {
        try {
            WORTHClient.close();
            udpDriver.close();
            this.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Closing error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
