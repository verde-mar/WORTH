package WORTH.client;

import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;

import java.io.IOException;


public class WORTHClient {
    private final TCPClient tcpClient;

    public WORTHClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public Response login(String username, String password) throws Exception {
        Request request_login = new Request();
        request_login.setNickUtente(username);
        request_login.setPassword(password);
        request_login.setRequest(Request.RequestType.login);
        tcpClient.send(request_login);
        return tcpClient.receive();
    }

    public Response logout(String username) throws IOException {
        Request requestLogOut = new Request();
        requestLogOut.setNickUtente(username);
        requestLogOut.setRequest(Request.RequestType.logout);
        tcpClient.send(requestLogOut);
        Response response = tcpClient.receive();
        if(!response.getDone()) throw new IOException(response.getExplanation());
        return response;
    }

    public void close() throws IOException {
        tcpClient.close();
    }

    public Response createProject(String projectName, String username) throws IOException {
        Request createPrj = new Request();
        createPrj.setRequest(Request.RequestType.createProject);
        createPrj.setProjectName(projectName);
        createPrj.setNickUtente(username);
        tcpClient.send(createPrj);

        return tcpClient.receive();
    }

    public Response listProjects(String username) throws IOException {
        Request listPrjs = new Request();
        listPrjs.setRequest(Request.RequestType.listProjects);
        listPrjs.setNickUtente(username);
        tcpClient.send(listPrjs);

        return tcpClient.receive();
    }

    public Response addCard(String nickUtente, String projectName, String cardName, String description) throws IOException {
        Request addCard = new Request();
        addCard.setNickUtente(nickUtente);
        addCard.setProjectName(projectName);
        addCard.setRequest(Request.RequestType.addCard);
        addCard.setCardName(cardName);
        addCard.setDescription(description);

        tcpClient.send(addCard);

        return tcpClient.receive();
    }

    public Response showCard(String nickUtente, String projectName, String cardName) throws IOException {
        Request showCard = new Request();
        showCard.setNickUtente(nickUtente);
        showCard.setProjectName(projectName);
        showCard.setRequest(Request.RequestType.showCard);
        showCard.setCardName(cardName);

        tcpClient.send(showCard);

        return tcpClient.receive();
    }
}
