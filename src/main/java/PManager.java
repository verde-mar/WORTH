import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class PManager implements Callable<Message>  {
    /* Buffer contenente i dati letti */
    private final ByteBuffer buffer;
    /* Oggetto che rappresenta una richiesta del client */
    private Message task_request;
    /* Oggetto che rappresenta una risposta del client */
    private final Message task_response;
    /* Oggetto che rappresenta l'insieme di progetti minchia*/
    private final ConcurrentHashMap<String, Project> projects;
    /* Oggetto necessario per la serializzazione dei file JSON, formato usato sia per risposte e per richieste */
    ObjectMapper objectMapper;
    /* Composizione della classe ProjectWorker. La classe contiene metodi ausiliari per la classe corrente */
    private final PWorker worker;

    /**
     * Costruttore dell'oggetto
     * @param buffer contenente la richiesta
     */
    public PManager(ByteBuffer buffer, ConcurrentHashMap<String, Project> projects) {
        this.buffer = buffer;
        this.projects = projects;
        objectMapper = new ObjectMapper();
        task_response = new Message();
        worker = new PWorker(projects);
    }

    /**
     * Effettua il parsing della richiesta
     * @throws IOException se ci sono errori nell' I/O
     */
    private void parser() throws IOException {
        /* Viene decodificata e letta la richiesta. Poi dalla deserializzazione viene creato un oggetto di tipo Message */
        String buffer_toString = StandardCharsets.UTF_8.decode(buffer).toString();
        task_request = objectMapper.readValue(buffer_toString, Message.class);
    }

    /**
     * Realizza la risposta base da inviare al client
     * @param outcome_b Booleano che indica se l'operazione e' avvenuta con successo
     * @param outcome_s Stringa da inviare al client che spiega a cosa e' dovuto un eventuale fallimento della richiesta.
     *                  Se l'operazione e' avvenuta con successo, sara' semplicemente "success"
     */
    private void response(boolean outcome_b, String outcome_s){
        task_response.setRequest(task_request.getRequest());
        if(outcome_b){
            task_response.setSuccess();
        } else {
            task_response.setFailure(outcome_s);
        }
    }

    /**
     * Override della chiamata call
     * @return Message Risposta in formato JSON
     */
    @Override
    public Message call() {
        try{
            parser();
            switch(task_request.getRequest()) {
                /*case login : {
                }
                case logout : {
                }
                case listUsers : {
                }
                case listOnlineUsers : {
                }
                case listProjects : {
                }

                case addMember : {
                    if(projects.containsKey(task_request.getProjectName())){
                        projects.get(task_request.getProjectName()).addMember(new User()); // new User() non va bene, devo sistemarlo in fase di registrazione
                        task_response.setMembers();
                    }
                }
                case readChat : {
                }
                case sendChatMsg : {
                }*/
                case createProject : projects.putIfAbsent(task_request.getProjectName(), new Project(task_request.getProjectName())); break;

                /* Restituisce tutte le card appartenenti ad un progetto specificato nella richiesta */
                //todo: stesso ragionamento per il progetto, se restituisce null magari e' stato inserito il nome del progetto sbagliato
                case showCards : {
                    List<Card> cards = projects.get(task_request.getProjectName()).showCards();
                    task_response.setCardsName(cards);
                    task_response.setProjectName(task_request.getProjectName());
                    break;
                }

                /* Restituisce una card e i parametri associati(history, nome, desscription, lista corrente in cui si trova */
                //todo: se restituisce null magari l'utente ha inserito il progetto sbagliato! Da inserire nell'interfaccia grafica del client
                //todo: stesso ragionamento per il progetto, se restituisce null magari e' stato inserito il nome del progetto sbagliato
                case showCard : {
                    Card card = worker.showCard(task_request.getProjectName(), task_request.getCardName());
                    task_response.setProjectName(task_request.getProjectName());
                    task_response.setCardName(card.getNameCard());
                    task_response.setHistory(card.getHistory());
                    task_response.setDescription(card.getDescription());
                    task_response.setCardCurrentList(card.getCurrentList());
                    break;
                }

                /* Aggiunge una card al progetto specificato nella richiesta */
                //todo: stesso ragionamento per il progetto, se restituisce null magari e' stato inserito il nome del progetto sbagliato
                case addCard : {
                    worker.addCard(task_request.getProjectName(), task_request.getCardName(), task_request.getDescription());
                    task_response.setProjectName(task_request.getProjectName());
                    task_response.setCardName(task_request.getCardName());
                    break;
                }

                /* Muove una card da una lista ad un'altra (specificate nella richiesta) */
                //todo: se restituisce null magari l'utente ha inserito il progetto sbagliato! Da inserire nell'interfaccia grafica del client
                //todo: stesso ragionamento per il progetto, se restituisce null magari e' stato inserito il nome del progetto sbagliato
                case moveCard : {
                    worker.moveCard(task_response.getProjectName(), task_request.getCardName(), task_request.getListaPartenza(), task_request.getListaDestinazione());
                    task_response.setProjectName(task_request.getProjectName());
                    task_response.setCardName(task_request.getCardName());
                    break;
                }

                /* Restituisce l'history della card specificata nella richiesta */
                //todo: se restituisce null magari l'utente ha inserito il progetto sbagliato! Da inserire nell'interfaccia grafica del client
                //todo: stesso ragionamento per il progetto, se restituisce null magari e' stato inserito il nome del progetto sbagliato
                case getCardHistory : {
                    List<String> history = worker.getCardHistory(task_request.getProjectName(), task_request.getCardName());
                    task_response.setHistory(history);
                    task_response.setCardName(task_request.getCardName());
                    break;
                }
                //todo: stesso ragionamento per il progetto, se restituisce null magari e' stato inserito il nome del progetto sbagliato

                /* Cancella un progetto */
                case cancelProject : {
                    worker.cancelProject(task_request.getProjectName());
                    task_response.setProjectName(task_request.getProjectName());
                    break;
                }
            }

            /* Definizione della risposta base: indica che l'operazione e' terminata con successo */
            response(true, "success");
        } catch(Exception e){
            /* Definizione della risposta base: indica che l'operazione e' fallita */
            response(false, e.getLocalizedMessage());
        }
        return task_response;
    }
}
