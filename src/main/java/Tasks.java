import com.fasterxml.jackson.annotation.JsonAutoDetect;

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

    private String projectName;
    private String cardName;
    private String nickUtente;
    private String password;
    private String description;
    private String text;
    private RequestType request;
    private String listaPartenza;
    private String listaDestinazione;


    public String getRequest(){
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

    public String getProjectName(){
        return projectName;
    }

    public String getCardName(){
        return cardName;
    }

    public String getNickUtente(){
        return nickUtente;
    }

    public String getPassword(){
        return password;
    }

    public String getDescription(){
        return description;
    }

    public String getText(){
        return text;
    }

    public String getListaPartenza(){
        return listaPartenza;
    }

    public String getListaDestinazione(){
        return listaDestinazione;
    }
}
