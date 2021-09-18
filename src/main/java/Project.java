import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Project {
    /* Nome del progetto, univoco */
    private String nameProject;
    /* Lista delle card che devono ancora essere prese in carico da un membro del progetto */
    private List<Card> to_Do;
    /* Lista delle card che sono state prese in carico da un membro del progetto */
    private List<Card> inProgress;
    /* Lista delle card le cui operazioni sono da revisionare da un membro del progetto */
    private List<Card> toBeRevised;
    /* Lista delle card le cui operazioni associate sono portate a termine da un membro del progetto */
    private List<Card> done;
    /* Lista di membri del progetto */
    private List<User> members_sync;
    /* Il progetto corrente */
    File project;

    public Project(){}

    /**
     * Costruttore della classe
     * @param nameProject Nome del progetto
     */
    public Project(String nameProject){
        this.nameProject = nameProject;
        to_Do = Collections.synchronizedList(new LinkedList<>());
        inProgress = Collections.synchronizedList(new LinkedList<>());
        toBeRevised = Collections.synchronizedList(new LinkedList<>());
        done = Collections.synchronizedList(new LinkedList<>());
        members_sync = Collections.synchronizedList(new LinkedList<>());
        project = new File("./" + nameProject);
        project.mkdir();

    }

    /**
     * Restituisce il nome del progetto
     * @return String Il nome del progetto
     */
    public String getNameProject() {
        return nameProject;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista done
     * @return List<Card>, lista che contiene le card all'interno della lista done
     */
    public List<Card> getDone(){
        return done;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista toBeRevised
     * @return List<Card>, lista che contiene le card all'interno della lista toBeRevised
     */
    public List<Card> getToBeRevised(){
        return toBeRevised;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista to_Do
     * @return List<Card>, lista che contiene le card all'interno della lista to_Do
     */
    public List<Card> getTo_Do(){
        return to_Do;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista inProgress
     * @return List<Card>, lista che contiene le card all'interno della lista inProgress
     */
    public List<Card> getInProgress(){
        return inProgress;
    }

    public void setDone(List<Card> done) {
        this.done = done;
    }

    public void setInProgress(List<Card> inProgress) {
        this.inProgress = inProgress;
    }

    public void setMembers_sync(List<User> members_sync) {
        this.members_sync = members_sync;
    }

    public void setTo_Do(List<Card> to_Do) {
        this.to_Do = to_Do;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public void setProject(File project) {
        this.project = project;
    }

    public void setToBeRevised(List<Card> toBeRevised) {
        this.toBeRevised = toBeRevised;
    }

    /**
     * Restituisce la lista dei membri del progetto
     * @return List<User> lista dei membri del progetto
     */
    public List<User> showMembers(){
        return members_sync;
    }

    /**
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

    /**
     * Aggiunge una card al progetto
     * @param cardname Nome della card da aggiungere
     * @param description Descrizione associata alla card
     * @param projectName Nome del progetto
     */
    public synchronized void addCardProject(String cardname, String description, String projectName) throws IOException {
        /* Aggiunge la card alla struttura dati locale */
        Card card = new Card(cardname);
        card.addHistory("added to to_Do; ");
        card.addDescription(description);
        card.addCurrentList("to_Do");
        to_Do.add(card);

        /* Memorizza la card su disco */
        card.writeOnDisk(projectName);
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto
     * @param projects Insieme totale dei progetti
     */
    public synchronized void cancelProject(String projectName, ConcurrentHashMap<String, Project> projects){
        /* Cancella il progetto dalla struttura dati locale */
        if(getTo_Do().size() == 0 && getInProgress().size() == 0 && getToBeRevised().size() == 0 && getDone().size()!=0)
            projects.remove(projectName, projects.get(projectName));

        /* Cancella il progetto dal disco */
        boolean eliminate = project.delete();
        if(eliminate)
            System.out.println(projectName + " was eliminated");
        else
            System.out.println("Could'nt eliminate "+projectName);
    }

    /**
     * Restituisce la card di nome cardName
     * @param cardname Nome della card
     * @return Card Restituisce la card di nome cardName
     */
    public synchronized Card showCardProject(String cardname){
        Card card = showCardTo_Do(cardname);
        if(card==null){
            card = showCardDoing(cardname);
            if(card==null) {
                card = showCardToBeRevised(cardname);
                if(card==null){
                    card = showCardDone(cardname);
                }
            }
        }
        return card;
    }

    /**
     * Restituisce la copia della card con nome cardName (la ricerca viene effettuata nella lista to_Do)
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public synchronized Card showCardTo_Do(String cardName){
        for (Card value : to_Do) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Restituisce la copia della card con nome cardName (la ricerca viene effettuata nella lista inProgress)
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public synchronized Card showCardDoing(String cardName){
        for (Card value : inProgress) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Restituisce la copia della card con nome cardName (la ricerca viene effettuata nella lista toBeRevised)
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public synchronized Card showCardToBeRevised(String cardName){
        for (Card value : toBeRevised) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }

        return null;
    }

    /**
     * Restituisce la copia della card cercata
     * @param cardName, nome della card
     * @return Card la copia della card se e' presente, altrimenti null
     */
    public synchronized Card showCardDone(String cardName){
        for (Card value : done) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Restituisce tutte le card del progetto
     * @return Vector<Card> contenente tutte le card del progetto
     */
    public synchronized Vector<Card> showCards(){
        Vector<Card> cardsToShow = new Vector<>();
        cardsToShow.removeAllElements();
        cardsToShow.addAll(to_Do);
        cardsToShow.addAll(toBeRevised);
        cardsToShow.addAll(inProgress);
        cardsToShow.addAll(done);
        return cardsToShow;
    }

    /**
     * Muove la card dalla lista del progetto listaDiPartenza alla lista del progetto listaDiDestinazione
     * @param listaDiPart Nome della lista in cui si trova attualmente la card
     * @param listadiDest Nome della lista in cui si trovera' la card
     * @param card Carta da spostare
     */
    //il messaggio di aggiornamento va nella chat
    public synchronized void moveCard(String listaDiPart, String listadiDest, Card card) throws IOException {
        card.eraseCurrentList();
        switch (listaDiPart) {
            case "to_Do" : {
                to_Do.remove(card);
                card.addHistory("removed from to_Do; ");
            }
            case "inProgress" : {
                inProgress.remove(card);
                card.addHistory("removed from inProgress; ");
            }
            case "done" : {
                done.remove(card);
                card.addHistory("removed from done; ");
            }
            case "toBeRevised" : {
                toBeRevised.remove(card);
                card.addHistory("removed from toBeRevised; ");
            }
        }

        switch (listadiDest) {
            case "to_Do" : {
                to_Do.add(card);
                card.addHistory("added to to_Do; ");
                card.addCurrentList("to_Do");
            }
            case "inProgress" : {
                inProgress.add(card);
                card.addHistory("added to inProgress; ");
                card.addCurrentList("inProgress");
            }
            case "done" : {
                done.add(card);
                card.addHistory("added to done; ");
                card.addCurrentList("done");
            }
            case "toBeRevised" : {
                toBeRevised.add(card);
                card.addHistory("added to toBeRevised; ");
                card.addCurrentList("toBeRevised");
            }
        }

        card.update();
    }

    /**
     * Restituisce la history della card che appartiene al progetto corrente
     * @param card Card che si trova nel programma
     * @return List<String> History di card
     */
    public List<String> getHistory(Card card){
        return card.getHistory();
    }
}