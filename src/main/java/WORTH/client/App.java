package WORTH.client;

import javax.swing.*;

public class App extends JFrame {
    /* Componente che invia i comandi al server tramite TCP */
    private TCPDriver tcpDriver;

    /* Componente che invia i comandi al server tramite UDP */
    private UDPDriver udpDriver;

    /* Lista dei progetti */
    private ProjectList projectsList;

    public App(TCPDriver tcpDriver, UDPDriver udpDriver) {
    }
}
