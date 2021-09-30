package WORTH.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class TCPClient {
    SocketChannel client;

    public TCPClient(SocketAddress address) throws IOException {
        client  = SocketChannel.open(address);
    }

    public Object getLocalAddress() throws IOException {
        return client.getLocalAddress();
    }

    public void close() throws IOException {
        client.close();
    }
}
