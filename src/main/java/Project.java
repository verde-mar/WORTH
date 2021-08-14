import java.util.*;

public class Project {
    /* Nome del progetto, univoco */
    private final String nameProject;
    /* Lista delle card che devono ancora essere prese in carico da un membro del progetto */
    private List<Card> toDo;
    /* Lista delle card che sono state prese in carico da un membro del progetto */
    private List<Card> inProgress;
    /* Lista delle card le cui operazioni sono da revisionare da un membro del progetto */
    private List<Card> toBeRevised;
    /* Lista delle card le cui operazioni associate sono portate a termine da un membro del progetto */
    private List<Card> done;
    /* Lista di membri del progetto */
    private List<User> members_sync;

    public Project(String nameProject){
        this.nameProject = nameProject;
        toDo = Collections.synchronizedList(new LinkedList<>());
        inProgress = Collections.synchronizedList(new LinkedList<>());
        toBeRevised = Collections.synchronizedList(new LinkedList<>());
        done = Collections.synchronizedList(new LinkedList<>());
        members_sync = Collections.synchronizedList(new LinkedList<>());
    }

    private List<User> showMembers(){
        return members_sync;
    }

    private List<Card> getDone(){
        return done;
    }

    private List<Card> getToBeRevised(){
        return toBeRevised;
    }

    private List<Card> getToDo(){
        return toDo;
    }

    private List<Card> getInProgress(){
        return inProgress;
    }

    public void addCard(String listaToAdd, Card card){
        if(listaToAdd.equals("toDo")){
            card.addHistory("toDo");
            toDo.add(card);
        } else if(listaToAdd.equals("toBeRevised")){
            card.addHistory("toBeRevised");
            toBeRevised.add(card);
        } else if(listaToAdd.equals("inProgress")){
            card.addHistory("inProgress");
            inProgress.add(card);
        } else {
            card.addHistory("done");
            done.add(card);
        }
    }


}

