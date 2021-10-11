package WORTH.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Card implements Serializable {
    /* Nome univoco della card */
    private String nameCard;
    /* Descrizione testuale della card */
    private String description;
    /* Storia associata alla card */
    private List<String> history;
    /* Lista corrente a cui appartiene la card */
    private String currentList;
    /* Oggetto necessario per creare un file all'interno della directory del progetto */
    ObjectMapper card_mapper;

    /**
     * Costruttore necessario a Jackson
     */
    public Card(){}

    /**
     * Costruttore della classe
     * @param nameCard nome della card
     */
    public Card(String nameCard){
        this.nameCard = nameCard;
        history = new LinkedList<>();
        card_mapper = new ObjectMapper();
    }

    /**
     * Restituisce il nome della card
     * @return String il nome della card
     */
    public String getNameCard(){
        return nameCard;
    }

    /**
     * Setta la variabile che indica la lista corrente in cui si trova la card
     * @param list Nome della lista corrente a cui appartiene la lista
     */
    public void addCurrentList(String list){ currentList = list; }

    /**
     * Funzione necessaria a Jackson per utilizzare il campo currentList
     * @return String La lista corrente
     */
    public String getCurrentList(){ return currentList; }

    /**
     * Resetta la lista corrente
     */
    public void eraseCurrentList(){
        currentList = null;
    }

    /**
     * Aggiorna la history di una card
     * @param start String Nuova azione effettuata sulla card
     */
    public void addHistory(String start) {
        history.add(start);
    }


    /**
     * Restituisce la history di una card
     * @return List<String> La history di una card
     */
    public List<String> getHistory(){
        return history;
    }

    /**
     * Aggiunge la descrizione ad una card
     * @param descript String Descrizione della card
     */
    public void addDescription(String descript) {
        this.description = descript;
    }

    /**
     * Scrive la card corrente nella directory definita da projectName
     * @param projectName Nome del progetto
     * @throws IOException Se vi e' un errore nell'IO
     */
    public synchronized void writeOnDisk(String projectName) throws IOException {

        card_mapper.writeValue(Paths.get("./projects/" + projectName + "/" + nameCard + ".json").toFile(), this);
    }

    /**
     * Funzione necessaria a Jackson per utilizzare il valore description
     * @return String La descrizione della card
     */
    public String getDescription() {
        return description;
    }

}
