package WORTH.server.ERRORS;

import WORTH.client.WORTHClient;
import WORTH.shared.worthProtocol.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class App extends JFrame {
    /* Componente che invia i comandi al server tramite TCP */
    private final WORTH.client.WORTHClient WORTHClient;

    /* Componente che invia i comandi al server tramite UDP */
    private final UDPDriver udpDriver;

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
        JPanel jPanel = new JPanel(new GridLayout(20, 15));

        /* Pulsante per creare un nuovo progetto */
        JLabel nameProject = new JLabel("Nome del nuovo progetto: ");
        jPanel.add(nameProject);
        JButton creator = new JButton("Crea un nuovo progetto");
        JTextField nomeProgetto = new JTextField();
        jPanel.add(nomeProgetto);
        jPanel.add(creator);
        creator.addActionListener(e -> {
            try {
                createNewProject(nomeProgetto.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JLabel projects = new JLabel("I tuoi progetti ");
        jPanel.add(projects);



        registerCloseOperation();

        setResizable(false);
        setSize(500, 500);
        setLocationRelativeTo(null);
        add(jPanel);
    }


    private void createNewProject(String nameProject) throws IOException {
        Response response = WORTHClient.createProject(nameProject, username);
        //todo:se la richiesta ha avuto successo devo aggiungere il bottone di questo progetto in ProjectsList
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
                    WORTHClient.logout(username);
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
