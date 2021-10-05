package WORTH.client;

import WORTH.shared.worthProtocol.Request;
import WORTH.shared.worthProtocol.Response;

import java.io.IOException;


public class WORTHClient {
    private final TCPClient tcpClient;

    public WORTHClient(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public void login(String username, String password) throws Exception {
        Request request_login = new Request();
        request_login.setUserName(username);
        request_login.setPassword(password);
        request_login.setRequest(Request.RequestType.login);
        tcpClient.send(request_login);
        Response response = tcpClient.receive();
        if(!response.getDone()) throw new Exception(response.getExplanation());
    }

    public void logout(String username) throws IOException {
        Request requestLogOut = new Request();
        requestLogOut.setUserName(username);
        requestLogOut.setRequest(Request.RequestType.logout);
        tcpClient.send(requestLogOut);
        Response response = tcpClient.receive();
        if(!response.getDone()) throw new IOException(response.getExplanation());
    }

    public void close() throws IOException {
        tcpClient.close();
    }

    public Response createProject(String projectName, String username) throws IOException {
        Request createPrj = new Request();
        createPrj.setRequest(Request.RequestType.createProject);
        createPrj.setProjectName(projectName);
        createPrj.setUserName(username);
        tcpClient.send(createPrj);

        return tcpClient.receive();
    }

    public Response listProjects(String username) throws IOException {
        Request listPrjs = new Request();
        listPrjs.setRequest(Request.RequestType.listProjects);
        listPrjs.setUserName(username);
        tcpClient.send(listPrjs);

        return tcpClient.receive();
    }
}
