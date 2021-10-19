package WORTH.shared.worthProtocol;

import WORTH.server.Card;
import WORTH.server.Project;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

/**
 * Oggetto rappresentante una risposta dal server al client
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Response {
    /* Richieste possibili */
    //todo: verifica se non servono davvero a niente
    public enum RequestType{
        login,
        logout,
        listProjects,
        createProject,
        addMember,
        showMembers,
        showCards,
        showCard,
        addCard,
        moveCard,
        getCardHistory,
        cancelProject,
    }
    /* Stringa contenente, in caso di fallimento, la spiegazione per cui la richiesta non e' andata a buon fine, altrimenti, conterra' solo 'success' */
    private String explanation;
    /* Booleano che indica se la richiesta e' andata a buon fine o no */
    private boolean done;
    /* Lista dei nomi dei membri di un progetto */
    private List<String> members;
    /* History della card, dove ogni stringa indica un movimento della card */
    private List<String> cardHistory;
    /* Card */
    private Card card;
    /* Project */
    private Project project;
    /* Richiesta */
    private Request.RequestType request;
    /* Lista dei progetti di un utente */
    private List<Project> projects;

    /**
     * Inizializza il campo richiesta, per associarla alla risposta
     * @param request Richiesta effettuata
     */
    public void setRequest(Request.RequestType request) {
        this.request = request;
    }

    /**
     * Restituisce la richiesta effettuata
     * @return Request.RequestType Richiesta effettuata
     */
    public Request.RequestType getRequest() {
        return request;
    }

    /**
     * Inizializza projects
     * @param list_prj Parametro con cui inizializzare projects
     */
    public void setProjects(List<Project> list_prj) {
        this.projects = list_prj;
    }

    /**
     * Restituisce la lista dei progetti di un utente
     * @return List<Project> Lista dei progetti
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * Restituisce la spiegazione per cui la richiesta non e' andata a buon fine
     * @return String La spiegazione per cui la richiesta non e' andata a buon fine
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Restituisce il valore di done
     * @return boolean Valore di done
     */
    public boolean getDone() {
        return done;
    }

    /**
     * Assegna alla variabile done il valore true. Quindi la richiesta e' stata eseguita con successo
     */
    public void setSuccess(){
        done = true;
        explanation = "success";
    }

    /**
     * Inizializza la variabile a false, quindi la richiesta non e' stata eseguita con successo
     * @param expl Stringa che identifica il motivo per cui la richiesta non e' terminata con successo
     */
    public void setFailure(String expl){
        done = false;
        explanation = expl;
    }

    /**
     * Inizializza i nomi dei membri del progetto
     * @param members Lista rappresentante i membri del progetto corrente
     */
    public void setMembers(List<String> members){
        this.members = members;
    }

    /**
     * Restituisce la lista dei nomi dei membri del progetto
     * @return List<String> Lista dei nomi del progetto
     */
    public List<String> getMembers() {
        return members;
    }

    /**
     * Inizializza l' history della card corrente
     * @param history History della card considerata
     */
    public void setHistory(List<String> history){
        cardHistory = history;
    }

    /**
     * Restituisce la history di una card
     * @return List<Project> History di una card
     */
    public List<String> getHistory() {
        return cardHistory;
    }

    /**
     * Inizializza il campo Card
     * @param card Card da inviare come risposta
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Restituisce una card
     * @return Card Una card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Inizializza il campo Project
     * @param project Progetto da inviare come risposta
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Restituisce un progetto
     * @return Project Un progetto
     */
    public Project getProject() {
        return project;
    }
}
