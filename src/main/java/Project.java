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

    public String getNameProject() {
        return nameProject;
    }

    /***
     * Restituisce la lista dei membri del progetto
     *
     * @return List<User> lista dei membri del progetto
     */
    private List<User> showMembers(){
        return members_sync;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista DONE
     *
     * @return List<Card>, lista che contiene le card all'interno della lista DONE
     */
    private List<Card> getDone(){
        return done;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista TOBEREVISED
     * @return List<Card>, lista che contiene le card all'interno della lista TOBEREVISED
     */
    private List<Card> getToBeRevised(){
        return toBeRevised;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista TODO
     * @return List<Card>, lista che contiene le card all'interno della lista TODO
     */
    private List<Card> getToDo(){
        return toDo;
    }

    /***
     * Restituisce la lista che contiene le card all'interno della lista INPROGRESS
     * @return List<Card>, lista che contiene le card all'interno della lista INPROGRESS
     */
    private List<Card> getInProgress(){
        return inProgress;
    }

    /***
     * Aggiunge una card alla lista listaToAdd
     *
     * @param listaToAdd lista a cui aggiungere card
     * @param card card da aggiungere a listaToAdd
     */
    public synchronized void addCard(String listaToAdd, Card card){
        if(showCard(card.getNameCard()) == null) {
            if (listaToAdd.equals("toDo")) {
                card.addHistory("toDo");
                toDo.add(card);
            } else if (listaToAdd.equals("toBeRevised")) {
                card.addHistory("toBeRevised");
                toBeRevised.add(card);
            } else if (listaToAdd.equals("inProgress")) {
                card.addHistory("inProgress");
                inProgress.add(card);
            } else {
                card.addHistory("done");
                done.add(card);
            }
        } else {
            System.out.println("The card already exists");
        }
    }

    /***
     * Restituisce la copia della card cercata
     * @param cardName, nome della card
     * @return Card la copia della card
     */
    public synchronized Card showCard(String cardName){
        cardsToShow.addAll(toDo);
        cardsToShow.addAll(toBeRevised);
        cardsToShow.addAll(inProgress);
        cardsToShow.addAll(done);
        for (Card value : cardsToShow) {
            if (value.getNameCard().equals(cardName)){
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
        cardsToShow.addAll(toDo);
        cardsToShow.addAll(toBeRevised);
        cardsToShow.addAll(inProgress);
        cardsToShow.addAll(done);
        return cardsToShow;
    }

    /***
     * Aggiunge l'utente user alla lista dei membri del progetto
     * @param user utente da inserire
     */
    public synchronized void addMember(User user){
        if(!members_sync.contains(user)) {
            members_sync.add(user);
        }
    }

    /***
     * Muove la card dalla lista del progetto listaDiPartenza alla lista del progetto listaDiDestinazione
     * @param listaDiPartenza lista in cui si trova attualmente la card
     * @param listaDiDestinazione lista in cui si trovera' la card
     * @param card da spostare
     */
    public synchronized void moveCard(List<Card> listaDiPartenza, List<Card> listaDiDestinazione, Card card){
        listaDiPartenza.remove(card); //si faccia il controllo che queste siano dello stesso progetto
        card.addHistory("removed from " + listaDiPartenza); //mi restituisce solo il nome o di piu'? Da testare
        listaDiDestinazione.add(card);
        card.addHistory("added to " + listaDiDestinazione); //mi restituisce solo il nome o di piu'? Da testare
    }

    /***
     * Restituisce la history di una card
     * @param cardname
     * @return
     */
    public String getCardHistory(String cardname){
        Card card = showCard(cardname);
        if(card!=null) {
            String history = card.getHistory();
            return history;
        }
        return null;
    }
}

