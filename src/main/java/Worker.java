import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Worker {
    /* Progetto corrente */
    Project project;

    /***
     * Costruttore della classe
     * @param project Progetto corrente
     */
    public Worker(Project project){
        this.project = project;
    }

    /***
     * Crea un progetto e lo aggiunge all'insieme dei progetti
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     */
    public void createProject(ConcurrentHashMap<String, Project> projects){
        Project result = projects.putIfAbsent(project.getNameProject(), project);
        if(result!=null) System.out.println("C'era gia'");
        else System.out.println("Appena inserito un progetto");
    }

    /***
     * Cancella un progetto
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     */
    public void cancelProject(ConcurrentHashMap<String, Project> projects){
        projects.remove(project.getNameProject(), project);
    }

    /***
     * Restituisce la lista dei progetti presenti nel server
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     * @return Collection<Project> Insieme dei progetti presenti nel server
     */
    public Collection<Project> listProjects(ConcurrentHashMap<String, Project> projects){
        return projects.values();
    }

    /***
     * Restituisce la lista degli utenti totali
     * @param projects Struttura dati rappresentante l'insieme dei progetti all'interno del server
     * @return Vector<User> Lista degli utenti
     */
    public Vector<User> listUsers(ConcurrentHashMap<String, Project> projects){
        Vector<Project> progetti = (Vector<Project>) listProjects(projects);
        Vector<User> utenti = new Vector<>();
        for (Project value : progetti) {
            utenti.addAll(value.showMembers());
        }
        return utenti;
    }

    /***
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
}
