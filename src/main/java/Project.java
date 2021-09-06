import java.util.*;

public class Project {
    /* Nome del progetto, univoco */
    private final String nameProject;
    /* Lista delle card che devono ancora essere prese in carico da un membro del progetto */
    private final List<Card> to_Do;
    /* Lista delle card che sono state prese in carico da un membro del progetto */
    private final List<Card> inProgress;
    /* Lista delle card le cui operazioni sono da revisionare da un membro del progetto */
    private final List<Card> toBeRevised;
    /* Lista delle card le cui operazioni associate sono portate a termine da un membro del progetto */
    private final List<Card> done;
    /* Lista di membri del progetto */
    private final List<User> members_sync;
    /* Vettore di tutte le carte totali */
    private final Vector<Card> cardsToShow;

    /***
     * Costruttore della classe
     *
     * @param nameProject, nome univoco del progetto da creare
     */
    public Project(String nameProject, User user){
        this.nameProject = nameProject;
        to_Do = Collections.synchronizedList(new LinkedList<>());
        inProgress = Collections.synchronizedList(new LinkedList<>());
        toBeRevised = Collections.synchronizedList(new LinkedList<>());
        done = Collections.synchronizedList(new LinkedList<>());
        members_sync = Collections.synchronizedList(new LinkedList<>());
        members_sync.add(user);
        cardsToShow = new Vector<>();
    }

    /***
     * Restituisce il nome del progetto
     * @return String Il nome del progetto
     */
    public String getNameProject() {
        return nameProject;
    }

    /***
     * Restituisce la lista dei membri del progetto
     * @return List<User> lista dei membri del progetto
     */
    public List<User> showMembers(){
        return members_sync;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista done
     * @return List<Card>, lista che contiene le card all'interno della lista done
     */
    public List<Card> getDone(){
        return done;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista toBeRevised
     * @return List<Card>, lista che contiene le card all'interno della lista toBeRevised
     */
    public List<Card> getToBeRevised(){
        return toBeRevised;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista to_Do
     * @return List<Card>, lista che contiene le card all'interno della lista to_Do
     */
    public List<Card> getToDo(){
        return to_Do;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista inProgress
     * @return List<Card>, lista che contiene le card all'interno della lista inProgress
     */
    public List<Card> getInProgress(){
        return inProgress;
    }

    /***
     * Restituisce la lista dei membri del progetto e che sono online
     * @return List<User> lista dei membri online del progetto
     */
    public synchronized List<User> showOnlineMembers(){
        Vector<User> onlineMembers = new Vector<>();
        for(User user : members_sync){
            if(user.isOnline()){
                onlineMembers.add(user);
            }
        }
        return onlineMembers;
    }

    /***
     * Aggiunge una card alla lista to_Do
     * @param cardname Nome della card da aggiungere a listaToAdd
     */
    public void addCardToDo(String cardname, String description){
        Card card = new Card(cardname);
        card.addHistory("added to to_Do; ");
        card.addDescription(description);
        to_Do.add(card);
    }

    /***
     * Restituisce la copia della card con nome cardName (la ricerca viene effettuata nella lista to_Do)
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public Card showCardTo_Do(String cardName){
        cardsToShow.removeAllElements();
        cardsToShow.addAll(to_Do);
        for (Card value : cardsToShow) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }


        return null;
    }

    /***
     * Restituisce la copia della card con nome cardName (la ricerca viene effettuata nella lista inProress
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public Card showCardDoing(String cardName){
        cardsToShow.removeAllElements();
        cardsToShow.addAll(inProgress);
        for (Card value : cardsToShow) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }


        return null;
    }

    /***
     * Restituisce la copia della card con nome cardName (la ricerca viene effettuata nella lista toBeRevised)
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public Card showCardToBeRevised(String cardName){
        if(cardName!=null) {
            cardsToShow.removeAllElements();
            cardsToShow.addAll(toBeRevised);
            for (Card value : cardsToShow) {
                if (value.getNameCard().equals(cardName)) {
                    return value;
                }
            }
        }

        return null;
    }

    /***
     * Restituisce la copia della card cercata
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public Card showCardDone(String cardName){
        cardsToShow.removeAllElements();
        cardsToShow.addAll(done);
        for (Card value : cardsToShow) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }
        return null;
    }

    /***
     * Restituisce tutte le card del progetto
     * @return Vector<Card> contenente tutte le card del progetto
     */
    public synchronized Vector<Card> showCards(){
        cardsToShow.removeAllElements();
        cardsToShow.addAll(to_Do);
        cardsToShow.addAll(toBeRevised);
        cardsToShow.addAll(inProgress);
        cardsToShow.addAll(done);
        return cardsToShow;
    }

    /***
     * Aggiunge l'utente user alla lista dei membri del progetto
     * @param user utente da inserire
     */
    public void addMember(User user){
        members_sync.add(user);
    }

    /***
     * Muove la card dalla lista del progetto listaDiPartenza alla lista del progetto listaDiDestinazione
     * @param listaDiPart Nome della lista in cui si trova attualmente la card
     * @param listadiDest Nome della lista in cui si trovera' la card
     * @param card Carta da spostare
     */
    public synchronized void moveCard(String listaDiPart, String listadiDest, Card card){
        switch (listaDiPart) {
            case "to_Do" -> {
                to_Do.remove(card);
                card.addHistory("removed from to_Do; ");
            }
            case "inProgress" -> {
                inProgress.remove(card);
                card.addHistory("removed from inProgress; ");
            }
            case "done" -> {
                done.remove(card);
                card.addHistory("removed from done; ");
            }
            case "toBeRevised" -> {
                toBeRevised.remove(card);
                card.addHistory("removed from toBeRevised; ");
            }
        }

        switch (listadiDest) {
            case "to_Do" -> {
                to_Do.add(card);
                card.addHistory("added to to_Do; ");
            }
            case "inProgress" -> {
                inProgress.add(card);
                card.addHistory("added to inProgress; ");
            }
            case "done" -> {
                done.add(card);
                card.addHistory("added to done; ");
            }
            case "toBeRevised" -> {
                toBeRevised.add(card);
                card.addHistory("added to toBeRevised; ");
            }
        }
    }

    /***
     * Restituisce la history di una card
     * @param card Card di cui prelevare la history
     * @return String la history della card
     */
    public synchronized String getCardHistory(Card card){
        return card.getHistory();
    }
}