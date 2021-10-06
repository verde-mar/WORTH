package WORTH.client;

import WORTH.shared.worthProtocol.Response;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClient extends JFrame {
    private static boolean interrupted = false;

    private static Response handleCommand(List<String> tokens, WORTHClient worthClient, RMIClient rmiClient) throws Exception {
        Response response = new Response();
        switch (tokens.get(0)) {
            case "login": {
                String nickUtente = tokens.get(1);
                String password = tokens.get(2);
                response = worthClient.login(nickUtente, password);
                break;
            }
            case "register": {
                String nickUtente = tokens.get(1);
                String password = tokens.get(2);
                rmiClient.register(nickUtente, password);
                response.setSuccess();
                break;
            }
            case "create_project": {
                String nickUtente = tokens.get(1);
                String projectName = tokens.get(2);
                response = worthClient.createProject(projectName, nickUtente);
                break;
            }
            case "add_card": {
                String nickUtente = tokens.get(1);
                String projectName = tokens.get(2);
                String cardName = tokens.get(3);
                String description = tokens.get(4);
                response = worthClient.addCard(nickUtente, projectName, cardName, description);
                break;
            }
            case "move_card": {
                String nickUtente = tokens.get(1);
                String projectName = tokens.get(2);
                String cardName = tokens.get(3);
                String fromList = tokens.get(4);
                String toList = tokens.get(5);
                // TODO
                break;
            }
            case "show_cards": {
                String nickUtente = tokens.get(1);
                String projectName = tokens.get(2);
                break;
            }
            case "show_card": {
                String nickUtente = tokens.get(1);
                String projectName = tokens.get(2);
                String cardName = tokens.get(3);
                response = worthClient.showCard(nickUtente, projectName, cardName);
                break;
            }
            case "cancel_project": {
                //String projectName = tokens[1];
                // TODO
                break;
            }
            case "add_member": {
                //String projectName = tokens[1];
                //String nickUtente = tokens[2];
                // TODO
                break;
            }
            case "show_members": {
                //String projectName = tokens[1];
                // TODO
                break;
            }
            case "get_card_history": {
                //String projectName = tokens[1];
                //String cardName = tokens[2];
                // TODO
                break;
            }
            case "list_users":
                // TODO
                break;
            case "list_online_users": {
                // TODO
                break;
            }
            case "list_projects": {
                String username = tokens.get(1);
                response = worthClient.listProjects(username);
                break;
            }
            case "logout":
                response = worthClient.logout(tokens.get(1));
                interrupted = true;
                break;
        }
        return response;
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        SocketAddress address = new InetSocketAddress("localhost", 8080);
        TCPClient tcpClient = new TCPClient(address);
        WORTHClient WORTHClient = new WORTHClient(tcpClient);
        RMIClient rmiClient = new RMIClient("localhost");

        Scanner scanner = new Scanner(System.in);
        while (!interrupted) {
            System.out.print("> ");
                String input = scanner.nextLine();
                List<String> list = new ArrayList<String>();
                Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
                while (m.find())
                    list.add(m.group(1));
            try {
                Response result = handleCommand(list, WORTHClient, rmiClient);
                if(result.getDone())
                    System.out.println(result.getExplanation());
            }
            catch (Exception e) { System.err.println(e.getMessage()); }
        }
    }
}
