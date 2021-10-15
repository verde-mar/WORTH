package WORTH.client;

import WORTH.shared.worthProtocol.Response;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//todo: non mi da' errore se metto argomenti piu' nella lettura da System.in, va bene? no, ma devo preocucparmene?
//todo: e' un problema se setto automaticamente la porta 8082 per le cose udp?
//todo: quando un utente fa read_chat vede anche i messaggi che ha mandato lui
public class MainClient {
    private static boolean login = false;
    private static String nickUtente;

    private static Response handleCommand(List<String> tokens, WORTHClient worthClient, RMIClient rmiClient) throws Exception {
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
                throw new Exception("You are already loggedin or another user is logged in");
        } else if ("register".equals(tokens.get(0))) {
            nickUtente = tokens.get(1);
            String password = tokens.get(2);
            rmiClient.register(nickUtente, password);
        } else if (tokens.get(0).equals("create_project") && login) {
            String projectName = tokens.get(1);
            response = worthClient.createProject(projectName, nickUtente);
            worthClient.setIPAddresses(response.getProjects());
        } else if (tokens.get(0).equals("add_card") && login) {
            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            String description = tokens.get(3);
            if (tokens.size() > 5) {
                throw new Exception("Description must be between \"\" ");
            }
            response = worthClient.addCard(nickUtente, projectName, cardName, description);
        } else if (tokens.get(0).equals("move_card") && login) {
            String projectName = tokens.get(1);
            String cardName = tokens.get(2);
            String fromList = tokens.get(3);
            String toList = tokens.get(4);
            response = worthClient.moveCard(nickUtente, projectName, cardName, fromList, toList);
            String message = nickUtente + "ha spostato " + cardName + "da " + fromList + " a " + toList + "," + projectName;
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
            System.out.println("You asked for help. Here's the syntax:\n");
            System.out.println("command param1 ...");
            System.out.println("register userName password");
            System.out.println("login userName password");
            System.out.println("logout");
            System.out.println("list_users");
            System.out.println("list_online_users");
            System.out.println("list_projects");
            System.out.println("create_project nameProject");
            System.out.println("add_card nameProject cardName cardDescription");
            System.out.println("get_card_history nameProject cardName");
            System.out.println("show_members nameProject");
            System.out.println("add_member nameProject userNameToAdd");
            System.out.println("show_card nameProject cardName");
            System.out.println("cancel_project nameProject");
            System.out.println("move_card nameProject cardName DepartureList ArrivalList");
        } else {
            throw new Exception("Are you sure this method exists? Or did the login fail?");
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        SocketAddress address = new InetSocketAddress("localhost", 8080);
        try(
                TCPClient tcpClient = new TCPClient(address);
                UDPClient udpClient = new UDPClient();
                WORTHClient WORTHClient = new WORTHClient(tcpClient, udpClient);
                Scanner scanner = new Scanner(System.in)
        ) {
            udpClient.start();
            RMIClient rmiClient = new RMIClient("localhost");
            Pattern p = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
            System.out.print("> ");
            while (!Thread.interrupted()) {
                String input = scanner.nextLine();
                List<String> list = new ArrayList<>();
                Matcher m = p.matcher(input);
                while (m.find())
                    list.add(m.group(1));
                try {
                    Response result = handleCommand(list, WORTHClient, rmiClient);
                    Worker.toString(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.print("> ");
            }
        }
        System.exit(0);
    }
}
