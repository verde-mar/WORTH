package WORTH.client;

import WORTH.server.Card;
import WORTH.server.Project;
import WORTH.shared.worthProtocol.Response;

/**
 * La classe stampa la risposta inviata dal Server in maniera intuitiva
 */
public class Worker {
    /**
     * La funzione stampa a video i parametri della risposta necessari all'utente per capire se questa e' avvenuta con successo o no;
     * e, nel caso positivo, i dati che aveva richiesto.
     * @param response Risposta dal server
     */
    public static void toString(Response response) {
        /* Se il server e' riuscito ad eseguire la richiesta */
        if(response.getDone()){
            System.out.println("< success: " + response.getRequest().name());
            /* In base alla richiesta, restituisce il parametro richiesto */
            switch(response.getRequest()){
                case listProjects: {
                    for(Project project : response.getProjects())
                        System.out.println("< " + project.getNameProject());
                    break;
                }
                case showCards: {
                    for(Card card : response.getProject().getTo_Do()) {
                        System.out.println("< toDo: \n" + "cardName: " + card.getNameCard() + "\ncardDescription: " + card.getDescription() + "\ncardCurrentList: " + card.getCurrentList());
                    }
                    for(Card card : response.getProject().getInProgress()) {
                        System.out.println("< inProgress: \n" + card.getNameCard() + "\ncardDescription: " + card.getDescription() + "\ncardCurrentList: " + card.getCurrentList());
                    }
                    for(Card card : response.getProject().getToBeRevised()) {
                        System.out.println("< toBeRevised: \n" + card.getNameCard() +"\ncardDescription: " +  card.getDescription() + "\ncardCurrentList: " + card.getCurrentList());
                    }
                    for(Card card : response.getProject().getDone()) {
                        System.out.println("< done: \n" + card.getNameCard() + "\ncardDescription: " + card.getDescription() + "\ncardCurrentList: " + card.getCurrentList());
                    }
                    break;
                }
                case getCardHistory: {
                    System.out.println("< " + response.getHistory());
                    break;
                }
                case showMembers: {
                    System.out.println("< " + response.getMembers());
                    break;
                }
                case showCard: {
                    System.out.println("<\ncardName: " + response.getCard().getNameCard() + "\ncardDescription: " + response.getCard().getDescription() + "\ncardCurrentList: " + response.getCard().getCurrentList());

                    break;
                }
            }
        }
        /* Se c'e' stato qualche fallimento */
        else if(!response.getDone() && response.getExplanation()!=null) {
            /* Stampa a video la causa del fallimento */
            System.err.println("< " + response.getExplanation());
        }
    }
}
