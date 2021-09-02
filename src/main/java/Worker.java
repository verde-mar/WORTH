import java.util.concurrent.ConcurrentHashMap;

public class Worker {
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
}
