package WORTH.server;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.IOException;
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
    @JsonIgnore /* Lista di membri del progetto */
    private List<String> members;
    @JsonIgnore /* Il progetto corrente */
    File project;
    @JsonIgnore /* Booleano che indica se e' stata creata una directory associata al progetto */
    boolean mkdir_bool;

    /**
     * Costruttore vuoto della classe (necessario a jackson)
     */
    public Project(){}

    /**
     * Costruttore della classe
     * @param nameProject Nome del progetto
     */
    public Project(String nameProject, String nickName){
        this.nameProject = nameProject;
        to_Do = Collections.synchronizedList(new LinkedList<>());
        inProgress = Collections.synchronizedList(new LinkedList<>());
        toBeRevised = Collections.synchronizedList(new LinkedList<>());
        done = Collections.synchronizedList(new LinkedList<>());
        members = Collections.synchronizedList(new LinkedList<>());
        project = new File("./" + nameProject);
        mkdir_bool = project.mkdir();
        if(!isMember(nickName)) members.add(nickName);
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
     * Inizializza il campo della lista done con il parametro
     * @param done Parametro contenente la lista done
     */
    public void setDone(List<Card> done) {
        this.done = done;
    }

    /**
     * Inizializza il campo della lista inProgress con il parametro
     * @param inProgress Parametro contenente la lista inProgress
     */
    public void setInProgress(List<Card> inProgress) {
        this.inProgress = inProgress;
    }

    /**
     * Inizializza il campo della lista to_Do con il parametro
     * @param to_Do Parametro contenente la lista to_Do
     */
    public void setTo_Do(List<Card> to_Do) {
        this.to_Do = to_Do;
    }

    /**
     * Inizializza il campo della lista nameProject con il parametro
     * @param nameProject Parametro contenente la lista nameProject
     */
    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    /**
     * Inizializza il campo project con il parametro
     * @param project Parametro contenente project
     */
    public void setProject(File project) {
        this.project = project;
    }

    /**
     * Inizializza il campo della lista toBeRevised con il parametro
     * @param toBeRevised Parametro contenente la lista toBeRevised
     */
    public void setToBeRevised(List<Card> toBeRevised) {
        this.toBeRevised = toBeRevised;
    }

    /**
     * Aggiunge una card al progetto
     * @param cardname Nome della card da aggiungere
     * @param description Descrizione associata alla card
     * @param projectName Nome del progetto
     */
    public synchronized void addCardProject(String cardname, String description, String projectName) throws IOException {
        /* Aggiunge la card alla struttura dati locale */
        if(showCardProject(cardname) == null) {
            Card card = new Card(cardname);
            card.addHistory("added to to_Do; ");
            card.addDescription(description);
            card.addCurrentList("to_Do");
            to_Do.add(card);

            /* Memorizza la card su disco */
            card.writeOnDisk(projectName);
        }
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto
     * @param projects Insieme totale dei progetti
     * @return boolean Restituisce false se il progetto non esiste, altrimenti true
     */
    public synchronized boolean cancelProject(String projectName, ConcurrentHashMap<String, Project> projects){
        /* Cancella il progetto dalla struttura dati locale */
        boolean remove_prj = true;
        if(getTo_Do().size() == 0 && getInProgress().size() == 0 && getToBeRevised().size() == 0 && getDone().size()!=0) {
            remove_prj = projects.remove(projectName, projects.get(projectName));
        }
        if(remove_prj) {
            /* Cancella il progetto dal disco */
            System.out.println(projects.values());
            boolean eliminate = project.delete();
            if (eliminate)
                System.out.println(projectName + " was eliminated");
            else
                System.out.println("Could'nt eliminate " + projectName);
        }
        return  remove_prj;
    }

    /**
     * Restituisce la card di nome cardName
     * @param cardname Nome della card
     * @return WORTH.server.Card Restituisce la card di nome cardName
     */
    public synchronized Card showCardProject(String cardname){
        Card card = showCardList(to_Do, cardname);
        if(card==null){
            card = showCardList(inProgress, cardname);
            if(card==null) {
                card = showCardList(toBeRevised, cardname);
                if(card==null){
                    card = showCardList(done, cardname);
                }
            }
        }
        return card;
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

}