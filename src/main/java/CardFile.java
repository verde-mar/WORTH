import java.util.LinkedList;
import java.util.List;

public class CardFile {
    /* Nome univoco della card */
    private String nameCard;
    /* Descrizione testuale della card */
    private String description;
    /* Storia associata alla card */
    private List<String> history;
    /* Lista corrente a cui appartiene la card */
    private String currentList;

    public CardFile() {
        history = new LinkedList<>();
    }

    public void setCurrentList(String currentList) {
        this.currentList = currentList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNameCard(String nameCard){
        this.nameCard = nameCard;
    }

    public void setHistory(List<String> history){
        this.history = history;
    }

    public List<String> getHistory() {
        return history;
    }

    public String getNameCard() {
        return nameCard;
    }

    public String getCurrentList() {
        return currentList;
    }

    public String getDescription() {
        return description;
    }
}
