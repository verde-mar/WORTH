
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
        this.nameCard = nameCard;
        history=null;
    }

    /***
     * Restituisce il nome della card
     * @return String il nome della card
     */
    public String getNameCard(){
        return nameCard;
    }

    /***
     * Aggiorna la history di una card
     * @param toConcat nuova azione effettuata sulla card
     * @return boolean Restituisce se l'operazione e' terminata con successo oppure no
     */
    public boolean addHistory(String toConcat) {
        boolean outcome;
        if(toConcat != null) {
            if (history == null) {
                history = toConcat;
            } else {
                history = history.concat(toConcat);
            }
            outcome = true;
        } else {
            outcome = false;
        }
        return outcome;
    }

    /***
     * Restituisce la history di una card
     * @return String la history di una card
     */
    public String getHistory(){
        return history;
    }

    /***
     * Aggiunge la descrizione ad una card
     * @param descript descrizione della card
     * @return boolean Restituisce se l'operazione e' terminata con successo oppure no
     */
    public boolean addDescription(String descript) {
        boolean outcome = false;
        if(descript != null) {
            this.description = descript;
            outcome = true;
        }
        return outcome;
    }
}
