package WORTH.persistence;

import java.util.List;

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
     * Inizializza la variabile currentList
     * @param currentList Parametro con cui inizializzare this.concurrentList
     */
    public void setCurrentList(String currentList) {
        this.currentList = currentList;
    }

    /**
     * Inizializza la variabile description
     * @param description Parametro con cui inizializzare this.description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Inizializza la variabile nameCard
     * @param nameCard Parametro con cui inizializzare this.nameCard
     */
    public void setNameCard(String nameCard){
        this.nameCard = nameCard;
    }

    /**
     * Inizializza la history
     * @param history Parametro con cui inizializzare this.history
     */
    public void setHistory(List<String> history){
        this.history = history;
    }

    /**
     * Restituisce la descrizione della card
     * @return String Descrizione della card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Restituisce il nome della card
     * @return String Il nome della card
     */
    public String getNameCard() {
        return nameCard;
    }

    /**
     * Restituisce la lista corrente
     * @return String La lista corrente
     */
    public String getCurrentList() {
        return currentList;
    }

    /**
     * Restituisce la history di una card
     * @return List<String> History della card
     */
    public List<String> getHistory() {
        return history;
    }
}
