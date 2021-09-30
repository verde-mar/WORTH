package WORTH.server;

import WORTH.shared.Project;
import WORTH.shared.Request;
import WORTH.shared.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
        /* Oggetto che rappresenta l'insieme dei  progetti nel server */
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
                /* Effettua il login */
                case login : {
                    userManager.login(task_request.getNickUtente(), task_request.getPassword());
                    break;
                }
                /* Effettua il logout */
                case logout : {
                    userManager.logout(task_request.getNickUtente());
                    break;
                }
                /* Restituisce la lista degli utenti registrati al servizio */
                case listUsers : {
                    Collection<User> users = userManager.listUsers();
                    List<String> utenti = new LinkedList<>();
                    for(User user : users){
                        utenti.add(user.getName());
                    }
                    task_response.setMembers(utenti);
                    break;
                }

                /* Restituisce la lista degli utenti registrati al servizio e che sono online */
                case listOnlineUsers : {
                    List<User> onlineUsers = userManager.listOnlineUsers();
                    List<String> utentiOnline = new LinkedList<>();
                    for(User user : onlineUsers){
                        utentiOnline.add(user.getName());
                    }
                    task_response.setMembers(utentiOnline);
                    break;
                }

                /* Restituisce la lista dei progetti di un determinato utente */
                case listProjects : {
                    User user = userManager.getUtente(task_request.getNickUtente());
                    task_response.setProjects(user.getList_prj());
                    break;
                }

                /* Aggiunge un utente ad un progetto */
                case addMember : {
                    worker.addMember(task_request.getProjectName(), task_request.getNickToAdd(), task_request.getNickUtente());
                    break;
                }

                /* Crea il progetto */
                case createProject : {
                    worker.createProject(task_request.getProjectName(), task_request.getNickUtente());
                    break;
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
                /* Restituisce i membri di un progetto */
                case showMembers : {
                    List<String> members = worker.showMembers(task_request.getProjectName());
                    task_response.setMembers(members);
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
