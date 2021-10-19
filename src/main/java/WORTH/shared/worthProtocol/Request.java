package WORTH.shared.worthProtocol;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


/**
 * Oggetto rappresentante una richiesta dal client al server
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Request {
    /* Tipo di richiesta possibili */
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
        register,
        listUsers,
        listOnlineUsers,
        sendChatMsg
    }

    /* Nome del progetto corrente */
    private String projectName;
    /* Nome della card corrente */
    private String cardName;
    /* Nickname dell'utente corrente */
    private String nickUtente;
    /* Nickname dell'utente da aggiungere */
    private String nickToAdd;
    /* Password corrente */
    private String password;
    /* Descrizione della card corrente */
    private String description;
    /* Richiesta corrente */
    private Request.RequestType request;
    /* Lista di partenza della card corrente */
    private String listaPartenza;
    /* Lista di destinazione della card corrente */
    private String listaDestinazione;

    /**
     * Restituisce la richiesta effettuata
     * @return String la richiesta effettuata
     */
    public Request.RequestType getRequest() {
        return request;
    }

    /**
     * Inizializza la richiesta da effettuare
     * @param type Tipo di richiesta
     */
    public void setRequest(RequestType type) {
        request = type;
    }

    /**
     * Restituisce il nick dell'utente da aggiungere al progetto
     * @return String nickname dell'utente da aggiungere al progetto
     */
    public String getNickToAdd() {
        return nickToAdd;
    }

    /**
     * Inizializza il campo relativo al nome dell'utente da aggiungere
     * @param nickToAdd Nickname dell'utente da aggiungere
     */
    public void setNickToAdd(String nickToAdd) {
        this.nickToAdd = nickToAdd;
    }

    /**
     * Restituisce il nome del progetto
     * @return String il nome del progetto
     */
    public String getProjectName(){
        return projectName;
    }

    /**
     * Inizializza il nome del progetto corrente
     * @param projectName Nome del progetto corrente
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Restituisce il nome della card corrente
     * @return String il nome della card corrente
     */
    public String getCardName(){
        return cardName;
    }

    /**
     * Inizializza il nome della card corrente
     * @param cardName Nome della card corrente
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Restituisce il nickname dell'utente corrente
     * @return String il nickname dell'utente corrente
     */
    public String getNickUtente(){
        return nickUtente;
    }

    /**
     * Inizializza il nickname dell'utente corrente
     * @param nickUtente Nickname dell'utente corrente
     */
    public void setNickUtente(String nickUtente) {
        this.nickUtente = nickUtente;
    }

    /**
     * Restituisce la descrizione da associare ad una card
     * @return String la descrizione da associare ad una card
     */
    public String getDescription(){
        return description;
    }

    /**
     * Inizializza la descrizione della card corrente
     * @param description Descrizione della card corrente
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Restituisce la lista di partenza per spostare una card
     * @return String la lista di partenza necessaria a spostare una card
     */
    public String getListaPartenza(){
        return listaPartenza;
    }

    /**
     * Inizializza la lista di partenza per spostare una card
     * @param listaPartenza Lista di partenza
     */
    public void setListaPartenza(String listaPartenza) {
        this.listaPartenza = listaPartenza;
    }

    /**
     * Restituisce la lista di destinazione per spostare una card
     * @return String la lista di destinazione necessaria a spostare una card
     */
    public String getListaDestinazione(){
        return listaDestinazione;
    }

    /**
     * Inizializza la lista di destinazione per spostare una card
     * @param listaDestinazione Lista di destinazione
     */
    public void setListaDestinazione(String listaDestinazione) {
        this.listaDestinazione = listaDestinazione;
    }

    /**
     * Restituisce la password dell'utente
     * @return String Password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Inizializza la password dell'utente
     * @param password Password dell'utente
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
