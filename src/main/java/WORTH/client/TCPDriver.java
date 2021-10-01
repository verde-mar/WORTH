package WORTH.client;

import WORTH.shared.Request;

import java.io.IOException;


public class TCPDriver {
    private final TCPClient tcpClient;

    public TCPDriver(TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public void loginRequest(String username, String password) throws IOException {
        Request request_login = new Request();
        request_login.setUserName(username);
        request_login.setPassword(password);
        tcpClient.send(request_login);
    }

    public void logoutRequest(String username) throws IOException {
        Request requestLogOut = new Request();
        requestLogOut.setUserName(username);
        tcpClient.send(requestLogOut);
    }

    public void close() throws IOException {
        tcpClient.close();
    }
}
