package WORTH.client;


import WORTH.shared.UserManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class Login extends JFrame {
    /* Campo di testo contenente lo username dell'utente */
    private final JTextField usernameField;
    /* Campo contenente la password */
    private final JPasswordField passwordField;
    /* Campo di testo contenente il nome dell'host del server */
    private final JTextField hostNameField;
    /* Struttura dati locale in cui memorizzare gli utenti registrati, e se sono online */
    private final UserManager userManagerClient;

    public Login(){
        /* Imposta il titolo della finestra */
        super("Benvenut* su WORTH");
        /* Se la finestra viene chiusa bisogna uscire immediatamente */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new GridLayout(4, 3));
        /* Controlli per il nome del server */
        JLabel hostNameLabel = new JLabel("Server");
        panel.add(hostNameLabel);
        hostNameField = new JTextField("localhost");
        panel.add(hostNameField);
        /* Controlli per lo username */
        JLabel usernameLabel = new JLabel("Username");
        panel.add(usernameLabel);
        usernameField = new JTextField();
        panel.add(usernameField);
        /* Controlli per la password */
        JLabel passwordLabel = new JLabel("Password");
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);
        /* Pulsante di login */
        JButton loginButton = new JButton("Login");
        panel.add(loginButton);
        /* Funzione di login */
        loginButton.addActionListener(e -> login());
        /* Pulsante di registrazione */
        JButton registerButton = new JButton("Registrati");
        panel.add(registerButton);
        /* Funzione di registrazione */
        registerButton.addActionListener(e -> register());
        /* Imposta la finestra come non ridimensionabile */
        setResizable(false);
        /* Imposta la dimensione della finestra */
        setSize(250, 250);
        /* Fa apparire la finestra al centro dello schermo */
        setLocationRelativeTo(null);
        /* Aggiunge il pannello con tutti i controlli alla finestra corrente */
        add(panel);

        /* Costruzione della struttura dati */
        userManagerClient = new UserManager(new HashMap<>());
    }

    /**
     * Esegue il login di un utente nel sistema
     */
    private void login() {
        TCPClient tcpClient = null;
        UDPClient udpClient = null;
        try {
            /* Indirizzo del server */
            SocketAddress address = new InetSocketAddress(hostNameField.getText(), 8080);
            /* Client che comunicano con il server */
            tcpClient = new TCPClient(address);
            udpClient = new UDPClient(tcpClient.getLocalAddress());
            /* Driver che scrivono sul server */
            TCPDriver tcpDriver = new TCPDriver(tcpClient, userManagerClient.getUtentiRegistrati());
            UDPDriver udpDriver = new UDPDriver(udpClient);
            /* Applicazione grafica che visualizza le informazioni */
            App app = new App(tcpDriver, udpDriver);
            /* Listener che attendono e notificano l'arrivo di pacchetti */
            TCPListener tcpListener = new TCPListener(tcpClient, app);
            UDPListener udpListener = new UDPListener(udpClient, app);
            /* Avvia i due ascoltatori */
            tcpListener.start();
            udpListener.start();
            /* Username dell'utente */
            String username = usernameField.getText();
            /* Password dell' utente */
            String password = new String(passwordField.getPassword());
            /* Richiede il login dell'utente */
            tcpDriver.loginRequest(username, password);
            /* Mostra la finestra principale */
            app.setVisible(true);
            /* Chiude la finestra corrente */
            this.dispose();
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login error", JOptionPane.ERROR_MESSAGE);
            try {
                if (tcpClient != null)
                    tcpClient.close();
                if (udpClient != null)
                    udpClient.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Registra l'utente nel sistema
     */
    private void register() {
        /* Indirizzo del server */
        String hostName = hostNameField.getText();
        /* Username dell'utente */
        String username = usernameField.getText();
        /* Password dell'utente */
        String password = new String(passwordField.getPassword());
        try {
            /* Oggetto remoto che consente di registrare l'utente */
            RMIClient client = new RMIClient(hostName);
            /* Registra l'utente */
            client.register(username, password);
            userManagerClient.register(username,password);
            /* Esegue il login */
            login();
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
    }
}
