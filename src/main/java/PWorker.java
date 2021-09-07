import java.util.concurrent.ConcurrentHashMap;

public class PWorker {

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
     * Restituisce la history della card
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
