package WORTH.server;

import WORTH.Persistence.UserFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Project e' la classe che rappresenta un progetto,
 * e include come e' possibile interagirci
 */

public class Project implements Serializable {
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
    private File project;
    @JsonIgnore /* File contenente informazioni */
    private UserFile userFile;
    @JsonIgnore /* Mapper necessario alla serializzazione/deserializzazione del file JSON */
    ObjectMapper mapper;


    /**
     * Costruttore vuoto della classe necessario a Jackson
     */
    public Project(){}

    /**
     * Costruttore della classe
     * @param nameProject Nome del progetto
     */
    public Project(String nameProject) throws Exception {
        /* Inizializza i parametri */
        this.nameProject = nameProject;
        to_Do = Collections.synchronizedList(new LinkedList<>());
        inProgress = Collections.synchronizedList(new LinkedList<>());
        toBeRevised = Collections.synchronizedList(new LinkedList<>());
        done = Collections.synchronizedList(new LinkedList<>());
        members = Collections.synchronizedList(new LinkedList<>());

        /* Crea la directory associata al progetto */
        project = new File("./projects/" + nameProject);
        if(!project.exists()) {
            boolean mkdir_bool = project.mkdir();
            userFile = new UserFile();
            userFile.setUtenti(members);
            mapper = new ObjectMapper();
            mapper.writeValue(Paths.get("./projects/" + nameProject + "/members.json").toFile(), userFile);
        }
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
    public Card showCardProject(String cardname) {
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
    }


    /**
     * Cerca nella lista passata come parametro la card di nome cardname
     * @param lista Lista in cui cercare
     * @param cardName Nome della card
     * @return Card La card ricercata
     */
    public synchronized Card showCardList(List<Card> lista, String cardName){
        for (Card value : lista) {
            System.out.println(value.getNameCard());
            if (value.getNameCard().equals(cardName)) {
                return value;
            }
        }
        return null;
    }

    /**
     * Funzione necessaria a Jackson
     * @return String Restituisce il nome del progetto
     */
    public String getNameProject(){
        return nameProject;
    }

    /**
     * Muove la card dalla lista del progetto listaDiPartenza alla lista del progetto listaDiDestinazione
     * @param listaDiPart Nome della lista in cui si trova attualmente la card
     * @param listadiDest Nome della lista in cui si trovera' la card
     * @param card Carta da spostare
     */
    public synchronized void moveCard(String listaDiPart, String listadiDest, Card card) throws Exception {
        /* Cancella la lista corrente per aggiornarla */
        card.eraseCurrentList();

        /* In base alla lista di partenza, si effettua il movimento della carta, altrimenti viene lanciata una eccezione */
        switch (listaDiPart) {
            case "to_Do" : {
                /* Viene rimossa dalla lista corrente e aggiornata la history */
                to_Do.remove(card);
                card.addHistory("removed from to_Do; ");
                /* Vengono effettuati i controlli per permettere o no il movimento
                    e in base a questi viene spostata oppure lanciata una eccezione */
                if(listadiDest.equals("inProgress") && !inProgress.contains(card)){
                    inProgress.add(card);
                    card.addHistory("added to inProgress; ");
                    card.addCurrentList("inProgress");
                } else throw new Exception("Moving not allowed.");
                break;
            }
            case "inProgress" : {
                /* Viene rimossa dalla lista corrente e aggiornata la history */
                inProgress.remove(card);
                card.addHistory("removed from inProgress; ");
                /* Vengono effettuati i controlli per permettere o no il movimento
                    e in base a questi viene spostata oppure lanciata una eccezione */
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
                /* Viene rimossa dalla lista corrente e aggiornata la history */
                toBeRevised.remove(card);
                card.addHistory("removed from toBeRevised; ");
                /* Vengono effettuati i controlli per permettere o no il movimento
                    e in base a questi viene spostata oppure lanciata una eccezione */
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
        /* Viene aggiornato il file su disco */
        card.writeOnDisk(nameProject);
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
     * Funzione che aggiunge utenti al progetto
     * @param userToAdd Nickname dell'utente da aggiungere
     * @param utenti_registrati Struttura dati in memoria che si riferisce a tutti gli utenti registrati
     * @throws Exception Nel caso in cui l'utente sia gia' membro del progetto o non sia registrato
     */
    public synchronized void addPeople(String userToAdd, HashMap<String, User> utenti_registrati) throws Exception {
        User user = utenti_registrati.get(userToAdd);
        if(user != null) {
            if(!isMember(userToAdd)) {
                members.add(userToAdd);
                user.getList_prj().add(this);
                userFile.setUtenti(members);
                mapper.writeValue(Paths.get("./projects/" + nameProject + "/members.json").toFile(), userFile);
            } else {
                throw new Exception("The user is already member of the project");
            }
        } else {
            throw new Exception("The user isn't registered");
        }
    }


    /**
     * Restituisce la lista dei membri al progetto
     * @return List<String> Lista dei membri del progetto
     */
    public List<String> getMembers(){
        return members;
    }

    /**
     * Nella fase iniziale di inizializzazione delle strutture dati in memoria,
     * questa funzione viene chiamata per aggiungere card ad un Project
     * @param cardname String Nome della card
     * @param description String Descrizione della card
     * @param currentList String Lista corrente
     * @throws Exception Nel caso in cui la card sia gia' presente nella struttura dati
     */
    public void addCard(String cardname, String description, String currentList) throws Exception {
        if (showCardProject(cardname) == null) {
            Card card = new Card(cardname);
            card.addHistory("added to " + currentList + "; ");
            card.addDescription(description);
            card.addCurrentList(currentList);
            switch (currentList) {
                case "to_Do":
                    to_Do.add(card);
                    break;
                case "inProgress":
                    inProgress.add(card);
                    break;
                case "toBeRevised":
                    toBeRevised.add(card);
                    break;
                default:
                    done.add(card);
                    break;
            }
        } else throw new Exception("The card was already there.");
    }

    /**
     * Nella fase iniziale di inizializzazione delle strutture dati in memoria,
     * questa funzione viene usata per aggiungere un utente al progetto
     * @param mbrs File in cui sono contenuti gli utenti gia' membri del progetto
     */
    public void addMembers(UserFile mbrs) {
        members.addAll(mbrs.getUtenti());
    }
}