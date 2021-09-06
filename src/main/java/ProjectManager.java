import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectManager implements Callable<Tasks>  {
    /* Buffer contenente i dati letti */
    private final ByteBuffer buffer;
    /* Oggetto che rappresenta una richiesta del client */
    private Tasks task_request;
    /* Oggetto che rappresenta una risposta del client */
    private final Tasks task_response;
    /* Oggetto che rappresenta l'insieme di progetti minchia*/
    private final ConcurrentHashMap<String, Project> projects;
    /* Oggetto necessario per la serializzazione dei file JSON, formato usato sia per risposte e per richieste */
    ObjectMapper objectMapper;
    /* Composizione della classe ProjectWorker. La classe contiene metodi ausiliari per la classe corrente */
    private ProjectWorker worker;

    /***
     * Costruttore dell'oggetto
     * @param buffer contenente la richiesta
     */
    public ProjectManager(ByteBuffer buffer, ConcurrentHashMap<String, Project> projects) {
        this.buffer = buffer;
        this.projects = projects;
        objectMapper = new ObjectMapper();
        task_response = new Tasks();
        worker = new ProjectWorker();
    }

    /***
     * Effettua il parsing della richiesta
     * @throws IOException se ci sono errori nell' I/O
     */
    private void parser() throws IOException {
        /* Viene decodificata e letta la richiesta. Poi dalla deserializzazione viene creato un oggetto di tipo Tasks */
        String buffer_toString = StandardCharsets.UTF_8.decode(buffer).toString();
        task_request = objectMapper.readValue(buffer_toString, Tasks.class);
    }

    /***
     * Realizza la risposta base da inviare al client
     * @param outcome_b Booleano che indica se l'operazione e' avvenuta con successo
     * @param outcome_s Stringa da inviare al client che spiega a cosa e' dovuto un eventuale fallimento della richiesta.
     *                  Se l'operazione e' avvenuta con successo, sara' semplicemente "success"
     */
    private void response(boolean outcome_b, String outcome_s){
        task_response.setRequest(task_request.getRequest());
        System.out.println(outcome_b);
        System.out.println(outcome_s);
        if(outcome_b){
            task_response.setSuccess();
        } else {
            task_response.setFailure(outcome_s);
        }
    }

    /***
     * Override della chiamata call
     * @return Tasks cioe' la risposta in formato JSON
     */
    @Override
    public Tasks call() {
        try{
            parser();
            if(task_request.getNameRequest().equals("createProject")) {
                worker.createProject(projects, task_request.getProjectName());
            }
            else if(task_request.getNameRequest().equals("cancelProject")) {
                worker.cancelProject(projects, task_request.getProjectName());
                task_response.setProjectName(task_response.getProjectName());
            }
            else if(task_request.getNameRequest().equals("listUsers")) {
                Vector<User> users = worker.listUsers(projects);
                task_response.setUsers(users);
            }
            else if(task_request.getNameRequest().equals("listOnlineUsers")) {
                Vector<User> users = worker.listOnlineUsers(projects);
                task_response.setOnlineUsers(users);
            }
            else if(task_request.getNameRequest().equals("listProjects")) {
                worker.listProjects(projects); //non e' corretto

            }
            else if(task_request.getNameRequest().equals("addMember")){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())){
                    projects.get(task_request.getProjectName()).addMember(new User()); // new User() non va bene, devo sistemarlo in fase di registrazione
                }
            } else if(task_request.getNameRequest().equals("showMembers")){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())){
                    List<User> users = projects.get(task_request.getProjectName()).showMembers(); // new User() non va bene, devo sistemarlo in fase di registrazione
                    task_response.setMembers(users);
                }
            } else if(task_request.getNameRequest().equals("showCards")){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())) {
                    List<Card> cards = projects.get(task_request.getProjectName()).showCards();
                    task_response.setCardsName(cards); //TODO: CORREGGERE LA FUNZIONE
                }
            } else if(task_request.getNameRequest().equals("showCard") && task_request.getCardName() != null){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())) {
                    Card card = worker.showCard(projects, task_request.getProjectName(), task_request.getCardName());
                    task_response.setCardName(card);
                    task_response.setHistory(card);
                    task_response.setDescription(card);
                }
            } else if(task_request.getNameRequest().equals("addCard") && task_request.getCardName() != null){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())) {
                    projects.get(task_request.getProjectName()).addCardToDo(task_request.getProjectName(), task_request.getCardName());
                }
            } else if(task_request.getNameRequest().equals("moveCard") && task_request.getCardName() != null){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())) {
                    Card card = worker.showCard(projects, task_request.getProjectName(), task_request.getCardName());
                    Project project = projects.get(task_request.getProjectName());
                    project.moveCard(task_request.getListaPartenza(), task_request.getListaDestinazione(), card);
                }
            } else if(task_request.getNameRequest().equals("getCardHistory") && task_request.getCardName() != null){
                if(task_request.getNameRequest()!=null && projects.containsKey(task_request.getProjectName())) {
                    Card card = worker.showCard(projects, task_request.getProjectName(), task_request.getCardName());
                    Project project = projects.get(task_request.getProjectName());
                    project.getCardHistory(card);
                    task_response.setHistory(card);
                }
            }
            response(true, "success");
        } catch(Exception e){
            response(false, e.getLocalizedMessage());
        }
        return task_response;
    }
}
