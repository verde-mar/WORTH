package WORTH.client;

import WORTH.server.Project;

import javax.swing.*;
import java.awt.*;

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
        super(new BorderLayout());
        this.driver = driver;
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setLayoutOrientation(JList.VERTICAL);
        add(new JScrollPane(list), BorderLayout.CENTER);

        /* Tasti che si riferiscono a progetti dell'utente */
        JButton projectButton = new JButton();
        projectButton.addActionListener(e -> seeProject());
        add(projectButton, BorderLayout.SOUTH);
        setSize(100, 500);
    }

    private void seeProject() {
    }
}
