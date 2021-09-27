package WORTH.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Request {
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
        cancelProject
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

    /**
     * Restituisce la richiesta effettuata
     * @return String la richiesta effettuata
     */
    public Request.RequestType getRequest() {
        return request;
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
}
