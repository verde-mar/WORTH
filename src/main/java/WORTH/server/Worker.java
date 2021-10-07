package WORTH.server;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Worker e' la classe ausiliaria a Handler, e che
 * esegue tutte le operazioni necessarie a restituire una risposta
 */
public class Worker {
    /* Insieme totale dei progetti sul WORTH.server */
    ConcurrentHashMap<String,Project> projects;
    private final HashMap<String, User> utenti_registrati;

    /**
     * Costruttore della classe
     * @param projects Insieme totale dei progetti sul WORTH.server
     */
    public Worker(ConcurrentHashMap<String, Project> projects, HashMap<String, User> utenti_registrati) {
        this.projects = projects;
        this.utenti_registrati = utenti_registrati;
    }

    /**
     * Aggiunge una card al progetto se questa non esiste gia'
     * @param projectName Nome del progetto in cui inserire la card
     * @param cardname Nome della card da inserire
     * @param descr Descrizione della card da inserire
     * @param userName NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     */
    public void addCard(String projectName, String cardname, String descr, String userName) throws Exception {
        Project project = projects.get(projectName);
        if(project!=null) {
            if(project.isMember(userName)) {
                project.addCardProject(cardname, descr, projectName);
            }
            else {
                throw new Exception("You are not allowed.");
            }
        } else throw new Exception("That project doesn't exist.");
    }

    /**
     * Aggiunge un utente ad un progetto
     * @param projectName Nome del progetto
     * @param userToAdd Nome dell'utente da aggiungere
     * @param userName Nome dell'utente che aggiunge l'utente al progetto
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione o l'utente da aggiungere al progetto ci sia gia'
     */
    public void addMember(String projectName, String userToAdd, String userName) throws Exception {
            Project project = projects.get(projectName);
            if (project != null) {
                if (project.isMember(userName)) {
                    project.addPeople(userToAdd, utenti_registrati);
                } else {
                    throw new Exception("You are not allowed.");
                }
            } else throw new Exception("That project doesn't exist.");
    }

    /**
     * Restituisce la card di nome cardName
     * @param projectName Nome del progetto in cui si trova probabilmente la card
     * @param cardname Nome della card desiderata
     * @param userName NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     * @return WORTH.server.Card La card desiderata
     */
    public Card showCard(String projectName, String cardname, String userName) throws Exception {
        Project project = projects.get(projectName);
        if(project!=null) {
            if(project.isMember(userName))
                return project.showCardProject(cardname);
            else throw new Exception("You are not allowed.");
        } else throw new Exception("That project doesn't exist.");
    }

    /**
     * Muove una card da una lista ad un'altra
     * @param projectName Nome del progetto in cui si trova la card
     * @param cardname Nome della card
     * @param listaPartenza Lista in cui si trova la card
     * @param listaDestinazione Lista in cui si vuole spostare la card
     * @param userName NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     */
    public void moveCard(String projectName, String cardname, String listaPartenza, String listaDestinazione, String userName) throws Exception {
        if(listaDestinazione != null && listaPartenza != null) {
            Project project = projects.get(projectName);
            if (project != null) {
                if (project.isMember(userName)) {
                    Card card = project.showCardProject(cardname);
                    project.moveCard(listaPartenza, listaDestinazione, card);
                } else throw new Exception("You are not allowed.");
            } else throw new Exception("That project doesn't exist.");
        } else throw new Exception("La lista di partenza o la lista di destinazione sono null o contengono qualche errore.");
    }

    /**
     * Restituisce la history della card
     * @param projectName Nome del progetto in cui si trova la card
     * @param cardname Nome della card
     * @param userName NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     * @return List<String> History della card
     */
    public List<String> getCardHistory(String projectName, String cardname, String userName) throws Exception {
        Project project = projects.get(projectName);
        if(project!=null) {
            if(project.isMember(userName)) {
                Card card = project.showCardProject(cardname);
                return project.getHistory(card);
            }
            else throw new Exception("You are not allowed.");
        } else throw new Exception("That project doesn't exist.");
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto da cancellare
     * @param userName NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     */
    public void cancelProject(String projectName, String userName) throws Exception {
        Project project = projects.get(projectName);
        if(project!=null) {
            if (project.isMember(userName)) {
                project.cancelProject(projectName, projects);
            } else throw new Exception("You are not allowed.");
        } else throw new Exception("That project doesn't exist.");
    }

    /**
     * Restituisce il progetto come risposta, da cui il client ricavera' ordinatamente tutte le card appartenenti
     * @param projectName Nome del progetto
     * @param userName Nickname dell'utente che ha richiesto l'operazione
     * @return Project Progetto richiesto
     * @throws Exception Nel caso in cui l'utente non sia autorizzato
     */
    public Project showCards(String projectName, String userName) throws Exception {
        Project project = projects.get(projectName);
        if(project!=null) {
            if(project.isMember(userName)) {
                return project;
            }
            else throw new Exception("You are not allowed.");
        } else throw new Exception("That project doesn't exist.");
    }

    /**
     * Crea il progetto
     * @param projectName Nome del progetto
     * @param nickUtente Nickname dell'utente che ha richiesto la creazione
     * @throws Exception Nel caso in cui il progetto esistesse gia'
     */
    public void createProject(String projectName, String nickUtente) throws Exception {
        if(projectName != null && nickUtente != null) {
            Project project = new Project(projectName);
            Project prj = projects.putIfAbsent(projectName, project);
            if(prj==null) {
                project.addPeople(nickUtente, utenti_registrati);
            }
            else throw new Exception("The project was already there");
        } else throw new Exception("Project or username are null.");
    }

    /**
     * Restituisce i membri del progetto
     * @param projectName Nome del progetto
     * @return List<String> Lista dei membri del progetti
     */
    public List<String> showMembers(String projectName) throws Exception {
        Project project = projects.get(projectName);
        if(project!=null) {
            return project.getMembers();
        } else throw new Exception("That project doesn't exist.");
    }
}
