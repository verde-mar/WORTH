package WORTH.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.LinkedList;
import java.util.List;

//todo: posso fare una classe WORTH.server.Message per risposta e richiesta separate

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Message {
    protected enum RequestType{
        login,
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

    /* Parametri necessari per la richiesta */
    private String projectName;
    private String cardName;
    private String nickUtente;
    private String password;
    private String description;
    private RequestType request;
    private String listaPartenza;
    private String listaDestinazione;
    private String nameUser;

    /* Parametri necessari per la risposta */
    private String explanation;
    private boolean done;
    private List<String> cardsName;
    private List<String> members;
    private List<String> projects;
    private List<String> cardHistory;
    private String cardCurrentList;

    /* ------ Iniziano le oeprazioni di get ------ */

    /**
     * Restituisce la richiesta effettuata
     * @return String la richiesta effettuata
     */
    public RequestType getRequest() {
        return request;
    }

    /**
     * Restituisce il nome del progetto
     * @return String il nome del progetto
     */
    public String getProjectName(){
        return projectName;
    }

    /**
     * Restituisce il nome della card
     * @return String il nome della card
     */
    public String getCardName(){
        return cardName;
    }

    /**
     * Restituisce il nickname dell'utente
     * @return String il nickname dell'utente
     */
    public String getNickUtente(){
        return nickUtente;
    }

    /**
     * Restituisce la descrizione da associare ad una card
     * @return String la descrizione da associare ad una card
     */
    public String getDescription(){
        return description;
    }

    /**
     * Restituisce la lista di partenza per spostare una card
     * @return String la lista di partenza necessaria a spostare una card
     */
    public String getListaPartenza(){
        return listaPartenza;
    }

    /**
     * Restituisce la lista di destinazione per spostare una card
     * @return String la lista di destinazione necessaria a spostare una card
     */
    public String getListaDestinazione(){
        return listaDestinazione;
    }

    /* ------ Iniziano le operazioni di set ------ */

    /**
     * Assegna al parametro cardCurrentList il valore passato per parametro
     * @param currentL String Lista corrente in cui si trova una card
     */
    public void setCardCurrentList(String currentL){
        cardCurrentList = currentL;
    }

    /**
     * Assegna al campo description la stringa passata per parametro
     * @param descr Descrizione della card
     */
    public void setDescription(String descr) {
        description = descr;
    }

    /**
     * Assegna alla variabile done il valore true. Quindi la richiesta e' stata eseguita con successo
     */
    public void setSuccess(){
        done = true;
        explanation = "success";
    }

    /**
     * Assegna al campo projectName la stringa passata per parametro
     * @param projectName Nome da assegnare a projectName
     */
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    /**
     * Assegna al campo cardName la stringa passata per parametro
     * @param name Nome della card da assegnare a cardName
     */
    public void setCardName(String name){
        this.cardName = name;
    }

    /**
     * Assegna a ciascun campo di cards i nomi delle card richieste
     * @param cards List<WORTH.server.Card> rappresentante le card di un determinato progetto, precedentemente restituite
     */
    public void setCardsName(List<Card> cards){
        cardsName = new LinkedList<>();
        for(Card card : cards){
            cardsName.add(card.getNameCard());
        }
    }

    /**
     * Assegna a ciascun campo di projects i nomi dei peoject richiesti
     * @param projs Lista dei progetti
     */
    public void setProjects(List<Project> projs){
        projects = new LinkedList<>();
        for(Project project : projs){
            projects.add(project.getNameProject());
        }
    }

    /**
     * Assegna al valore request quello passato per parametro
     * @param request Tipo di richiesta effettuata
     */
    public void setRequest(RequestType request){
        this.request = request;
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
    public void setMembers(List<User> members){
        this.members = new LinkedList<>();
        for(User user : members){
            this.members.add(user.getName());
        }
    }

    /**
     * Setta l' history della card corrente
     * @param history History della card considerata
     */
    public void setHistory(List<String> history){
        cardHistory = history;
    }
}
