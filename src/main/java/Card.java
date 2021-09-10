import java.util.LinkedList;
import java.util.List;

public class Card {
    /* Nome univoco della card */
    private final String nameCard;
    /* Descrizione testuale della card */
    private String description;
    /* Storia associata alla card */
    private final List<String> history;
    /* Lista corrente a cui appartiene la card */
    private String currentList;

    /**
     * Costruttore della classe
     * @param nameCard nome della card
     */
    public Card(String nameCard){
        this.nameCard = nameCard;
        history = new LinkedList<>();
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
}
