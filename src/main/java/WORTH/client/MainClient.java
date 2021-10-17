package WORTH.client;

import WORTH.server.Project;
import WORTH.shared.worthProtocol.Response;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//todo: non mi da' errore se metto argomenti piu' nella lettura da System.in, va bene? no, ma devo preocucparmene?
//todo: setto automaticamente la porta 8082 per le cose udp
//todo: quando studi bene RMI modifica pure i commenti
//todo: manca la risposta ok per operazioni di rmi e udp
public class MainClient {
    /* Variabile che indica se e' stato fatto il login */
    private static boolean login = false;
    /* Nickname dell'utente */
    private static String nickUtente;

    /**
     * La funzione effettua il parsing della riga di comando, e in base a quanto scritto manda le richieste al server
     * @param tokens Lista dei token presi da riga di comando
     * @param worthClient Istanza della classe che si occupa di creare le richieste e di inviarle todo: e' corretto dire comunicazione RMI?
     * @param rmiClient Istanza della classe che si occupa della comunicazione RMI con il server
     * @return Response Restituisce la risposta del server
     * @throws Exception Nel caso in cui la richiesta non possa essere eseguita
     */
    static Response handleCommand(List<String> tokens, WORTHClient worthClient, RMIClient rmiClient) throws Exception {
        Response response = new Response();

        if (tokens.get(0).equals("login")) {

            if (!login) {
                nickUtente = tokens.get(1);
                String password = tokens.get(2);

                response = worthClient.login(nickUtente, password);
                if (response.getDone()) {
                    login = true;
                    rmiClient.registerForCallback();
                    worthClient.setIPAddresses(response.getProjects());
                }
            } else
                throw new Exception("You are already logged in or another user is logged in");

        } else if ("register".equals(tokens.get(0))) {
            nickUtente = tokens.get(1);
            String password = tokens.get(2);
            rmiClient.register(nickUtente, password);

        } else if (tokens.get(0).equals("create_project") && login) {
            String projectName = tokens.get(1);
            response = worthClient.createProject(projectName, nickUtente);
            if(response.getDone()) {
                List<Project> projects = new LinkedList<>();
                projects.add(response.getProject());
                worthClient.setIPAddresses(projects);
            }
        } else if (tokens.get(0).equals("add_card") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            String description = null;
            if(tokens.size()==4)
                description = tokens.get(3);
            else if (tokens.size() >= 5) {
                throw new Exception("Description must be between \"\" ");
            }
            response = worthClient.addCard(nickUtente, projectName, cardName, description);

        } else if (tokens.get(0).equals("move_card") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            String fromList = tokens.get(3);
            String toList = tokens.get(4);

            response = worthClient.moveCard(nickUtente, projectName, cardName, fromList, toList);
            String message = nickUtente + " ha spostato " + cardName + " da " + fromList + " a " + toList + "," + projectName;
            if(response.getDone()) worthClient.sendMsg(projectName, message);

        } else if (tokens.get(0).equals("show_cards") && login) {

            String projectName = tokens.get(1);
            response = worthClient.showCards(nickUtente, projectName);

        } else if (tokens.get(0).equals("show_card") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            response = worthClient.showCard(nickUtente, projectName, cardName);

        } else if (tokens.get(0).equals("cancel_project") && login) {

            String projectName = tokens.get(1);
            response = worthClient.cancelProject(projectName, nickUtente);

        } else if (tokens.get(0).equals("add_member") && login) {

            String projectName = tokens.get(1);
            String nickToAdd = tokens.get(2);
            response = worthClient.addMember(nickUtente, projectName, nickToAdd);

        } else if (tokens.get(0).equals("show_members") && login) {

            String projectName = tokens.get(1);
            response = worthClient.showMembers(nickUtente, projectName);

        } else if (tokens.get(0).equals("get_card_history") && login) {

            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            response = worthClient.getCardHistory(nickUtente, projectName, cardName);

        } else if ("list_users".equals(tokens.get(0))) {

            rmiClient.listUsers();

        } else if ("list_online_users".equals(tokens.get(0))) {

            rmiClient.listOnlineUsers();

        } else if (tokens.get(0).equals("list_projects") && login) {

            response = worthClient.listProjects(nickUtente);
            worthClient.setIPAddresses(response.getProjects());

        } else if (tokens.get(0).equals("logout") && login) {

            response = worthClient.logout(nickUtente);
            rmiClient.unregisterForCallback();
            Thread.currentThread().interrupt();

        } else if(tokens.get(0).equals("read_chat") && login) {

            String projectName = tokens.get(1);
            worthClient.readChat(projectName);

        } else if(tokens.get(0).equals("send_chat_msg") && login) {

            String projectName = tokens.get(1);
            String msg = tokens.get(2);
            String message = nickUtente + " ha scritto: " + msg + "," + projectName;
            worthClient.sendMsg(projectName, message);

        } else if(tokens.get(0).equals("help")){

            System.out.println("You asked for help. Here's the syntax:\ncommand param1 ..." +
                    "\nregister userName password\nlogin userName password\nlogout\nlist_users\n" +
                    "list_online_users\nlist_projects\ncreate_project nameProject\nadd_card nameProject cardName cardDescription\n" +
                    "get_card_history nameProject cardName\nshow_members nameProject\nadd_member nameProject userNameToAdd\n" +
                    "show_card nameProject cardName\ncancel_project nameProject\nmove_card nameProject cardName DepartureList ArrivalList");

        } else {
            System.err.println("Are you sure this method exists? Or did the login fail? Try again");
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

                } catch (Exception e){
                    if(e.getMessage().contains("Index"))
                        System.err.println("You forgot some parameters");
                    else
                        System.err.println("Something went wrong: " + e.getMessage());
                }
                System.out.print("> ");
            }
        }
        System.exit(0);
    }
}
