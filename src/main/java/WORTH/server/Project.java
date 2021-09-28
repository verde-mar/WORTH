package WORTH.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project e' la classe che rappresenta un progetto,
 * e include come e' possibile interagirci
 */
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
    private List<String> members;
    @JsonIgnore /* Il progetto corrente */
    File project;
    @JsonIgnore /* Booleano che indica se e' stata creata una directory associata al progetto */
    boolean mkdir_bool;
    @JsonIgnore /* Utenti registrati al servizio */
    HashMap<String, User> utenti_registrati;

    /**
     * Costruttore vuoto della classe (necessario a jackson)
     */
    public Project(){}

    /**
     * Costruttore della classe
     * @param nameProject Nome del progetto
     */
    public Project(String nameProject, String nickName, HashMap<String, User> utenti_registrati){
        this.nameProject = nameProject;
        to_Do = Collections.synchronizedList(new LinkedList<>());
        inProgress = Collections.synchronizedList(new LinkedList<>());
        toBeRevised = Collections.synchronizedList(new LinkedList<>());
        done = Collections.synchronizedList(new LinkedList<>());
        members = Collections.synchronizedList(new LinkedList<>());
        project = new File("./" + nameProject);
        mkdir_bool = project.mkdir();
        this.utenti_registrati = utenti_registrati;
        if(!isMember(nickName)) {
            members.add(nickName);
            User user = utenti_registrati.get(nickName);
            if(!user.getList_prj().contains(nameProject))
                user.getList_prj().add(nameProject);
        }
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
     * @return List<WORTH.server.Card>, lista che contiene le card all'interno della lista done
     */
    public List<Card> getDone(){
        return done;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista toBeRevised
     * @return List<WORTH.server.Card>, lista che contiene le card all'interno della lista toBeRevised
     */
    public List<Card> getToBeRevised(){
        return toBeRevised;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista to_Do
     * @return List<WORTH.server.Card>, lista che contiene le card all'interno della lista to_Do
     */
    public List<Card> getTo_Do(){
        return to_Do;
    }

    /**
     * Restituisce la lista che contiene le card all'interno della lista inProgress
     * @return List<WORTH.server.Card>, lista che contiene le card all'interno della lista inProgress
     */
    public List<Card> getInProgress(){
        return inProgress;
    }


    /**
     * Aggiunge una card al progetto
     * @param cardname Nome della card da aggiungere
     * @param description Descrizione associata alla card
     * @param projectName Nome del progetto
     */
    public synchronized void addCardProject(String cardname, String description, String projectName) throws Exception {
        /* Aggiunge la card alla struttura dati locale */
        if(cardname != null) {
            if (showCardProject(cardname) == null) {
                Card card = new Card(cardname);
                card.addHistory("added to to_Do; ");
                card.addDescription(description);
                card.addCurrentList("to_Do");
                to_Do.add(card);

                /* Memorizza la card su disco */
                card.writeOnDisk(projectName);
            } else throw new Exception("The card was already there.");
        } else throw new Exception("The card is null");
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto
     * @param projects Insieme totale dei progetti
     */
    public synchronized void cancelProject(String projectName, ConcurrentHashMap<String, Project> projects) throws Exception {
        /* Cancella il progetto dalla struttura dati locale */
        boolean remove_prj = true;
        if(getTo_Do().size() == 0 && getInProgress().size() == 0 && getToBeRevised().size() == 0 && getDone().size()!=0) {
            remove_prj = projects.remove(projectName, projects.get(projectName));
        }
        if(remove_prj) {
            /* Cancella il progetto dal disco */
            boolean eliminate = project.delete();
            if (!eliminate) {
                File[] files = project.listFiles();
                assert files != null;
                for(File file : files){
                    boolean file_eliminate = file.delete();
                    if(!file_eliminate) throw new Exception("Cannot eliminate the files in " + projectName);
                }
                eliminate = project.delete();
                if(eliminate) System.out.println(nameProject + " was eliminated");
            }

        }
    }

    /**
     * Restituisce la card di nome cardName
     * @param cardname Nome della card
     * @return WORTH.server.Card Restituisce la card di nome cardName
     */
    public Card showCardProject(String cardname) throws Exception {
        if(cardname!=null) {
            Card card = showCardList(to_Do, cardname);
            if (card == null) {
                card = showCardList(inProgress, cardname);
                if (card == null) {
                    card = showCardList(toBeRevised, cardname);
                    if (card == null) {
                        card = showCardList(done, cardname);
                    }
                }
            }
            return card;
        } throw new Exception("The card is null.");
    }

    /**
     * Cerca nella lista passata come parametro la card di nome cardname
     * @param lista Lista in cui cercare
     * @param cardName Nome della card
     * @return Card La card ricercata
     */
    public synchronized Card showCardList(List<Card> lista, String cardName){
        for (Card value : lista) {
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Muove la card dalla lista del progetto listaDiPartenza alla lista del progetto listaDiDestinazione
     * @param listaDiPart Nome della lista in cui si trova attualmente la card
     * @param listadiDest Nome della lista in cui si trovera' la card
     * @param card Carta da spostare
     */
    public synchronized void moveCard(String listaDiPart, String listadiDest, Card card) throws Exception {
        card.eraseCurrentList();
        switch (listaDiPart) {
            case "to_Do" : {
                to_Do.remove(card);
                card.addHistory("removed from to_Do; ");
                if(listadiDest.equals("inProgress") && !inProgress.contains(card)){
                    inProgress.add(card);
                    card.addHistory("added to inProgress; ");
                    card.addCurrentList("inProgress");
                } else throw new Exception("Moving not allowed.");
                break;
            }
            case "inProgress" : {
                inProgress.remove(card);
                card.addHistory("removed from inProgress; ");
                if((listadiDest.equals("done") && !done.contains(card))){
                    done.add(card);
                    card.addHistory("added to done; ");
                    card.addCurrentList("done");
                } else if((listadiDest.equals("toBeRevised") && !toBeRevised.contains(card))){
                    toBeRevised.add(card);
                    card.addHistory("added to toBeRevised; ");
                    card.addCurrentList("toBeRevised");
                } else throw new Exception("Moving not allowed.");
                break;
            }
            case "toBeRevised" : {
                toBeRevised.remove(card);
                card.addHistory("removed from toBeRevised; ");
                if((listadiDest.equals("done") && !done.contains(card))){
                    done.add(card);
                    card.addHistory("added to done; ");
                    card.addCurrentList("done");
                } else if((listadiDest.equals("inProgress") && !inProgress.contains(card))){
                    inProgress.add(card);
                    card.addHistory("added to inProress; ");
                    card.addCurrentList("inProgress");
                }  else throw new Exception("Moving not allowed.");
                break;
            }
        }

        card.update();
    }

    /**
     * Restituisce la history della card che appartiene al progetto corrente
     * @param card WORTH.server.Card che si trova nel programma
     * @return List<String> History di card
     */
    public List<String> getHistory(Card card){
        return card.getHistory();
    }

    /**
     * Indica se un utente e' membro del progetto corrente
     * @param username Username dell'utente che ha effettuato la richiesta
     * @return boolean
     */
    public boolean isMember(String username){
        return members.contains(username);
    }

    /**
     * Aggiunge un utente alla lista dei membri del progetto
     * @param userToAdd Nickname dell'utente da aggiungere ai membri del progetto
     */
    public synchronized void addPeople(String userToAdd) throws Exception {
        if(!isMember(userToAdd)) {
            members.add(userToAdd);
            User user = utenti_registrati.get(userToAdd);
            user.getList_prj().add(nameProject);
        } else throw new Exception("The user is already logged in that project");
    }

    /**
     * Restituisce la lista dei membri al progetto
     * @return List<String> Lista dei membri del progetto
     */
    public List<String> getMembers(){
        return members;
    }
}