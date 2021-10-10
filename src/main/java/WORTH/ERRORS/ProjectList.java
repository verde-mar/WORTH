package WORTH.ERRORS;

import WORTH.client.WORTHClient;
import WORTH.server.Project;

import javax.swing.*;

public class ProjectList extends JPanel {
    private static final long serialVersionUID = 4038002113147564810L;
    /* Driver che permette di inviare richieste di amicizia */
    private WORTHClient driver;
    /* Modello dei dati presenti nella lista */
    private DefaultListModel<Project> listModel;
    /* Lista contenente il modello */
    private JList<Project> list;

    /**
     * Inizializza il componente passandogli il driver
     * @param driver Permette di inviare richieste di operazioni da effettuare (visione di un progetto, creazione di un progetto, uscita)
     */
    public ProjectList(WORTHClient driver) {

    }

    private void seeProject() {

    }
}
