import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectWorker {

    /***todo: new User() non va bene, bisogna inserire quell'utente al progetto
     * Crea un progetto e lo aggiunge all'insieme dei progetti
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     */
    public synchronized void createProject(ConcurrentHashMap<String, Project> projects, String projectName){
        projects.putIfAbsent(projectName, new Project(projectName, new User()));

    }

    /***
     * Cancella un progetto solo se tutte le card sono nella lista done
     * @param projects Struttura dati rappresentante l' insieme dei progetti all' interno del server
     */
    public void cancelProject(ConcurrentHashMap<String, Project> projects, String projectName){
        Project project = projects.get(projectName);
        if(project.getToDo().size() == 0 && project.getInProgress().size() == 0 && project.getToBeRevised().size() == 0 && project.getDone().size()!=0)
            projects.remove(projectName, projects.get(projectName));
        else
            System.out.println("There are two possibilities: each card isn't in 'done' or the project has just been created.");
    }

    /***
     * TODO: CORREGGERLA, serve il nome dell'utente
     * Restituisce la lista dei progetti presenti nel server
     * @param projects Struttura dati rappresentante l' insieme dei progetti all' interno del server
     * @return Collection<Project> Insieme dei progetti presenti nel server
     */
    public Collection<Project> listProjects(ConcurrentHashMap<String, Project> projects){
        return projects.values();
    }

    /***
     * //TODO: DA CORREGGERE PURE QUESTA, VEDI TRELLO
     * Restituisce la lista degli utenti totali
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     * @return Vector<User> Lista degli utenti
     */
    public Vector<User> listUsers(ConcurrentHashMap<String, Project> projects){
        Vector<Project> progetti = (Vector<Project>) projects.values();
        Vector<User> utenti = new Vector<>();
        for (Project value : progetti) {
            utenti.addAll(value.showMembers());
        }
        return utenti;
    }

    /***
     * //TODO: DA CORREGGERE PURE QUESTA, VEDI TRELLO
     * Restituisce la lista degli utenti totali e che sono online attualmente
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     * @return Vector<User> Lista degli utenti online
     */
    public Vector<User> listOnlineUsers(ConcurrentHashMap<String, Project> projects){
        Vector<Project> progetti = (Vector<Project>) listProjects(projects);
        Vector<User> utenti = new Vector<>();
        for (Project value : progetti) {
            utenti.addAll(value.showOnlineMembers());
        }
        return utenti;
    }

    /***
     * Restituisce la card di nome cardName
     * @param projects Insieme totale dei progetti
     * @param projectName Nome del progetto a cui appartiene la card
     * @param cardname Nome della card
     * @return Card Restituisce la card di nome cardName
     */
    public synchronized Card showCard(ConcurrentHashMap<String, Project> projects, String projectName, String cardname){
        Project project = projects.get(projectName);
        Card card = project.showCardTo_Do(cardname);
        if(card==null){
            card = project.showCardDoing(cardname);
            if(card==null) {
                card = project.showCardToBeRevised(cardname);
                if(card==null){
                    card = project.showCardDone(cardname);
                }
            }
        }
        return card;
    }

    /***
     * Aggiunge una carta alla lista to_Do
     * @param projects Insieme totale dei progetti
     * @param projectName Nome del progetto a cui aggiungere la carta
     * @param cardname Nome della carta
     * @param description Descrizione della carta
     */
    public synchronized void addCard(ConcurrentHashMap<String, Project> projects, String projectName, String cardname, String description){
        Project project = projects.get(projectName);
        project.addCardToDo(cardname, description);
    }

    /***
     * Richiede la history della card
     * @param projects Insieme totali dei progetti
     * @param projectName Nome del progetto a cui appartiene la card
     * @param cardName Nome della card
     * @return String La history della card
     */
    public synchronized String getCardHistory(ConcurrentHashMap<String, Project> projects, String projectName, String cardName){
        Card card = showCard(projects, projectName, cardName);
        return card.getHistory();
    }
}
