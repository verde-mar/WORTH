package WORTH.server;

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
            String request ="{\"request\": \"createProject\", \"projectName\": \"cancello\"}";
            byte[] byte_request = request.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            ByteBuffer buffer = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer.putInt(byte_request.length);
            buffer.put(byte_request);
            buffer.flip();
            while (buffer.hasRemaining())
                client.write(buffer);
            // Buffer contenente la lunghezza
            ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.BYTES);


            while(lengthBuffer.hasRemaining())
                client.read(lengthBuffer);
            System.out.println("dopo read size");
            lengthBuffer.flip();
            System.out.println(lengthBuffer);

            // Buffer contenente i dati
            ByteBuffer buffer_response = ByteBuffer.allocate(lengthBuffer.getInt());
            while(buffer_response.hasRemaining())
                client.read(buffer_response);
            buffer_response.flip();
            System.out.println(buffer_response);
            // Stringa estratta dal buffer
            String message = new String(buffer_response.array(), StandardCharsets.UTF_8);
            System.out.printf("[TCP] Received string:\n%s\n", message);

            String request1 ="{\"request\": \"addCard\", \"cardName\": \"CAGNOTTO\", \"projectName\": \"cancello\"}";
            byte_request = request1.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            ByteBuffer buffer1 = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer1.putInt(byte_request.length);
            buffer1.put(byte_request);
            buffer1.flip();
            while (buffer1.hasRemaining())
                client.write(buffer1);
            // Buffer contenente la lunghezza
            lengthBuffer = ByteBuffer.allocate(Integer.BYTES);

            while(lengthBuffer.hasRemaining())
                client.read(lengthBuffer);
            System.out.println("dopo read size");
            lengthBuffer.flip();
            System.out.println(lengthBuffer);

            // Buffer contenente i dati
            buffer_response = ByteBuffer.allocate(lengthBuffer.getInt());
            while(buffer_response.hasRemaining())
                client.read(buffer_response);
            buffer_response.flip();
            System.out.println(buffer_response);
            // Stringa estratta dal buffer
            message = new String(buffer_response.array(), StandardCharsets.UTF_8);
            System.out.printf("[TCP] Received string:\n%s\n", message);

            request1 ="{\"request\": \"addCard\", \"cardName\": \"BASSOTTO\", \"projectName\": \"cancello\"}";
            byte_request = request1.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            buffer1 = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer1.putInt(byte_request.length);
            buffer1.put(byte_request);
            buffer1.flip();
            while (buffer1.hasRemaining())
                client.write(buffer1);
            // Buffer contenente la lunghezza
            lengthBuffer = ByteBuffer.allocate(Integer.BYTES);

            while(lengthBuffer.hasRemaining())
                client.read(lengthBuffer);
            System.out.println("dopo read size");
            lengthBuffer.flip();
            System.out.println(lengthBuffer);

            // Buffer contenente i dati
            buffer_response = ByteBuffer.allocate(lengthBuffer.getInt());
            while(buffer_response.hasRemaining())
                client.read(buffer_response);
            buffer_response.flip();
            System.out.println(buffer_response);
            // Stringa estratta dal buffer
            message = new String(buffer_response.array(), StandardCharsets.UTF_8);
            System.out.printf("[TCP] Received string:\n%s\n", message);

            request1 ="{\"request\": \"showCards\", \"projectName\": \"cancello\"}";
            byte_request = request1.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            buffer1 = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer1.putInt(byte_request.length);
            buffer1.put(byte_request);
            buffer1.flip();
            while (buffer1.hasRemaining())
                client.write(buffer1);
            // Buffer contenente la lunghezza
            lengthBuffer = ByteBuffer.allocate(Integer.BYTES);

            while(lengthBuffer.hasRemaining())
                client.read(lengthBuffer);
            System.out.println("dopo read size");
            lengthBuffer.flip();
            System.out.println(lengthBuffer);

            // Buffer contenente i dati
            buffer_response = ByteBuffer.allocate(lengthBuffer.getInt());
            while(buffer_response.hasRemaining())
                client.read(buffer_response);
            buffer_response.flip();
            System.out.println(buffer_response);
            // Stringa estratta dal buffer
            message = new String(buffer_response.array(), StandardCharsets.UTF_8);
            System.out.printf("[TCP] Received string:\n%s\n", message);

        } catch(IOException ex) { ex.printStackTrace(); }
    }
}
