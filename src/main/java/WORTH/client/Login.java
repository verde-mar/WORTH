package WORTH.client;

import WORTH.shared.Configuration;

import javax.swing.*;
import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Login extends JFrame {
    /* Campo di testo contenente lo username dell'utente */
    private JTextField usernameField;
    /* Campo contenente la password */
    private JPasswordField passwordField;
    /* Campo di testo contenente il nome dell'host del server */
    private JTextField hostNameField;

    public Login(){
        /* Imposta il titolo della finestra */
        super("Benvenut* su WORTH");
        /* Se la finestra viene chiusa bisogna uscire immediatamente */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* Pannello che contiene i controlli (layout da 3 righe e 3 colonne) */
        JPanel panel = new JPanel(new GridLayout(4, 3));
        /* Controlli per il nome del server */
        JLabel hostNameLabel = new JLabel("Server");
        panel.add(hostNameLabel);
        hostNameField = new JTextField(Configuration.SERVER_HOSTNAME);
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
    }

    /**
     * Esegue il login di un utente nel sistema
     */
    private void login() {
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
            client.registerUser(username, password);
            /* Esegue il login */
            login();
        } catch (RemoteException | NotBoundException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Something went wrong", JOptionPane.ERROR_MESSAGE);
        }
    }
}
