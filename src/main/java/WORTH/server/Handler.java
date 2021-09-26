package WORTH.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WORTH.server.Handler e' la classe che si occupa di distinguere il messaggio
 * e di eseguire la computazione
 */
public class Handler implements Callable<Response>  {
    /* Buffer contenente i dati letti */
    private final ByteBuffer buffer;
    /* Oggetto che rappresenta una richiesta del WORTH.client */
    private Request task_request;
    /* Oggetto che rappresenta una risposta del WORTH.client */
    private final Response task_response;
    /* Oggetto che rappresenta l'insieme dei  progetti nel server */
    private final ConcurrentHashMap<String, Project> projects;
    /* Oggetto che rappresenza l'insieme degli utenti registrati al servizio */
    private HashMap<String, User> utenti_registrati;
    /* Oggetto necessario per la serializzazione dei file JSON, formato usato sia per risposte e per richieste */
    ObjectMapper objectMapper;
    /* Composizione della classe Worker. La classe contiene metodi ausiliari per la classe corrente */
    private final Worker worker;
    /* Composizione della classe UserManager. La classe contiene metodi ausiliari per la classe corrente */
    private final UserManager userManager;

    /**
     * Costruttore della classe
     * @param buffer ByteBuffer rappresentante la richiesta
     * @param projects Insieme totale dei progetti nel server
     * @param utenti_registrati Insieme totale degli utenti registrati al servizio
     */
    public Handler(ByteBuffer buffer, ConcurrentHashMap<String, Project> projects, HashMap<String, User> utenti_registrati) {
        this.buffer = buffer;
        this.projects = projects;
        this.userManager = new UserManager(utenti_registrati);
        objectMapper = new ObjectMapper();
        task_response = new Response();
        worker = new Worker(projects, utenti_registrati);
    }

    /**
     * Effettua il parsing della richiesta
     * @throws IOException se ci sono errori nell' I/O
     */
    private void parser() throws IOException {
        /* Viene decodificata e letta la richiesta. Poi dalla deserializzazione viene creato un oggetto di tipo WORTH.server.Message */
        String buffer_toString = StandardCharsets.UTF_8.decode(buffer).toString();
        task_request = objectMapper.readValue(buffer_toString, Request.class);
    }

    /**
     * Realizza la risposta base da inviare al WORTH.client
     * @param outcome_b Booleano che indica se l'operazione e' avvenuta con successo
     * @param outcome_s Stringa da inviare al WORTH.client che spiega a cosa e' dovuto un eventuale fallimento della richiesta.
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
     * @return WORTH.server.Message Risposta in formato JSON per il WORTH.client
     */
    @Override
    public Response call() {
        try{
            parser();
            switch(task_request.getRequest()) {
                case login : {
                    userManager.login(task_request.getNickUtente(), task_request.getPassword());
                    break;
                }
                case logout : {
                    userManager.logout(task_request.getNickUtente());
                    break;
                }
                /*case listUsers : {
                }
                case listOnlineUsers : {
                }
                case listProjects : {
                }

                case addMember : {
                    if(projects.containsKey(task_request.getProjectName())){
                        projects.get(task_request.getProjectName()).addMember(new WORTH.server.User()); // new WORTH.server.User() non va bene, devo sistemarlo in fase di registrazione
                        task_response.setMembers();
                    }
                }
                case readChat : {
                }
                case sendChatMsg : {
                }*/

                case createProject : {
                    worker.createProject(task_request.getProjectName(), task_request.getNickUtente());
                }
                /* Restituisce tutte le card appartenenti ad un progetto specificato nella richiesta */
                case showCards : {
                    Project project = worker.showCards(task_request.getProjectName(), task_request.getNickUtente());
                    task_response.setProject(project);
                    break;
                }

                /* Restituisce una card e i parametri associati(history, nome, description, lista corrente in cui si trova */
                case showCard : {
                    Card card = worker.showCard(task_request.getProjectName(), task_request.getCardName(), task_request.getNickUtente());
                    task_response.setCard(card);
                    break;
                }

                /* Aggiunge una card al progetto specificato nella richiesta */
                case addCard : {
                    worker.addCard(task_request.getProjectName(), task_request.getCardName(), task_request.getDescription(), task_request.getNickUtente());
                    break;
                }

                /* Muove una card da una lista ad un'altra (specificate nella richiesta) */
                case moveCard : {
                    worker.moveCard(task_request.getProjectName(), task_request.getCardName(), task_request.getListaPartenza(), task_request.getListaDestinazione(), task_request.getNickUtente());
                    break;
                }

                /* Restituisce l'history della card specificata nella richiesta */
                case getCardHistory : {
                    List<String> history = worker.getCardHistory(task_request.getProjectName(), task_request.getCardName(), task_request.getNickUtente());
                    task_response.setHistory(history);
                    break;
                }

                /* Cancella un progetto */
                case cancelProject : {
                    worker.cancelProject(task_request.getProjectName(), task_request.getNickUtente());
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
