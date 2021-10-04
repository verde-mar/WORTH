package WORTH.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;


public class Cliente_1 {
    public static void main(String[] args) {
        int port = 8080;
        try { SocketAddress address = new InetSocketAddress(args[0], port);
            SocketChannel client  = SocketChannel.open(address);
            String request ="{\"request\": \"login\", \"nickUtente\": \"robert\", \"password\": \"roby55\"}";
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
            sleep(10000);

            request ="{\"request\": \"createProject\", \"nickUtente\": \"robert\", \"projectName\": \"roby55\"}";
            byte_request = request.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            buffer = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer.putInt(byte_request.length);
            buffer.put(byte_request);
            buffer.flip();
            while (buffer.hasRemaining())
                client.write(buffer);
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

            request ="{\"request\": \"addCard\", \"nickUtente\": \"robert\", \"projectName\": \"roby55\", \"cardName\": \"tropicci\"}";
            byte_request = request.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            buffer = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer.putInt(byte_request.length);
            buffer.put(byte_request);
            buffer.flip();
            while (buffer.hasRemaining())
                client.write(buffer);
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

            request ="{\"request\": \"moveCard\", \"projectName\": \"roby55\", \"cardName\": \"tropicci\", \"nickUtente\": \"robert\", \"listaPartenza\": \"to_Do\", \"listaDestinazione\": \"inProgress\"}";
            byte_request = request.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            buffer = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer.putInt(byte_request.length);
            buffer.put(byte_request);
            buffer.flip();
            while (buffer.hasRemaining())
                client.write(buffer);
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

            request ="{\"request\": \"logout\", \"nickUtente\": \"robert\"}";
            byte_request = request.getBytes(StandardCharsets.UTF_8);
            // Buffer contenente il messaggio
            System.out.println("numero di bytes: " + (byte_request.length + Integer.BYTES));
            buffer = ByteBuffer.allocate(byte_request.length + Integer.BYTES);
            buffer.putInt(byte_request.length);
            buffer.put(byte_request);
            buffer.flip();
            while (buffer.hasRemaining())
                client.write(buffer);
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
        } catch(IOException | InterruptedException ex) { ex.printStackTrace(); }
    }
}

