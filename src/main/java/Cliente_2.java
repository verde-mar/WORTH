import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Cliente_2 {
    public static void main(String[] args) {
        int port = 8080;
        try { SocketAddress address = new InetSocketAddress(args[0], port);
            SocketChannel client  = SocketChannel.open(address);
            String request ="{\"request\": \"createProject\", \"projectName\": \"cancello\", \"cardName\": \"astadelcazzo\"}";
            byte[] byte_request = request.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            ByteBuffer buffer = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer.putInt(byte_request.length);
            buffer.put(byte_request);
            buffer.flip();
            while (buffer.hasRemaining())
                client.write(buffer);
            //client.read(buffer);
        } catch(IOException ex) { ex.printStackTrace(); }
    }
}
