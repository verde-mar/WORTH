package WORTH.server;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class CardFile {
    /* Nome univoco della card */
    private String nameCard;
    /* Descrizione testuale della card */
    private String description;
    /* Storia associata alla card */
    private List<String> history;
    /* Lista corrente a cui appartiene la card */
    private String currentList;

    /**
     * Costruttore della classe
     */
    public CardFile() {
        history = new LinkedList<>();
    }

    /**
     * Inizializza la variabile this.currentList
     * @param currentList Parametro con cui inizializzare this.concurrentList
     */
    public void setCurrentList(String currentList) {
        this.currentList = currentList;
    }

    /**
     * Inizializza la variabile this.description
     * @param description Parametro con cui inizializzare this.description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Inizializza la variabile this.nameCard
     * @param nameCard Parametro con cui inizializzare this.nameCard
     */
    public void setNameCard(String nameCard){
        this.nameCard = nameCard;
    }

    /**
     * Inizializza la list this.history
     * @param history Parametro con cui inizializzare this.history
     */
    public void setHistory(List<String> history){
        this.history = history;
    }

    /**
     * Restituisce this.history
     * @return List<String> La storia della card
     */
    public List<String> getHistory() {
        return history;
    }

    /**
     * Restituisce il nome della card
     * @return String Nome della card
     */
    public String getNameCard() {
        return nameCard;
    }

    /**
     * Restituisce la lista corrente
     * @return String Lista Corrente
     */
    public String getCurrentList() {
        return currentList;
    }

    /**
     * Restituisce la descrizione della card
     * @return String Descriizone della card
     */
    public String getDescription() {
        return description;
    }
}
