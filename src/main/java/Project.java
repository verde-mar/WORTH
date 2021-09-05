import java.util.*;

public class Project {
    /* Nome del progetto, univoco */
    private final String nameProject;
    /* Lista delle card che devono ancora essere prese in carico da un membro del progetto */
    private final List<Card> toDo;
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
        toDo = Collections.synchronizedList(new LinkedList<>());
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
     * Restituisce la lista che contiene le card all'interno della lista DONE
     * @return List<Card>, lista che contiene le card all'interno della lista DONE
     */
    public List<Card> getDone(){
        return done;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista TOBEREVISED
     * @return List<Card>, lista che contiene le card all'interno della lista TOBEREVISED
     */
    public List<Card> getToBeRevised(){
        return toBeRevised;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista TODO
     * @return List<Card>, lista che contiene le card all'interno della lista TODO
     */
    public List<Card> getToDo(){
        return toDo;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista INPROGRESS
     * @return List<Card>, lista che contiene le card all'interno della lista INPROGRESS
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
     * Aggiunge una card alla lista passata per parametro
     * @param card card da aggiungere a listaToAdd
     * @return boolean Restituisce true se e' stato possibile aggiungere la card
     */
    public synchronized boolean addCardToDo(Card card){
        boolean outcome = false;
        if(card != null) {
            card.addHistory("added to toDo; ");
            toDo.add(card);
        }
        return outcome;
    }

    /***
     * Aggiunge una card alla lista passata per parametro
     * @param card card da aggiungere a listaToAdd
     * @return boolean Restituisce true se e' stato possibile aggiungere la card
     */
    public synchronized boolean addCardDoing(Card card){
        boolean outcome = false;
        if(card != null) {
            card.addHistory("added to inProgress; ");
            inProgress.add(card);
        }
        return outcome;
    }

    /***
     * Aggiunge una card alla lista passata per parametro
     * @param card card da aggiungere a listaToAdd
     * @return boolean Restituisce true se e' stato possibile aggiungere la card
     */
    public synchronized boolean addCardRevised(Card card){
        boolean outcome = false;
        if(card != null) {
            card.addHistory("added to toBeRevised; ");
            toBeRevised.add(card);
        }
        return outcome;
    }

    /***
     * Aggiunge una card alla lista passata per parametro
     * @param card card da aggiungere a listaToAdd
     * @return boolean Restituisce true se e' stato possibile aggiungere la card
     */
    public synchronized boolean addCardDone(Card card){
        boolean outcome = false;
        if(card != null) {
            outcome = true;
            card.addHistory("added to done; ");
            done.add(card);
        }
        return outcome;
    }

    /***
     * Restituisce la copia della card cercata
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public synchronized Card showCardToDo(String cardName){
        if(cardName!=null) {
            cardsToShow.removeAllElements();
            cardsToShow.addAll(toDo);
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
    public synchronized Card showCardDoing(String cardName){
        if(cardName!=null) {
            cardsToShow.removeAllElements();
            cardsToShow.addAll(inProgress);
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
    public synchronized Card showCardToBeRevised(String cardName){
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
    public synchronized Card showCardDone(String cardName){
        if(cardName!=null) {
            cardsToShow.removeAllElements();
            cardsToShow.addAll(done);
            for (Card value : cardsToShow) {
                if (value.getNameCard().equals(cardName)) {
                    return value;
                }
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
        cardsToShow.addAll(toDo);
        cardsToShow.addAll(toBeRevised);
        cardsToShow.addAll(inProgress);
        cardsToShow.addAll(done);
        return cardsToShow;
    }

    /***
     * Aggiunge l'utente user alla lista dei membri del progetto
     * @param user utente da inserire
     * @return boolean Restituisce true se e' stato possibile aggiungere un utente come membro del progetto
     */
    public synchronized boolean addMember(User user){
        boolean outcome = false;
        if(!members_sync.contains(user) || user == null) { //oppure user non e' registrato
            outcome = members_sync.add(user);
        }
        return outcome;
    }

    /***
     * Muove la card dalla lista del progetto listaDiPartenza alla lista del progetto listaDiDestinazione
     * @param listaDiPartenza lista in cui si trova attualmente la card
     * @param listaDiDestinazione lista in cui si trovera' la card
     * @param card Carta da spostare
     * @return boolean Restituisce true se e' stato possibile muovere la card
     */
    public synchronized boolean moveCard(List<Card> listaDiPartenza, List<Card> listaDiDestinazione, String listaDiPart, String listadiDest, Card card){
        boolean outcome = false;
        if(listadiDest != null || listaDiPart != null || card != null) { //oppure listaDiPartenza e listaDiDestinazione non sono ne' todo, ne' inprogress, ne' toberevised ne' done
            outcome = true;
            listaDiPartenza.remove(card);
            card.addHistory("removed from " + listaDiPart + "; ");
            listaDiDestinazione.add(card);
            card.addHistory("added to " + listadiDest + "; ");
        }
        return outcome;
    }

    /***
     * Restituisce la history di una card
     * @param card Card di cui prelevare la history
     * @return String la history della card
     */
    public synchronized String getCardHistory(Card card){
        if(card!=null)
            return card.getHistory();
        else
            return null;
    }
}