public class Card {
    private final String nameCard;
    private String description;
    private String history;


    public Card(String nameCard){
        this.nameCard = nameCard;
    }

    public void addHistory(String toConcat) {
        history = history.concat(toConcat);
    }

    public void addDescription(String descript) {
        this.description = descript;
    }
}
