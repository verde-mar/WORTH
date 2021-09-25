package WORTH.server;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Worker e' la classe ausiliaria a Handler, e che
 * esegue tutte le operazioni necessarie a restituire una risposta
 */
public class Worker {
    /* Insieme totale dei progetti sul WORTH.server */
    ConcurrentHashMap<String,Project> projects;

    /**
     * Costruttore della classe
     * @param projects Insieme totale dei progetti sul WORTH.server
     */
    public Worker(ConcurrentHashMap<String,Project> projects) {
        this.projects = projects;
    }

    /**
     * Aggiunge una card al progetto se questa non esiste gia'
     * @param projectName Nome del progetto in cui inserire la card
     * @param cardname Nome della card da inserire
     * @param descr Descrizione della card da inserire
     * @param username NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     */
    public void addCard(String projectName, String cardname, String descr, String username) throws Exception {
        Project project = projects.get(projectName);
        if(project.isMember(username))
            project.addCardProject(cardname, descr, projectName);
        else throw new Exception("You are not allowed.");
    }

    /**
     * Restituisce la card di nome cardName
     * @param projectName Nome del progetto in cui si trova probabilmente la card
     * @param cardname Nome della card desiderata
     * @param username NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     * @return WORTH.server.Card La card desiderata
     */
    public Card showCard(String projectName, String cardname, String username) throws Exception {
        Project project = projects.get(projectName);
        if(project.isMember(username))
            return project.showCardProject(cardname);
        else throw new Exception("You are not allowed.");
    }

    /**
     * Muove una card da una lista ad un'altra
     * @param projectName Nome del progetto in cui si trova la card
     * @param cardname Nome della card
     * @param listaPartenza Lista in cui si trova la card
     * @param listaDestinazione Lista in cui si vuole spostare la card
     * @param username NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     */
    public void moveCard(String projectName, String cardname, String listaPartenza, String listaDestinazione, String username) throws Exception {
        Project project = projects.get(projectName);
        if(project.isMember(username)) {
            Card card = project.showCardProject(cardname);
            project.moveCard(listaPartenza, listaDestinazione, card);
        }
        else throw new Exception("You are not allowed.");
    }

    /**
     * Restituisce la history della card
     * @param projectName Nome del progetto in cui si trova la card
     * @param cardname Nome della card
     * @param username NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     * @return List<String> History della card
     */
    public List<String> getCardHistory(String projectName, String cardname, String username) throws Exception {
        Project project = projects.get(projectName);
        if(project.isMember(username)) {
            Card card = project.showCardProject(cardname);
            return project.getHistory(card);
        }
        else throw new Exception("You are not allowed.");
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto da cancellare
     * @param username NickName dell'utente che ha richiesto l'operazione
     * @throws Exception Nel caso l'utente non abbia l'autorizzazione ad effettuare tale oeprazione
     */
    public void cancelProject(String projectName, String username) throws Exception {
        Project project = projects.get(projectName);
        if(project.isMember(username)) {
            project.cancelProject(projectName, projects);
        }
        else throw new Exception("You are not allowed.");
    }
}
