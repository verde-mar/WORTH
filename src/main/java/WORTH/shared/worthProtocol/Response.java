package WORTH.shared.worthProtocol;

import WORTH.server.Card;
import WORTH.server.Project;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Response {
    public List<Project> getProjects() {
        return projects;
    }

    public Project getProject() {
        return project;
    }

    public enum RequestType{
        logout,
        listUsers,
        listOnlineUsers,
        listProjects,
        createProject,
        addMember,
        showMembers,
        showCards,
        showCard,
        addCard,
        moveCard,
        getCardHistory,
        readChat,
        sendChatMsg,
        cancelProject
    }

    /* Parametri necessari per la risposta */
    private String explanation;
    private boolean done;
    private List<Card> cards;
    private List<String> members;
    private List<String> cardHistory;
    private Card card;
    private Project project;
    private Request.RequestType request;
    private List<Project> projects;

    /**
     * Inizializza il campo richiesta, per associarla alla risposta
     * @param request Richiesta effettuata
     */
    public void setRequest(Request.RequestType request) {
        this.request = request;
    }

    public List<String> getHistory() {
        return cardHistory;
    }
    public Card getCard() {
        return card;
    }


    public List<String> getMembers() {
        return members;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Request.RequestType getRequest() {
        return request;
    }

    public String getExplanation() {
        return explanation;
    }

    /**
     * Assegna alla variabile done il valore true. Quindi la richiesta e' stata eseguita con successo
     */
    public void setSuccess(){
        done = true;
        explanation = "success";
    }

    /**
     * Inizializza projects
     * @param list_prj Parametro con cui inizializzare projects
     */
    public void setProjects(List<Project> list_prj) {
        this.projects = list_prj;
    }

    public boolean getDone() {
        return done;
    }

    /**
     * Assegna a ciascun campo di cards i nomi delle card richieste
     * @param cards List<WORTH.server.Card> rappresentante le card di un determinato progetto, precedentemente restituite
     */
    public void setCards(List<Card> cards){
        this.cards = cards;
    }

    /**
     * Setta la variabile a false, quindi la richiesta non e' stata eseguita con successo
     * @param expl Stringa che identifica il motivo per cui la richiesta non e' terminata con successo
     */
    public void setFailure(String expl){
        done = false;
        explanation = expl;
    }

    /**
     * Setta i nomi dei membri del progetto
     * @param members Lista rappresentante i membri del progetto corrente
     */
    public void setMembers(List<String> members){
        this.members = members;
    }

    /**
     * Setta l' history della card corrente
     * @param history History della card considerata
     */
    public void setHistory(List<String> history){
        cardHistory = history;
    }

    /**
     * Inizializza il campo Card
     * @param card Card da inviare come risposta
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Inizializza il campo Project
     * @param project Progetto da inviare come risposta
     */
    public void setProject(Project project) {
        this.project = project;
    }
}
