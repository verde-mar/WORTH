package WORTH.server;

import WORTH.persistence.CardFile;
import WORTH.persistence.ProjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.Collator;

/**
 * Oggetto rappresentate un progetto all'interno di WORTH
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
    @JsonIgnore /* Il progetto corrente su disco */
    private File project;
    @JsonIgnore  /* Istanza della classe ausiliaria per serializzazione/deserializzazione su disco del progetto corrente */
    private ProjectUtils info;
    @JsonIgnore /* Mapper necessario alla serializzazione/deserializzazione del file JSON */
    private ObjectMapper mapper;
    /* Indirizzo IP per la comunicazione UDP */
    private InetAddress addressUdp;
    @JsonIgnore /* Composizione della classe AddressGenerator. La classe contiene metodi per determinare l'indirizzo IP da associare al progetto */
    private AddressGenerator addressGenerator;
    @JsonIgnore/* ID per la serializzazione/deserializzazione della classe */
    private static final long serialVersionUID = 6125601553614499595L;

    /**
     * Costruttore vuoto della classe necessario per la serializzazione/deserializzazione del file JSON
     */
    public Project(){}

    /**
     * Costruttore della classe
     * @param nameProject Nome del progetto
     */
    public Project(String nameProject) throws Exception {
        /* Inizializza gli attributi */
        this.nameProject = nameProject;
        to_Do = new ArrayList<>();
        inProgress = new ArrayList<>();
        toBeRevised = new ArrayList<>();
        done = new ArrayList<>();
        members = new ArrayList<>();

        /* Ottiene l'istanza di AddressGenerator */
        addressGenerator = AddressGenerator.getInstance();

        /* Crea la directory associata al progetto */
        project = new File("./projects/" + nameProject);
        mapper = new ObjectMapper();
        if(!project.exists()) {
            /* Crea la directory */
            boolean mkdir_bool = project.mkdir();

            /* Inizializza e crea il file contenente i membri del progetto e l'indirizzo IP associato al progetto */
            info = new ProjectUtils();
            info.setUtenti(members);

            /* Assegna all'attributo addressUdp un indirizzo IP */
            try {
                addressUdp = addressGenerator.lookForAddress(nameProject);
            } catch(Exception e) {
                e.printStackTrace();
            }
            info.setIpAddress(addressUdp);

            /* Scrive i dati di cui sopra su disco */
            mapper.writeValue(Paths.get("./projects/" + nameProject + "/info.json").toFile(), info);
        } else {
            /* Legge da disco i membri del progetto e l'indirizzo IP, caricandoli poi in memoria */
            File informations = new File("./projects/" + nameProject + "/info.json");
            info = mapper.readValue(informations, ProjectUtils.class);
            addMembers();
            addressUdp = info.getIpAddress();

            /* Scorre tutte le card presenti su disco e le carica in memoria */
            File[] filesName = project.listFiles();
            assert filesName != null;
            for (File curr_f : filesName) {
                if(!curr_f.isDirectory() && !curr_f.getName().equals("info.json")) {
                    CardFile cardFile = mapper.readValue(curr_f, CardFile.class);
                    int endIndex = curr_f.getName().indexOf(".");
                    addCard(curr_f.getName().substring(0, endIndex), cardFile.getDescription(), cardFile.getHistory(), cardFile.getCurrentList());
                }
            }
        }
        UserManager userManager = UserManager.getInstance();
        userManager.updateRegisteredFile();
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
        /* Aggiunge la card alla struttura dati locale, se non esiste gia' */
        if (showCardProject(cardname) == null) {
            Card card = new Card(cardname);
            card.addHistory("added to to_Do; ");
            card.addDescription(description);
            card.addCurrentList("to_Do");
            to_Do.add(card);

            /* Memorizza la card su disco */
            card.writeOnDisk(projectName);
            UserManager userManager = UserManager.getInstance();
            userManager.updateRegisteredFile();
        } else throw new Exception("The card was already there.");
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto
     * @param projects Insieme totale dei progetti
     * @throws Exception Nel caso la richiesta non possa essere portata a termine
     */
    public synchronized void cancelProject(String projectName, ConcurrentHashMap<String, Project> projects) throws Exception {
        /* Cancella il progetto dalla struttura dati locale */
        boolean remove_prj = false;
        if(getTo_Do().size() == 0 && getInProgress().size() == 0 && getToBeRevised().size() == 0 && getDone().size()!=0) {
            remove_prj = projects.remove(projectName, projects.get(projectName));
            UserManager userManager = UserManager.getInstance();
            for(String nameUser : members) {
                User user = userManager.getUtente(nameUser);
                user.getList_prj().remove(this);
            }
            userManager.updateRegisteredFile();
        }
        if(remove_prj) {
            /* Cancella il progetto dal disco */
            File[] files = project.listFiles();
            assert files != null;
            for(File file : files){
                boolean file_eliminate = file.delete();
                if(!file_eliminate) throw new Exception("Cannot eliminate the files in " + projectName);
            }
            boolean eliminate = project.delete();
            if(eliminate) {
                System.out.println(nameProject + " was eliminated");
                addressGenerator.resetAddress(addressUdp);
            }
        } else {
            throw new Exception("The project cannot be removed");
        }
    }

    /**
     * Restituisce la card di nome cardName
     * @param cardname Nome della card
     * @return WORTH.server.Card Restituisce la card di nome cardName
     */
    public synchronized Card showCardProject(String cardname) {
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
     * @param cardname Nome della card da spostare
     * @throws Exception Nel caso in cui la richiesta di spostamento non vada a buon fine
     */
    public synchronized void moveCard(String listaDiPart, String listadiDest, String cardname) throws Exception {
        Card card = showCardProject(cardname);
        /* Se la card esiste */
        if(card != null) {
            Collator myCollator = Collator.getInstance();
            /* In base alla lista di partenza, si effettua il movimento della carta, altrimenti viene lanciata una eccezione */
            if ("to_Do".equals(listaDiPart)) {
                if (myCollator.compare(listadiDest, "inProgress") == 0 && !inProgress.contains(card)) {
                    to_Do.remove(card);
                    card.addHistory("removed from to_Do; ");
                    inProgress.add(card);
                    card.addHistory("added to inProgress; ");
                    /* Cancella la lista corrente per aggiornarla */
                    card.eraseCurrentList();
                    card.addCurrentList("inProgress");
                    /* Viene aggiornato il file su disco */
                    card.writeOnDisk(nameProject);
                } else throw new Exception("Moving not allowed.");
            } else if ("inProgress".equals(listaDiPart)) {
                if (myCollator.compare(listadiDest, "done") == 0 && !done.contains(card)) {
                    inProgress.remove(card);
                    card.addHistory("removed from inProgress; ");
                    done.add(card);
                    card.addHistory("added to done; ");
                    /* Cancella la lista corrente per aggiornarla */
                    card.eraseCurrentList();
                    card.addCurrentList("done");
                    /* Viene aggiornato il file su disco */
                    card.writeOnDisk(nameProject);
                } else if (myCollator.compare(listadiDest, "toBeRevised") == 0 && !toBeRevised.contains(card)) {
                    inProgress.remove(card);
                    card.addHistory("removed from inProgress; ");
                    toBeRevised.add(card);
                    card.addHistory("added to toBeRevised; ");
                    /* Cancella la lista corrente per aggiornarla */
                    card.eraseCurrentList();
                    card.addCurrentList("toBeRevised");
                    /* Viene aggiornato il file su disco */
                    card.writeOnDisk(nameProject);
                } else throw new Exception("Moving not allowed.");
            } else if ("toBeRevised".equals(listaDiPart)) {
                if (myCollator.compare(listadiDest, "done") == 0 && !done.contains(card)) {
                    toBeRevised.remove(card);
                    card.addHistory("removed from toBeRevised; ");
                    done.add(card);
                    card.addHistory("added to done; ");
                    /* Cancella la lista corrente per aggiornarla */
                    card.eraseCurrentList();
                    card.addCurrentList("done");
                    /* Viene aggiornato il file su disco */
                    card.writeOnDisk(nameProject);
                } else if (myCollator.compare(listadiDest, "inProgress") == 0 && !inProgress.contains(card)) {
                    toBeRevised.remove(card);
                    card.addHistory("removed from toBeRevised; ");
                    inProgress.add(card);
                    card.addHistory("added to inProress; ");
                    /* Cancella la lista corrente per aggiornarla */
                    card.eraseCurrentList();
                    card.addCurrentList("inProgress");
                    /* Viene aggiornato il file su disco */
                    card.writeOnDisk(nameProject);
                } else throw new Exception("Moving not allowed.");
            } else {
                throw new Exception(listaDiPart + " is not a valid entry");
            }
            UserManager userManager = UserManager.getInstance();
            userManager.updateRegisteredFile();
        } else {
            throw new Exception("This card doesn't exist");
        }
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
     * @return boolean Se l'utente e' gia membro del progetto o no
     */
    public boolean isMember(String username){
        return members.contains(username);
    }

    /**
     * Aggiunge un utente al progetto
     * @param userToAdd Nickname dell'utente da inserire
     * @throws Exception Nel caso in cui l'utente identificato da userToAdd non sia registrato o sia gia' membro del progetto
     */
    public synchronized void addPeople(String userToAdd) throws Exception {
        /* Cerca l'utente il cui nickname e' userToAdd */
        UserManager userManager = UserManager.getInstance();
        User user = userManager.getUtenti().get(userToAdd);
        /* Se l'utente da aggiungere non esiste */
        if (user != null) {
            /* Se l'utente non e' gia' membro del progetto */
            if (!isMember(userToAdd)) {
                /* Aggiunge l'utente alla lista dei membri del progetto */
                members.add(userToAdd);
                /* Aggiunge il progetto alla lista dei progetti a cui l'utente appartiene */
                user.getList_prj().add(this);
                /* Aggiorna il file dei membri del progetto presente su disco */
                info.setUtenti(members);
                mapper.writeValue(Paths.get("./projects/" + nameProject + "/info.json").toFile(), info);
            } else {
                throw new Exception("The user is already member of the project");
            }
            userManager.updateRegisteredFile();
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
    public void addCard(String cardname, String description, List<String> history, String currentList) throws Exception {
        if (showCardProject(cardname) == null) {
            Card card = new Card(cardname);
            card.addDescription(description);
            card.addCurrentList(currentList);
            for(String story : history) {
                card.addHistory(story);
            }
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
     */
    public void addMembers() {
        members.addAll(info.getUtenti());
    }

    /**
     * Restituisce l'indirizzo IP del progetto
     * @return InetAddress Indirizzo IP del progetto
     */
    public InetAddress getAddressUdp() {
        return addressUdp;
    }

}