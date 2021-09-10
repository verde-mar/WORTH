import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PWorker {
    /* Insieme totale dei progetti sul server */
    ConcurrentHashMap<String,Project> projects;

    /**
     * Costruttore della classe
     * @param projects Insieme totale dei progetti sul server
     */
    public PWorker(ConcurrentHashMap<String,Project> projects) {
        this.projects = projects;
    }

    /**
     * Aggiunge una card al progetto se questa non esiste gia'
     * @param projectName Nome del progetto in cui inserire la card
     * @param cardname Nome della card da inserire
     * @param descr Descrizione della card da inserire
     */
    public void addCard(String projectName, String cardname, String descr){
        Project project = projects.get(projectName);
        if(project.showCardProject(cardname)==null)
            project.addCardProject(cardname, descr);
    }

    /**
     * Restituisce la card di nome cardName
     * @param projectName Nome del progetto in cui si trova probabilmente la card
     * @param cardname Nome della card desiderata
     * @return Card La card desiderata
     */
    public Card showCard(String projectName, String cardname){
        Project project = projects.get(projectName);
        return project.showCardProject(cardname);
    }

    /**
     * Muove una card da una lista ad un'altra
     * @param projectName Nome del progetto in cui si trova la card
     * @param cardname Nome della card
     * @param listaPartenza Lista in cui si trova la card
     * @param listaDestinazione Lista in cui si vuole spostare la card
     */
    public void moveCard(String projectName, String cardname, String listaPartenza, String listaDestinazione){
        Project project = projects.get(projectName);
        Card card = project.showCardProject(cardname);
        project.moveCard(listaPartenza, listaDestinazione, card);
    }

    /**
     * Restituisce la history della card
     * @param projectName Nome del progetto in cui si trova la card
     * @param cardname Nome della card
     * @return List<String> History della card
     */
    public List<String> getCardHistory(String projectName, String cardname){
        Project project = projects.get(projectName);
        Card card = project.showCardProject(cardname);
        return project.getHistory(card);
    }

    /**
     * Cancella un progetto
     * @param projectName Nome del progetto da cancellare
     */
    public void cancelProject(String projectName) {
        Project project = projects.get(projectName);
        project.cancelProject(projectName);
    }
}