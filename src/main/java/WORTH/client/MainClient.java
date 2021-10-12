package WORTH.client;

import WORTH.shared.worthProtocol.Response;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//todo: c'e' da testare tutte le operazioni con molti client e in sezione critica
//todo: c'e' da non permettere che un utente faccia delle operazioni se non e' loggato
public class MainClient extends JFrame {
    private static boolean login = false;

    private static Response handleCommand(List<String> tokens, WORTHClient worthClient, RMIClient rmiClient) throws Exception {
        Response response = new Response();

        if (tokens.get(0).equals("login")) {
            if(!login) {
                String nickUtente = tokens.get(1);
                String password = tokens.get(2);
                response = worthClient.login(nickUtente, password);
                if(response.getDone()) {
                    login = true;
                    rmiClient.registerForCallback();
                }
            } else
                throw new Exception("You are already loggedin");
        } else if ("register".equals(tokens.get(0))) {
            String nickUtente = tokens.get(1);
            String password = tokens.get(2);
            rmiClient.register(nickUtente, password);
        } else if (tokens.get(0).equals("create_project") && login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            response = worthClient.createProject(projectName, nickUtente);
        } else if (tokens.get(0).equals("add_card") && login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            String cardName = tokens.get(3);
            String description = tokens.get(4);
            if(tokens.size()>5){
                throw new Exception("Description must be between \"\" ");
            }
            response = worthClient.addCard(nickUtente, projectName, cardName, description);
        } else if (tokens.get(0).equals("move_card")&& login) { //todo: devi aggiornare la chat
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            String cardName = tokens.get(3);
            String fromList = tokens.get(4);
            String toList = tokens.get(5);
            response = worthClient.moveCard(nickUtente, projectName, cardName, fromList, toList);
        } else if (tokens.get(0).equals("show_cards")&& login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            response = worthClient.showCards(nickUtente, projectName);
        } else if (tokens.get(0).equals("show_card")&& login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            String cardName = tokens.get(3);
            response = worthClient.showCard(nickUtente, projectName, cardName);
        } else if (tokens.get(0).equals("cancel_project")&& login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            response = worthClient.cancelProject(projectName, nickUtente);
        } else if (tokens.get(0).equals("add_member")&& login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            String nickToAdd = tokens.get(3);
            response = worthClient.addMember(nickUtente, projectName, nickToAdd);
        } else if (tokens.get(0).equals("show_members")&& login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            response = worthClient.showMembers(nickUtente, projectName);
        } else if (tokens.get(0).equals("get_card_history")&& login) {
            String nickUtente = tokens.get(1);
            String projectName = tokens.get(2);
            String cardName = tokens.get(3);
            response = worthClient.getCardHistory(nickUtente, projectName, cardName);
        } else if ("list_users".equals(tokens.get(0))) {
          rmiClient.listUsers();
        } else if ("list_online_users".equals(tokens.get(0))) {
            rmiClient.listOnlineUsers();
        } else if (tokens.get(0).equals("list_projects")&& login) {
            String username = tokens.get(1);
            response = worthClient.listProjects(username);
        } else if (tokens.get(0).equals("logout") && login) {
            response = worthClient.logout(tokens.get(1));
            rmiClient.unregisterForCallback();
            Thread.currentThread().interrupt();
        } else if(tokens.get(0).equals("help")){
            System.out.println("You asked for help. Here's the syntax:\n");
            System.out.println("command param1 ...");
            System.out.println("register userName password");
            System.out.println("login userName password");
            System.out.println("logout userName");
            System.out.println("list_users");
            System.out.println("list_online_users");
            System.out.println("list_projects userName");
            System.out.println("create_project userName nameProject");
            System.out.println("add_card userName nameProject cardName cardDescription");
            System.out.println("get_card_history nickName nameProject cardName");
            System.out.println("show_members userName nameProject");
            System.out.println("add_member userName nameProject userNameToAdd");
            System.out.println("show_card userName nameProject cardName");
            System.out.println("cancel_project userName nameProject");
            System.out.println("move_card userName nameProject cardName DepartureList ArrivalList");
        } else {
            throw new Exception("You have'nt loggedin or this method doesn't exist");
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        SocketAddress address = new InetSocketAddress("localhost", 8080);
        try(
                TCPClient tcpClient = new TCPClient(address);
                WORTHClient WORTHClient = new WORTHClient(tcpClient);
                Scanner scanner = new Scanner(System.in)
        ) {
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
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                System.out.print("> ");
            }
        }
        System.exit(0);
    }
}
