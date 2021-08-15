import java.util.List;

public class Card {
    /* Nome univoco della card */
    private final String nameCard;
    /* Descrizione testuale della card */
    private String description;
    /* Storia associata alla card */
    private String history;

    /***
     * Costruttore della classe
     * @param nameCard nome della card
     */
    public Card(String nameCard){
        this.nameCard = nameCard; history=null;
    }

    /***
     * Aggiorna la history di una card
     * @param toConcat nuova azione effettuata sulla card
     */
    public void addHistory(String toConcat) {
        history = history.concat(toConcat); //TODO: devi vedere come concatena le stringhe, sono tutte attacate? ==> meglio un array?
        System.out.println(history); //da sistemare
    }

    /***
     * Aggiunge la descrizione ad una card
     * @param descript descrizione della card
     */
    public void addDescription(String descript) {
        this.description = descript;
    }
}
