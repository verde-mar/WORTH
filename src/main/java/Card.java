import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Card {
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
     * Costruttore della classe
     * @param nameCard nome della card
     */
    public Card(String nameCard){
        this.nameCard = nameCard;
        history = new LinkedList<>();
        card_mapper = new ObjectMapper();
    }

    public Card(){}

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
     * Restituisce la lista corrente
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
     * Aggiorna il file su disco associato alla card corrente
     * @throws IOException Nel caso ci siano errori I/O nella scrittura sul file
     */
    public void update() throws IOException {
        File card_file = new File(nameCard);
        if(card_file.exists()){
            try(BufferedWriter out = new BufferedWriter(new FileWriter(card_file.getName()))) {
                out.write(String.valueOf(getHistory()));
            }
        }
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
     * Restituisce la descrizione associata alla card
     * @return String La descrizione della card
     */
    public String getDescription() {
        return description;
    }


    /**
     * Scrive la card corrente nella directory definita da projectName
     * @param projectName Nome del progetto
     * @throws IOException Se vi e' un errore nell'IO
     */
    public void writeOnDisk(String projectName) throws IOException {
        CardFile cardFile = new CardFile();
        cardFile.setNameCard(nameCard);
        cardFile.setCurrentList(getCurrentList());
        cardFile.setDescription(description);
        cardFile.setHistory(getHistory());

        card_mapper.writeValue(Paths.get("./" + projectName + "/" + nameCard + ".json").toFile(), cardFile);
    }
}
