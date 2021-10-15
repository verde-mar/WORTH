package WORTH.shared.worthProtocol;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Request {
    public enum RequestType{
        login,
        logout,
        readChat,
        sendChatMsg,
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

    /* Parametri necessari per la richiesta */
    private String projectName;
    private String cardName;
    private String nickUtente;
    private String nickToAdd;
    private String password;
    private String description;
    private Request.RequestType request;
    private String listaPartenza;
    private String listaDestinazione;
    private String message;
    private List<String> readMessages;
    /**
     * Restituisce la richiesta effettuata
     * @return String la richiesta effettuata
     */
    public Request.RequestType getRequest() {
        return request;
    }
    public void setRequest(RequestType login) {
        request = login;
    }

    /**
     * Restituisce il nick dell'utente da aggiungere al progetto
     * @return String nickname dell'utente da aggiungere al progetto
     */
    public String getNickToAdd() {
        return nickToAdd;
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

    /**
     * Restituisce la password dell'utente che ha richiesto di effettuare il login
     * @return String Password dell'utente
     */
    public String getPassword() {
        return password;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getReadMessages() {
        return readMessages;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setListaDestinazione(String listaDestinazione) {
        this.listaDestinazione = listaDestinazione;
    }

    public void setListaPartenza(String listaPartenza) {
        this.listaPartenza = listaPartenza;
    }

    public void setNickToAdd(String nickToAdd) {
        this.nickToAdd = nickToAdd;
    }

    public void setNickUtente(String nickUtente) {
        this.nickUtente = nickUtente;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
