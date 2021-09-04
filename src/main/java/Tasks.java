import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.LinkedList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Tasks {
    private enum RequestType{
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
        cancelProjects
    }

    /* Parametri necessari per la richiesta */
    private String projectName;
    private String cardName;
    private String nickUtente;
    private String password;
    private String description;
    private String text;
    private RequestType request;
    private String listaPartenza;
    private String listaDestinazione;

    /* Parametri necessari per la risposta */
    private String explanation;
    private boolean done;
    private List<String> cardsName;
    private List<String> members;
    private List<String> projects;
    private List<String> users;
    private List<String> onlineUsers;
    private String cardHistory;

    /***
     * Restituisce la richiesta effettuata
     * @return String la richiesta effettuata
     */
    public RequestType getRequest(){
        switch(request){
            case readChat -> {
                return RequestType.readChat;
            }
            case login -> {
                return RequestType.login;
            }
            case createProject -> {
                return RequestType.createProject;
            }
            case logout -> {
                return RequestType.logout;
            }
            case addCard -> {
                return RequestType.addCard;
            }
            case moveCard -> {
                return RequestType.moveCard;
            }
            case showCard -> {
                return RequestType.showCard;
            }
            case addMember -> {
                return RequestType.addMember;
            }
            case listUsers -> {
                return RequestType.listUsers;
            }
            case showCards -> {
                return RequestType.showCards;
            }
            case sendChatMsg -> {
                return RequestType.sendChatMsg;
            }
            case showMembers -> {
                return RequestType.showMembers;
            }
            case listProjects -> {
                return RequestType.listProjects;
            }
            case cancelProjects -> {
                return RequestType.cancelProjects;
            }
            case getCardHistory -> {
                return RequestType.getCardHistory;
            }
            case listOnlineUsers -> {
                return RequestType.listOnlineUsers;
            }
        }
        return null;
    }

    public String getNameRequest(){
        switch(request){
            case readChat -> {
                return RequestType.readChat.name();
            }
            case login -> {
                return RequestType.login.name();
            }
            case createProject -> {
                return RequestType.createProject.name();
            }
            case logout -> {
                return RequestType.logout.name();
            }
            case addCard -> {
                return RequestType.addCard.name();
            }
            case moveCard -> {
                return RequestType.moveCard.name();
            }
            case showCard -> {
                return RequestType.showCard.name();
            }
            case addMember -> {
                return RequestType.addMember.name();
            }
            case listUsers -> {
                return RequestType.listUsers.name();
            }
            case showCards -> {
                return RequestType.showCards.name();
            }
            case sendChatMsg -> {
                return RequestType.sendChatMsg.name();
            }
            case showMembers -> {
                return RequestType.showMembers.name();
            }
            case listProjects -> {
                return RequestType.listProjects.name();
            }
            case cancelProjects -> {
                return RequestType.cancelProjects.name();
            }
            case getCardHistory -> {
                return RequestType.getCardHistory.name();
            }
            case listOnlineUsers -> {
                return RequestType.listOnlineUsers.name();
            }
        }
        return null;
    }

    /***
     * Restituisce il nome del progetto
     * @return String il nome del progetto
     */
    public String getProjectName(){
        return projectName;
    }

    /***
     * Restituisce il nome della card
     * @return String il nome della card
     */
    public String getCardName(){
        return cardName;
    }

    /***
     * Restituisce il nickname dell'utente
     * @return String il nickname dell'utente
     */
    public String getNickUtente(){
        return nickUtente;
    }

    /***
     * Restituisce la descrizione da associare ad una card
     * @return String la descrizione da associare ad una card
     */
    public String getDescription(){
        return description;
    }

    /***
     * Restituisce la lista di partenza per spostare una card
     * @return String la lista di partenza necessaria a spostare una card
     */
    public String getListaPartenza(){
        return listaPartenza;
    }

    /***
     * Restituisce la lista di destinazione per spostare una card
     * @return String la lista di destinazione necessaria a spostare una card
     */
    public String getListaDestinazione(){
        return listaDestinazione;
    }

    /***
     * Setta la variabile a true, quindi la richiesta e' stata eseguita con successo
     */
    public void setSuccess(){
        done = true;
        explanation = "success";
    }

    /***
     * Assegna al campo projectName la stringa passata per parametro
     * @param projectName Nome da assegnare a projectName
     */
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    /***
     * Assegna al campo cardName la stringa passata per parametro
     * @param card Card il cui nome e' da assegnare a cardName
     */
    public void setCardName(Card card){
        this.cardName = card.getNameCard();
    }

    /***
     * Assegna a ciascun campo di cards i nomi delle card richieste
     * @param cards List<Card> rappresentante le card di un determinato progetto, precedentemente restituite
     */
    public void setCardsName(List<Card> cards){
        cardsName = new LinkedList<>();
        for(Card card : cards){
            cardsName.add(card.getNameCard());
        }
    }

    /***
     * Assegna a ciascun campo di projects i nomi dei peoject richiesti
     * @param projs Lista dei progetti
     */
    public void setProjects(List<Project> projs){
        projects = new LinkedList<>();
        for(Project project : projs){
            projects.add(project.getNameProject());
        }
    }

    public void setRequest(RequestType request){
        this.request = request;
    }

    /***
     * Setta la variabile a false, quindi la richiesta non e' stata eseguita con successo
     * @param expl Stringa che identifica il motivo per cui la richiesta non e' terminata con successo
     */
    public void setFailure(String expl){
        done = false;
        explanation = expl;
    }

    /***
     * Setta i nomi dei membri del progetto
     * @param members Lista rappresentante i membri del progetto corrente
     */
    public void setMembers(List<User> members){
        this.members = new LinkedList<>();
        for(User user : members){
            this.members.add(user.getName());
        }
    }

    /***
     * Setta i nomi degli utenti del progetto
     * @param users Lista rappresentante gli utenti
     */
    public void setUsers(List<User> users){
        this.users = new LinkedList<>();
        for(User user : users){
            this.members.add(user.getName());
        }
    }

    /***
     * Setta i nomi degli utenti online del progetto
     * @param onlineUsers Lista rappresentante gli utenti online
     */
    public void setOnlineUsers(List<User> onlineUsers){
        this.onlineUsers = new LinkedList<>();
        for(User user : onlineUsers){
            this.onlineUsers.add(user.getName());
        }
    }

    /***
     * Setta l' history della card corrente
     * @param card Card corrente
     */
    public void setHistory(Card card){
        cardHistory = card.getHistory();
    }
}
