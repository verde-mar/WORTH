package WORTH.client;

import WORTH.server.Card;
import WORTH.server.Project;
import WORTH.shared.worthProtocol.Response;


public class Worker {
    public static void toString(Response response) {
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
                        System.out.println("< inProgress: " + card.getNameCard() + card.getDescription() + card.getCurrentList());
                    }
                    for(Card card : response.getProject().getToBeRevised()) {
                        System.out.println("< toBeRevised: " + card.getNameCard() + card.getDescription() + card.getCurrentList());
                    }
                    for(Card card : response.getProject().getDone()) {
                        System.out.println("< done: " + card.getNameCard() + card.getDescription() + card.getCurrentList());
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
        } else if(!response.getDone() && response.getExplanation()!=null) {
            System.err.println("< " + response.getExplanation());
        } else {
            System.out.println("< success: register");
        }
    }
}
