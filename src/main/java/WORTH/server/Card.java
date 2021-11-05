package WORTH.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Oggetto rappresentante una card all'interno di un progetto
 */
public class Card implements Serializable {
    /* Nome univoco della card */
    private String nameCard;
    /* Descrizione testuale della card */
    private String description;
    /* Storia associata alla card */
    private List<String> history;
    /* Lista corrente a cui appartiene la card */
    private String currentList;
    @JsonIgnore /* Mapper necessario alla serializzazione/deserializzazione del file JSON */
    private ObjectMapper card_mapper;
    @JsonIgnore /* ID per la serializzazione/deserializzazione della classe */
    private static final long serialVersionUID = -4242944971815086218L;

    /**
     * Costruttore necessario a serializzare/deserializzare il file JSON
     */
    public Card(){}

    /**
     * Costruttore della classe
     * @param nameCard Nome della card
     */
    public Card(String nameCard){
        this.nameCard = nameCard;
        history = new LinkedList<>();
        card_mapper = new ObjectMapper();
        card_mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }


    /**
     * Restituisce il nome della card
     * @return String Nome della card
     */
    public String getNameCard(){
        return nameCard;
    }

    /**
     * Inizializza la variabile che indica la lista corrente in cui si trova la card
     * @param list Nome della lista corrente a cui appartiene la card
     */
    public void addCurrentList(String list){ currentList = list; }

    /**
     * Funzione necessaria a Jackson per per serializzare/deserializzare il campo currentList
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
     * Scrive la card corrente su disco
     * @param projectName Nome del progetto
     * @throws IOException Se vi e' un errore nella scrittura sul file
     */
    public void writeOnDisk(String projectName) throws IOException {
        card_mapper.writeValue(Paths.get("./projects/" + projectName + "/" + nameCard + ".json").toFile(), this);
    }

    /**
     * Funzione necessaria a Jackson per serializzare/deserializzare il valore description
     * @return String La descrizione della card
     */
    public String getDescription() {
        return description;
    }

}
