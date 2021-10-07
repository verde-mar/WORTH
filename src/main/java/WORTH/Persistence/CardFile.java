package WORTH.Persistence;

import java.util.List;

/**
 * CardFile e' la classe che rappresenta una card
 * su disco in formato JSON
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

    public String getDescription() {
        return description;
    }

    public String getNameCard() {
        return nameCard;
    }

    public String getCurrentList() {
        return currentList;
    }

    public List<String> getHistory() {
        return history;
    }
}
