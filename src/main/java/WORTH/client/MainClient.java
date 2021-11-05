package WORTH.client;

import WORTH.server.Project;
import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClient {
    /* Variabile che indica se e' stato fatto il login */
    private static boolean login = false;
    /* Nickname dell'utente */
    private static String nickUtente;

    /**
     * La funzione effettua il parsing della riga di comando, e in base a quanto scritto manda le richieste al server
     * @param tokens Lista dei token presi da riga di comando
     * @param worthClient Istanza della classe che si occupa di creare le richieste e di inviarle
     * @param rmiClient Istanza della classe che si occupa della comunicazione tramite RMI con il server
     * @return Response Restituisce la risposta del server
     * @throws Exception Nel caso in cui la richiesta non possa essere eseguita
     */
    static Response handleCommand(List<String> tokens, WORTHClient worthClient, RMIClient rmiClient) throws Exception {
        Response response = new Response();

        if (tokens.get(0).equals("login")) {

            if (!login) {
                nickUtente = tokens.get(1);
                String password = tokens.get(2);
                /* Se il numero di parametri eccede quello necessario */
                if(tokens.size()>3)
                    throw new Exception("Too many parameters");
                response = worthClient.login(nickUtente, password);
                if (response.getDone()) {
                    login = true;
                    try {
                        rmiClient.registerForCallback();
                        response.setSuccess();
                        response.setRequest(Request.RequestType.login);
                    } catch (Exception e){
                        response.setFailure(e.getMessage());
                    }

                    worthClient.setIPAddresses(response.getProjects());
                }
            } else
                throw new Exception("You are already logged in or another user is logged in");

        } else if ("register".equals(tokens.get(0))) {
            nickUtente = tokens.get(1);
            String password = tokens.get(2);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>3)
                throw new Exception("Too many parameters");
            try {
                rmiClient.register(nickUtente, password);
                response.setSuccess();
                response.setRequest(Request.RequestType.register);
            } catch (Exception e){
                response.setFailure(e.getMessage());
            }

        } else if (tokens.get(0).equals("createProject") && login) {
            String projectName = tokens.get(1);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>2)
                throw new Exception("Too many parameters");
            response = worthClient.createProject(projectName, nickUtente);
            if(response.getDone()) {
                List<Project> projects = new LinkedList<>();
                projects.add(response.getProject());
                worthClient.setIPAddresses(projects);
            }
        } else if (tokens.get(0).equals("addCard") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            String description = null;
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()==4)
                description = tokens.get(3);
            else if (tokens.size() >= 5) {
                throw new Exception("Description must be between \"\", or you used too many parameters");
            }
            response = worthClient.addCard(nickUtente, projectName, cardName, description);

        } else if (tokens.get(0).equals("moveCard") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            String fromList = tokens.get(3);
            String toList = tokens.get(4);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>5)
                throw new Exception("Too many parameters");
            response = worthClient.moveCard(nickUtente, projectName, cardName, fromList, toList);
            String message = nickUtente + " ha spostato " + cardName + " da " + fromList + " a " + toList + "\n" + projectName;
            if(response.getDone()) worthClient.sendMsg(projectName, message);

        } else if (tokens.get(0).equals("showCards") && login) {

            String projectName = tokens.get(1);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>2)
                throw new Exception("Too many parameters");
            response = worthClient.showCards(nickUtente, projectName);

        } else if (tokens.get(0).equals("showCard") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>3)
                throw new Exception("Too many parameters");
            response = worthClient.showCard(nickUtente, projectName, cardName);

        } else if (tokens.get(0).equals("cancelProject") && login) {

            String projectName = tokens.get(1);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>2)
                throw new Exception("Too many parameters");
            response = worthClient.cancelProject(projectName, nickUtente);

        } else if (tokens.get(0).equals("addMember") && login) {

            String projectName = tokens.get(1);
            String nickToAdd = tokens.get(2);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>3)
                throw new Exception("Too many parameters");
            response = worthClient.addMember(nickUtente, projectName, nickToAdd);

        } else if (tokens.get(0).equals("showMembers") && login) {

            String projectName = tokens.get(1);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>2)
                throw new Exception("Too many parameters");
            response = worthClient.showMembers(nickUtente, projectName);

        } else if (tokens.get(0).equals("getCardHistory") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>3)
                throw new Exception("Too many parameters");
            response = worthClient.getCardHistory(nickUtente, projectName, cardName);

        } else if ("listUsers".equals(tokens.get(0))) {

            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>1)
                throw new Exception("Too many parameters");
            try {
                rmiClient.listUsers();
                response.setSuccess();
                response.setRequest(Request.RequestType.listUsers);
            }catch(Exception e){
                response.setFailure(e.getMessage());
            }

        } else if ("listOnlineUsers".equals(tokens.get(0))) {

            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>1)
                throw new Exception("Too many parameters");
            try {
                rmiClient.listOnlineUsers();
                response.setSuccess();
                response.setRequest(Request.RequestType.listOnlineUsers);
            }catch(Exception e){
                response.setFailure(e.getMessage());
            }

        } else if (tokens.get(0).equals("listProjects") && login) {

            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>1)
                throw new Exception("Too many parameters");
            response = worthClient.listProjects(nickUtente);
            if(response.getDone())
                worthClient.setIPAddresses(response.getProjects());

        } else if (tokens.get(0).equals("logout") && login) {

            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>1)
                throw new Exception("Too many parameters");
            response = worthClient.logout(nickUtente);
            try {
                rmiClient.unregisterForCallback();
                response.setSuccess();
                response.setRequest(Request.RequestType.logout);
                Thread.currentThread().interrupt();
            } catch(Exception e){
                response.setFailure(e.getMessage());
            }

        } else if(tokens.get(0).equals("readChat") && login) {

            String projectName = tokens.get(1);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>2)
                throw new Exception("Too many parameters");
            try {
                worthClient.readChat(projectName);
            } catch(Exception e){
                response.setFailure(e.getMessage());
            }

        } else if(tokens.get(0).equals("sendChatMsg") && login) {

            String projectName = tokens.get(1);
            String msg = tokens.get(2);
            /* Se il numero di parametri eccede quello necessario */
            if(tokens.size()>3)
                throw new Exception("Too many parameters");
            String message = nickUtente + " ha scritto: " + msg + "\n" + projectName;
            try {
                worthClient.sendMsg(projectName, message);
                response.setSuccess();
                response.setRequest(Request.RequestType.sendChatMsg);
            } catch (Exception e){
                response.setFailure(e.getMessage());
            }
        } else if(tokens.get(0).equals("help")){

            System.out.println("You asked for help. Here's the syntax:\ncommand param1 ..." +
                    "\nregister userName password\nlogin userName password\nlogout\nlistUsers" +
                    "\nlistOnlineUsers\nlistProjects\ncreateProject nameProject\naddCard nameProject cardName cardDescription" +
                    "\naddCard nameProject cardName \"\"\n" + "getCardHistory nameProject cardName\nshowMembers nameProject" +
                    "\naddMember nameProject userNameToAdd\nshowCard nameProject cardName\ncancelProject nameProject" +
                    "\nmoveCard nameProject cardName DepartureList ArrivalList\nsendChatMsg projectName msg\nreadChat projectName");
            System.out.println("[NOTE]: IF YOU'LL USE MORE PARAMETERS THAN THE ONES INCLUDED ABOVE, AN EXCEPTION WILL BE THROWN");

        } else {
            System.err.println("Are you sure this method exists (try using 'help')? Or did the login fail? Try again");
            response = null;
        }
        return response;
    }
    public static void main(String[] args) throws Exception {
        SocketAddress address = new InetSocketAddress(args[0], 8080);
        try(
                TCPClient tcpClient = new TCPClient(address);
                UDPClient udpClient = new UDPClient();
                WORTHClient WORTHClient = new WORTHClient(tcpClient, udpClient);
                Scanner scanner = new Scanner(System.in)
        ) {
            /* Mette in esecuzione il thread in ascolto sulla connessione UDP */
            udpClient.start();
            /* Mette in esecuzione il client sulla comunicazione RMI */
            RMIClient rmiClient = new RMIClient(args[0]);

            Pattern p = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
            System.out.print("> ");
            while (!Thread.interrupted()) {
                /* Legge da riga di comando */
                String input = scanner.nextLine();
                List<String> list = new ArrayList<>();
                Matcher m = p.matcher(input);
                while (m.find())
                    list.add(m.group(1));

                /* Manda la richiesta, e si aspetta la risposta */
                try {
                    Response result = handleCommand(list, WORTHClient, rmiClient);
                    /* La risposta viene parsata, e viene stampato a video il risultato*/
                    if (result != null)
                        Worker.toString(result);
                } catch (Exception e) {
                    if (e.getMessage().contains("Index"))
                        System.err.println("You forgot some parameters");
                    else if (e.getMessage().contains("Indirizzo"))
                        System.err.println("L'indirizzo e' gia' in uso. Prova a creare un progetto di nome diverso, aggiungendogli un carattere a tua scelta");
                    else {
                        System.err.println("Something went wrong: " + e.getMessage());
                    }
                }
                System.out.print("> ");
            }
        }
        System.exit(0);
    }
}
